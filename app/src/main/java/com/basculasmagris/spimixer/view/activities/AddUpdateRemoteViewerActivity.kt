package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateRemoteViewerBinding
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.RemoteViewerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RemoteViewerViewModelFactory
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateRemoteViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateRemoteViewerBinding
    private var mRemoteViewerDetails: RemoteViewer? = null
    private var mNewRemoteViewerDetails: RemoteViewer? = null
    // Bluetooth
    private lateinit var mService: BluetoothSDKService

    private val mRemoteViewerViewModel: RemoteViewerViewModel by viewModels {
        RemoteViewerViewModelFactory((application as SpiMixerApplication).remoteViewerRepository)
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
        binding = ActivityAddUpdateRemoteViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddRemoteViewer.visibility = GONE
        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            mRemoteViewerDetails = intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS)
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

        mRemoteViewerDetails?.let {
            if (it.id != 0L){
                binding.tiRemoteViewerName.setText(it.name)
                binding.tiRemoteViewerDescription.setText(it.description)
                binding.btnAddRemoteViewer.text = resources.getString(R.string.lbl_update_remote_viewer)
                binding.etBtBox.setText(it.btBox)
                binding.etMac.setText(it.mac)

            }
        }

        //binding.btnAddRemoteViewer.setOnClickListener(this)
        //bindBluetoothService()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateRemoteViewer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mRemoteViewerDetails != null && mRemoteViewerDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_remote_viewer)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_remote_viewer)
            }
        }

        binding.toolbarAddUpdateRemoteViewer.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveMremoteViewer(){

        val remoteViewerName = binding.tiRemoteViewerName.text.toString().trim { it <= ' ' }
        val remoteViewerDescription = binding.tiRemoteViewerDescription.text.toString().trim { it <= ' ' }
        var remoteId = 0L
        var remoteViewerMac = binding.etMac.text.toString().trim { it <= ' ' }
        var remoteViewerBtBox = binding.etBtBox.text.toString().trim { it <= ' ' }

        mRemoteViewerDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(remoteViewerName) -> {
                Toast.makeText(
                    this@AddUpdateRemoteViewerActivity,
                    resources.getString(R.string.err_msg_remote_viewer_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var remoteViewerId = 0L
                var updatedDate = ""
                var linked = false

                mRemoteViewerDetails?.let {
                    if (it.id != 0L){
                        remoteViewerId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        linked = it.linked == true
                    }
                }

                val remoteViewer = RemoteViewer(
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

                mNewRemoteViewerDetails = remoteViewer

                runBlocking {
                    if (remoteViewerId == 0L){
                        mRemoteViewerViewModel.insertSync(remoteViewer)
                    } else {
                        mRemoteViewerViewModel.updateSync(remoteViewer)
                    }
                    Log.i("SYNC", "Se actualiza remoteViewer con fecha ${remoteViewer.updatedDate}")
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
            mService.LocalBinder().startDiscovery(this@AddUpdateRemoteViewerActivity)
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
            Log.i("${this.javaClass.name} BLUE", "ACT onDeviceConnected")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("${this.javaClass.name} BLUE", "ACT onMessageReceived")
        }

        override fun onMessageSent(device: BluetoothDevice?) {
            Log.i("${this.javaClass.name} BLUE", "ACT onMessageSent")
        }

        override fun onError(message: String?) {
            Log.i("${this.javaClass.name} BLUE", "ACT onError")
        }

        override fun onDeviceDisconnected() {
            Log.i("${this.javaClass.name} BLUE", "ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i("${this.javaClass.name} BLUE", "ACT onBondedDevices")
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