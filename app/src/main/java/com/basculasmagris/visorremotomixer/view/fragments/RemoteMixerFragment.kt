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
import com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail
import com.basculasmagris.visorremotomixer.model.entities.MinDietDetail
import com.basculasmagris.visorremotomixer.model.entities.MinProductDetail
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.MarginItemDecoration
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class RemoteMixerFragment : BottomSheetDialogFragment() {

    private var countMsg: Int = 0
    lateinit var mBinding: FragmentRemoteMixerBinding
    private val TAG: String = "DEBMixerVR"
    var menu: Menu? = null
    private var dialogResto: AlertDialog? = null
    private var rest: Int = 0
    private var timerTask: TimerTask? = null
    private var estableTimer: Timer? = null
    private var defaultStep = 5.0
    private var activity: MainActivity? = null
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentProductDetail: MinProductDetail? = null
    private var mixerDetail: MixerDetail? = null
    var minRoundRunDetail : MinRoundRunDetail? = null
    var roundRunProductAdapter : RoundRunProductAdapter? = null
    private var currentCorralDetail : MinCorralDetail? = null
    var roundRunCorralAdapter : RoundRunCorralDownloadAdapter? = null

    private var bInFree: Boolean = true
    private var bInLoad: Boolean = false
    private var bInDownload : Boolean = false

    private var tick: Long = 0L

    private var selectedMixerInFragment: Mixer? = null
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null
    private var knowDevices: List<BluetoothDevice>? = null

    private var targetReachedDialog: AlertDialog? = null
    private var dialogCountDown: AlertDialog? = null

    private val REFRESH_TIME = 10

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
            tareDialog("Tara","Seguro quiere hacer tara?")
            Log.i(TAG, "btnTara")
        }

        mBinding.btnJump.setOnClickListener{
            Log.i(TAG, "btnJump")

            if(bInLoad){
                roundRunProductAdapter?.let { productAdapter->
                    val position = productAdapter.selectedPosition.plus(1)
                    if(position < productAdapter.itemCount){
                        val nextItem = minRoundRunDetail?.round?.diet?.products?.get(position)
                        nextItem?.let { productDetail->
                            targetReachedDialog = dialogAlertTargetWeight(if(bInFree) getString(R.string.proximo_producto) else productDetail.name)
                        }
                    }else{
                        targetReachedDialog = dialogAlertTargetWeight("descarga")
                    }
                }
            }

            if(bInDownload){
                roundRunCorralAdapter?.let {corralAdapter ->
                    val position = corralAdapter.selectedPosition.plus(1)
                    if(position < corralAdapter.itemCount){
                        val nextItem = minRoundRunDetail?.round?.corrals?.get(position)
                        nextItem?.let {corralDetail ->
                            targetReachedDialog = dialogAlertTargetWeight(corralDetail.name)
                        }
                    }else{
                        targetReachedDialog = dialogAlertTargetWeight("fin")
                    }
                }
            }
        }

        mBinding.btnPause.setOnCheckedChangeListener { v, isChecked ->
            Log.i(TAG,"setOnCheckedChangeListener $isChecked")
            if(isChecked){
                v.setBackgroundResource(R.drawable.btn_round_to_run_red)
                val msg = "CMD${Constants.CMD_PAUSE_ON}"
                activity?.mService?.LocalBinder()?.write(msg.toByteArray())
            }else{
                val msg = "CMD${Constants.CMD_PAUSE_OFF}"
                activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                v.setBackgroundResource(R.drawable.btn_round_rounded_green)
            }
        }

        mBinding.btnRest.setOnClickListener{
            dialogResto = if(dialogResto == null){
                customDialog("Resto","${rest}Kg",40f, Gravity.CENTER)
            }else{
                dialogResto?.dismiss()
                null
            }
        }

        loadRoundDetail()
        mBinding.rvMixerProductsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler_horizontal)))

        //Conectar bluetooth
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        minRoundRunDetail?.state = Constants.STATE_LOAD
        Log.i(TAG,"onViewCreated ready")
    }

    private fun loadRoundDetail() {
        minRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
            if(bInLoad){
                roundRunProductAdapter =  RoundRunProductAdapter(
                    this@RemoteMixerFragment,
                    dietDetail,
                    defaultStep)
                dietDetail.products.let { products ->
                    roundRunProductAdapter?.productList(products)
                }

                mBinding.rvMixerProductsToLoad.adapter = roundRunProductAdapter

                dietDetail.products.let { products ->
                    var currentProduct = products[0]
                    products.sortedByDescending { it.order }.forEach{ productInRound ->
                        if(productInRound.finalWeight == 0L && productInRound.initialWeight != 0L){
                            currentProduct = productInRound
                            roundRunProductAdapter?.selectProduct(productInRound.order-1)
                            return@forEach
                        }
                    }
                    currentProductDetail = currentProduct
                    Log.i(TAG,"currentProductDetail ${currentProductDetail?.name?:""} position ${currentProduct.order}")
//                    if(currentProduct != products[0] && currentProduct.order < (mBinding.rvMixerProductsToLoad.adapter?.itemCount ?: 0)){
                    if(currentProduct != products[0]){
                        mBinding.rvMixerProductsToLoad.scrollToPosition(currentProduct.order-1)
                    }
                }
            }else if(bInDownload){
                roundRunCorralAdapter =  RoundRunCorralDownloadAdapter(
                    this@RemoteMixerFragment)
                minRoundRunDetail?.round?.corrals?.let { corrals ->
                    roundRunCorralAdapter?.corralList(corrals)
                }
                mBinding.rvMixerProductsToLoad.adapter = roundRunCorralAdapter

                minRoundRunDetail?.round?.corrals?.let { corrals ->
                    var currentCorral = corrals[0]
                    corrals.sortedByDescending { it.order }.forEach{ corralInRound ->
                        if(corralInRound.initialWeight != 0L && corralInRound.finalWeight == 0L){
                            currentCorral = corralInRound
                            roundRunCorralAdapter?.selectCorral(corralInRound.order-1)
                            return@forEach
                        }
                        currentCorralDetail = currentCorral
                    }
//                    if(currentCorral != corrals[0]  && currentCorral.order < (mBinding.rvMixerProductsToLoad.adapter?.itemCount ?: 0)){
                    if(currentCorral != corrals[0]){
                        mBinding.rvMixerProductsToLoad.scrollToPosition(currentCorral.order-1)
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
                    currentProductDetail?.let {productDetail ->
                        activity?.let {mainActivity ->
                            if(productDetail.finalWeight - productDetail.initialWeight > productDetail.targetWeight*0.9){
                                if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(mainActivity, R.color.white)){
                                    if(productDetail.finalWeight - productDetail.initialWeight > productDetail.targetWeight){
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
                                if(productDetail.finalWeight - productDetail.initialWeight > productDetail.targetWeight && noPrevAlert){
                                    countGreaterThanTarget++
                                }
                        }
                    }

                if(bInDownload)
                    currentCorralDetail?.let { corralDetail ->
                        activity?.let {mainActivity ->
                            Log.i(TAG,"corralDetail ${corralDetail.name}   ${corralDetail.initialWeight}    ${corralDetail.finalWeight}  ")
                            if(corralDetail.finalWeight != 0L && corralDetail.initialWeight - corralDetail.finalWeight  > corralDetail.actualTargetWeight*0.9){
                                if(mBinding.tvCurrentProductWeightPending.currentTextColor == ContextCompat.getColor(activity!!, R.color.white)){
                                    if(corralDetail.initialWeight - corralDetail.finalWeight > corralDetail.actualTargetWeight){
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
                            if(corralDetail.initialWeight - corralDetail.finalWeight > corralDetail.actualTargetWeight && noPrevAlert){
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
//            (requireActivity() as MainActivity).changeStatusConnected()
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
            Log.i("message", String(message))
            when (command){

                Constants.CMD_ROUNDDETAIL->{

                    val convertZip = ConvertZip()
                    Log.i(TAG,"message.lenght ${message.size}")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i(TAG,"CMD_ROUNDDETAIL ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                    try{
                        val jsonString : String = convertZip.decompressText(byteArrayUtil)
                        Log.i("Json","jsonString * ${jsonString}")

                        if(jsonString.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : MinRoundRunDetail = gson.fromJson(jsonString,  MinRoundRunDetail::class.java)
                            minRoundRunDetail = roundRunDetail
//                            if(roundRunDetail.state == Constants.STATE_LOAD){
//                                bInLoad = true
//                                bInDownload = false
//                                roundRunProductAdapter?.updateRound(roundRunDetail)
//                            } else if(roundRunDetail.state == Constants.STATE_DOWNLOAD){
//                                bInLoad = false
//                                bInDownload = true
//                                roundRunCorralAdapter?.updateRound(roundRunDetail)
//                            }else{
//                                bInLoad = false
//                                bInDownload = false
//                            }
                            loadRoundDetail()
                            roundRunDetail.mixer?.let {mixerDetail->
                                val mixer = Mixer(
                                    mixerDetail.name,
                                    mixerDetail.description,
                                    mixerDetail.mac,
                                    "",
                                    0.0,
                                    1F,
                                    0L,
                                    0,
                                    "",
                                    "",
                                    true,
                                    mixerDetail.id
                                )
                                setMixer(mixer)

                            }
                            minRoundRunDetail?.let { minRoundRunDetail->
                                val title = "Mixer: ${mixerDetail?.name} - ${minRoundRunDetail.round.name} : ${minRoundRunDetail.round.diet.name}"
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
                        if(bInLoad || bInDownload){
                            noInLoadOrDownload()
                        }

                        if(countMsg++ > REFRESH_TIME){
                            noInLoadOrDownload()
                        }
                        refreshWeight(message)
                        bInLoad = false
                        bInDownload = false
                        bInFree = false
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_LOAD->{
                    try{
                        if(bInLoad == false){
                            inLoad()
                        }
                        if(countMsg++ > REFRESH_TIME){
                            inLoad()
                        }
                        refreshWeight(message)
                        bInLoad = true
                        bInDownload = false
                        bInFree = false
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT_LOAD Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_DWNL->{
                    try{
                        if(bInDownload == false){
                            inDownload()
                        }
                        mBinding.tvCurrentProduct.text = currentCorralDetail?.name
                        if(countMsg++ > REFRESH_TIME){
                            inDownload()
                        }
                        refreshWeight(message)
                        bInDownload = true
                        bInLoad = false
                        bInFree = false
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT_DWNL Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_LOAD_FREE->{
                    try{
                        if(bInLoad == false){
                            inLoad()
                        }
                        if(countMsg++ > REFRESH_TIME){
                            inLoad()
                        }
                        refreshWeight(message)
                        bInLoad = true
                        bInDownload = false
                        bInFree = true
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT_LOAD Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_DWNL_FREE->{
                    try{
                        if(bInDownload == false){
                            inDownload()
                        }
                        mBinding.tvCurrentProduct.text = currentCorralDetail?.name
                        if(countMsg++ > REFRESH_TIME){
                            inDownload()
                        }
                        refreshWeight(message)
                        bInDownload = true
                        bInLoad = false
                        bInFree = true
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT_DWNL Exception $e")
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

    private fun refreshWeight(message:ByteArray) {
        val weight = String(message,4,8).toLong()
        val sign = String(message,3,1)
        val progress = String(message,12,3).toInt()
        val signRest = String(message,15,1)
        rest = String(message,16,8).toIntOrNull()?:0
        mBinding.btnPause.isChecked = (String(message, 24, 1).toIntOrNull() ?: 0) == 1
        mBinding.tvCurrentProductWeightPending.text = "${sign}${weight}Kg"
        mBinding.pbCurrentProduct.progress = progress
        dialogResto?.setMessage("$signRest${rest}Kg")

    }

    private fun inDownload() {
        countMsg = 0
        requestRoundRunDetail()
        mBinding.tvTitleProduct.text = getString(R.string.descargar)
        mBinding.tvCurrentProduct.text = currentCorralDetail?.name
        mBinding.btnJump.isVisible = true
        mBinding.btnPause.isVisible = true
    }

    private fun inLoad() {
        countMsg = 0
        requestRoundRunDetail()
        mBinding.tvTitleProduct.text = getString(R.string.cargar)
        mBinding.tvCurrentProduct.text = currentProductDetail?.name
        mBinding.btnJump.isVisible = true
        mBinding.btnPause.isVisible = true

    }

    private fun noInLoadOrDownload() {
        requestMixer()
        countMsg = 0
        val mutableList = ArrayList<MinProductDetail>()
        val dietDetail = MinDietDetail (
            name = "",
            description = "",
            products = mutableList,
            id = 0L
        )

        val emptyAdapter =  RoundRunProductAdapter(
            this@RemoteMixerFragment,
            dietDetail,
            defaultStep)

        mBinding.rvMixerProductsToLoad.adapter = emptyAdapter
        mBinding.tvTitleProduct.text = getString(R.string.mixer)
        mBinding.tvCurrentProduct.text = mixerDetail?.name ?: ""
        mBinding.btnJump.isVisible = false
        mBinding.btnPause.isVisible = false

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
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                dialogResto = null
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
        if(selectedMixerInFragment?.equals(mixerIn) == true && mixerDetail != null){
            return
        }
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
            if(bInFree){
                if(currentProductDetail != null) {
                    title = currentProductDetail?.name
                    message = if(nextItem.isNotEmpty()){
                        "Se pasa a $nextItem"
                    }else{
                        "Se va a pasar al proximo producto! "
                    }
                }else {
                    title = "Elegir producto"
                    message = nextItem
                }
            }else{
                currentProductDetail?.let {currentProduct->
                    title = currentProduct.name
                    message = if(nextItem.isNotEmpty()){
                        "Se pasa a $nextItem"
                    }else{
                        "Se va a pasar al proximo producto! "
                    }
                }?: Log.i(TAG,"current product null")

            }
        if(bInDownload)
            if(bInFree){
                if(currentProductDetail != null) {
                    title = currentProductDetail?.name
                    message = if(nextItem.isNotEmpty()){
                        nextItem
                    }else{
                        "Se va a pasar al proximo producto! "
                    }
                }else {
                    title = "Elegir producto"
                    message = nextItem
                }
            }else{
                currentCorralDetail?.let{ currentCorral->
                    title = currentCorral.name
                    message = if(nextItem.isNotEmpty()){
                        "Se pasa a $nextItem"
                    }else{
                        "Se va a pasar al proximo corral! "
                    }
                }?: Log.i(TAG,"current corral null")
            }

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
            if(bInLoad){
                sendNextProduct()
            }
            if(bInDownload){
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

    fun sendNextCorral(){
        val msg = "CMD${Constants.CMD_NXTCORRAL}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
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

    fun tareDialog(title: String, message: String,fontSize : Float = 0F,gravity: Int = 0) : AlertDialog? {
        val fragmentInstance = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_round_run)
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { _, _ ->
                val msg = "CMD${Constants.CMD_TARA}"
                activity?.mService?.LocalBinder()?.write(msg.toByteArray())
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

}