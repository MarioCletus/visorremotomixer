package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.size
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityMixerConfigBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.view.fragments.ConfirmDialogFragment
import com.basculasmagris.visorremotomixer.view.fragments.InputValueDialogFragment
import com.basculasmagris.visorremotomixer.view.fragments.SeleccionCalibracionDlgFragment
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.math.roundToInt

class MixerConfigActivity : AppCompatActivity(),ConfirmDialogFragment.OnConfirmListener,InputValueDialogFragment.ValueInputListener,SeleccionCalibracionDlgFragment.OnSeleccionCalibracionListener{
    private var macaddress: String = ""
    private var bIsDetail: Boolean = false
    private var contMensajes: Int = 0
    private var calibrationTara: Double = 0.0
    private var knowWeight : Double = 0.0
    private var valueWeightInMixer: Double = 0.0
    private var TAG = "DEBConfig"
    private var firstIn: Boolean = false
    private lateinit var mBinding: ActivityMixerConfigBinding

    private val CALIB_STEP_1 = 1
    private val CALIB_STEP_2 = 2
    private val CALIB_STEP_3 = 3
    private val CALIB_STEP_4 = 4
    private val DIALOG_1 = 1
    private val DIALOG_2 = 2
    private val DIALOG_3 = 3
    private val DIALOG_4 = 4
    private val DIALOG_5 = 5

    private val FROM_TOLVA = 0x20
    private val FROM_OTRA_BALANZA = 0x40

    private val divisionesExternas = arrayOf(1, 2, 5, 10, 20)
    private var arrayAdapter: ArrayAdapter<Int>? = null
    private var calibrating: Boolean = false

    private var isSearching: Boolean = false
    private var divExt: Int = 1
    private var knowDevices: List<BluetoothDevice>? = null
    private var mMixerDetails: Mixer? = null
    private var weight: Double = 0.0
    private var dataReceive : Double = 0.0
    private var tara : Double = 0.0

