package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateTabletBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddUpdateTabletActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateTabletBinding
    private var mTabletDetails: TabletMixer? = null
    private var mNewTabletDetails: TabletMixer? = null
    // Bluetooth
    private lateinit var mService: BluetoothSDKService

    private val mTabletViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((application as SpiMixerVRApplication).tabletMixerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateTabletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddTablet.visibility = GONE
        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            mTabletDetails = intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS)
        }

        setupActionBar()

        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_add_update_round, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_round -> {
                        if(!isKeyBoardShowing()){
                            saveMremoteViewer()
                        }else{
                            hideSoftKeyboard()
                        }
                        return true
                    }
                    R.id.action_hide_keyboard -> {
                        toggleKeyboard()
                        return true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)

        mTabletDetails?.let {
            if (it.id != 0L){
                binding.tiTabletName.setText(it.name)
                binding.tiTabletMixerName.setText(it.mixerName)
                binding.btnAddTablet.text = resources.getString(R.string.lbl_update_tablet_mixer)
                binding.etBtBox.setText(it.btName)
                binding.etMac.setText(it.mac)

            }
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateTablet)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mTabletDetails != null && mTabletDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_tablet_mixer)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_tablet_mixer)
            }
        }

        binding.toolbarAddUpdateTablet.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveMremoteViewer(){

        val remoteTabletName = binding.tiTabletName.text.toString().trim { it <= ' ' }
        val remoteTabletMixerNAme = binding.tiTabletMixerName.text.toString().trim { it <= ' ' }
        val remoteTabletMac = binding.etMac.text.toString().trim { it <= ' ' }
        val remoteTabletBtName = binding.etBtBox.text.toString().trim { it <= ' ' }

        when {
            TextUtils.isEmpty(remoteTabletName) -> {
                Toast.makeText(
                    this@AddUpdateTabletActivity,
                    resources.getString(R.string.err_msg_tablet_mixer_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var remoteTabletId = 0L
                var updatedDate = ""

                mTabletDetails?.let {
                    if (it.id != 0L){
                        remoteTabletId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(Date())
                    }
                }

                val tabletMixer = TabletMixer(
                    name = remoteTabletName,
                    mixerName = remoteTabletMixerNAme,
                    mac = remoteTabletMac,
                    serial = "",//TODO Serial
                    btName = remoteTabletBtName,
                    updatedDate = updatedDate,
                    id = remoteTabletId
                )

                mNewTabletDetails = tabletMixer

                runBlocking {
                    if (remoteTabletId == 0L){
                        mTabletViewModel.insertSync(tabletMixer)
                    } else {
                        mTabletViewModel.updateSync(tabletMixer)
                    }
                    Log.i("SYNC", "Se actualiza remoteViewer con fecha ${tabletMixer.updatedDate}")
                    finish()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //mService.LocalBinder().stopDiscovery()
    }



    fun toggleKeyboard() {
        if(isKeyBoardShowing()){
            hideSoftKeyboard()
        }else{
            showKeyboard()
        }
    }
    fun Activity.hideSoftKeyboard() {
        val view: View = binding.root
        view.let { _view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(_view.windowToken, 0)
        }
    }

    fun Activity.showKeyboard() { // Or View.showKeyboard()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethod.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun isKeyBoardShowing(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val view: View = binding.root
            return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
        }else{
            val view: View = binding.root
            val decorView = view.rootView
            val rootViewHeight = decorView.height
            val insets = decorView.rootWindowInsets
            return rootViewHeight > insets.stableInsetBottom + insets.stableInsetTop
        }
    }
}