package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityTabletMixerConfigBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


class TabletMixerConfigActivity : AppCompatActivity(){
    private var bIsDetail: Boolean = false
    private var contMensajes: Int = 0
    private var TAG = "DEBConfig"
    private var firstIn: Boolean = false
    private lateinit var mBinding: ActivityTabletMixerConfigBinding

    private var isSearching: Boolean = false
    private var knowDevices: List<BluetoothDevice>? = null
    private var mTabletMixerDetails: TabletMixer? = null

    // Bluetooth
    var mService: BluetoothSDKService? = null
    private var bluetoothDevices: MutableList<BluetoothDevice> = ArrayList()
    private var bluetoothDeviceSelected: BluetoothDevice? = null

    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((this.application as SpiMixerApplication).tabletMixerRepository)
    }

    private var mLocalTabletMixers: List<TabletMixer>? = null
    private var mDevicesFound : MutableSet<BluetoothDevice> = mutableSetOf()
    private var likingTabletMixer: TabletMixer? = null

    private var mProgressDialog: Dialog? = null
    private var dialog: AlertDialog? = null    // Bluetooth
    private var mBound: Boolean = false

    private var snackbar: Snackbar? = null
    private var isConnected: Boolean = false
    var showSnackbar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTabletMixerConfigBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(Constants.EXTRA_MIXER_MODE)) {
            bIsDetail = intent.getBooleanExtra(Constants.EXTRA_MIXER_MODE, false)
        }


        setupActionBar()
        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_mixer_config, menu)
                if(bIsDetail){
                    menu.getItem(0).isVisible = false
                    menu.getItem(2).isVisible = false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_mixer -> {
                        if(!isKeyBoardShowing()){
                            saveMremoteViewer()
                            finish()
                        }else
                            hideSoftKeyboard()
                        return true
                    }
                    R.id.icon_bluetooth -> {
                        if(!isConnected && bluetoothDeviceSelected != null){
                            snackbar?.dismiss()
                            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(bluetoothDeviceSelected!!)
                            showCustomProgressDialog()
                            Timer().schedule(5000) {
                                hideCustomProgressDialog()
                                showSnackbar = true
                                changeStatusConnection(false)
                            }
                        }

                        return true
                    }
                    R.id.action_hide_keyboard->{
                        toggleKeyboard()
                        return true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)

        if(bIsDetail){
            mBinding.etTabletMixerName.isEnabled  = false
            mBinding.etTabletMixerName.isEnabled  = false
            mBinding.etTabletMixerDescription.isEnabled  = false
            mBinding.layBTAsoc.isEnabled  = false
        }

        if (intent.hasExtra(Constants.EXTRA_TABLET_MIXER_DETAILS)){
            mTabletMixerDetails = intent.getParcelableExtra(Constants.EXTRA_TABLET_MIXER_DETAILS)
        }

        mBinding.tvTitleTabletMixer.text = mTabletMixerDetails?.name ?: ""
        mBinding.etTabletMixerName.setText( mTabletMixerDetails?.name ?: "")
        mBinding.etTabletMixerDescription.setText( mTabletMixerDetails?.description?: "")



        mBinding.tvCajaBluetoothAsoc.text = mTabletMixerDetails?.btBox?: ""
        mBinding.tvMac.text = mTabletMixerDetails?.mac?: ""

        mBinding.layBTAsoc.setOnClickListener {
            if(mTabletMixerDetails == null){
                val remoteViewerName = mBinding.etTabletMixerName.text.toString().trim { it <= ' ' }
                val remoteViewerDescription = mBinding.etTabletMixerDescription.text.toString().trim { it <= ' ' }
                val remoteViewerMac = mBinding.tvMac.text.toString().trim { it <= ' ' }
                val remoteViewerBtBox = mBinding.tvCajaBluetoothAsoc.text.toString().trim { it <= ' ' }
                val remoteId = 0L
                val linked = false
                val remoteViewerId = 0L
                mTabletMixerDetails = TabletMixer(
                    remoteViewerName,
                    remoteViewerDescription,
                    remoteViewerMac,
                    remoteViewerBtBox,
                    remoteId,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                    "",
                    linked,
                    remoteViewerId)
            }
            bluetoothDialog()
        }

        mBinding.ivExitConfig.setOnClickListener{
            if(!bIsDetail){
                saveMremoteViewer()
            }
            finish()
        }


        mBinding.llKeyboard.setOnClickListener{
            hideSoftKeyboard()
        }

        mBinding.llConnectionStatus.setOnClickListener{
            if(!isConnected && bluetoothDeviceSelected != null){
                snackbar?.dismiss()
                mService?.LocalBinder()?.connectKnowDeviceWithTransfer(bluetoothDeviceSelected!!)
                showCustomProgressDialog()
                Timer().schedule(5000) {
                    hideCustomProgressDialog()
                    showSnackbar = true
                    changeStatusConnection(false)
                }
            }
        }

        bindBluetoothService()
        bluetoothDeviceSelected = intent.getParcelableExtra("BTDEVICE")
        Log.i("BLUE", "Se inicia la búsqueda de dispositivos asociados")
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(this, mBluetoothListener)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onDestroy() {
        super.onDestroy()
        mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(applicationContext, mBluetoothListener)
    }

    private fun linkDevice(tablet_mixer: TabletMixer){
        if(isSearching)
            return
        likingTabletMixer = tablet_mixer
        dialog = Helper.setProgressDialog(this, "Buscando tablet_mixer...")
        dialog?.show()
        this.mService?.LocalBinder()?.startDiscovery(this)
        Timer().schedule(5000){
            dialog?.cancel()
            mDevicesFound.forEach { devideFound ->
                Log.i("FOUND", devideFound.name + "  " + devideFound.address)
                mService?.LocalBinder()?.stopDiscovery()
            }

        }
    }

    fun getSelectedTabletMixerBluetoothDevice() : BluetoothDevice? {
        Log.i(TAG, "[getSelectedTabletMixerBluetoothDevice] Local tablet_mixer: ${bluetoothDevices.size} | mLocalTabletMixers: ${mLocalTabletMixers?.size}")
        if(!firstIn)
            mService?.LocalBinder()?.getBondedDevices()

        bluetoothDevices.forEach { bluetoothDevice ->

            val localTabletMixer = mLocalTabletMixers?.firstOrNull{ tablet_mixer ->
                Log.i(TAG, "[getSelectedTabletMixerBluetoothDevice] tablet_mixer: ${tablet_mixer.mac} | knowDevices: ${bluetoothDevice.address}")
                tablet_mixer.mac == bluetoothDevice.address
            }

            Log.i(TAG, "[getSelectedTabletMixerBluetoothDevice] localTabletMixer: ${localTabletMixer?.name}")
            if (localTabletMixer != null){
                return  bluetoothDevice
            }
        }
        return null
    }

    //Bluetooth
    private fun bindBluetoothService() {
        // Bind to LocalService
        Log.d(TAG,"bindBluetoothService")
        Intent(
            this.applicationContext,
            BluetoothSDKService::class.java
        ).also { intent ->
            Log.i(TAG, "intent Se inicia servicio")
            this.applicationContext.bindService(
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
            mBound = true
            Log.i(TAG, "*********** onServiceConnected [RoundRunActivity] CONECTADO")
            if(bluetoothDeviceSelected!=null){
                mService?.LocalBinder()?.connectKnowDeviceWithTransfer(bluetoothDeviceSelected!!)
            }
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i("BLUE", "*********** [RoundRunActivity]  DESCONECTADO")
            mBound = false
        }
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "[TabletMixerConfigActivity] onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[TabletMixerConfigActivity] onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[TabletMixerConfigActivity] onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            Log.i(TAG, "[TabletMixerConfigActivity] onDeviceConnected")
            changeStatusConnection(true)
            mBinding.bluetoothIcon.setImageDrawable(getDrawable(R.drawable.ic_bluetooth_connected))
        }


        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("command", "TabletMixerConfigActivity onCommandReceived ${message?.let { String(it) }}")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("command", "TabletMixerConfigActivity onMessageReceived $message")
            if(message==null){
                return
            }
            if(contMensajes++ >50){
                mBinding.bluetoothIcon.setImageDrawable(getDrawable(R.drawable.ic_bluetooth_connected))
                Log.i(TAG, "[TabletMixerConfigActivity] onMessageReceived: $message")
                contMensajes = 0
            }
            changeStatusConnection(true)
            message.let{

            }
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i(TAG, "onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i(TAG, "onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            changeStatusConnection(false)
            mBinding.bluetoothIcon.setImageDrawable(getDrawable(R.drawable.ic_bluetooth_disconnected))
            Log.i(TAG, "[TabletMixerConfigActivity] onError")
        }

        override fun onDeviceDisconnected() {
            changeStatusConnection(false)
            mBinding.bluetoothIcon.setImageDrawable(getDrawable(R.drawable.ic_bluetooth_disconnected))
            Log.i(TAG, "[TabletMixerConfigActivity] onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[HomeFragment] ACT onBondedDevices")
            knowDevices?.forEach {
                Log.i(TAG, "[HomeFragment] knowDevices: ${it.name} | ${it.address}")
            }
            knowDevices = device

            if(firstIn){
                bluetoothDeviceSelected = getSelectedTabletMixerBluetoothDevice()
                if(bluetoothDeviceSelected!=null){
                    Log.d(TAG,"bluetoothDeviceSelected: " + bluetoothDeviceSelected.toString())
                    mService!!.LocalBinder().connectKnowDeviceWithTransfer(bluetoothDeviceSelected!!)
                    firstIn = false
                }
            }

        }

    }


    private fun bluetoothDialog() {
        val opcion0 = arrayOfNulls<String>(2)
        opcion0[0] = this.getString(R.string.equipos_vinculados)
        opcion0[1] = this.getString(R.string.buscar_equipos)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Buscar balanzas cercanas")
        builder.setItems(opcion0) { _, which ->
            when (which) {
                0 -> {
                    equiposVinculados()
                }
                1 -> {
                    if(mTabletMixerDetails!=null){
                        linkDevice(mTabletMixerDetails!!)
                    }
                    else{
                        Log.i(TAG,"mTabletMixerDetail = null")
                    }
                }
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun selectDeviceDialog(devices: MutableSet<BluetoothDevice>): AlertDialog {
        val dispositivos = arrayOfNulls<String>(devices.size)
        var i = 0
        for (device in devices) {
            dispositivos[i] = device.name
            i++
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Equipos encontrados")
        val newDevices = arrayOfNulls<BluetoothDevice>(devices.size)
        i = 0
        for (device in devices) {
            newDevices[i] = device
            i++
        }

        builder.setItems(
            dispositivos
        ) { dialog, which ->
            bluetoothDeviceSelected = newDevices[which]!!
            mTabletMixerDetails?.mac = bluetoothDeviceSelected?.address.toString()
            mTabletMixerDetails?.btBox = dispositivos[which].toString()
            mTabletMixerViewModel.update(mTabletMixerDetails!!)
            mBinding.tvCajaBluetoothAsoc.text = mTabletMixerDetails?.btBox
            mBinding.tvMac.text = mTabletMixerDetails?.mac
            Log.i(
                "SELECTED",
                "Se selecciono: " + bluetoothDeviceSelected!!.name + "   " + bluetoothDeviceSelected!!.address
            )
            dialog.dismiss()
        }

        return builder.create()
    }

    private fun equiposVinculados() {
        val bluetoothDevicesKnow = BluetoothAdapter.getDefaultAdapter().bondedDevices
        val devices = arrayOfNulls<BluetoothDevice>(bluetoothDevicesKnow.size)
        val dispositivos = arrayOfNulls<String>(bluetoothDevicesKnow.size)
        var i = 0
        for (device in bluetoothDevicesKnow) {
            dispositivos[i] = device.name
            devices[i] = device
            i++
        }
        if (i == 0) {
            Toast.makeText(this, "No hay equipos", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Dispositivo")
        builder.setItems(dispositivos) { dialog, which ->
            bluetoothDeviceSelected = devices[which]!!
            if(mTabletMixerDetails != null){
                mTabletMixerDetails?.mac = bluetoothDeviceSelected?.address.toString()
                mTabletMixerDetails?.btBox = dispositivos[which].toString()
                mTabletMixerViewModel.update(mTabletMixerDetails!!)
            }
            mBinding.tvCajaBluetoothAsoc.text = dispositivos[which].toString()
            mBinding.tvMac.text = bluetoothDeviceSelected?.address.toString()
            Log.i("SELECTED","Se selecciono: " + bluetoothDeviceSelected!!.name + "   " + bluetoothDeviceSelected!!.address)
            dialog.dismiss()
            //**********************************************************************
        }
        val alertDialog = builder.create()
        alertDialog.window!!
            .setBackgroundDrawable(ColorDrawable(Color.rgb(1, 116, 147)))
        alertDialog.show()
    }


    fun showCustomProgressDialog(){
        if(mProgressDialog != null && mProgressDialog!!.isShowing){
            return
        }
        mProgressDialog = Dialog(this)
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.let {progressDialog->
            progressDialog.setContentView(R.layout.dialog_custom_progress)
            progressDialog.show()
            val textMessage : TextView = progressDialog.findViewById(android.R.id.message)
            textMessage.text = "Conectando con Visor Remoto"
            textMessage.gravity = Gravity.CENTER
            Handler(Looper.getMainLooper()).postDelayed({
                if (progressDialog.isShowing){
                    progressDialog.dismiss()
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle("Alerta")
                    builder.setMessage("No se pudo establecer la comunicación con el visor remoto")

                    builder.setPositiveButton(android.R.string.yes) { _, _ ->
                        progressDialog.dismiss()
                    }
                    builder.show()
                }
            }, 10000)
        }
    }

    fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }



    fun changeStatusConnection(sucess: Boolean){
        runOnUiThread {
            if (sucess) {
                if(isConnected){
                    return@runOnUiThread
                }
                showSnackbar = true
                deviceConnected()
                snackbar?.dismiss()
            } else {
                deviceDisconnected()
                if (showSnackbar){
                    showSnackbar = false
                    snackbar =
                        Snackbar.make(mBinding.viewActivityTabletMixerConfig, "Se perdió la conexión",
                            BaseTransientBottomBar.LENGTH_INDEFINITE
                        )
                    snackbar?.setAction(
                        "Reconectar"
                    ) {
                        bluetoothDeviceSelected.let {
                            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it!!)
                        }
                        showCustomProgressDialog()
                        Timer().schedule(5000) {
                            hideCustomProgressDialog()
                            showSnackbar = true
                            changeStatusConnection(false)
                        }
                    }
                    snackbar?.show()
                }

            }
        }
    }

    private fun deviceDisconnected() {
        mBinding.bluetoothIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_disconnected))
        isConnected = false
    }

    private fun deviceConnected() {
        mBinding.bluetoothIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_connected))
        isConnected = true
    }

    /**
     *
     * Funciones sacasdas de AddUopdateTabletMixerActivity
     */

    fun saveMremoteViewer(){
        val remoteViewerName = mBinding.etTabletMixerName.text.toString().trim { it <= ' ' }
        val remoteViewerDescription = mBinding.etTabletMixerDescription.text.toString().trim { it <= ' ' }
        val remoteViewerMac = mBinding.tvMac.text.toString().trim { it <= ' ' }
        val remoteViewerBtBox = mBinding.tvCajaBluetoothAsoc.text.toString().trim { it <= ' ' }

        when {
            TextUtils.isEmpty(remoteViewerName) -> {
                Toast.makeText(
                    this@TabletMixerConfigActivity,
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
                    0,
                    updatedDate,
                    "",
                    linked,
                    remoteViewerId
                )


                runBlocking {
                    if (remoteViewerId == 0L){
                        tabletMixer.id = mTabletMixerViewModel.insertSync(tabletMixer)
                        mTabletMixerDetails = tabletMixer
                        Toast.makeText(this@TabletMixerConfigActivity, "TabletMixer guardado", Toast.LENGTH_SHORT).show()
                    } else {
                        mTabletMixerViewModel.updateSync(tabletMixer)
                        Toast.makeText(this@TabletMixerConfigActivity, "TabletMixer actualizado", Toast.LENGTH_SHORT).show()
                    }
                    Log.i("SYNC", "Se actualiza tablet_mixer con fecha ${tabletMixer.updatedDate}")
                }

            }
        }
    }

    fun deleteTabletMixer(tablet_mixer: TabletMixer){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.title_delete_tablet_mixer))
        builder.setMessage(resources.getString(R.string.msg_delete_tablet_mixer_dialog, tablet_mixer.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mTabletMixerViewModel.delete(tablet_mixer)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun toggleKeyboard() {
        if(isKeyBoardShowing()){
            hideSoftKeyboard()
        }else{
            showKeyboard()
        }
    }

    fun Activity.hideSoftKeyboard() {
        val view = mBinding.root
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
        val view: View = mBinding.root
        return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
    }

    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarConfigTabletMixer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.let {
            it.title = resources.getString(R.string.screen_tablet_mixer_detail)
        }

        mBinding.toolbarConfigTabletMixer.setNavigationOnClickListener {
            onBackPressed()
        }
//        toolbar.menu.getItem(R.id.action_hide_keyboard).isVisible = false
    }
}

