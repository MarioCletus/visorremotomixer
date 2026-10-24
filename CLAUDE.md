# visor-remoto-mixer — Contexto del proyecto

## Qué es esta app
App del **Visor Remoto**. Corre en una tablet secundaria que opera a distancia del mixer.
Muestra la misma información que la app principal y permite controlar acciones remotamente (ver peso, avanzar productos/corrales, etc.).
Actúa como **CLIENTE Bluetooth** que se conecta al HOST (`spi-mixer-android`).

La app principal (`spi-mixer-android`) es el HOST/servidor.
El Visor Remoto (`visor-remoto-mixer`) es el cliente/esclavo.

---

## Arquitectura Bluetooth

### BluetoothSDKService.kt
Servicio Android bound. Maneja la conexión como cliente al HOST.

| Clase | Rol |
|---|---|
| `ConnectThread` | Conecta puntualmente (sin transferencia continua) |
| `ConnectThreadWithTransfer` | Conecta y mantiene transferencia continua con el HOST |
| `ConnectedThread` | Handler de la conexión activa con el HOST |

### Protocolo de comunicación con el HOST (ConnectedThread)
Se usa **length-prefix framing**: cada mensaje va precedido de 4 bytes big-endian con el tamaño.
Implementado con `DataInputStream.readFully()` + `DataOutputStream`.

```kotlin
// Lectura
val length = dis.readInt()
val bytes = ByteArray(length)
dis.readFully(bytes)

// Escritura
@Synchronized
fun write(bytes: ByteArray): Int {
    dos.writeInt(bytes.size)
    dos.write(bytes)
    dos.flush()
    ...
}
```

**Formato de comandos recibidos:** `"CMD" + código_3_chars + payload`
**PING keepalive:** el HOST envía `"*"` cada 1 segundo; el VR responde con `ACTION_MESSAGE_RECEIVED`.

---

## Patrón de binding del servicio

### mBinder — regla fundamental
```kotlin
var mBinder: BluetoothSDKService.LocalBinder? = null
```
Se cachea en `onServiceConnected`. **Nunca usar `mService?.LocalBinder()`** (crea instancia nueva).
Siempre usar `mBinder?.metodo()`.

En todas las Activities:
```kotlin
// onServiceConnected:
mBinder = service as BluetoothSDKService.LocalBinder
mService = mBinder!!.getService()

// onServiceDisconnected:
mBinder = null
```

Si el servicio ya estaba corriendo al entrar a la Activity, `bindBluetoothService()` debe llamarse
**siempre** desde `onCreate` (no condicionalmente) para que `onServiceConnected` setee `mBinder`.

### Archivos que usan mBinder
- `MainActivity.kt` — Activity principal
- `TabletConfigActivity.kt` — config de tablets
- Fragments: `HomeFragment`, `RemoteMixerFragment`, `AdminFragment`, `RoundListFragment`,
  `TabletListFragment`, `SyncMixerFragment`

---

## Arquitectura general

### Patrón MVVM
- `ViewModel` + `LiveData` + `MediatorLiveData`
- Room database
- DataStore para persistir selección de tablet

### Activities
| Activity | Propósito |
|---|---|
| `MainActivity` | Pantalla principal con navigation |
| `TabletConfigActivity` | Selección y vinculación de tablets |

### Fragments principales (dentro de MainActivity)
| Fragment | Propósito |
|---|---|
| `HomeFragment` | Pantalla de inicio |
| `RemoteMixerFragment` | Vista principal de control remoto |
| `RoundListFragment` | Lista de rondas |
| `AdminFragment` | Administración |
| `TabletListFragment` | Lista de tablets conocidas |
| `SyncMixerFragment` | Sincronización |

---

## Constants.kt y BluetoothUtils.kt
Todos los `CMD_*`, `ACTION_*` y `EXTRA_*` son `const val` (compile-time constants).
UUID Bluetooth: `"00001101-0000-1000-8000-00805F9B34FB"` (SPP)

Constantes añadidas:
```kotlin
const val CMD_CFG = "CFG"
const val PREF_VR_SETTINGS: String = "PREF_VR_SETTINGS"
const val ENABLE_VR_DOWNLOAD: String = "vr_download_enable"
```

