package com.basculasmagris.visorremotomixer.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
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
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityMainBinding
import com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.MinCorral
import com.basculasmagris.visorremotomixer.model.entities.MinEstablishment
import com.basculasmagris.visorremotomixer.model.entities.MinProduct
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.MinUser
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.fragments.HomeFragment
import com.basculasmagris.visorremotomixer.view.fragments.RemoteMixerFragment
import com.basculasmagris.visorremotomixer.view.fragments.TabletMixerListFragment
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.datastore by preferencesDataStore(name = "PREFERENCIAS")
class MainActivity : AppCompatActivity() {

    private val TAG : String =  "DEBMain"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var mProgressDialog: Dialog? = null
    private var roles: ArrayList<Pair<Int, String>> = arrayListOf(
        Pair(1, "Administrador"),
        Pair(2, "Operario"),
        Pair(3, "Supervisor"),
        Pair(4, "Super Admin")
    )
    // Bluetooth
    var mService: BluetoothSDKService? = null
    private var bluetoothDevice: BluetoothDevice? = null
    var selectedTabletMixerInActivity: TabletMixer? = null

    var minRoundRunDetail : MinRoundRunDetail? = null
    var listOfMedRoundsRun: ArrayList<MedRoundRunDetail> = ArrayList()
    var listOfMinUsers: ArrayList<MinUser> = ArrayList()
    private var bReconnect: Boolean = true
    var knowDevices: List<BluetoothDevice>? = null
    private var dialogProduct: AlertDialog? = null
    private var dialogEstablishment: AlertDialog? = null
    private var dialogCorral: AlertDialog? = null

    // -------------------
    // Mixer
    // -------------------
    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((application as SpiMixerApplication).mixerRepository)
    }

    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((application as SpiMixerApplication).tabletMixerRepository)
    }

    private var mLocalUsers: List<User>? = null
    private var mLocalRoundsLocal: List<RoundLocal>? = null
    private val mRoundLocalViewModel: RoundLocalViewModel by viewModels {
        RoundLocalViewModelFactory((application as SpiMixerApplication).roundLocalRepository)
    }

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as SpiMixerApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbarMain)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: CancellableNavigationView = binding.navView
        val currentUser = Helper.getCurrentUser(this)
        val navHeader = navView.getHeaderView(0)
        val tvFullName = navHeader.findViewById<TextView>(R.id.user_display_name)
        val tvRoleDescription = navHeader.findViewById<TextView>(R.id.role_description)

        Log.i("tvFullName", "DATA USER tvFullName: ${currentUser.displayName}")
        Log.i("tvRoleDescription", "DATA USER tvRoleDescription: ${currentUser.codeRole}")
        tvFullName.text = currentUser.displayName.trim()
        tvRoleDescription.text = roles.first { role -> role.first == currentUser.codeRole }.second  //currentUser.role.description.trim()

        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
