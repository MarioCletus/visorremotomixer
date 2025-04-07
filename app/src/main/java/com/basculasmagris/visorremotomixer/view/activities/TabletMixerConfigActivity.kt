package com.basculasmagris.visorremotomixer.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityTabletMixerConfigBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.MinUser
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.schedule


class TabletMixerConfigActivity : AppCompatActivity(){

    private var macaddress: String = ""
    private var contMensajes: Int = 0
    private var TAG = "DEBConfig"
    private var firstIn: Boolean = false
    private lateinit var mBinding: ActivityTabletMixerConfigBinding

    private var isSearching: Boolean = false
    private var knowDevices: List<BluetoothDevice>? = null
    private var tabletMixerReceibed: TabletMixer? = null

    //*******************************************************************
    //Sincronizacion
    var listOfMedRoundsRun: ArrayList<MedRoundRunDetail> = ArrayList()
    var listOfMinUsers: ArrayList<MinUser> = ArrayList()
    var listOfMixers: ArrayList<MixerDetail> = ArrayList()
    private var mLocalUsers: List<User>? = null
    private var mLocalRoundsLocal: List<RoundLocal>? = null

    private var bSyncroUsers = false
    private var bSyncroRounds = false
    private var bSyncroMixers = false
    private var bIsDetail: Boolean = false
    private var bIsFirstIn: Boolean = false
    //*******************************************************************