---

## Despacho de comandos — cómo llegan a los fragments

### CRÍTICO: ConnectedThread strip el prefijo "CMD"
El `ConnectedThread` del VR recibe el mensaje completo (`"CMD" + código + payload`), pero antes
de disparar el broadcast hace:
```kotlin
arrayCommand = bytes.copyOfRange(3, length)   // quita los 3 bytes "CMD"
```
Por lo tanto, cuando `onCommandReceived(message: ByteArray)` se invoca en los fragments,
`message` ya **NO contiene** el prefijo `"CMD"`. El layout es:
```
message[0..2]  = código de 3 chars  (ej. "CFG", "WEI", "DNL"...)
message[3..]   = payload
```
Uso en el when:
```kotlin
val messageStr = String(message, 0, minOf(message.size, 9))
when (messageStr.substring(0, 3)) {
    Constants.CMD_CFG -> { ... }
    Constants.CMD_WEI -> { ... }
    ...
}
```

---

## CMD_CFG — Configuración recibida del HOST

### Qué es
El HOST (`spi-mixer-android`) envía `"CMD" + "CFG" + json_de_AppSettings` al conectarse el VR.
El VR recibe el JSON y persiste los settings en `SharedPreferences("PREF_VR_SETTINGS")`.

### Handler en RemoteMixerFragment
```kotlin
Constants.CMD_CFG -> {
    try {
        val json = String(message, 3, message.size - 3)
        val gson = Gson()
        val settingsMap = gson.fromJson(json, Map::class.java)
        val enableVrDownload = settingsMap["enableVrDownload"] as? Boolean ?: false
        (requireActivity() as MainActivity).saveVrSettings(enableVrDownload)
    } catch (e: Exception) {
        Log.e(TAG, "CMD_CFG exception: $e")
    }
}
```

### Métodos en MainActivity.kt
```kotlin
fun saveVrSettings(enableVrDownload: Boolean) {
    val prefs = getSharedPreferences(Constants.PREF_VR_SETTINGS, Context.MODE_PRIVATE)
    prefs.edit().putBoolean(Constants.ENABLE_VR_DOWNLOAD, enableVrDownload).apply()
}

fun isVrDownloadEnabled(): Boolean {
    val prefs = getSharedPreferences(Constants.PREF_VR_SETTINGS, Context.MODE_PRIVATE)
    return prefs.getBoolean(Constants.ENABLE_VR_DOWNLOAD, false)
}
```

### ENABLE_VR_DOWNLOAD
Por defecto `false`. El HOST puede habilitarlo desde `SettingsActivity`.
- Si es `false`: cuando llega `CMD_WEIGHT_DWNL` / `CMD_WEIGHT_DWNL_FREE`, el VR vuelve a Home.
- Si es `true`: el VR muestra la pantalla de descarga normalmente.

### Comportamiento de CMD_WEIGHT_DWNL cuando descarga está deshabilitada
Al inicio del handler (antes de cualquier procesamiento):
```kotlin
Constants.CMD_WEIGHT_DWNL -> {
    if (!(requireActivity() as MainActivity).isVrDownloadEnabled()) {
        if (isAdded) findNavController().popBackStack(R.id.nav_home, false)
        return
    }
    // ... resto del handler sin cambios ...
}
```
Lo mismo aplica para `CMD_WEIGHT_DWNL_FREE`.
El `return` es crítico: el comando llega cada ~200ms, sin él la navegación se dispararía en loop.
Se usa `popBackStack(R.id.nav_home, false)` para volver directamente a HomeFragment (que siempre
está en el back stack como start destination) sin usar `onBackPressed()`.

---

## Correcciones aplicadas

### BluetoothSDKService.kt
- `socketConnectWithPermission` (era `socketConcetWithPermission` — typo corregido)
- `disconnectKnowDevice()` — corregido para cancelar `connectThread` (no `connectThreadWithTransfer`)
- `isConnected()` — eliminada indirección circular con binder
- `ConnectedThread` — reescrito con length-prefix framing (DataInputStream/DataOutputStream)