//                R.id.nav_mixer_remoto,
                R.id.nav_round,
                R.id.nav_tablet_mixer,
                R.id.nav_sync,
                R.id.nav_user
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedPreviewListener { item ->
            Log.d(TAG, "got item: ${item.itemId}")
            var outPut = true
            when (item.itemId) {
                R.id.nav_logout -> {
                    outPut = false
                    AlertDialog.Builder(this)
                        .setMessage("¿Desea cerrar la sesión?")
                        .setPositiveButton("Sí")
                        { _, _ ->
                            Helper.logOut(this)
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        .setNegativeButton("No") { _, _ ->
                        }
                        .show()
                    drawerLayout.close()
                }
                R.id.nav_exit ->{
                    AlertDialog.Builder(this)
                        .setMessage("¿Desea cerrar la aplicación?")
                        .setPositiveButton("Sí")
                        { _, _ ->
                            Helper.logOut(this)
                            finish()
                        }
                        .setNegativeButton("No") { _, _ ->
                        }
                        .show()
                    drawerLayout.close()
                }
                R.id.nav_home ->{
                    navController.popBackStack(R.id.nav_home, false)
                }

            }
            outPut
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        mMixerViewModel.allMixerList.observe(this){
//            mService?.LocalBinder()?.getBondedDevices()
        }

        refreshLogo()
        getLocalData()
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        bindBluetoothService()
    }

    fun refreshLogo() {
        binding.appBarMain.ibLogoMain.setImageDrawable(getDrawable(R.drawable.magris_logo_topbar))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            Activity.RESULT_OK -> {
                Log.i(TAG, "Se recibe: ${data?.getParcelableExtra<RoundRunDetail>("currentRound")}")
            }
            // Other result codes
            else -> {}
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Bluetooth
    private fun bindBluetoothService() {
        // Bind to LocalService
        Log.i(TAG, "Se inicia servicio")
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
            Log.i(TAG, "[MAIN] ****** CONECTADO")
//            mService?.LocalBinder()?.getBondedDevices()
            mService?.let {
                Helper.getServiceInstance().setBluetoothService(it)
            }
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i(TAG, "[MAIN] ****** DESCONECTADO")
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
                    val builder = AlertDialog.Builder(this)
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


    private fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    fun saveTabletMixer(tabletMixer: TabletMixer){
        lifecycleScope.launch(Dispatchers.IO){
            saveTabletMixer(tabletMixer.id)
        }
    }

    private suspend fun saveTabletMixer(idTablet: Long){
        datastore.edit { preferences->
            preferences[longPreferencesKey("IDTABLET")] = idTablet
        }
    }

    fun getSavedTabletMixer() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getSavedTabletMixer")
            val flowLong = getSavedMixerTabletId()
            flowLong.collect { id ->
                id ?: return@collect
                observeTabletMixerById(id)
            }
        }
    }

    private fun observeTabletMixerById(id: Long) {
        lifecycleScope.launch {
            val localKnowDevice = mTabletMixerViewModel.getTabletMixerById(id)
            localKnowDevice.observe(this@MainActivity) { tabletMixer ->
                tabletMixer ?: return@observe
                handleTabletMixerUpdate(tabletMixer)
                localKnowDevice.removeObservers(this@MainActivity)
            }
        }
    }

    private fun handleTabletMixerUpdate(tabletMixer: TabletMixer) {
        selectedTabletMixerInActivity = tabletMixer
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        navHost?.childFragmentManager?.primaryNavigationFragment?.let { fragment ->
            when (fragment) {
                is HomeFragment -> {
                    Log.i(TAG, "setTabletMixer in HomeFragment $selectedTabletMixerInActivity")
                    fragment.setTabletMixer(tabletMixer)}
                is RemoteMixerFragment -> {
                    Log.i(TAG, "setTabletMixer in RemoteMixerFragment $selectedTabletMixerInActivity")
                    fragment.setTabletMixer(tabletMixer)}
                is TabletMixerListFragment -> {
                    Log.i(TAG, "setTabletMixer in TabletMixerListFragment $selectedTabletMixerInActivity")
                    fragment.setTabletMixer(tabletMixer)
                }
            }
        }
    }

    fun changeStatusConnected(){
        hideCustomProgressDialog()
        Log.v("CONNECTION","Home connected")
        showDeviceConnected()
        Helper.saveBluetoothState(true)
    }

    private var isActionRunning = false
    fun changeStatusDisconnected(){
        Log.i("CONNECTION","Home disconnected")
        Helper.saveBluetoothState(false)
        showDeviceDisconnected()
        if(!isActionRunning && bReconnect){
            isActionRunning = true
            Handler(Looper.getMainLooper()).postDelayed({
                connectDevice(bluetoothDevice)
                isActionRunning = false
            }, 250)
        }    }
    fun connectDevice(bluetoothDevice: BluetoothDevice?){
        this.bluetoothDevice = bluetoothDevice
        if(mService?.isConnected() == true){
            changeStatusConnected()
            return
        }

        bluetoothDevice?.let {deviceBluetooth->
            if(mService?.LocalBinder()?.isConnected() == false){
                Log.i(TAG,"connectDevice connectKnowDeviceWithTransfer $deviceBluetooth")
                mService?.LocalBinder()?.connectKnowDeviceWithTransfer(deviceBluetooth)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            connectDevice(this.bluetoothDevice)
        }, 2000)
    }

    private fun showDeviceDisconnected() {
        if(binding.appBarMain.toolbarMain.menu.size>0){
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_disconnected_24px)
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon?.setTint(getColor(R.color.color_full_red))
        }
    }

    private fun showDeviceConnected() {
        if(binding.appBarMain.toolbarMain.menu.size>0){
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_connected_24px)
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon?.setTint(getColor(R.color.white))
        }
    }


    private fun getSavedMixerTabletId() = datastore.data.map { preferences->
        preferences[longPreferencesKey("IDTABLET")]
    }


    fun changeActionBarTitle(title: String) {
        supportActionBar?.let {
            it.title = title
        }
    }

    fun sendRequestRoundRunDetail() {
        val msg = "CMD${Constants.CMD_ROUNDDETAIL}"
        Log.i("send_cmd","Send requestRoundRunDetail $msg")
        mService?.LocalBinder()?.write(msg.toByteArray())
    }

    fun requestDataRoundRunDetail() {
        val msg = "CMD${Constants.CMD_ROUNDDATA}"
        Log.i("send_cmd","Send requestRoundRunData $msg")
        mService?.LocalBinder()?.write(msg.toByteArray())
    }
    fun requestMixer() {
        val msg = "CMD${Constants.CMD_MIXER}"
        mService?.LocalBinder()?.write(msg.toByteArray())
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
            Log.i(TAG,"listOfMinRounds $(requireactivity() as MainActivity).listOfMedRoundsRun")

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
                    state = minRound.state,
                    tabletMixerId = minRound.round.id,
                    tabletMixerMac = selectedTabletMixerInActivity?.mac?:"",
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

    private fun selectProductDialog(productsToSelect : ArrayList<MinProduct>): AlertDialog? {
        if(productsToSelect.isEmpty()){
            return null
        }
        val productosStr = arrayOfNulls<String>(productsToSelect.size)
        for ((i, producto) in productsToSelect.withIndex()) {
            productosStr[i] = producto.name + if(producto.description.isEmpty())"" else " - ${producto.description}"
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto")
        builder.setItems(productosStr) { dialog, which ->
            val productSelected = productsToSelect[which]
            val minProduct = MinProduct(
                name = productSelected.name,
                description = productSelected.description,
                remoteId = productSelected.remoteId,
                id = productSelected.id
            )
            Log.i(TAG,"producto seleccionado $minProduct")
            sendSelectProductToMixer(minProduct)
            dialog.dismiss()
            dialogProduct = null
        }
        builder.setNegativeButton("Cancelar"){dialog,_->
            dialog.dismiss()
            dialogProduct = null
        }
        val alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }

    private fun selectEstablishmentDialog(establishmentsToSelect: ArrayList<MinEstablishment>): AlertDialog? {
        val establishments = java.util.ArrayList<MinEstablishment>()
        establishmentsToSelect.forEach{ establishment : MinEstablishment ->
            establishments.add(establishment)
        }
        val establishmentStr = arrayOfNulls<String>(establishments.size)
        for ((i, establishment) in establishments.withIndex()) {
            establishmentStr[i] = establishment.name
            establishments[i] = establishment
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Establecimientos")
        builder.setItems(establishmentStr) { dialog, which ->
            val establishmentSelected = establishmentsToSelect[which]
            sendSelectEstablishmentToMixer(establishmentSelected)
            dialog.dismiss()
            dialogEstablishment = null
            //**********************************************************************
        }
        val alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }


    private fun selectCorralDialog(corralsToSelect : ArrayList<MinCorral>): AlertDialog?{
        if(corralsToSelect.isEmpty()){
            return null
        }
        val corralStr = arrayOfNulls<String>(corralsToSelect.size)
        for ((i, corral) in corralsToSelect.withIndex()) {
            corralStr[i] = corral.name +if(corral.description.isEmpty()) "" else " - ${corral.description}"
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Corrales")
        builder.setItems(corralStr) { dialog, which ->
            val corralSelected = corralsToSelect[which]
            sendSelectCorralToMixer(corralSelected)
            dialog.dismiss()
            dialogCorral = null
            //**********************************************************************
        }
        builder.setPositiveButton(getString(R.string.finalizar)){dialog,_->
            sendEndToMixer()
            dialog.dismiss()
            dialogCorral = null
        }
        builder.setNegativeButton(getString(R.string.cancelar)){dialog,_->
            dialog.dismiss()
            dialogCorral = null
        }
        val alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }

    fun sendIniToMixer() {
        val byteArray = "CMD${Constants.CMD_INI}${String.format("%06d",0)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun sendEndToMixer() {
        val byteArray = "CMD${Constants.CMD_END}${String.format("%06d",0)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    private fun sendSelectCorralToMixer(minCorral: MinCorral) {
        val byteArray = "CMD${Constants.CMD_SELECT_CORRAL}${String.format("%06d",minCorral.id)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    private fun sendSelectProductToMixer(minProduct: MinProduct) {
        Log.i(TAG,"sendSelectProductToMixer $minProduct")
        val byteArray = "CMD${Constants.CMD_SELECT_PRODUCT}${String.format("%06d",minProduct.id)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    private fun sendSelectEstablishmentToMixer(minEstablishment: MinEstablishment) {
        val byteArray = "CMD${Constants.CMD_SELECT_ESTAB}${String.format("%06d",minEstablishment.id)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun requestListOfProducts() {
        val byteArray = "CMD${Constants.CMD_REQ_PRODUCT}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }


    fun sendGoToRound(id:Long) {
        val byteArray = "CMD${Constants.CMD_GO_TO_ROUND}${String.format("%06d",id)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun sendGoToFreeRound() {
        val byteArray = "CMD${Constants.CMD_GO_TO_FREE_ROUND}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun sendGoToResume(id:Long) {
        val byteArray = "CMD${Constants.CMD_GO_TO_RESUME}${String.format("%06d",id)}".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }    
    
    fun requestListOfCorrals() {
        val byteArray = "CMD${Constants.CMD_REQ_CORRAL}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun requestListOfUsers() {
        val byteArray = "CMD${Constants.CMD_USER_LIST}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }

    fun requestListOfRounds() {
        val byteArray = "CMD${Constants.CMD_ROUNDS}000000".toByteArray()
        mService?.LocalBinder()?.write(byteArray)
    }
    fun dlgProduct(message: ByteArray) {
        try {
            val convertZip = ConvertZip()
            val json = convertZip.decompressText(message.copyOfRange(7, message.size - 1))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<MinProduct>>() {}.type
            val listOfMinProductsToSelect =
                gson.fromJson<ArrayList<MinProduct>>(json, listType) ?: ArrayList()
            Log.i(TAG, "dlgProduct $listOfMinProductsToSelect")
            dialogProduct = selectProductDialog(listOfMinProductsToSelect)
            return
        } catch (e: NumberFormatException) {
            Log.i(TAG, "dlgProduct NumberFormatException $e")
            return
        } catch (e: Exception) {
            Log.i(TAG, "dlgProduct Exception $e")
            return
        }
    }

    fun dlgEstablishment(message: ByteArray) {
        try{
            val convertZip = ConvertZip()
            val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<MinEstablishment>>() {}.type
            val listOfMinEstablishmentToSelect = gson.fromJson<ArrayList<MinEstablishment>>(json, listType)?:ArrayList()
            Log.i(TAG,"dlgEstablishment $listOfMinEstablishmentToSelect")
            dialogEstablishment = selectEstablishmentDialog(listOfMinEstablishmentToSelect)
            return
        }catch (e: NumberFormatException){
            Log.i(TAG,"dlgEstablishment NumberFormatException $e")
            return
        }catch (e:Exception){
            Log.i(TAG,"dlgEstablishment Exception $e")
            return
        }

    }

    fun dlgCorral(message: ByteArray) {
        try {
            val convertZip = ConvertZip()
            val json = convertZip.decompressText(message.copyOfRange(7, message.size - 1))
            val gson = Gson()
            val listType = object : TypeToken<ArrayList<MinCorral>>() {}.type
            val listOfMinCorralsToSelect =
                gson.fromJson<ArrayList<MinCorral>>(json, listType) ?: ArrayList()
            Log.i(TAG, "dlgCorral $listOfMinCorralsToSelect")
            dialogCorral = selectCorralDialog(listOfMinCorralsToSelect)
            return
        } catch (e: NumberFormatException) {
            Log.i(TAG, "dlgCorral NumberFormatException $e")
            return
        } catch (e: Exception) {
            Log.i(TAG, "dlgCorral Exception $e")
            return
        }
    }

    fun getLoadDifference(): Double {
        var totalDiff = 0.0
        minRoundRunDetail?.round?.diet?.products?.forEach {
            totalDiff += (it.finalWeight-it.initialWeight)- it.targetWeight
        }
        return totalDiff
    }

    fun getFinalLoad(): Double {
        var totalLoad = 0.0
        minRoundRunDetail?.round?.diet?.products?.forEach {
            totalLoad += (it.finalWeight-it.initialWeight)
        }
        return totalLoad
    }

    fun getDownloadDifference(): Double {
        var totalDiff = 0.0
        minRoundRunDetail?.round?.corrals?.forEach {
            totalDiff += (it.initialWeight-it.finalWeight)-it.actualTargetWeight
        }
        return totalDiff
    }

    fun getFinalDownload(): Double {
        var totalDownload = 0.0
        minRoundRunDetail?.round?.corrals?.forEach {
            totalDownload += (it.initialWeight-it.finalWeight)
        }
        return totalDownload
    }

    fun getTargetWeight(): Double {
        var totalTarget = 0.0
        minRoundRunDetail?.round?.corrals?.forEach {
            totalTarget += (it.actualTargetWeight)
        }
        return totalTarget
    }

    fun getName(btDevice: BluetoothDevice?): String {
        var name = ""
        btDevice?.let{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
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

    fun getAddress(btDevice: BluetoothDevice?): String {
        var address = ""
        btDevice?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
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

    fun getBluetoothName(btDevice: BluetoothDevice?): String {
        var name = ""
        btDevice?.let{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
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
                        this@MainActivity,
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


    fun selectTabletMixer(tabletMixer: TabletMixer) {
        selectedTabletMixerInActivity = tabletMixer
    }

    fun selectBluetooth(deviceBluetooth: BluetoothDevice?) {
        this.bluetoothDevice = deviceBluetooth
    }

    fun reconnectDisable() {
        bReconnect = false
    }

    fun reconnectEnable() {
        bReconnect = true
    }

    fun closeDialogs() {
        dialogProduct?.dismiss()
        dialogProduct = null

        dialogCorral?.dismiss()
        dialogCorral = null

        dialogEstablishment?.dismiss()
        dialogEstablishment = null


    }

}

class CancellableNavigationView(context: Context, attrs: AttributeSet) :
    NavigationView(context, attrs) {
    private var cancellableListener =
        object : OnNavigationItemSelectedListener {
            var listener: OnNavigationItemSelectedListener? = null
            var prevListener: OnNavigationItemSelectedListener? = null

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (listener?.onNavigationItemSelected(item) == false) {
                    return false
                }
                return prevListener?.onNavigationItemSelected(item) ?: true
            }
        }

    override fun setNavigationItemSelectedListener(
        listener: OnNavigationItemSelectedListener?
    ) {
        cancellableListener.prevListener = listener
        super.setNavigationItemSelectedListener(cancellableListener)
    }

    fun setNavigationItemSelectedPreviewListener(
        listener: OnNavigationItemSelectedListener?
    ) {
        cancellableListener.listener = listener
    }

}