    // Bluetooth
    var mService: BluetoothSDKService? = null
    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((this.application as SpiMixerApplication).mixerRepository)
    }
    private var mLocalMixers: List<Mixer>? = null
    private var mDevicesFound : MutableSet<BluetoothDevice> = mutableSetOf()
    private var selectedMixer: Mixer? = null

    private var mProgressDialog: Dialog? = null
    private var dialog: AlertDialog? = null    // Bluetooth
    private var mBound: Boolean = false

    private var snackbar: Snackbar? = null
    private var isConnected: Boolean = false
    var showSnackbar = true

    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    private var selectedBluetoothDevice : BluetoothDevice? = null
    var dialogCustomListBinding: DialogCustomListBinding? = null
    private lateinit var mCustomListDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMixerConfigBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (intent.hasExtra("BTDEVICE")) {
            selectedBluetoothDevice = intent.getParcelableExtra("BTDEVICE")
            Log.i(TAG,"BTDEVICE $selectedBluetoothDevice")
        }
        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            mMixerDetails = intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS)
            Log.i(TAG,"mMixerDetails $mMixerDetails")
        }
        if (intent.hasExtra(Constants.EXTRA_MIXER_MODE)) {
            bIsDetail = intent.getBooleanExtra(Constants.EXTRA_MIXER_MODE, false)
            Log.i(TAG,"bIsDetail $bIsDetail")
        }

        setupActionBar()
        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_mixer_config, menu)
                if(bIsDetail){
                    menu.getItem(1).isVisible = false
                    menu.getItem(2).isVisible = false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_mixer -> {
                        if(!isKeyBoardShowing()){
                            saveMixer()
                            mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                            BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(applicationContext, mBluetoothListener)
                            finish()
                        }else
                            hideSoftKeyboard()
                        return true
                    }
                    R.id.icon_bluetooth -> {
                        Log.i(TAG,"icon_bluetooth pressed $isConnected   | ${selectedBluetoothDevice?.name}" )
                        if(!isConnected && selectedBluetoothDevice != null){
                            Log.i(TAG,"try to connect " )
                            snackbar?.dismiss()
                            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(selectedBluetoothDevice!!)
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
            mBinding.btnCalibrate.visibility = View.GONE
            mBinding.btnTara.visibility = View.GONE
            mBinding.etMixerName.isEnabled  = false
            mBinding.etMixerName.isEnabled  = false
            mBinding.etMixerDescription.isEnabled  = false
            mBinding.etCapacity.isEnabled  = false
            mBinding.layKnowWeight.isEnabled  = false
            mBinding.layCapacity.isEnabled  = false
            mBinding.layBTAsoc.isEnabled  = false
        }

        mBinding.etMixerName.setText( mMixerDetails?.name ?: "")
        mBinding.etMixerDescription.setText( mMixerDetails?.description?: "")
        if(mMixerDetails != null && mMixerDetails?.calibration != null) {
            mBinding.tvCalibration.setText(mMixerDetails?.calibration.toString())
        }else{
            mBinding.tvCalibration.setText("1")
        }


        mBinding.tvCajaBluetoothAsoc.setText(mMixerDetails?.btBox?: "")
        macaddress = mMixerDetails?.mac?: ""
        if(mMixerDetails != null && mMixerDetails?.tara != null) {
            mBinding.tvTara.setText(mMixerDetails?.tara?.roundToInt().toString())
        }else{
            mBinding.tvTara.setText("0")
        }
        mBinding.tvPesoConocido.setText("100")

        mBinding.tvCajaBluetoothAsoc.setOnClickListener {
            if(mMixerDetails == null){
                val mixerName = mBinding.etMixerName.text.toString().trim { it <= ' ' }
                val mixerDescription = mBinding.etMixerDescription.text.toString().trim { it <= ' ' }
                val mixerMac = macaddress
                val mixerBtBox = mBinding.tvCajaBluetoothAsoc.text.toString().trim { it <= ' ' }
                val mixerTara = mBinding.tvTara.text.toString().trim { it <= ' ' }
                val mixerCalibration = mBinding.tvCalibration.text.toString().trim { it <= ' ' }
                val remoteId = 0L
                val rfId = 0L
                val linked = false
                val mixerId = 0L
                mMixerDetails = Mixer(
                    mixerName,
                    mixerDescription,
                    mixerMac,
                    mixerBtBox,
                    if (mixerTara.isEmpty()) 0.0 else mixerTara.toDouble(),
                    if (mixerCalibration.isEmpty()) 1.toFloat() else mixerCalibration.toFloat(),
                    rfId,
                    remoteId,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                    "",
                    linked,
                    mixerId)
            }
            bluetoothDialog()
        }

        mBinding.btnTara.setOnClickListener {
            setTara()
        }

        mBinding.btnCalibrate.setOnClickListener {
            if(mMixerDetails!=null){
                SeleccionCalibracionDlgFragment().show(supportFragmentManager,"Seleccion calibracion")
            }
        }
        mBinding.tvPesoConocido.setOnClickListener{
            InputValueDialogFragment(100.0,DIALOG_5,"Ingrese peso conocido").show(supportFragmentManager,"Peso conocido")
        }

        mBinding.tvCalibration.setOnClickListener{
            InputValueDialogFragment(mBinding.tvCalibration.text.toString().toDouble(),DIALOG_4,"Ingrese calibracion",0.1).show(supportFragmentManager,"Factor calibracion")
        }

        mBinding.mixerDivExt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                divExt = divisionesExternas[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        arrayAdapter = ArrayAdapter(this, R.layout.spinner_tolvas, divisionesExternas)
        arrayAdapter!!.setDropDownViewResource(R.layout.spinner_dropdown_tolva)
        mBinding.mixerDivExt.adapter = arrayAdapter


        bindBluetoothService()
        Log.i("BLUE", "Se inicia la búsqueda de dispositivos asociados")
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(this, mBluetoothListener)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }


    private fun setTara() {
        if(mMixerDetails==null){
            return
        }
        tara = dataReceive
        mMixerDetails!!.tara = tara
        mMixerViewModel.update(mMixerDetails!!)
        mBinding.tvTara.setText(tara.roundToInt().toString())
    }

    private fun calibrateDialog() {
        if(mMixerDetails==null){
            return
        }
        val icon = getDrawable(R.drawable.icon_warning)
        icon.let {_icon->
            _icon!!.setTint(getColor(R.color.color_acent_green))
            ConfirmDialogFragment(
                getString(R.string.calibracion),
                getString(R.string.vaciar_tolva)
                ,_icon,CALIB_STEP_1).show(supportFragmentManager,"Dialogo calibración 1")
        }
        return
    }

    override fun onPause() {
        Log.i(TAG,"onResume")
        super.onPause()
    }


    override fun onStart() {
        Log.i(TAG,"onStart")
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
        mService?.LocalBinder()?.getBondedDevices()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"onDestroy")
        mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(applicationContext, mBluetoothListener)
    }

    private fun linkDevice(mixer: Mixer){
        if(isSearching)
            return
        selectedMixer = mixer
        dialog = Helper.setProgressDialog(this, "Buscando mixer...")
        dialog?.show()
        mService?.LocalBinder()?.startDiscovery(this)
        Timer().schedule(5000){
            dialog?.cancel()
            mDevicesFound.forEach { deviceFounded ->
                Log.i("FOUND", deviceFounded.name + "  " + deviceFounded.address)
            }
            mService?.LocalBinder()?.stopDiscovery()
        }
    }

    private fun getSelectedMixerBluetoothDevice() : BluetoothDevice? {
        Log.i(TAG, "[getSelectedMixerBluetoothDevice] Local mixer: ${knowDevices?.size} | mLocalMixers: ${mLocalMixers?.size}")
        knowDevices?.forEach { bluetoothDevice ->

            if(selectedMixer?.mac == bluetoothDevice.address){
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
            Log.i(TAG, "*********** onServiceConnected [MixerConfigActivity] CONECTADO " )
            if(selectedBluetoothDevice!=null){
                Log.i(TAG, "*********** onServiceConnected [MixerConfigActivity] CONECTADO ${selectedBluetoothDevice?.name} ${selectedBluetoothDevice?.address} | $mService" )
                mService?.LocalBinder()?.connectKnowDeviceWithTransfer(selectedBluetoothDevice!!)
            }
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i("BLUE", "*********** [MixerConfigActivity]  DESCONECTADO")
            mBound = false
        }
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "[MixerListFragment] onDiscoveryStarted")
            customItemsDialog("Dispositivos disponibles", ArrayList(), Constants.DEVICE_REF)        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[MixerConfigActivity] onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            device?.let { currentDevice ->
                allBluetoothDevice.add(device)

                val name = if (currentDevice.name == null) "No identificado" else currentDevice.name
                val mac = if (currentDevice.address == null) "00:00:00:00" else currentDevice.address

                val item = CustomListItem(0L, 0, name, mac, R.drawable.icon_balance)
                (dialogCustomListBinding?.rvList?.adapter as CustomListItemAdapter).updateList(item)

            }
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            Log.i(TAG, "[MixerConfigActivity] onDeviceConnected")
            changeStatusConnection(true)
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("${this.javaClass.name} BLUE", "ACT onCommandReceived")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            if(message==null){
                return
            }
            hideCustomProgressDialog()
            if(contMensajes++ >20){
                Log.i(TAG, "[MixerConfigActivity] onMessageReceived: $message")
                contMensajes = 0
                deviceConnected()
            }
            changeStatusConnection(true)
            message.let{
                if (it.length > 12){
                    val currentDataRead : Double? = it.substring(1, 7).toDoubleOrNull()
                    if (currentDataRead != null) {
                        dataReceive = currentDataRead
                        weight = Helper.getWeightKg(currentDataRead,mMixerDetails!!)
                        if(divExt == 0){
                            divExt = 1
                        }
                        mBinding.tvWeight.setText("${((weight/divExt).roundToInt()*divExt)}Kg")
                        mBinding.tvDatoRecibido.setText(it.substring(0, 12))
                    }
                }
            }
        }

        override fun onMessageSent(device: BluetoothDevice?) {
            Log.i(TAG, "[MixerConfigActivity] onMessageSent")
        }

        override fun onError(message: String?) {
            changeStatusConnection(false)
            Log.i(TAG, "[MixerConfigActivity] onError")
        }

        override fun onDeviceDisconnected() {
            changeStatusConnection(false)
            Log.i(TAG, "[MixerConfigActivity] onDeviceDisconnected")
        }

        /*


            knowDevices = device
            val selectedMixer = getSelectedMixer()
            if (selectedMixer != null){
                myMenu?.findItem(R.id.action_change_mixer)?.title = "   " +selectedMixer.name
            }

            mLocalMixers?.forEach {
                val alreadyExist = mLocalMixersCustomList.firstOrNull {  customItem ->
                    customItem.id == it.id
                }

                val isLinked = knowDevices?.any { knowDevice -> knowDevice.address == it.mac } == true
                if (alreadyExist == null && isLinked){
                    val customList = CustomListItem(it.id, it.remoteId, it.name)
                    mLocalMixersCustomList.add(customList)
                }
            }



         */

    override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[HomeFragment] ACT onBondedDevices")
            knowDevices?.forEach {
                Log.i(TAG, "knowDevices bfr: ${it.name} | ${it.address}")
            }
            knowDevices = device
            knowDevices?.forEach {
                Log.i(TAG, "knowDevices aft: ${it.name} | ${it.address}")
            }

            if(firstIn && knowDevices != null){

                selectedBluetoothDevice = knowDevices?.firstOrNull { bluetoothDevice ->
                    mMixerDetails?.mac == bluetoothDevice.address
                }

                selectedBluetoothDevice = getSelectedMixerBluetoothDevice()
                if(selectedBluetoothDevice!=null){
                    Log.d(TAG,"selectedBluetoothDevice: " + selectedBluetoothDevice.toString())
                    mService!!.LocalBinder().connectKnowDeviceWithTransfer(selectedBluetoothDevice!!)
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
                    if(mMixerDetails!=null){
                        linkDevice(mMixerDetails!!)
                    }
                    else{
                        Log.i(TAG,"mMixerDetail = null")
                    }
                }
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun equiposVinculados() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothDevicesKnow = bluetoothManager.adapter.bondedDevices
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
            selectDevice(devices[which])
            dialog.dismiss()
            //**********************************************************************
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun selectDevice(device : BluetoothDevice?) {
        selectedBluetoothDevice = device
        if(mMixerDetails != null){
            mMixerDetails?.mac = selectedBluetoothDevice?.address.toString()
            mMixerDetails?.btBox = device?.name.toString()
            mMixerViewModel.update(mMixerDetails!!)
        }
        macaddress = mMixerDetails?.mac.toString()
        mBinding.tvCajaBluetoothAsoc.setText(device?.name.toString() + "   " + selectedBluetoothDevice?.address.toString() )
        Log.i(TAG,"Se seleccionó: " + selectedBluetoothDevice!!.name + "   " + selectedBluetoothDevice!!.address)


        if(!isConnected && selectedBluetoothDevice != null){
            snackbar?.dismiss()
            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(selectedBluetoothDevice!!)
            showCustomProgressDialog()
            Timer().schedule(5000) {
                hideCustomProgressDialog()
                showSnackbar = true
            }
        }
    }


    fun showCustomProgressDialog(){
        if(mProgressDialog != null && mProgressDialog!!.isShowing){
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
                    builder.setTitle("Alerta")
                    builder.setMessage("No se pudo establecer la comunicación con el Mixer")

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

    override fun onConfirm(yesNo: Int, source: Int) {
        when (source) {
            CALIB_STEP_1 ->{
                Log.i(TAG,"CALIB_STEP_1")
                if (yesNo == ConfirmDialogFragment.ACCEPT) {
                    val icon = getDrawable(R.drawable.icon_warning)
                    icon.let{_icon->
                        _icon?.setTint(getColor(R.color.color_acent_green))
                        ConfirmDialogFragment(
                            getString(R.string.calibracion),
                            """
                    ${getString(R.string.cargar_mixer_con)}. ${getString(R.string.luego_aceptar)}
                    """.trimIndent(), _icon!!, CALIB_STEP_2).show(supportFragmentManager, "DialogoCalibracion 3")
                        calibrationTara = dataReceive
                        return
                    }
                }
                calibrating = false

            }
            CALIB_STEP_2 -> {
                Log.i(TAG,"CALIB_STEP_2")
                if (yesNo == ConfirmDialogFragment.ACCEPT && !calibrating) {
                    calibrating = true
                    knowWeight = dataReceive
                    InputValueDialogFragment(mBinding.tvPesoConocido.text.toString().toDouble(),DIALOG_1,"Peso Ingresado").show(supportFragmentManager,"Peso conocido")
                    return
                }
                calibrating = false

            }

            CALIB_STEP_3 -> {
                Log.i(TAG,"CALIB_STEP_3")
                if (yesNo == ConfirmDialogFragment.ACCEPT) {
                    mBinding.tvDummy.requestFocus()
                    val strKnowWeight:String = mBinding.tvPesoConocido.text.toString()
                    fun String.fullTrim() = trim().replace("\uFEFF", "")
                    val knowWeight = strKnowWeight.fullTrim().toDouble()
                    mMixerDetails?.tara = calibrationTara
                    tara = calibrationTara
                    val calib = (knowWeight/(this.knowWeight-calibrationTara)).toFloat()
                    Log.i("CALIBRACION", "Calibracion: $calib\ntara: $tara")
                    if(calib>0){
                        mMixerDetails?.calibration = calib
                        mMixerViewModel.update(mMixerDetails!!)
                        Log.d("CALIBRACION","Ok " + mMixerDetails?.calibration)
                        Toast.makeText(this,R.string.calibracion_done,Toast.LENGTH_LONG).show()
                        mBinding.tvCalibration.setText(mMixerDetails?.calibration.toString() )       //(((mixer.calibration)*DECIMALPRESICION).roundToInt()/DECIMALPRESICION).toString()
                        mBinding.tvTara.setText(calibrationTara.toString())
                    }else{
                        Toast.makeText(this,R.string.calibracion_mal,Toast.LENGTH_LONG).show()
                    }
                }
                calibrating = false
                hideSoftKeyboard()
            }
            CALIB_STEP_4->{
                Log.i(TAG,"CALIB_STEP_4")
                InputValueDialogFragment(0.0,FROM_TOLVA,getString(R.string.ingrese_peso_tolva)).show(
                    supportFragmentManager,"Ingrese peso en tolva")
            }
            else->{}
        }
    }

    override fun inputValue(value: Double?, source: Int) {
        when (source) {
            DIALOG_1 -> {
                Log.i(TAG, "DIALOG_1 - value: $value   source: $source")
                mBinding.tvPesoConocido.setText(value?.toInt().toString())
                val icon = getDrawable(R.drawable.icon_warning)
                icon.let {_icon->
                    icon?.setTint(R.color.color_acent_green)
                    ConfirmDialogFragment(
                        getString(R.string.calibracion),
                        """
                    ${getString(R.string.calibracion_ok)}
                    ${getString(R.string.desea_guardar)}
                    
                    """.trimIndent(),_icon!!, CALIB_STEP_3).show(supportFragmentManager, "DialogoCalibracion 4")
                    return
                }

            }
            DIALOG_2 -> {
                Log.i(TAG, "DIALOG_2 - value: $value   source: $source")
                hideSoftKeyboard()
            }
            DIALOG_3 -> {
                Log.i(TAG, "DIALOG_3 - value: $value   source: $source")
                hideSoftKeyboard()
            }
            DIALOG_4 -> {
                Log.i(TAG, "DIALOG_4 - value: $value   source: $source")
                if (value != null) {
                    mBinding.tvCalibration.setText(value.toString() ) //((value*DECIMALPRESICION).roundToInt()/DECIMALPRESICION).toString()
                    mMixerDetails?.let {mixerDetail->
                        mixerDetail.calibration = value.toFloat()
                        mMixerViewModel.update(mixerDetail)
                    }
                }
                hideSoftKeyboard()
            }
            DIALOG_5 -> {
                Log.i(TAG, "DIALOG_5 - value: $value   source: $source")
                if (value != null) {
                    mBinding.tvPesoConocido.setText(value.toString() ) //((value*DECIMALPRESICION).roundToInt()/DECIMALPRESICION).toString()
                    knowWeight = value
                }
                hideSoftKeyboard()
            }
            FROM_TOLVA ->{
                Log.i(TAG, "FROM_TOLVA - value: $value   source: $source")
                valueWeightInMixer = value as Double
                InputValueDialogFragment(
                    0.0,
                    FROM_OTRA_BALANZA,
                    getString(R.string.ingrese_peso_valanza_externa)
                ).show(
                    supportFragmentManager, "Valor balanza externa"
                )
            }
            FROM_OTRA_BALANZA->{
                Log.i(TAG, "FROM_OTRA_BALANZA - value: $value   source: $source")
                hideSoftKeyboard()
            }
            else -> {}
        }
    }

    override fun onSeleccionCalibracion(modo: Int) {
        when (modo) {
            1 -> {
                calibrateDialog()
            }
            2 -> {
                val icon = getDrawable(R.drawable.icon_warning)
                icon.let {_icon->
                    icon?.setTint(getColor(R.color.color_acent_green))
                    ConfirmDialogFragment(
                        getString(R.string.instrucciones),
                        getString(R.string.instrucciones_cal_otra),
                        _icon!!,
                        CALIB_STEP_4).show(
                        supportFragmentManager, "Instrucciones calibracion"
                    )
                }
            }
        }
    }

    fun changeStatusConnection(bConexion: Boolean){
        runOnUiThread {
            if (bConexion) {
                if(isConnected){
                    return@runOnUiThread
                }
                showSnackbar = true
                deviceConnected()
                snackbar?.dismiss()
            } else {
                Log.i(TAG,"chenageStatusConnection false")
                deviceDisconnected()
                if (showSnackbar){
                    showSnackbar = false
                    snackbar =
                        Snackbar.make(mBinding.viewActivityMixerConfig, "Se perdió la conexión",
                            BaseTransientBottomBar.LENGTH_INDEFINITE
                        )
                    snackbar?.setAction(
                        "Reconectar"
                    ) {
                        selectedBluetoothDevice.let {
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
        if(mBinding.toolbarConfigMixer.menu != null && mBinding.toolbarConfigMixer.menu?.size!! >0){
            mBinding.toolbarConfigMixer.menu?.getItem(1)?.icon =
                ContextCompat.getDrawable(this@MixerConfigActivity,R.drawable.ic_bluetooth_disconnected_24px)
            mBinding.toolbarConfigMixer.menu?.getItem(1)?.icon?.setTint(getColor(R.color.white))
            mBinding.toolbarConfigMixer.menu?.getItem(1)?.icon?.colorFilter
        }
        isConnected = false
    }

    private fun deviceConnected() {
        if(mBinding.toolbarConfigMixer.menu != null && mBinding.toolbarConfigMixer.menu?.size!! >0){
            mBinding.toolbarConfigMixer.menu?.getItem(1)?.icon =
                ContextCompat.getDrawable(this@MixerConfigActivity,R.drawable.ic_bluetooth_connected_24px)
            mBinding.toolbarConfigMixer.menu?.getItem(1)?.icon?.setTint(getColor(R.color.white))
        }
        isConnected = true
    }

    /**
     *
     * Funciones sacasdas de AddUopdateMixerActivity
     */

    fun saveMixer(){
        if(bIsDetail){
            return
        }
        val mixerName = mBinding.etMixerName.text.toString().trim { it <= ' ' }
        val mixerDescription = mBinding.etMixerDescription.text.toString().trim { it <= ' ' }
        var remoteId = 0L
        val mixerBtBox = mBinding.tvCajaBluetoothAsoc.text.toString().trim { it <= ' ' }
        val mixerTara = mBinding.tvTara.text.toString().trim { it <= ' ' }
        val mixerCalibration = mBinding.tvCalibration.text.toString().trim { it <= ' ' }
        var mixerMac = macaddress

        mMixerDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(mixerName) -> {
                Toast.makeText(
                    this@MixerConfigActivity,
                    resources.getString(R.string.err_msg_mixer_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                if(mixerMac.isEmpty()){
                    mixerMac = selectedBluetoothDevice?.address.toString()
                }
                var mixerId = 0L
                var updatedDate = ""
                var linked = false

                mMixerDetails?.let {
                    if (it.id != 0L){
                        mixerId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        linked = it.linked == true
                    }
                }

                val mixer = Mixer(
                    mixerName,
                    mixerDescription,
                    mixerMac,
                    mixerBtBox,
                    if (mixerTara.isEmpty()) 0.0 else mixerTara.toDouble(),
                    if (mixerCalibration.isEmpty()) 1.toFloat() else mixerCalibration.toFloat(),
                    0,
                    remoteId,
                    updatedDate,
                    "",
                    linked,
                    mixerId
                )

                runBlocking {
                    if (mixerId == 0L){
                        mixer.id = mMixerViewModel.insertSync(mixer)
                        mMixerDetails = mixer
                        Toast.makeText(this@MixerConfigActivity, "Mixer guardado", Toast.LENGTH_SHORT).show()
                    } else {
                        mMixerViewModel.updateSync(mixer)
                        Toast.makeText(this@MixerConfigActivity, "Mixer actualizado", Toast.LENGTH_SHORT).show()
                    }
                    Log.i("SYNC", "Se actualiza mixer con fecha ${mixer.updatedDate}")
                }

            }
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
        val view: View = mBinding.root
        return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
    }


    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarConfigMixer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.let {
            if(bIsDetail){
                it.title = resources.getString(R.string.screen_mixer_detail)
            }else{
                it.title = resources.getString(R.string.screen_mixer_config)
            }
        }

        mBinding.toolbarConfigMixer.setNavigationOnClickListener {
            if(!bIsDetail){
                saveMixer()
            }
            mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
            BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(applicationContext, mBluetoothListener)
            saveMixer()
            onBackPressed()
        }

    }


    fun selectedListItem(item: CustomListItem, selection: String){
        selectedBluetoothDevice = allBluetoothDevice.firstOrNull { device ->
            device.address == item.description
        }
        mBinding.tvCajaBluetoothAsoc.setText(selectedBluetoothDevice?.name)

        selectedBluetoothDevice?.let { bluetoothDevice ->
            when (selection){
                Constants.DEVICE_REF -> {
                    Log.i(TAG,"Dispositivo seleccionado ${bluetoothDevice.name}")
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
            deviceDisconnected()
        }
    }

    fun bluetoothConnectionSuccess(){
        Log.i(TAG,"bluetoothConnectionSuccess")
        runOnUiThread {
            deviceConnected()
        }
    }

}