### MainActivity.kt
- `mBinder` cacheado correctamente
- Métodos `beaconReceived()` y `weightReceived()` (eran `beaconReceibed`/`weightReceibed` — typo)
- `saveVrSettings()` / `isVrDownloadEnabled()` añadidos para gestión de settings del HOST
- `alertDialog(title, message, onAccept)` — añadido parámetro opcional `onAccept: (() -> Unit)? = null` (backwards compatible)
- `connectDevice()` — bug fix: el `Runnable` del loop de reconexión ahora usa `this@MainActivity.bluetoothDevice` (campo actual) en vez del parámetro capturado en el closure. Esto evita que un Runnable viejo sobreescriba el device target cuando el usuario cambia de tablet. También se cancela el Runnable anterior con `handlerRecconect.removeCallbacks()` antes de crear uno nuevo.

### TabletConfigActivity.kt
- `mBinder` cacheado
- `bindBluetoothService()` siempre llamado desde `onCreate` (no condicionalmente)

### Fragments
- Todos reemplazados `mService?.LocalBinder()?.` por `mBinder?.`
- Corregidos typos `beaconReceibed` → `beaconReceived`, `weightReceibed` → `weightReceived`
- `RemoteMixerFragment`: añadido handler `CMD_CFG` y guard de descarga en `CMD_WEIGHT_DWNL` / `CMD_WEIGHT_DWNL_FREE`
- `RemoteMixerFragment`: `bDownloadDisabledAlertShowing` flag para no repetir el diálogo cada 200ms. Al llegar `CMD_WEIGHT_DWNL`/`CMD_WEIGHT_DWNL_FREE` con VR download deshabilitado, llama `mainActivity.alertDialog()` con callback que navega a Home al aceptar.
- `RoundListFragment`, `TabletListFragment`, `SyncFragment`, `SyncMixerFragment`: añadido FAB `btn_back` (Home) en layouts y wired en `onViewCreated` con `findNavController().popBackStack(R.id.nav_home, false)`.
- `TabletListFragment.selectTabletMixer()`: siempre llama `disconnectKnowDeviceWithTransfer()` (antes solo si `selectedBluetoothDevice != null`).
- `TabletListFragment.goToRemoteMixerFragment()`: ahora llama `changeTabletMixer(tabletMixer, deviceBluetooth)` en vez de `saveTabletMixer()`. NOTA: este método actualmente es código muerto (no llamado desde ningún lado), la navegación real ocurre por back stack o por `HomeFragment`.

---

## Cambio de tablet (bug de switching) — Estado actual

### Síntoma
Al cambiar de Tablet 1 → Tablet 2 en `TabletListFragment`, la toolbar muestra T2 pero la conexión BT va a T1.

### Causa raíz identificada
`connectDevice()` tiene un loop de reconexión con un `Runnable`. El bug original: el Runnable capturaba `bluetoothDevice` (el **parámetro**), no `this.bluetoothDevice` (el **campo**). Cuando el usuario cambiaba de tablet, `selectBluetooth(T2)` actualizaba el campo a T2, pero el Runnable del loop anterior (del T1) disparaba y llamaba `connectDevice(T1_capturado)`, sobreescribiendo el campo a T1 y conectando al equipo incorrecto.

### Fix aplicado en MainActivity.connectDevice() (línea ~538)
```kotlin
// ANTES (bug):
connectDevice(bluetoothDevice)  // parámetro capturado en closure

// DESPUÉS (fix):
connectDevice(this@MainActivity.bluetoothDevice)  // campo actual de la clase
```
Y antes de crear el nuevo Runnable:
```kotlin
reconnectRunnable?.let { handlerRecconect.removeCallbacks(it) }
```

### Fix adicional — eliminar campos BluetoothDevice redundantes (sesión siguiente)
Se eliminaron los campos `bluetoothDevice` cacheado de múltiples lugares y se unificó bajo una sola fuente de verdad:
- `MainActivity.bluetoothDevice` pasó de `var` a `val` computed desde `selectedTabletInActivity.mac`:
  ```kotlin
  private val bluetoothDevice: BluetoothDevice?
      get() = selectedTabletInActivity?.mac
          ?.takeIf { it.isNotEmpty() }
          ?.let { Helper.getBluetoothDeviceFromMac(it) }
  ```
