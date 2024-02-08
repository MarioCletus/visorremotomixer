package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.model.entities.DietDetail
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.MarginItemDecoration
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.time.LocalDateTime
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class RemoteMixerFragment : BottomSheetDialogFragment() {

    private var countMsg: Int = 0
    private var countNoLoad: Int = 0
    lateinit var mBinding: FragmentRemoteMixerBinding
    private val TAG: String = "DEBMixerVR"
    var menu: Menu? = null
    private var dialogResto: AlertDialog? = null
    private var mixerWeight = 0.0
    private var totalMixerWeight: Double = 0.0
    private var lastUpdate: LocalDateTime? = null
    private var timerTask: TimerTask? = null
    private var estableTimer: Timer? = null
    private var defaultStep = 5.0
    private var activity: MainActivity? = null
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentProductDetail: ProductDetail? = null
    private var previousProductDetail: ProductDetail? =null
    private var mixerDetail: MixerDetail? = null
    var currentRoundRunDetail : RoundRunDetail? = null
    private var productDetail : ProductDetail? = null
    var roundRunProductAdapter : RoundRunProductAdapter? = null

    private var currentCorralDetail : CorralDetail? = null
    private var previousCorralDetail : CorralDetail? = null
    private var corralDetail : CorralDetail? = null
    var roundRunCorralAdapter : RoundRunCorralDownloadAdapter? = null

    private var bInLoad: Boolean = false
    private var bInDownload : Boolean = false

    private var tick: Long = 0L

    private var isConnected = false
    private var selectedMixerInFragment: Mixer? = null
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null
    private var knowDevices: List<BluetoothDevice>? = null

    private var targetReachedDialog: AlertDialog? = null
    private var dialogCountDown: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG,"onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = (requireActivity() as MainActivity)
        activity?.showCustomProgressDialog()

        val args: RemoteMixerFragmentArgs by navArgs()

        selectedTabletMixerInFragment = args.tabletMixer
        Log.i(TAG,"tabletMixer ${selectedTabletMixerInFragment}")

        mBinding.btnTara.setOnClickListener{
            Log.i(TAG, "btnTara")
            val msg = "CMD${Constants.CMD_TARA}"
            activity?.mService?.LocalBinder()?.write(msg.toByteArray())
        }

        mBinding.btnJump.setOnClickListener{
            Log.i(TAG, "btnJump")
            roundRunProductAdapter?.let { productAdapter->
                val position = productAdapter.selectedPosition.plus(1)
                if(position < productAdapter.itemCount){
                    val nextItem = currentRoundRunDetail?.round?.diet?.products?.get(position)
                    nextItem?.let { productDetail->
                        targetReachedDialog = dialogAlertTargetWeight(productDetail.name)
                    }
                }else{
                    targetReachedDialog = dialogAlertTargetWeight("descarga")
                }
            }
        }

        mBinding.btnPause.setOnClickListener{
        }

        mBinding.btnRest.setOnClickListener{
            dialogResto = if(dialogResto==null){
                customDialog("Resto","${totalMixerWeight.roundToLong()}Kg",40f, Gravity.CENTER)
            }else{
                dialogResto?.dismiss()
                null
            }
        }

        loadRoundDetail()
        mBinding.rvMixerProductsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler_horizontal)))

        //Conectar bluetooth
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        currentRoundRunDetail?.state = Constants.STATE_LOAD
        Log.i(TAG,"onViewCreated ready")
    }

    private fun loadRoundDetail() {
        currentRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
            if(bInLoad){
                Log.i(TAG,"loadAdapter RoundRunProductAdapter")
                roundRunProductAdapter =  RoundRunProductAdapter(
                    this@RemoteMixerFragment,
                    dietDetail,
                    defaultStep)
                currentRoundRunDetail?.round?.diet?.products?.let { products ->
                    roundRunProductAdapter?.productList(products)
                }
                mBinding.rvMixerProductsToLoad.adapter = roundRunProductAdapter

                var countProductPosition = 0
                var prevProduct : ProductDetail? = null
                currentRoundRunDetail?.round?.diet?.products?.let { products ->
                    products.forEach{ productInRound ->
                        if(productInRound.finalWeight == 0.0){
                            currentProductDetail = productInRound
                            roundRunProductAdapter?.selectProduct(countProductPosition)
                            previousProductDetail = prevProduct
                            return@forEach
                        }
                        prevProduct = productInRound
                        countProductPosition++
                    }
                }
            }else if(bInDownload){
                Log.i(TAG,"loadAdapter RoundRunCorralDownloadAdapter")
                roundRunCorralAdapter =  RoundRunCorralDownloadAdapter(
                    this@RemoteMixerFragment)
                currentRoundRunDetail?.round?.corrals?.let { corrals ->
                    roundRunCorralAdapter?.corralList(corrals)
                }
                mBinding.rvMixerProductsToLoad.adapter = roundRunCorralAdapter

                var countCorralPosition = 0
                var prevCorral: CorralDetail? = null
                currentRoundRunDetail?.round?.corrals?.let { corrals ->
                    corrals.forEach{ corralInRound ->
                        if(corralInRound.finalWeight == 0.0){
                            currentCorralDetail = corralInRound
                            roundRunCorralAdapter?.selectCorral(countCorralPosition)
                            previousCorralDetail = prevCorral
                            return@forEach
                        }
                        prevCorral = corralInRound
                        countCorralPosition++
                    }
                }
            }else{}

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        permission()
        mBinding = FragmentRemoteMixerBinding.inflate(inflater, container, false)


        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_remote_mixer_fragment, menu)
                this@RemoteMixerFragment.menu = menu

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.bluetooth_tablet_mixer -> {
                        Log.v(TAG,"Force connection")
                        val deviceBluetooth = knowDevices?.firstOrNull { bd->
                            bd.address == selectedTabletMixerInFragment?.mac
                        }
                        deviceBluetooth?.let {
                            Log.v(TAG,"Force connection ${it.name}")
                            (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                            (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                            (requireActivity() as MainActivity).showCustomProgressDialog()
                        }
                        return true
                    }
                    R.id.menu_selected_remote_tablet -> {
                        Log.i(TAG,"touch tablet icon")
                        goToTabletMixerListFragment()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return mBinding.root
    }

    private fun permission() {
        val permission1 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH)
        val permission2 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_ADMIN)
        val permission3 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
        val permission4 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        val permission5 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_SCAN)
        val permission6 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT)
        if (permission1 != PackageManager.PERMISSION_GRANTED
            || permission2 != PackageManager.PERMISSION_GRANTED
            || permission3 != PackageManager.PERMISSION_GRANTED
            || permission4 != PackageManager.PERMISSION_GRANTED
            || permission5 != PackageManager.PERMISSION_GRANTED
            || permission6 != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                642)
        } else {
            Log.d("BLUE", "Permissions Granted")
        }

    }


    var countInResume = 0
    override fun onResume() {
        super.onResume()
        activity = this.getActivity() as MainActivity?
        Log.i(TAG,"onResume fragment: $this")
        if(activity is MainActivity){
            Log.i(TAG,"activity is MainActivity $activity")
            (requireActivity() as MainActivity).getSavedTabletMixer()
        }

        if(estableTimer != null ){
            Log.i(TAG,"estableTimer != null $estableTimer")
            return
        }
        estableTimer = Timer(false)
        timerTask = object : TimerTask() {
            init {
                Log.i(TAG,"init timerTask ${countInResume++}: $timerTask estableTimer $estableTimer")
            }
            override fun run() {
                tick ++

                if(bInLoad )
                    productDetail?.let {productDetail ->
                        activity?.let {mainActivity ->
                            if(productDetail.currentWeight-productDetail.initialWeight > productDetail.targetWeight*0.9 && bInDownload){
                                if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(mainActivity, R.color.white)){
                                    if(productDetail.currentWeight-productDetail.initialWeight > productDetail.targetWeight){
                                        mBinding.tvCurrentProductWeightPending.setTextColor(
                                            ContextCompat.getColor(mainActivity, R.color.color_yellow))
                                        }else{
                                            mBinding.tvCurrentProductWeightPending.setTextColor(
                                                ContextCompat.getColor(mainActivity, R.color.color_orange))
                                        }
                                    }else{
                                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                                    }
                                }else{
                                    mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                                }
                                if(productDetail.currentWeight-productDetail.initialWeight > productDetail.targetWeight && noPrevAlert){
                                    countGreaterThanTarget++
                                }

                        }
                    }

                if(bInDownload)
                    corralDetail?.let { corralDetail ->
                        activity?.let {mainActivity ->
                            if(corralDetail.initialWeight - corralDetail.currentWeight > corralDetail.actualTargetWeight*0.9){
                                if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(activity!!, R.color.white)){
                                    if(corralDetail.initialWeight - corralDetail.currentWeight > corralDetail.actualTargetWeight){
                                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.color_yellow))
                                    }else{
                                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.color_orange))
                                    }
                                }else{
                                    mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                                }
                            }else{
                                mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                            }
                            if(corralDetail.currentWeight-corralDetail.initialWeight > corralDetail.actualTargetWeight && noPrevAlert){
                                countGreaterThanTarget++
                            }
                        }
                    }

            }
        }
        estableTimer?.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        init {
            Log.i(TAG,"create mBluetoothListener")
        }

        override fun onDiscoveryStarted() {
            Log.i(TAG, "[MixRem] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[MixRem] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[MixRem] ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            isConnected = false
            (requireActivity() as MainActivity).changeStatusConnected()
            Log.i(TAG, "onDeviceConnected ${device?.name} ${device?.address}")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            (requireActivity() as MainActivity).changeStatusConnected()
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val messageStr = String(message,0, message.size)
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","command $command")
            when (command){

                Constants.CMD_ROUNDDETAIL->{

                    val convertZip = ConvertZip()
                    Log.i(TAG,"message.lenght ${message.size}")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i(TAG,"CMD_ROUNDDETAIL ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                    try{

                        val jsonString : String = convertZip.decompressText(byteArrayUtil)

                        var count = 0
                        var index = 0
                        val mult = 256
                        Log.i("Json","jsonString lenght ${jsonString.length}")
                        while( (count*mult) + mult < jsonString.length){
                            count ++
                            Log.i("Json","jsonString $count ${jsonString.substring(index,count*mult)}")
                            index = count * mult
                        }
                        Log.i("Json","jsonString * $count ${jsonString.substring(index,jsonString.length-1)}")


                        if(jsonString.isNotEmpty()){       //TODO hay que verificar bien esto. Me parece que lo mas adecuado es que actalice el adapter cada 1seg y listo!
//                            val gson = Gson()

                            val objectMapper = ObjectMapper().registerKotlinModule()
                            val roundRunDetail = objectMapper.readValue<RoundRunDetail>(jsonString)


//                            val roundRunDetail : RoundRunDetail = gson.fromJson(jsonString,  RoundRunDetail::class.java)
                            currentRoundRunDetail = roundRunDetail
                            if(roundRunDetail.state == Constants.STATE_LOAD){
                                bInLoad = true
                                bInDownload = false
                                roundRunProductAdapter?.updateRound(roundRunDetail)
                            } else if(roundRunDetail.state == Constants.STATE_DOWNLOAD){
                                bInLoad = false
                                bInDownload = true
                                roundRunCorralAdapter?.updateRound(roundRunDetail)
                            }else{
                                bInLoad = false
                                bInDownload = false
                            }
                            loadRoundDetail()
                            roundRunDetail.mixer?.let {mixerDetail->
                                val mixer = Mixer(
                                    mixerDetail.name,
                                    mixerDetail.description,
                                    mixerDetail.mac,
                                    mixerDetail.btBox,
                                    mixerDetail.tara,
                                    mixerDetail.calibration,
                                    mixerDetail.rfid,
                                    mixerDetail.remoteId,
                                    mixerDetail.updatedDate,
                                    mixerDetail.archiveDate,
                                    true,
                                    mixerDetail.id
                                )
                                setMixer(mixer)

                            }
                            currentRoundRunDetail?.let {roundRunDetail->
                                val title = "Mixer: ${mixerDetail?.name} - ${roundRunDetail.round.name} : ${roundRunDetail.round.diet.name}"
                                activity?.changeActionBarTitle(title)
                            }
                        }
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"CMD_ROUNDDETAIL NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i(TAG,"CMD_ROUNDDETAIL Exception $e")
                    }

                }


                Constants.CMD_WEIGHT->{
                    try{
                        val weight = String(message,5,8).toLong()
                        val progress = String(message,13,3).toInt()

                        mBinding.tvCurrentProductWeightPending.text = "${String(message,4,1)}${weight}Kg"
                        mBinding.pbCurrentProduct.progress = progress
                        if(countMsg++ > 10){
                            requestMixer()
                            countMsg = 0
                            val mutableList = mutableListOf<ProductDetail>()
                            val dietDetail = DietDetail (
                                name = "",
                                description = "",
                                remoteId = 0L,
                                updatedDate = "",
                                archiveDate ="",
                                usePercentage = false,
                                products = mutableList,
                                id = 0L
                            )

                            val emptyAdapter =  RoundRunProductAdapter(
                                this@RemoteMixerFragment,
                                dietDetail,
                                defaultStep)

                            mBinding.rvMixerProductsToLoad.adapter = emptyAdapter
                            bInLoad = false
                            bInDownload = false
                            mBinding.tvTitleProduct.text = getString(R.string.mixer)
                            mBinding.tvCurrentProduct.text = mixerDetail?.name ?: ""
                            mBinding.btnJump.isVisible = false
                            mBinding.btnPause.isVisible = false
                        }
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_LOAD->{
                    try{
                        val weight = String(message,5,8).toLong()
                        val progress = String(message,13,3).toInt()
                        Log.i("WEIGHT","CMD_WEIGHT ${String(message)}   mixerWeight $mixerWeight")
                        countNoLoad = 0
                        mixerWeight = (productDetail?.targetWeight?.minus(weight.toDouble()) ?: 0.0)
                        roundRunProductAdapter?.updateWeight(mixerWeight)
                        productDetail?.currentWeight = mixerWeight
                        mBinding.tvCurrentProductWeightPending.text = "${String(message,4,1)}${weight}Kg"
                        mBinding.pbCurrentProduct.progress = progress
                        if(bInLoad == false){
                            mBinding.tvTitleProduct.text = getString(R.string.cargar)
                        }
                        bInLoad = true
                        bInDownload = false
                        mBinding.tvCurrentProduct.text = productDetail?.name
                        if(countMsg++ > 25){
                           countMsg = 0
                           requestRoundRunDetail()
                            mBinding.btnJump.isVisible = true
                            mBinding.btnPause.isVisible = true
                        }
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_DWNL->{
                    lastUpdate = LocalDateTime.now()
                    try{
                        val weight = String(message,5,8).toLong()

                        val progress = String(message,13,3).toInt()
                        Log.i("WEIGHT","CMD_WEIGHT ${String(message)}   mixerWeight $mixerWeight")
                        countNoLoad = 0
                        mixerWeight = (corralDetail?.actualTargetWeight?.minus(weight.toDouble()) ?: 0.0)
                        roundRunCorralAdapter?.updateCorralWeight(mixerWeight)
                        productDetail?.currentWeight = mixerWeight
                        mBinding.tvCurrentProductWeightPending.text = "${String(message,4,1)}${weight}Kg"
                        mBinding.pbCurrentProduct.progress = progress
                        if(bInDownload == false){
                            mBinding.tvTitleProduct.text = getString(R.string.descargar)
                        }
                        bInDownload = true
                        bInLoad = false
                        mBinding.tvCurrentProduct.text = corralDetail?.name
                        if(countMsg++ > 5){
                            countMsg = 0
                            requestRoundRunDetail()
                            mBinding.btnJump.isVisible = true
                            mBinding.btnPause.isVisible = true
                        }
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT Exception $e")
                    }

                }

                Constants.CMD_MIXER ->{
                    Log.i(TAG,"CMD_MIXER")
                    try{
                        val convertZip = ConvertZip()
                        val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
                        val gson = Gson()
                        mixerDetail = gson.fromJson(json,  MixerDetail::class.java)
                        Log.i(TAG,"mixer receibe $mixerDetail")
                        val title = "Mixer: ${mixerDetail?.name}"
                        activity?.changeActionBarTitle(title)

                    }catch (e: NumberFormatException){
                        Log.i(TAG,"bSyncroMixer NumberFormatException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"bSyncroMixer Exception $e")
                        return
                    }
                }
                else->{
                    Log.i(TAG,"else $command")
                }
            }

        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("message","message $message")
            (requireActivity() as MainActivity).changeStatusConnected()
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("sent", "onMessageSent ${device?.address} $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i("sent", "onCommandSent ${device?.address} ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[MixRem] ACT onError")
            (requireActivity() as MainActivity).changeStatusDisconnected()        }

        override fun onDeviceDisconnected() {
            (requireActivity() as MainActivity).changeStatusDisconnected()
            Log.i(TAG, "[MixRem]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[MixRem]onBondedDevices ${device?.size} \n$device")
            knowDevices = device
            knowDevices?.forEach{
                Log.i(TAG,"selectedTabletMixerInFragment $selectedTabletMixerInFragment \nbluetoothKnowed $it")
                selectedTabletMixerInFragment?.let { tabletMixer ->
                    if(tabletMixer.mac == it.address){
                        Log.i(TAG,"Se seleccionó ${it.name} : ${it.address}")
                        if(tabletMixerBluetoothDevice != it){
                            connectTable(tabletMixer)
                        }
                        tabletMixerBluetoothDevice = it
                    }
                }
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanAll()
        Log.i(TAG, "on destroy")
    }

    private fun cleanAll(){
        Log.i(TAG,"clean all estableTimer: $estableTimer timerTask: $timerTask")
        estableTimer?.purge()
        estableTimer?.cancel()
        estableTimer = null
        timerTask = null
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }


    fun tare() {
    }

    private fun customDialog(title: String, message: String, fontSize : Float = 0F, gravity: Int = 0) : AlertDialog? {
        val dialogBuilder = AlertDialog.Builder(requireActivity() as MainActivity)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { _, _ ->
                    tare()

            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
        if(fontSize>0){
            val textView : TextView = alert.findViewById(android.R.id.message)
            textView.textSize = fontSize
        }
        if(gravity != Gravity.NO_GRAVITY){
            val textView : TextView = alert.findViewById(android.R.id.message)
            textView.gravity = gravity
        }
        return alert
    }

    fun setMixer(mixerIn: Mixer?) {
        mixerIn.let {mixer ->
            selectedMixerInFragment = mixer
            mixer?.let { mixer1->
                mixerDetail = MixerDetail(
                    mixer1.name,
                    mixer1.description,
                    mixer1.mac,
                    mixer1.btBox,
                    mixer1.tara,
                    mixer1.calibration,
                    mixer1.rfid,
                    mixer1.remoteId,
                    mixer1.updatedDate,
                    mixer1.archiveDate,
                    mixer1.id
                )
            }
            Log.i(TAG,"setMixer $mixer  \nmService ${(requireActivity() as MainActivity).mService}")
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
        }
    }

    private fun dialogAlertTargetWeight(nextItem : String ="", strKg :String="") : AlertDialog? {
        Log.i(TAG,"dialogAlertTargetWeight")
        dialogCountDown?.let {
            if(it.isShowing){
                Log.i(TAG,"already showing")
                return dialogCountDown
            }
        }
        var title: String? = null
        var message: String? = null
        val strkg : String =  strKg

        if(bInLoad)
            currentProductDetail?.let {currentProduct->
                title = currentProduct.name
                message = if(nextItem.isNotEmpty()){
                    "Se pasa a $nextItem"
                }else{
                    "Se va a pasar al proximo producto! "
                }
            }?: Log.i(TAG,"current product null")
        if(bInDownload)
            currentCorralDetail?.let{ currentCorral->
                title = currentCorral.name
                message = if(nextItem.isNotEmpty()){
                    "Se pasa a $nextItem"
                }else{
                    "Se va a pasar al proximo corral! "
                }
            }?: Log.i(TAG,"current corral null")

        val builder = AlertDialog.Builder(requireActivity())
            .create()
        val  view = layoutInflater.inflate(R.layout.dialog_custom_target_weight_reached,null)
        val  btnAcept = view.findViewById<Button>(R.id.btn_dialog_acept)
        val  btnCancel = view.findViewById<Button>(R.id.btn_dialog_cancel)
        val  tvTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val  tvMessage = view.findViewById<TextView>(R.id.tv_dialog_message)
        val  tvCount = view.findViewById<TextView>(R.id.tv_dialog_count)


        if(title == null || message == null){
            Log.i(TAG,"title = $title message = $message")
            return null
        }
        builder.setView(view)
        tvMessage.text = message
        tvTitle.text = title
        btnAcept.setOnClickListener {
            if(bInLoad && getCurrentProduct() != null){
                sendNextProduct()
            }
            if(bInDownload && getCurrentCorral() != null){
                sendNextCorral()
            }
            builder.dismiss()
        }
        btnCancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)

        builder.setOnShowListener(object : DialogInterface.OnShowListener {
            private val AUTO_DISMISS_MILLIS = 10000
            override fun onShow(dialog: DialogInterface) {
                (dialog as AlertDialog)
                object : CountDownTimer(AUTO_DISMISS_MILLIS.toLong(), 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        tvCount?.text = String.format(
                            Locale.getDefault(), "%s (%d)",
                            strkg,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        )
                    }

                    override fun onFinish() {
                        if (dialog.isShowing) {
                            if(bInLoad)
                                sendNextProduct()
                            if(bInDownload)
                                sendNextCorral()
                            dialog.dismiss()
                            dialogCountDown = null
                        }
                    }
                }.start()
            }

        })

        builder.show()
        dialogCountDown = builder
        return dialogCountDown
    }

    fun sendNextProduct(){
        val msg = "CMD${Constants.CMD_NXTPRODUCT}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    private fun getCurrentProduct(): ProductDetail? {
        return currentProductDetail
    }

    fun sendNextCorral(){
        val msg = "CMD${Constants.CMD_NXTCORRAL}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    private fun getCurrentCorral(): CorralDetail? {
        return previousCorralDetail
    }

    private fun requestRoundRunDetail() {
        val msg = "CMD${Constants.CMD_ROUNDDETAIL}"
        Log.i(TAG,"Send requestRoundRunDetail $msg")
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    private fun requestMixer() {
        val msg = "CMD${Constants.CMD_MIXER}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    private fun requestProduct(){
        val msg = "CMD${Constants.CMD_PRODUCT}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    private fun requestCorral(){
        val msg = "CMD${Constants.CMD_CORRAL}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    fun goToTabletMixerListFragment(){
        findNavController().navigate(RemoteMixerFragmentDirections.actionRemoteMixerFragmentToTableMixerListFragment())
    }

    fun setTabletMixer(tabletMixerIn: TabletMixer) {
            tabletMixerIn.let { tabletMixer ->
                menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + tabletMixer.name
            selectedTabletMixerInFragment = tabletMixer
            Log.i(
                TAG,
                "setTabletMixer $tabletMixer  \nmService ${(requireActivity() as MainActivity).mService}"
            )
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
        }
    }

    fun connectTable(tabletMixer: TabletMixer){
        Log.i(TAG, "Cantidad: ${knowDevices?.size}")
        val deviceBluetooth = knowDevices?.firstOrNull { bd->
            bd.address == tabletMixer.mac
        }

        if (deviceBluetooth != null){
            Log.i(TAG,"Try to connect with ${deviceBluetooth}")
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).connectDevice(deviceBluetooth)
        } else {
            Toast.makeText(requireActivity(), "No se pudo conectar", Toast.LENGTH_SHORT).show()
        }
    }


}