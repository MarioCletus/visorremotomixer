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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ActivitySplashBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Session

sealed class MergedLocalData
data class CorralData(val corrals: MutableList<Corral>): MergedLocalData()
data class TabletMixerData(val tabletMixers: MutableList<TabletMixer>): MergedLocalData()
data class DietData(val diets: MutableList<Diet>): MergedLocalData()
data class RoundData(val rounds: MutableList<Round>): MergedLocalData()
data class RoundCorralDetailData(val roundCorralDetail: MutableList<RoundCorralDetail>): MergedLocalData()
data class UserData(val users: MutableList<User>): MergedLocalData()
data class RoundLocalData(val roundsLocal: MutableList<RoundLocal>): MergedLocalData()

class SplashActivity : AppCompatActivity() {

    private val TAG = "DEBSplash"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sSplashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(sSplashBinding.root)
        val sharedpreferences = getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE)

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
                permission()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }

    private fun hideNavigationBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
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
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

        }, 1000)
    }}