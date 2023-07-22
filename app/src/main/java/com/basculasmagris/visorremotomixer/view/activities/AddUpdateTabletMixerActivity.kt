package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateTabletMixerBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateTabletMixerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateTabletMixerBinding
    private var mTabletMixerDetails: TabletMixer? = null
    private var mNewTabletMixerDetails: TabletMixer? = null
    // Bluetooth
    private lateinit var mService: BluetoothSDKService

    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((application as SpiMixerApplication).tabletMixerRepository)
    }

    private var mProgressDialog: Dialog? = null

    private fun showCustomProgressDialog(){
        if(mProgressDialog != null && mProgressDialog!!.isShowing){
            return
        }
        mProgressDialog = Dialog(this)
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateTabletMixerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddTabletMixer.visibility = GONE
        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            mTabletMixerDetails = intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS)
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

        mTabletMixerDetails?.let {
            if (it.id != 0L){
                binding.tiTabletMixerName.setText(it.name)
                binding.tiTabletMixerDescription.setText(it.description)
                binding.btnAddTabletMixer.text = resources.getString(R.string.lbl_update_tablet_mixer)
                binding.etBtBox.setText(it.btBox)
                binding.etMac.setText(it.mac)

            }
        }

        //binding.btnAddTabletMixer.setOnClickListener(this)
        //bindBluetoothService()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateTabletMixer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mTabletMixerDetails != null && mTabletMixerDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_tablet_mixer)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_tablet_mixer)
            }
        }

        binding.toolbarAddUpdateTabletMixer.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveMremoteViewer(){

        val remoteViewerName = binding.tiTabletMixerName.text.toString().trim { it <= ' ' }
        val remoteViewerDescription = binding.tiTabletMixerDescription.text.toString().trim { it <= ' ' }
        var remoteId = 0L
        var remoteViewerMac = binding.etMac.text.toString().trim { it <= ' ' }
        var remoteViewerBtBox = binding.etBtBox.text.toString().trim { it <= ' ' }

        mTabletMixerDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(remoteViewerName) -> {
                Toast.makeText(
                    this@AddUpdateTabletMixerActivity,
                    resources.getString(R.string.err_msg_tablet_mixer_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var remoteViewerId = 0L
                var updatedDate = ""
                var linked = false

                mTabletMixerDetails?.let {
                    if (it.id != 0L){
                        remoteViewerId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        linked = it.linked == true
                    }
                }

                val tabletMixer = TabletMixer(
                    remoteViewerName,
                    remoteViewerDescription,
                    remoteViewerMac,
                    remoteViewerBtBox,
                    remoteId,
                    updatedDate,
                    "",
                    linked,
                    remoteViewerId
                )

                mNewTabletMixerDetails = tabletMixer

                runBlocking {
                    if (remoteViewerId == 0L){
                        mTabletMixerViewModel.insertSync(tabletMixer)
                    } else {
                        mTabletMixerViewModel.updateSync(tabletMixer)
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

    //Bluetooth
    private fun bindBluetoothService() {
        // Bind to LocalService
        Log.i("BLUE", "Se inicia servicio")
        Intent(
            this,
            BluetoothSDKService::class.java
        ).also { intent ->
            Log.i("BLUE", "intent Se inicia servicio")
            this.bindService(
                intent,
                connection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as BluetoothSDKService.LocalBinder
            mService = binder.getService()
            mService.LocalBinder().startDiscovery(this@AddUpdateTabletMixerActivity)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i("BLUE", "[onServiceDisconnected] Se desconecta ")
        }
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i("BLUE", "ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i("BLUE", "ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i("BLUE", "ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            Log.i("${this.javaClass.name}", "ACT onDeviceConnected")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("${this.javaClass.name}", "ACT onCommandReceived")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("${this.javaClass.name}", "ACT onMessageReceived")
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("${this.javaClass.name}", "onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i("${this.javaClass.name}", "onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i("${this.javaClass.name}", "ACT onError")
        }

        override fun onDeviceDisconnected() {
            Log.i("${this.javaClass.name}", "ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i("${this.javaClass.name}", "ACT onBondedDevices")
        }

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
        val view: View = binding.root
        return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
    }
}