    // Bluetooth
    var mService: BluetoothSDKService? = null
    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((this.application as SpiMixerVRApplication).tabletMixerRepository)
    }
    private val mRoundLocalViewModel: RoundLocalViewModel by viewModels {
        RoundLocalViewModelFactory((this.application as SpiMixerVRApplication).roundLocalRepository)
    }
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((this.application as SpiMixerVRApplication).userRepository)
    }
    private var mLocalTabletMixers: List<TabletMixer>? = null
    private var selectedTabletMixer: TabletMixer? = null

    private var mProgressDialog: Dialog? = null
    private var dialog: AlertDialog? = null    // Bluetooth
    private var mBound: Boolean = false
    private var isConnected: Boolean = false

    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    private var selectedBluetoothDevice : BluetoothDevice? = null
    var dialogCustomListBinding: DialogCustomListBinding? = null
    private lateinit var mCustomListDialog : Dialog

    private val handlerBaliza = Handler(Looper.getMainLooper())
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            sendBeacon()
            handlerBaliza.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTabletMixerConfigBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (intent.hasExtra("BTDEVICE")) {
            selectedBluetoothDevice = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("BTDEVICE",  BluetoothDevice::class.java)
            } else {
                intent.getParcelableExtra< BluetoothDevice>("BTDEVICE")
            }
            Log.i(TAG,"BTDEVICE $selectedBluetoothDevice")
        }
        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            tabletMixerReceibed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS,  TabletMixer::class.java)
            } else {
                intent.getParcelableExtra< TabletMixer>(Constants.EXTRA_MIXER_DETAILS)
            }
            Log.i(TAG,"mTabletMixerDetails $tabletMixerReceibed")
        }
        if (intent.hasExtra(Constants.EXTRA_MIXER_MODE)) {
            bIsDetail = intent.getBooleanExtra(Constants.EXTRA_MIXER_MODE, false)
            Log.i(TAG,"bIsDetail $bIsDetail")
        }
        if (intent.hasExtra(Constants.FIRST_IN)) {
            bIsFirstIn = intent.getBooleanExtra(Constants.FIRST_IN, false)
            if(bIsFirstIn){
                mBinding.llSyncProgress.visibility = View.VISIBLE
                getLocalData()
            }
            Log.i(TAG,"bIsFirstIn $bIsFirstIn")
        }

        setupActionBar()

        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_mixer_config, menu)
                checkBTStatus()
                if(bIsDetail){
                    menu.findItem(R.id.action_hide_keyboard)?.isVisible = false
                    menu.findItem(R.id.action_save_mixer)?.isVisible = false
                }
                if(bIsFirstIn){
                    menu.findItem(R.id.action_save_mixer)?.isVisible = false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_mixer -> {
                        if(!isKeyBoardShowing()){
                            if(bIsFirstIn)
                                return true
                            saveTabletMixer()
                            cleanObservers()
                            finish()
                        }else
                            hideSoftKeyboard()
                        return true
                    }
                    R.id.menu_selected_remote_tablet -> {
                        Log.i(TAG,"try to connect " )
                        selectedBluetoothDevice?.let {
                            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                            showCustomProgressDialog()
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

        mBinding.etTabletMixerName.setText( tabletMixerReceibed?.name ?: "")
        mBinding.etTabletMixerDescription.setText( tabletMixerReceibed?.description?: "")


        mBinding.tvCajaBluetoothAsoc.setText(tabletMixerReceibed?.btBox?: "")
        macaddress = tabletMixerReceibed?.mac?: ""

        mBinding.layBTAsoc.setOnClickListener {
            val tabletMixerName = mBinding.etTabletMixerName.text.toString().trim { it <= ' ' }
            if(tabletMixerName.isNullOrEmpty()){
                Toast.makeText(
                    this@TabletMixerConfigActivity,
                    resources.getString(R.string.err_msg_tablet_mixer_name),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if(tabletMixerReceibed == null){
                val tabletMixerDescription = mBinding.etTabletMixerDescription.text.toString().trim { it <= ' ' }
                val tabletMixerMac = macaddress
                val tabletMixerBtBox = mBinding.tvCajaBluetoothAsoc.text.toString().trim { it <= ' ' }
                val remoteId = 0L
                val rfId = 0L
                val linked = false
                val tabletMixerId = 0L
                tabletMixerReceibed = TabletMixer(
                    tabletMixerName,
                    tabletMixerDescription,
                    tabletMixerMac,
                    tabletMixerBtBox,
                    remoteId,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(Date()),
                    "",
                    linked,
                    tabletMixerId)

            }
            bluetoothDialog()
        }


        mBinding.btnSincro.setOnClickListener{
            GlobalScope.launch (Dispatchers.Main) {

                syncData()
            }
        }

        mService = Helper.getServiceInstance().getBluetoothService()
        selectedBluetoothDevice?.let{
            Log.i(TAG, "*********** onServiceConnected [TabletMixerConfigActivity] CONECTADO ${it.toString()} ${it.address} | $mService" )
            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
        }

        if(mService == null){
            bindBluetoothService()
        }
        Log.i("BLUE", "Se inicia la búsqueda de dispositivos asociados")
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(this, mBluetoothListener)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun cleanObservers(){
        mLocalUsers = null
        mLocalRoundsLocal = null
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(applicationContext, mBluetoothListener)
    }


    override fun onPause() {
        Log.i(TAG,"onPause")
        if(mService?.isConnected() == true){
            showDeviceConnected()
        }else{
            showDeviceDisconnected()
        }
        super.onPause()
    }


    override fun onStart() {
        Log.i(TAG,"onStart")
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        checkBTStatus()
        mService?.LocalBinder()?.getBondedDevices()
    }

    private fun checkBTStatus() {
        if(Helper.isBluetoothEnabled){
            Log.i(TAG,"onResume connected")
            showDeviceConnected()
        }else{
            Log.i(TAG,"onResume disconnected")
            showDeviceDisconnected()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerBaliza.removeCallbacks(runnable)
        Log.i(TAG,"onDestroy")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(applicationContext, mBluetoothListener)
    }

    private fun linkDevice(tabletMixer: TabletMixer){
        if(isSearching)
            return
        selectedTabletMixer = tabletMixer
        dialog = Helper.setProgressDialog(this, "Buscando tabletMixer...")
        dialog?.show()
        Log.i(TAG,"buscando..")
        mService?.LocalBinder()?.startDiscovery(this)
        Timer().schedule(10000){
            dialog?.cancel()
            mService?.LocalBinder()?.stopDiscovery()
        }
    }

    private fun getSelectedTabletMixerBluetoothDevice() : BluetoothDevice? {
        Log.i(TAG, "[getSelectedTabletMixerBluetoothDevice] Local tabletMixer: ${knowDevices?.size} | mLocalTabletMixers: ${mLocalTabletMixers?.size}")
        knowDevices?.forEach { bluetoothDevice ->

            if(selectedTabletMixer?.mac == bluetoothDevice.address){
                return bluetoothDevice
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
            Log.i(TAG, "*********** onServiceConnected [TabletMixerConfigActivity] CONECTADO " )
            selectedBluetoothDevice?.let{
                Log.i(TAG, "*********** onServiceConnected [TabletMixerConfigActivity] CONECTADO ${it.toString()} ${it.address} | $mService" )
                mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
            }
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i("BLUE", "*********** [TabletMixerConfigActivity]  DESCONECTADO")
            mBound = false
        }
    }

    private var countMessage = 0
    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "[TabletMixerListFragment] onDiscoveryStarted")
            customItemsDialog("Dispositivos disponibles", ArrayList(), Constants.DEVICE_REF)        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[TabletMixerConfigActivity] onDiscoveryStopped")
            mCustomListDialog.dismiss()
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            device?.let { currentDevice ->
                Log.i(TAG, "onDeviceDiscovered $currentDevice")
                allBluetoothDevice.add(device)

                val name = if(getBluetoothName(currentDevice).isEmpty()) getString(R.string.no_identificado) else getBluetoothName(currentDevice)
                val mac = if (getBluetoothAddress(currentDevice).isEmpty()) "00:00:00:00" else getBluetoothAddress(currentDevice)

                val item = CustomListItem(0L, 0, name, mac, R.drawable.icon_balance)
                (dialogCustomListBinding?.rvList?.adapter as CustomListItemAdapter).updateList(item)

            }
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            Log.i(TAG, "[TabletMixerConfigActivity] onDeviceConnected")
            changeStatusConnected()
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }

            val messageStr = String(message,0, message.size)
            if(countMessage++>20){
                Log.d("message","Tablet config message $messageStr")
                countMessage = 0
            }
            val command = messageStr.substring(0,3)
            Log.i("command","Tablet config command $command")

            when (command){
                Constants.CMD_USER_LIST->{
                    Log.i("showCommand","CMD_USER_LIST")
                    bSyncroUsers = refreshUsers(message)
                    if(bSyncroUsers){
                        mBinding.pbUsers.progress = 100
                        mBinding.tvUsersPercentage.text = "100%"
                    }
                    bSyncroUsers = false
                }

                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS TMCA")
                    bSyncroRounds = refreshRounds(message)
                    if(bSyncroRounds){
                        mBinding.pbRounds.progress = 100
                        mBinding.tvRoundsPercentage.text = "100%"
                        saveTabletMixer()
                    }
                    bSyncroRounds = false
                }

                Constants.CMD_MIXERS->{
                    Log.i("showCommand","CMD_MIXERS")
                    bSyncroMixers = refreshMixer(message)
                    bSyncroMixers = false
                }
            }

        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            if(message == null){
                return
            }
            hideCustomProgressDialog()
            if(contMensajes++ >20){
                Log.i("message", "[TabletMixerConfigActivity] onMessageReceived: $message")
                contMensajes = 0
//                deviceConnected()
            }
            changeStatusConnected()
            message.let{
            }
        }

        override fun onMessageSent(device: BluetoothDevice?, message: String?) {
            Log.i("send", "[TabletMixerConfigActivity] onMessageSent")
        }

        override fun onCommandSent(device: BluetoothDevice?, message: ByteArray?) {
            Log.i("send", "[TabletMixerConfigActivity] onCommandSent")

        }

        override fun onError(message: String?) {
            changeStatusDisconnected()
            Log.i(TAG, "[TabletMixerConfigActivity] onError")
        }

        override fun onDeviceDisconnected() {
            changeStatusDisconnected()
            Log.i(TAG, "[TabletMixerConfigActivity] onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[TabletMixerConfigActivity] ACT onBondedDevices")
            knowDevices?.forEach {
                Log.i(TAG, "knowDevices bfr: ${it.toString()} | ${it.address}")
            }
            knowDevices = device
            knowDevices?.forEach {
                Log.i(TAG, "knowDevices aft: ${it.toString()} | ${it.address}")
            }

            if(firstIn && knowDevices != null){

                selectedBluetoothDevice = knowDevices?.firstOrNull { bluetoothDevice ->
                    selectedTabletMixer?.mac == bluetoothDevice.address
                }

                selectedBluetoothDevice = getSelectedTabletMixerBluetoothDevice()
                selectedBluetoothDevice?.let{
                    Log.d(TAG,"selectedBluetoothDevice: " + it.toString())
                    mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                    firstIn = false
                }
            }

        }
    }


    private fun bluetoothDialog() {
        val opcion0 = arrayOfNulls<String>(2)
        opcion0[0] = this.getString(R.string.tablets_vinculadas)
        opcion0[1] = this.getString(R.string.buscar_tablets)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.conectar_tablets_cercanas))
        builder.setItems(opcion0) { _, which ->
            when (which) {
                0 -> {
                    equiposVinculados()
                }
                1 -> {
                    tabletMixerReceibed?.let {
                        linkDevice(it)
                    }
                }
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun equiposVinculados() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothDevicesKnow = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothManager.adapter.bondedDevices
            }else{
                (emptyList <BluetoothDevice>())
            }
        }else{
            bluetoothManager.adapter.bondedDevices
        }

        val devices = arrayOfNulls<BluetoothDevice>(bluetoothDevicesKnow.size)
        val dispositivos = arrayOfNulls<String>(bluetoothDevicesKnow.size)
        var i = 0
        for (device in bluetoothDevicesKnow) {
            dispositivos[i] = device.name
            devices[i] = device
            i++
        }
        if (i == 0) {
            Toast.makeText(this, getString(R.string.no_hay_dispositivos), Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dispositivo))
        builder.setItems(dispositivos) { dialog, which ->
            selectDevice(devices[which])
            dialog.dismiss()
            //**********************************************************************
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun selectDevice(device : BluetoothDevice?) {
        handlerBaliza.post(runnable)
        selectedBluetoothDevice = device
        if(tabletMixerReceibed != null){
            tabletMixerReceibed?.mac = selectedBluetoothDevice?.address.toString()

            tabletMixerReceibed?.btBox =  getBluetoothName(device)


            mTabletMixerViewModel.update(tabletMixerReceibed!!)
        }
        macaddress = tabletMixerReceibed?.mac.toString()
        mBinding.tvCajaBluetoothAsoc.setText(getBluetoothName(device) + "   " + getBluetoothAddress(device) )
        Log.i(TAG,"Se seleccionó: " + selectedBluetoothDevice + "   " + getBluetoothAddress(device))


        selectedBluetoothDevice?.let{
            mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
            showCustomProgressDialog()
        }
    }


    fun showCustomProgressDialog(){
        if(mProgressDialog?.isShowing == true){
            return
        }
        mProgressDialog = Dialog(this)
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
            Handler(Looper.getMainLooper()).postDelayed({
                if (it.isShowing){
                    it.dismiss()
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.warning))
                    val strMixer = getString(R.string.la_tablet_de_mixer)
                    builder.setMessage(getString(R.string.no_se_pudo_establecer_comunicacion_con,strMixer,""))

                    builder.setPositiveButton(android.R.string.yes) { _, _ ->
                        it.dismiss()
                    }

                    builder.show()
                }
            }, 10000)
        }

    }

    fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    fun changeStatusConnected(){
        hideCustomProgressDialog()
        Log.v("con_status","TabletMixerConfigActivity connected")
        showDeviceConnected()
        Helper.saveBluetoothState(true)
    }

    private var isActionRunning = false
    fun changeStatusDisconnected(){
        Log.i("con_status","TabletMixerConfigActivity disconnected")
        Helper.saveBluetoothState(false)

        showDeviceDisconnected()
        if(!isActionRunning){
            isActionRunning = true
            Handler(Looper.getMainLooper()).postDelayed({
                connectDevice(selectedBluetoothDevice)
                isActionRunning = false
            }, 250)
        }
    }

    private fun connectDevice(bluetoothDevice: BluetoothDevice?){

        if(mService?.isConnected() == true){
            changeStatusConnected()
            return
        }

        bluetoothDevice?.let {deviceBluetooth->
            Log.i(TAG,"connectDevice connectKnowDeviceWithTransfer $deviceBluetooth")
            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(deviceBluetooth)

        }

        Handler(Looper.getMainLooper()).postDelayed({
            connectDevice(bluetoothDevice)
        }, 2000)
    }

    private fun showDeviceDisconnected() {
        mBinding.toolbarConfigTabletMixer.menu?.let {
            val menuItem = it.findItem(R.id.menu_selected_remote_tablet)
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_tablet_disconnected_48px)
        }
        isConnected = false
    }

    private fun showDeviceConnected() {
        mBinding.toolbarConfigTabletMixer.menu?.let {
            val menuItem = it.findItem(R.id.menu_selected_remote_tablet)
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_tablet_connected_48px)
        }
        isConnected = true
    }

    /**
     *
     * Funciones sacasdas de AddUopdateTabletMixerActivity
     */

    fun saveTabletMixer(){
        if(bIsDetail){
            return
        }
        val tabletMixerName = mBinding.etTabletMixerName.text.toString().trim { it <= ' ' }
        val tabletMixerDescription = mBinding.etTabletMixerDescription.text.toString().trim { it <= ' ' }
        var remoteId = 0L
        val tabletMixerBtBox = mBinding.tvCajaBluetoothAsoc.text.toString().trim { it <= ' ' }
        var tabletMixerMac = macaddress

        tabletMixerReceibed?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(tabletMixerName) -> {
                Toast.makeText(
                    this@TabletMixerConfigActivity,
                    resources.getString(R.string.err_msg_tablet_mixer_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                if(tabletMixerMac.isEmpty()){
                    tabletMixerMac = selectedBluetoothDevice?.address.toString()
                }
                var tabletMixerId = 0L
                var updatedDate = ""
                var linked = false

                tabletMixerReceibed?.let {
                    if (it.id != 0L){
                        tabletMixerId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        linked = it.linked == true
                    }
                }

                val tabletMixer = TabletMixer(
                    tabletMixerName,
                    tabletMixerDescription,
                    tabletMixerMac,
                    tabletMixerBtBox,
                    remoteId,
                    updatedDate,
                    "",
                    linked,
                    tabletMixerId
                )

                runBlocking {
                    if (tabletMixerId == 0L){
                        tabletMixer.id = mTabletMixerViewModel.insertSync(tabletMixer)
                        tabletMixerReceibed = tabletMixer
                        Toast.makeText(this@TabletMixerConfigActivity, getString(R.string.guardado), Toast.LENGTH_SHORT).show()
                    } else {
                        mTabletMixerViewModel.updateSync(tabletMixer)
                        Toast.makeText(this@TabletMixerConfigActivity, getString(R.string.actualizado), Toast.LENGTH_SHORT).show()
                    }

                    result(tabletMixer)
                    if(bIsFirstIn){
                        lifecycleScope.launch(Dispatchers.IO){
                            datastore.edit { preferences->
                                preferences[longPreferencesKey("IDTABLET")] = tabletMixer.id
                            }
                        }
                        cleanObservers()
                        finish()
                    }
                }

            }

        }

    }

    private fun result(tabletMixer: TabletMixer){
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.EXTRA_MIXER_DETAILS, tabletMixer)
        setResult(Activity.RESULT_OK, resultIntent)
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

    fun Activity.showKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethod.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }


    fun isKeyBoardShowing(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val view: View = mBinding.root
            return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
        }else{
            val view: View = mBinding.root
            val decorView = view.rootView
            val rootViewHeight = decorView.height
            val insets = decorView.rootWindowInsets
            return rootViewHeight > insets.stableInsetBottom + insets.stableInsetTop
        }
    }


    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarConfigTabletMixer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.let {
            if(bIsDetail){
                it.title = resources.getString(R.string.screen_tablet_mixer_detail)
            }else{
                it.title = resources.getString(R.string.screen_tablet_mixer_config)
            }
        }

        mBinding.toolbarConfigTabletMixer.setNavigationOnClickListener {
            Log.i(TAG,"onBackPressed")
            if(!bIsDetail){
                saveTabletMixer()
            }else{
                tabletMixerReceibed?.let {
                    Log.i(TAG,"result $it")
                    result(it)
                }
            }
            cleanObservers()
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        tabletMixerReceibed?.let {
            result(it)
        }
        super.onBackPressed()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        selectedBluetoothDevice = allBluetoothDevice.firstOrNull { device ->
            device.address == item.description
        }
        mBinding.tvCajaBluetoothAsoc.setText(getBluetoothName(selectedBluetoothDevice))

        selectedBluetoothDevice?.let { bluetoothDevice ->
            when (selection){
                Constants.DEVICE_REF -> {
                    Log.i(TAG,"Dispositivo seleccionado ${bluetoothDevice.toString()}")
                    selectDevice(bluetoothDevice)
                }
                else -> {}
            }
        }
        mCustomListDialog.dismiss()

    }

    private fun customItemsDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(this)
        dialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(dialogCustomListBinding!!.root)
        dialogCustomListBinding?.tvTitle?.text = title
        dialogCustomListBinding?.rvList?.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this, itemsList.toMutableList(), selection)
        dialogCustomListBinding?.rvList?.adapter = adapter
        mCustomListDialog.show()
    }


    fun bluetoothConnectionError(){
        Log.i(TAG,"bluetoothConnectionError")
        runOnUiThread {
            changeStatusDisconnected()
        }
    }

    fun bluetoothConnectionSuccess(){
        Log.i(TAG,"bluetoothConnectionSuccess")
        runOnUiThread {
            changeStatusConnected()
        }
    }

    fun getBluetoothName(btDevice: BluetoothDevice?): String {
        var name = ""
        btDevice?.let{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (ActivityCompat.checkSelfPermission(
                        this@TabletMixerConfigActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    name = it.name
                }
            }else{
                name = it.name
            }
        }
        return name
    }

    fun getBluetoothAddress(btDevice: BluetoothDevice?): String {
        var address = ""
        btDevice?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (ActivityCompat.checkSelfPermission(
                        this@TabletMixerConfigActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    address = it.address
                }
            }else{
                address = it.address
            }
        }
        return address
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mUserViewModel.allUserList) {
            if (it != null) {
                liveDataMerger.value = UserData(it)
            }
        }
        liveDataMerger.addSource(mRoundLocalViewModel.allRoundLocalList) {
            if (it != null) {
                liveDataMerger.value = RoundLocalData(it)
            }
        }

        return liveDataMerger
    }
    private fun getLocalData(){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(this, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is UserData -> mLocalUsers = it.users
                    is RoundLocalData -> mLocalRoundsLocal = it.roundsLocal
                    else -> {}
                }

                if (mLocalUsers != null && mLocalRoundsLocal != null) {
                    Log.i(TAG, "Rondas: ${mLocalRoundsLocal?.size}  Usuarios: ${mLocalUsers?.size}")
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    fun refreshUsers(message: ByteArray):Boolean {
        try{
            val convertZip = ConvertZip()
            val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<MinUser>>() {}.type
            listOfMinUsers = gson.fromJson<ArrayList<MinUser>>(json, listType)?:ArrayList()
            listOfMinUsers.forEach{ minUser ->
                val isUser = mLocalUsers?.firstOrNull{
                    it.id == minUser.id
                }
                val user = User(
                    username = minUser.username,
                    name = minUser.name,
                    lastname = minUser.lastname,
                    mail = "",
                    password = minUser.password,
                    remoteId = minUser.remoteId,
                    updatedDate = "",
                    archiveDate = null,
                    codeRole = minUser.codeRole,
                    codeClient = "",
                    id = minUser.id
                )
                Log.i(TAG,"user $user")
                if(isUser == null){
                    mUserViewModel.insert(user)
                }else{
                    mUserViewModel.update(user)
                }
            }

            return true
        }catch (e: NumberFormatException){
            Log.i(TAG,"bSyncroUsers NumberFormatException $e")
            return false
        }catch (e:Exception){
            Log.i(TAG,"bSyncroUsers Exception $e")
            return false
        }
    }

    fun refreshRounds(message: ByteArray): Boolean {
        try{
            val convertZip = ConvertZip()
            val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<MedRoundRunDetail>>() {}.type
            listOfMedRoundsRun = gson.fromJson<ArrayList<MedRoundRunDetail>>(json, listType)?:ArrayList()
            Log.i(TAG,"listOfMinRounds $listOfMedRoundsRun")

            listOfMedRoundsRun.forEach{ minRound ->
                val isRound = mLocalRoundsLocal?.firstOrNull{
                    it.id == minRound.round.id
                }
                val roundLocal = RoundLocal(
                    name = minRound.round.name,
                    description = minRound.round.description,
                    remoteId = minRound.round.remoteId,
                    startDate = minRound.startDate,
                    endDate = minRound.endDate,
                    progress = minRound.progress,
                    status = minRound.status,
                    tabletMixerId = minRound.round.id,
                    tabletMixerMac = tabletMixerReceibed?.mac?:"",
                    id = isRound?.id?:0L
                )
                if(isRound == null){
                    mRoundLocalViewModel.insert(roundLocal)
                }else{
                    mRoundLocalViewModel.update(roundLocal)
                }
            }

            return true
        }catch (e: NumberFormatException){
            Log.i(TAG,"bSyncroRounds NumberFormatException $e")
            return false
        }catch (e:Exception){
            Log.i(TAG,"bSyncroRounds Exception $e")
            return false
        }

    }


    fun refreshMixer(message: ByteArray): Boolean {
        try{
            val convertZip = ConvertZip()
            val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<MixerDetail>>() {}.type
            listOfMixers = gson.fromJson<ArrayList<MixerDetail>>(json, listType)?:ArrayList()
            Log.i(TAG,"listOfMinMixers $listOfMixers")
            return true
        }catch (e: NumberFormatException){
            Log.i(TAG,"bSyncroMixers NumberFormatException $e")
            return false
        }catch (e:Exception){
            Log.i(TAG,"bSyncroMixers Exception $e")
            return false
        }

    }

    fun requestListOfUsers() {
        val byteArray = "CMD${Constants.CMD_USER_LIST}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun requestListOfRounds() {
        val byteArray = "CMD${Constants.CMD_ROUNDS}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun requestListOfMixers() {
        val byteArray = "CMD${Constants.CMD_MIXERS}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    private suspend fun syncData(){
        requestListOfUsers()
        delay(1000)
        requestListOfRounds()
    }

    fun sendBeacon() {
        val byteArray = "CMD${Constants.CMD_BEACON}${String.format("%06d",0)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }
}

