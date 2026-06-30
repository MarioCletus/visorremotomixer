package com.basculasmagris.visorremotomixer.view.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ActivitySplashBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Session
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

sealed class MergedLocalData
data class TabletMixerData(val tabletMixers: MutableList<TabletMixer>): MergedLocalData()
data class UserData(val users: MutableList<User>): MergedLocalData()
data class RoundLocalData(val roundsLocal: MutableList<RoundLocal>): MergedLocalData()

class SplashActivity : AppCompatActivity() {

    private val TAG = "DEBSplash"

    // --- Actualización forzada (Play In-App Update, flujo IMMEDIATE) ---
    private lateinit var appUpdateManager: AppUpdateManager

    /**
     * Resultado del flujo de actualización IMMEDIATE lanzado por Play Store.
     * Si todo sale bien, Play reinicia la app automáticamente con la nueva versión
     * y este callback ni siquiera llega a ejecutarse. Si el usuario cancela o el
     * flujo falla (resultCode != RESULT_OK), como la actualización es OBLIGATORIA,
     * volvemos a intentarlo: la app no debe quedar utilizable con una versión vieja
     * mientras haya una nueva disponible y haya internet.
     */
    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            Log.w(TAG, "Actualización obligatoria no completada (resultCode=${result.resultCode}). Reintentando...")
            checkForUpdate { permission() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val sSplashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(sSplashBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }

        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        sSplashBinding.ivAppName.animation = splashAnimation

        val pInfo = applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName, 0
        )
        val version = pInfo.versionName
        sSplashBinding.tvVersion.text = "${getString(R.string.app_name)} $version"

        hideNavigationBar()

        splashAnimation.setAnimationListener(object :
            Animation.AnimationListener {

            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                checkForUpdate { permission() }
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }

    private fun hideNavigationBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    /**
     * Chequea contra Play Store si hay una versión más nueva publicada y, de haberla,
     * obliga a actualizar antes de continuar (flujo IMMEDIATE: pantalla completa de
     * Play, no se puede usar la app hasta instalar la actualización).
     *
     * Si la consulta falla (sin internet, dispositivo sin Play Services/Play Store,
     * timeout, etc.) o no hay actualización disponible, se invoca [onProceed] y la app
     * sigue su arranque normal — esto cubre el caso de uso en establecimientos remotos
     * sin conexión.
     */
    private fun checkForUpdate(onProceed: () -> Unit) {
        try {
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { info ->
                    if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                    ) {
                        try {
                            Log.i(TAG, "Actualización obligatoria disponible (availableVersionCode=${info.availableVersionCode()}). Lanzando flujo IMMEDIATE.")
                            appUpdateManager.startUpdateFlowForResult(
                                info,
                                updateResultLauncher,
                                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "No se pudo iniciar el flujo de actualización: ${e.message}", e)
                            onProceed()
                        }
                    } else {
                        onProceed()
                    }
                }
                .addOnFailureListener { e ->
                    // Sin internet, sin Play Services, app instalada fuera de Play Store, etc.
                    Log.w(TAG, "No se pudo verificar actualizaciones (¿sin internet?): ${e.message}")
                    onProceed()
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error al consultar AppUpdateManager: ${e.message}", e)
            onProceed()
        }
    }

    /**
     * Si la app vuelve a primer plano mientras una actualización IMMEDIATE quedó a
     * mitad de camino (ej. el usuario salió de la pantalla de Play con el botón Home),
     * Play no la reanuda solo: hay que volver a invocar el flujo.
     */
    override fun onResume() {
        super.onResume()
        if (!::appUpdateManager.isInitialized) return
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                Log.i(TAG, "Reanudando actualización IMMEDIATE en progreso.")
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }

    private fun permission(){
        this.let {activity->
            val permission1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH)
            val permission2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)
            val permission3 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            val permission4 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            val permission5 = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN)
            val permission6 = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
            val permission7 = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_PRIVILEGED)
            if (permission1 != PackageManager.PERMISSION_GRANTED
                || permission2 != PackageManager.PERMISSION_GRANTED
                || permission3 != PackageManager.PERMISSION_GRANTED
                || permission4 != PackageManager.PERMISSION_GRANTED
                || permission5 != PackageManager.PERMISSION_GRANTED
                || permission6 != PackageManager.PERMISSION_GRANTED
                || permission7 != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_PRIVILEGED
                    ),
                    642)
            } else {
                Log.d(TAG, "Permissions Granted")
                initApp()
            }
        }

    }

    // This function is called when the user accepts or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when the user is prompt for permission.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.i(TAG,"Requestcode $requestCode")
        if (requestCode == 642) {
            initApp()
        } else {
        }
    }

    private fun initApp() {
        val sharedpreferences = getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE)
        Handler(Looper.getMainLooper()).postDelayed({
            val isLogged = sharedpreferences.getBoolean(Constants.PREF_IS_LOGGED, false)
            Session.accessToken =
                sharedpreferences.getString(Constants.PREF_LOGIN_KEY_ACCESS_TOKEN, "").toString()
            if (isLogged){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

        }, 1000)
    }}