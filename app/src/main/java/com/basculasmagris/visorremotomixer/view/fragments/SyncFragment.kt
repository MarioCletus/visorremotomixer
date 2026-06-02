package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentSyncBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.TabletRemote
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.ApiError
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.BluetoothUtils
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.Session
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerData
import com.basculasmagris.visorremotomixer.view.activities.UserData
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.CancellationException

class SyncFragment : Fragment() {

    private lateinit var mBinding: FragmentSyncBinding
    private val TAG = "DEBSync"

    // ─── modo de sincronización ──────────────────────────────────────────────
    private enum class SyncMode { SERVER, TABLET }
    private var currentMode = SyncMode.SERVER

    // ─── BT discovery ────────────────────────────────────────────────────────
    private val allBluetoothDevice = mutableListOf<BluetoothDevice>()
    private var mCustomListDialog: Dialog? = null
    private var dialogCustomListBinding: DialogCustomListBinding? = null
    /** true cuando el usuario inició sync y está esperando que la conexión se establezca */
    private var pendingSync = false
    private var isSearching = false

    // ─── ViewModels ──────────────────────────────────────────────────────────
    private val mTabletMixerViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerVRApplication).tabletMixerRepository)
    }
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as SpiMixerVRApplication).userRepository)
    }

    private var liveData: MediatorLiveData<MergedLocalData>? = null
    private var mLocalTabletMixers: MutableList<TabletMixer>? = null
    private var mLocalUsers: MutableList<User>? = null

    // Servidor
    private var mUserViewModelRemote: UserRemoteViewModel? = null
    private var mTabletViewModelRemote: TabletRemoteViewModel? = null
    private var mRemoteUsers: MutableList<UserRemote>? = null
    private var mRemoteTablets: MutableList<TabletRemote>? = null
    var currentUser: com.basculasmagris.visorremotomixer.model.entities.UserRemote? = null

    // Tablet sync flags
    private var bSyncroRounds  = false
    private var bSyncroTablets = false
    private var bSyncroUsers   = false

    // ─── lifecycle ───────────────────────────────────────────────────────────

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSyncBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) (requireActivity() as MainActivity).clearbShowDevice()
        mBinding.llSyncNoInternet.visibility = View.INVISIBLE
        currentUser = Helper.getCurrentUser(requireContext())
        mBinding.llRounds.visibility = View.INVISIBLE

        mBinding.btnModeServer.setOnClickListener { switchMode(SyncMode.SERVER) }
        mBinding.btnModeTablet.setOnClickListener { switchMode(SyncMode.TABLET) }

        mBinding.btnSync.setOnClickListener {
            when (currentMode) {
                SyncMode.SERVER -> runServerSync()
                SyncMode.TABLET -> runTabletSync()
            }
        }

        mBinding.btnBack.setOnClickListener {
            if (isAdded) findNavController().popBackStack(R.id.nav_home, false)
        }

        switchMode(SyncMode.SERVER)
        getLocalData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopDiscoveryIfNeeded()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    // ─── cambio de modo ──────────────────────────────────────────────────────

    private fun switchMode(mode: SyncMode) {
        currentMode = mode
        val ctx = requireContext()
        val green = ctx.getColor(R.color.color_medium_green)
        val grey  = ctx.getColor(R.color.gray_dark)
        when (mode) {
            SyncMode.SERVER -> {
                mBinding.btnModeServer.backgroundTintList = android.content.res.ColorStateList.valueOf(green)
                mBinding.btnModeTablet.backgroundTintList = android.content.res.ColorStateList.valueOf(grey)
                mBinding.llRounds.visibility = View.INVISIBLE
                stopDiscoveryIfNeeded()
                BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(ctx, mBluetoothListener)
            }
            SyncMode.TABLET -> {
                mBinding.btnModeServer.backgroundTintList = android.content.res.ColorStateList.valueOf(grey)
                mBinding.btnModeTablet.backgroundTintList = android.content.res.ColorStateList.valueOf(green)
                mBinding.llRounds.visibility = View.VISIBLE
                BluetoothSDKListenerHelper.registerBluetoothSDKListener(ctx, mBluetoothListener)
                val tabletName = (requireActivity() as? MainActivity)?.selectedTabletInActivity?.name ?: ""
                mBinding.btnSync.text =
                    if (tabletName.isNotEmpty()) "${getString(R.string.sincronizar)} ($tabletName)"
                    else getString(R.string.sincronizar)
            }
        }
    }

    // ─── sync Servidor ───────────────────────────────────────────────────────

    private fun runServerSync() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                runSync_SUSPEND()
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                Log.e(TAG, "runServerSync error", t)
                val apiError = parseApiError(t)
                val msg = when (apiError?.code) {
                    "DEVICE_NOT_REGISTERED"  -> "Esta tablet no está registrada"
                    "DEVICE_NOT_SUBSCRIBED"  -> "Esta tablet no tiene suscripción activa"
                    "DEVICE_SERIAL_REQUIRED" -> "Falta el número de serie de la tablet"
                    "INVALID_DEVICE_SERIAL"  -> "El número de serie de la tablet es inválido"
                    else -> apiError?.message
                        ?: context?.getString(R.string.error_descargando_datos_servidor)
                        ?: "Error de sincronización"
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
        }
    }

    // ─── sync Tablet ─────────────────────────────────────────────────────────

    private fun runTabletSync() {
        val mainActivity = activity as? MainActivity ?: return
        if (mainActivity.mBinder?.isConnected() == true) {
            doTabletSync(mainActivity)
        } else {
            // No conectado: ofrecer selección de dispositivo BT
            showBluetoothDeviceSelection()
        }
    }

    private fun doTabletSync(mainActivity: MainActivity) {
        pendingSync = false
        resetTabletSyncProgress()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            if (isAdded) mainActivity.sendRequestListOfUsers()
            delay(200)
            if (isAdded) mainActivity.deleteRoundsFromDB()
            delay(200)
            if (isAdded) mainActivity.sendRequestListOfRounds()
            delay(200)
            if (isAdded) mainActivity.sendRequestTablet()
            delay(200)
            if (isAdded) mainActivity.sendRequestVrTabletList()
        }
    }

    private fun resetTabletSyncProgress() {
        mBinding.pbUsers.progress        = 0
        mBinding.tvUsersPercentage.text  = "0%"
        mBinding.pbTablet.progress       = 0
        mBinding.tvTabletPercentage.text = "0%"
        mBinding.pbRounds.progress       = 0
        mBinding.tvRoundsPercentage.text = "0%"
    }

    // ─── BT device selection ─────────────────────────────────────────────────

    /** Muestra un diálogo con dos opciones: dispositivos vinculados o buscar nuevos. */
    private fun showBluetoothDeviceSelection() {
        if (!isAdded) return
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.conectar_tablet_para_sync))
            .setItems(arrayOf(
                getString(R.string.tablets_vinculadas),
                getString(R.string.buscar_tablets)
            )) { _, which ->
                when (which) {
                    0 -> showBondedDevices()
                    1 -> startBluetoothDiscovery()
                }
            }
            .setNegativeButton(getString(R.string.cancelar), null)
            .show()
    }

    /** Lista los dispositivos BT ya emparejados. */
    private fun showBondedDevices() {
        if (!isAdded) return
        val activity = requireActivity()
        val btManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val bonded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    activity, android.Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) btManager?.adapter?.bondedDevices else null
        } else {
            btManager?.adapter?.bondedDevices
        }

        val deviceList = bonded?.toList() ?: emptyList()
        if (deviceList.isEmpty()) {
            Toast.makeText(context, getString(R.string.no_hay_dispositivos), Toast.LENGTH_SHORT).show()
            return
        }

        val names = deviceList.map {
            BluetoothUtils.getBluetoothName(activity, it).ifEmpty { getString(R.string.no_identificado) }
        }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.seleccionar_tablet))
            .setItems(names) { _, which ->
                connectToDevice(deviceList[which])
            }
            .setNegativeButton(getString(R.string.cancelar), null)
            .show()
    }

    /** Inicia el descubrimiento activo de dispositivos BT cercanos. */
    private fun startBluetoothDiscovery() {
        if (!isAdded) return
        if (isSearching) return
        isSearching = true
        allBluetoothDevice.clear()

        // Mostrar diálogo de lista vacío antes de arrancar — se irá poblando
        openDiscoveryDialog()

        mCustomListDialog?.setOnDismissListener {
            stopDiscoveryIfNeeded()
        }

        (requireActivity() as? MainActivity)?.mBinder?.startDiscovery(requireContext())

        // Timeout de seguridad (BT discovery dura hasta ~12 s)
        Handler(Looper.getMainLooper()).postDelayed({
            stopDiscoveryIfNeeded()
        }, 15_000)
    }

    private fun openDiscoveryDialog() {
        val ctx = context ?: return
        mCustomListDialog = Dialog(ctx, R.style.CustomDialogTheme)
        dialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog!!.setContentView(dialogCustomListBinding!!.root)
        dialogCustomListBinding!!.tvTitle.text = getString(R.string.dispositivos_disponibles)
        dialogCustomListBinding!!.rvList.layoutManager = LinearLayoutManager(ctx)
        discoveryAdapter = CustomListItemAdapterFragment(this, emptyList(), Constants.DEVICE_REF)
        dialogCustomListBinding!!.rvList.adapter = discoveryAdapter
        mCustomListDialog!!.show()
    }

    private var discoveryAdapter: CustomListItemAdapterFragment? = null

    private fun stopDiscoveryIfNeeded() {
        if (isSearching) {
            isSearching = false
            (activity as? MainActivity)?.mBinder?.stopDiscovery()
        }
    }

    /** Conecta a un dispositivo BT y marca sync pendiente. */
    private fun connectToDevice(device: BluetoothDevice) {
        val mainActivity = activity as? MainActivity ?: return
        pendingSync = true
        Log.i(TAG, "connectToDevice: ${BluetoothUtils.getBluetoothName(requireActivity(), device)} ${device.address}")
        mainActivity.showCustomProgressDialog()
        mainActivity.mBinder?.disconnectKnowDeviceWithTransfer()
        mainActivity.mBinder?.connectKnowDeviceWithTransfer(device)
    }

    /**
     * Llamado por [CustomListItemAdapterFragment] cuando el usuario selecciona
     * un dispositivo de la lista de descubrimiento.
     */
    fun selectedListItem(item: CustomListItem, selection: String) {
        if (selection != Constants.DEVICE_REF) return
        val device = allBluetoothDevice.firstOrNull { it.address == item.description }
        if (device != null) {
            mCustomListDialog?.dismiss()
            connectToDevice(device)
        }
    }

    // ─── BT listener (modo Tablet) ───────────────────────────────────────────

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {

        override fun onDiscoveryStarted() {
            Log.i(TAG, "[SyncFragment] onDiscoveryStarted")
            // El diálogo ya se abrió en startBluetoothDiscovery()
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[SyncFragment] onDiscoveryStopped: ${allBluetoothDevice.size} encontrados")
            isSearching = false
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            device ?: return
            allBluetoothDevice.add(device)
            val act = activity ?: return
            val name = BluetoothUtils.getBluetoothName(act, device)
                .ifEmpty { getString(R.string.no_identificado) }
            val mac  = BluetoothUtils.getAddress(act, device).ifEmpty { "00:00:00:00" }
            val item = CustomListItem(0L, 0, name, mac, R.drawable.ic_tablet_disconnected_48px)
            Log.i(TAG, "onDeviceDiscovered: $name $mac")
            act.runOnUiThread {
                discoveryAdapter?.updateList(item)
            }
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            Log.i(TAG, "[SyncFragment] onDeviceConnected pendingSync=$pendingSync device=$device")
            val mainActivity = activity as? MainActivity ?: return
            // Guardar el dispositivo en RemoteTabletSession para que processTabletInfo()
            // pueda asociar el MAC cuando llegue CMD_TABLET
            if (device != null) {
                com.basculasmagris.visorremotomixer.utils.RemoteTabletSession.setBluetoothDevice(device)
            }
            mainActivity.hideCustomProgressDialog()
            if (pendingSync) {
                doTabletSync(mainActivity)
                val name = BluetoothUtils.getBluetoothName(requireActivity(), device)
                if (name.isNotEmpty() && isAdded) {
                    mBinding.btnSync.text = "${getString(R.string.sincronizar)} ($name)"
                }
            }
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            if (message == null || message.size < 9) return
            val mainActivity = activity as? MainActivity ?: return
            if (!isAdded) return
            val command = String(message, 0, message.size).substring(0, 3)
            when (command) {
                Constants.CMD_USER_LIST -> {
                    bSyncroUsers = mainActivity.processUsers(message)
                    if (bSyncroUsers) { mBinding.pbUsers.progress = 100; mBinding.tvUsersPercentage.text = "100%" }
                    bSyncroUsers = false
                }
                Constants.CMD_TABLET -> {
                    bSyncroTablets = mainActivity.processTabletInfo(message)
                    if (bSyncroTablets) { mBinding.pbTablet.progress = 100; mBinding.tvTabletPercentage.text = "100%" }
                    bSyncroTablets = false
                }
                Constants.CMD_VTL -> {
                    mainActivity.processVrTabletList(message)
                    mBinding.pbTablet.progress = 100; mBinding.tvTabletPercentage.text = "100%"
                }
                Constants.CMD_ROUNDS -> {
                    bSyncroRounds = mainActivity.refreshRounds(message)
                    if (bSyncroRounds) { mBinding.pbRounds.progress = 100; mBinding.tvRoundsPercentage.text = "100%" }
                    bSyncroRounds = false
                }
                Constants.CMD_NTA -> {
                    if (isAdded) mainActivity.alertDialog(getString(R.string.warning), getString(R.string.no_disponible))
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            (activity as? MainActivity)?.changeStatusConnected()
        }
        override fun onMessageSent(device: BluetoothDevice?, message: String?) {}
        override fun onCommandSent(device: BluetoothDevice?, command: ByteArray?) {}
        override fun onError(message: String?) { if (isAdded) (activity as? MainActivity)?.changeStatusDisconnected() }
        override fun onDeviceDisconnected() { if (isAdded) (activity as? MainActivity)?.changeStatusDisconnected() }
        override fun onBondedDevices(device: List<BluetoothDevice>?) {}
    }

    // ─── datos locales (modo Servidor) ───────────────────────────────────────

    private fun getLocalData() {
        if (liveData != null) return
        liveData = fetchLocalData()
        liveData?.observe(viewLifecycleOwner) { data ->
            when (data) {
                is TabletMixerData -> mLocalTabletMixers = data.tabletMixers
                is UserData        -> mLocalUsers        = data.users
                else               -> {}
            }
            if (mLocalTabletMixers != null && mLocalUsers != null) {
                liveData?.removeObservers(viewLifecycleOwner)
                liveData = null
            }
        }
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val merger = MediatorLiveData<MergedLocalData>()
        merger.addSource(mTabletMixerViewModel.allTabletMixerList) { if (it != null) merger.value = TabletMixerData(it) }
        merger.addSource(mUserViewModel.allUserList) { if (it != null) merger.value = UserData(it) }
        return merger
    }

    // ─── sync suspendida (modo Servidor) ─────────────────────────────────────

    private suspend fun runSync_SUSPEND() = kotlinx.coroutines.coroutineScope {
        refreshLocalDataBeforeSync()
        val mainActivity = (activity as? MainActivity) ?: return@coroutineScope
        val userVm   = ViewModelProvider(mainActivity)[UserRemoteViewModel::class.java]
        val tabletVm = ViewModelProvider(mainActivity)[TabletRemoteViewModel::class.java]
        mUserViewModelRemote   = userVm
        mTabletViewModelRemote = tabletVm

        val tabletAsync = async { tabletVm.getTabletFromAPI_SUSPEND(currentUser?.codeClient ?: "") }
        val usersAsync  = async { userVm.getUsersFromAPI_SUSPEND() }
        mRemoteUsers   = usersAsync.await()
        mRemoteTablets = tabletAsync.await()
        initSyncUiAfterRemoteLoaded()

        mRemoteTablets?.forEachIndexed { index, tabletRemote ->
            val local = mLocalTabletMixers?.firstOrNull { it.serial == tabletRemote.serial }
            if (local != null) {
                val updated = TabletMixer(
                    name = tabletRemote.name ?: local.name, mixerName = local.mixerName,
                    mac = local.mac, serial = tabletRemote.serial ?: local.serial,
                    btName = tabletRemote.bluetoothName ?: local.btName,
                    updatedDate = Helper.getCurrentDateTime(), id = local.id
                )
                mTabletMixerViewModel.update(updated)
                mLocalTabletMixers = mLocalTabletMixers?.map { if (it.id == updated.id) updated else it }?.toMutableList()
            } else {
                val toInsert = TabletMixer(
                    name = tabletRemote.name ?: "", mixerName = "", mac = "",
                    serial = tabletRemote.serial ?: "", btName = tabletRemote.bluetoothName ?: "",
                    updatedDate = Helper.getCurrentDateTime()
                )
                mTabletMixerViewModel.insert(toInsert)
                mLocalTabletMixers?.add(toInsert)
            }
            updateTabletsProgress(index + 1, mRemoteTablets?.size ?: 0)
        }

        mRemoteUsers?.forEachIndexed { index, userRemote ->
            val local = mLocalUsers?.firstOrNull { it.remoteId == userRemote.id || it.username == userRemote.username }
            if (local != null) {
                val updated = User(
                    username = userRemote.username, name = userRemote.name,
                    lastname = userRemote.lastname, mail = userRemote.mail,
                    password = userRemote.password, remoteId = userRemote.id,
                    updatedDate = Helper.getCurrentDateTime(), archiveDate = local.archiveDate,
                    codeRole = userRemote.codeRole, codeClient = userRemote.codeClient, id = local.id
                )
                mUserViewModel.update(updated)
                mLocalUsers = mLocalUsers?.map { if (it.id == updated.id) updated else it }?.toMutableList()
            } else {
                val toInsert = User(
                    username = userRemote.username, name = userRemote.name,
                    lastname = userRemote.lastname, mail = userRemote.mail,
                    password = userRemote.password, remoteId = userRemote.id,
                    updatedDate = Helper.getCurrentDateTime(), archiveDate = null,
                    codeRole = userRemote.codeRole, codeClient = userRemote.codeClient
                )
                mUserViewModel.insertSync(toInsert)
                mLocalUsers?.add(toInsert)
            }
            updateUsersProgress(index + 1, mRemoteUsers?.size ?: 0)
        }

        // Enviar al servidor los nombres BT de las tablets que el VR conoce
        // para que futuros installs del VR puedan recuperarlos sin re-emparejar
        sendKnownBtNamesToServer()
    }

    /**
     * Envía al servidor los nombres BT que el VR tiene asociados a cada tablet.
     * El servidor los guarda en `bluetooth_name` del registro de cada tablet.
     */
    private fun sendKnownBtNamesToServer() {
        val ctx = context ?: return
        val tablets = mLocalTabletMixers
            ?.filter { !it.serial.isNullOrEmpty() && !it.btName.isNullOrEmpty() }
            ?.map { mapOf("serial" to it.serial, "bluetoothName" to it.btName) }
            ?: return

        if (tablets.isEmpty()) return
        Log.i(TAG,"sendKnownBtNamesToServer: ${Session.accessToken}")
        val codeClient = currentUser?.codeClient ?: return
        val tabletApiService = com.basculasmagris.visorremotomixer.model.network.TabletApiService()
        tabletApiService.updateTabletBtNames(codeClient, tablets)
            .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
            .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe({ _ ->
                Log.i(TAG, "sendKnownBtNamesToServer: enviados ${tablets.size} nombres BT al servidor")
            }, { error ->
                Log.w(TAG, "sendKnownBtNamesToServer: error (no crítico) $error")
            })
    }

    fun parseApiError(t: Throwable): ApiError? = try {
        if (t is HttpException) {
            val body = t.response()?.errorBody()?.string()
            if (!body.isNullOrEmpty()) Gson().fromJson(body, ApiError::class.java) else null
        } else null
    } catch (_: Exception) { null }

    private fun initSyncUiAfterRemoteLoaded() {
        mBinding.pbUsers.progress = 0; mBinding.tvUsersPercentage.text = "0% (0 / ${mRemoteUsers?.size ?: 0})"
        mBinding.pbTablet.progress = 0; mBinding.tvTabletPercentage.text = "0% (0 / ${mRemoteTablets?.size ?: 0})"
    }

    private fun updateUsersProgress(current: Int, total: Int) {
        val p = if (total == 0) 100 else (current * 100) / total
        mBinding.pbUsers.progress = p; mBinding.tvUsersPercentage.text = "$p% ($current / $total)"
    }

    private fun updateTabletsProgress(current: Int, total: Int) {
        val p = if (total == 0) 100 else (current * 100) / total
        mBinding.pbTablet.progress = p; mBinding.tvTabletPercentage.text = "$p% ($current / $total)"
    }

    private suspend fun refreshLocalDataBeforeSync() {
        kotlinx.coroutines.suspendCancellableCoroutine<Unit> { cont ->
            val local = fetchLocalData()
            local.observe(viewLifecycleOwner) { data ->
                when (data) {
                    is TabletMixerData -> mLocalTabletMixers = data.tabletMixers
                    is UserData        -> mLocalUsers        = data.users
                    else               -> {}
                }
                if (mLocalTabletMixers != null && mLocalUsers != null) {
                    local.removeObservers(viewLifecycleOwner)
                    if (cont.isActive) cont.resume(Unit) {}
                }
            }
        }
    }
}
