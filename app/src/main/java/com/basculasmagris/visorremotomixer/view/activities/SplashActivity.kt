package com.basculasmagris.visorremotomixer.view.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ActivitySplashBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Session

sealed class MergedLocalData
data class EstablishmentData(val establishments: MutableList<Establishment>): MergedLocalData()
data class CorralData(val corrals: MutableList<Corral>): MergedLocalData()
data class ProductData(val products: MutableList<Product>): MergedLocalData()
data class MixerData(val mixers: MutableList<Mixer>): MergedLocalData()
data class TabletMixerData(val tabletMixers: MutableList<TabletMixer>): MergedLocalData()
data class DietData(val diets: MutableList<Diet>): MergedLocalData()
data class DietProductDetailData(val dietProductsDetail: MutableList<DietProductDetail>): MergedLocalData()
data class DietProductData(val dietProducts: MutableList<DietProduct>): MergedLocalData()
data class RoundData(val rounds: MutableList<Round>): MergedLocalData()
data class RoundCorralDetailData(val roundCorralDetail: MutableList<RoundCorralDetail>): MergedLocalData()
data class RoundCorralData(val roundCorrals: MutableList<RoundCorral>): MergedLocalData()
data class RoundRunData(val roundRun: MutableList<RoundRun>): MergedLocalData()
data class RoundRunProgressLoadData(val roundRunProgressLoad: MutableList<RoundRunProgressLoad>): MergedLocalData()
data class RoundRunProgressDownloadData(val roundRunProgressDownload: MutableList<RoundRunProgressDownload>): MergedLocalData()
data class UserData(val users: MutableList<User>): MergedLocalData()

class SplashActivity : AppCompatActivity() {

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
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }

    private fun hideNavigationBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}