- `connectDevice()` ya no asigna `this.bluetoothDevice`; usa `val target = bluetoothDevice ?: this.bluetoothDevice ?: return`
- `selectBluetooth()` en MainActivity quedó como no-op
- `changeTabletMixer()` ya no asigna `this.bluetoothDevice`
- Eliminado `selectedBluetoothDevice` de `TabletListFragment`; toolbar usa `Helper.getBluetoothDeviceFromMac(mac)`
- `selectTabletMixer()` en TabletListFragment ya no llama `selectBluetooth()` ni guarda `selectedBluetoothDevice`
- Eliminado `tabletMixerBluetoothDevice` de `HomeFragment`, `RoundListFragment`, `AdminFragment`
- `selectTablet()` en esos fragments reemplaza `if(tabletMixerBluetoothDevice != it)` con `if(mBinder?.isConnected() != true)`

### Estado
Pendiente de prueba por el usuario.

---

## Flujo de navegación (importante para entender el switching)

### Navigation graph (mobile_navigation.xml)
- `nav_home` (HomeFragment) — start destination
- `nav_home` → `nav_tablet_mixer` (TabletListFragment) via `action_home_to_mixer`
- `nav_mixer_remoto` (RemoteMixerFragment) → `nav_tablet_mixer` via `action_remote_mixer_fragment_to_table_mixer_list_fragment`
- `nav_tablet_mixer` → `nav_mixer_remoto` via `action_tablet_mixer_list_fragment_to_remote_mixer_fragment`

### Cómo se selecciona y conecta una tablet en TabletListFragment
- **Short tap** en el card → `goToConfigMixer()` → abre `TabletConfigActivity`
- **Long tap** en el card → `selectTabletMixer()` → resalta la tablet, actualiza `selectedTabletInFragment`, llama `selectBluetooth()` y desconecta
- **`btnSelect` popup** (`action_select_mixer`) → solo `saveTabletMixer()` + actualiza UI, NO conecta ni desconecta
- `goToRemoteMixerFragment()` es código muerto (no invocado desde el adapter ni desde ningún otro lugar)

### Cómo se selecciona tablet desde HomeFragment
- RecyclerView (`TabletMixerHomeAdapter`) tap → `onTabletMixerPressed()` → **`changeTabletMixer()`** + `saveTabletMixerAndWait()` + navega a `nav_round`
- Toolbar menu item (`menu_selected_remote_tablet`) → `selectedListItem()` → **llama `changeTabletMixer()`** (el camino correcto)

**NOTA**: `onTabletMixerPressed()` debe llamar `changeTabletMixer()` ANTES de `saveTabletMixerAndWait()`.
Sin `changeTabletMixer()`, el switch BT nunca ocurría — solo se guardaba en DataStore y navegaba,
pero la conexión a T1 seguía activa indefinidamente.

### changeTabletMixer() en MainActivity — el método correcto para cambiar de tablet
```kotlin
fun changeTabletMixer(tabletMixer: TabletMixer, bluetoothDevice: BluetoothDevice?) {
    reconnectDisable()
    // desconecta si está conectado
    selectedTabletInActivity = tabletMixer
    this.bluetoothDevice = bluetoothDevice
    // ...
    Handler(...).postDelayed({
        reconnectEnable()
        connectDevice(bluetoothDevice)
    }, Constants.CHANGE_MIXER_TIME)
}
```

---

## Pendientes conocidos
- **Bug switching de tablet**: RESUELTO. Ver historial en sección anterior.
- Revisar bounds checking en `refreshWeight()`
- Strings hardcodeados en español (internacionalización pendiente)
- Considerar enviar `CMD_CFG` también al reconectar durante fase de descarga (desde HOST)
- `goToRemoteMixerFragment()` en TabletListFragment es código muerto — limpiar o implementar correctamente
