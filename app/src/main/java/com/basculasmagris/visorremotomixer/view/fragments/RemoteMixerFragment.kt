package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertStringToZip
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.getCurrentUser
import com.basculasmagris.visorremotomixer.utils.MarginItemDecorationHorizontal
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class RemoteMixerFragment : BottomSheetDialogFragment() {

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

    private var bSyncroSequence: Boolean = false
    private var bSyncroUser: Boolean = false
    private var bSyncroMixer: Boolean = false
    private var bSyncroRound: Boolean = false
    private var bSyncroProduct: Boolean = false
    private var bSyncroCorral: Boolean = false
    private var bInLoad: Boolean = false
    private var bInDownload : Boolean = false

    private var tick: Long = 0L
    private var tickCountMessages : Long = 0L
    private var totalWeightLoaded: Double = 0.0

    //    private var showSnackbar = true
    private var isConnected = false
//    private var snackbar: Snackbar? = null
    private var selectedMixerInFragment: Mixer? = null
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null
    private var knowDevices: List<BluetoothDevice>? = null

    private var targetReachedDialog: AlertDialog? = null
    private var dialogCountDown: AlertDialog? = null
//    private var tabletMixer : TabletMixer? = null

    private var countConection : Int = 0

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
            val position = roundRunProductAdapter?.selectedPosition?.plus(1)
            if(position != null && roundRunProductAdapter != null && position < roundRunProductAdapter!!.itemCount){
                val nextItem = currentRoundRunDetail?.round?.diet?.products?.get(position)
                nextItem.let {product->
                    targetReachedDialog = dialogAlertTargetWeight(product!!.name)
                }
            }else{
                targetReachedDialog = dialogAlertTargetWeight("descarga")
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
        mBinding.rvMixerProductsToLoad.addItemDecoration(MarginItemDecorationHorizontal(resources.getDimensionPixelSize(R.dimen.margin_recycler_horizontal)))

        //Conectar bluetooth
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        currentRoundRunDetail?.state = Constants.STATE_LOAD
        Log.i(TAG,"onViewCreated ready")
    }

    private fun loadRoundDetail() {

        currentRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = LinearLayoutManager(requireActivity(),0,false)
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
                currentRoundRunDetail?.round?.diet?.products?.let { products ->
                    products.forEach{productInProducts ->
                        if(productInProducts.finalWeight == 0.0){
                            currentProductDetail = productInProducts
                            roundRunProductAdapter?.selectProduct(countProductPosition)
                            return@forEach
                        }
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
                currentRoundRunDetail?.round?.corrals?.let { corrals ->
                    corrals.forEach{corralInRound ->
                        if(corralInRound.finalWeight == 0.0){
                            currentCorralDetail = corralInRound
                            roundRunCorralAdapter?.selectCorral(countCorralPosition)
                            return@forEach
                        }
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


//        // Navigation Menu
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
                        Log.i(TAG,"touch bluetooth icon")
                        return true
                    }
                    R.id.menu_selected_remote_mixer -> {
                        Log.i(TAG,"touch tablet icon")
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
            activity!!.getSavedTabletMixer()
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
//                Log.i("timer", "REMOTE MIXER mixerWeight:$mixerWeight | lastUpdate: $lastUpdate |  Time:${tick}   timerTask : ${timerTask}          estableTimer:${estableTimer}")
                tick ++
                if(tick == 5L){
                    MainScope().launch {
                        if(!isConnected){
                            if(activity?.mService != null && tabletMixerBluetoothDevice != null){
                                Log.i(TAG,"connectKnowDeviceWithTransfer L815 ${countConection++}")
                                activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(tabletMixerBluetoothDevice!!)
                            }

                        }
                    }
                }
                if(!bSyncroSequence){
                    if(!bSyncroUser){
                        val userRemote = getCurrentUser(requireActivity())
                        val userIdStr = String.format("%06d",userRemote.id)
                        val userName = userRemote.displayName
                        val userNameLenght = String.format("%02d",userName.length)
                        val msg = "CMD${Constants.CMD_USER}$userIdStr$userNameLenght$userName"
                        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                    }else if(!bSyncroMixer){
                        requestMixer()
                    }else if(!bSyncroRound && (bInLoad || bInDownload) ){
                        Log.i(TAG,"bSyncroRound = false")
                        requestRoundRunDetail()
                    }else if(!bSyncroProduct && bInLoad){
                        requestProduct()
                    }else if(!bSyncroCorral && bInDownload){
                        requestCorral()
                    }else if((bSyncroProduct || bSyncroCorral) && bSyncroRound && bSyncroMixer && bSyncroUser){
                        bSyncroSequence = true
                    }
                }

                if(bInLoad && productDetail!=null && activity!=null){
                    if(productDetail!!.currentWeight-productDetail!!.initialWeight > productDetail!!.targetWeight*0.9 && bInDownload){
                        if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(activity!!, R.color.white)){
                            if(productDetail!!.currentWeight-productDetail!!.initialWeight > productDetail!!.targetWeight){
                                mBinding.tvCurrentProductWeightPending.setTextColor(
                                    ContextCompat.getColor(activity!!,
                                    R.color.color_yellow
                                ))
                            }else{
                                mBinding.tvCurrentProductWeightPending.setTextColor(
                                    ContextCompat.getColor(activity!!,
                                    R.color.color_orange))
                            }

                        }else{
                            mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                        }
                    }else{
                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    }

                    if(productDetail!!.currentWeight-productDetail!!.initialWeight > productDetail!!.targetWeight && noPrevAlert){
                        countGreaterThanTarget++
                    }
                }

                if(bInDownload && corralDetail!=null && activity!=null){
                    if(corralDetail!!.initialWeight - corralDetail!!.currentWeight > corralDetail!!.actualTargetWeight*0.9){
                        if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(activity!!, R.color.white)){
                            if(corralDetail!!.initialWeight - corralDetail!!.currentWeight > corralDetail!!.actualTargetWeight){
                                mBinding.tvCurrentProductWeightPending.setTextColor(
                                    ContextCompat.getColor(activity!!,
                                        R.color.color_yellow
                                    ))
                            }else{
                                mBinding.tvCurrentProductWeightPending.setTextColor(
                                    ContextCompat.getColor(activity!!,
                                        R.color.color_orange))
                            }

                        }else{
                            mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                        }
                    }else{
                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    }

                    if(corralDetail!!.currentWeight-corralDetail!!.initialWeight > corralDetail!!.actualTargetWeight && noPrevAlert){
                        countGreaterThanTarget++
                    }
                }

                //Check connection

                if (tick - tickCountMessages > 5){
                    if(tick%10 == 0L){
                        Log.i("CONEXION", "Conexión: NO ${lastUpdate} ${activity?.isCustomProgresDialogShowing()}")
                        changeStatusConnection(false)
                        MainScope().launch {
                            isConnected = true
                            deviceDisconnected()
                        }
                        if(tabletMixerBluetoothDevice != null && activity?.isCustomProgresDialogShowing() == false){
                            tabletMixerBluetoothDevice?.let {
                                Log.i("CONEXION","Reconectando $tabletMixerBluetoothDevice")
                                Log.i(TAG,"connectKnowDeviceWithTransfer L386 ${countConection++}")
                                activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                            }
                        }
                            }
                } else if(tick>5){
                    Log.i("CONSTATUS", "Conexión: SI | Tiempo: ${lastUpdate}  ${tick-tickCountMessages}  $tick")
                    if(isAdded){
                        Log.i("CONSTATUS", "Change status connection rt")
                        changeStatusConnection(true)
                    }else{
                        Log.i("CONEXION", "Conexión: NO | Tiempo: ${lastUpdate}")
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
            deviceConnected()
            Log.i(TAG, "onDeviceConnected ${device?.name} ${device?.address}")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            deviceConnected()
            tickCountMessages = tick
            val convertStringToZip = ConvertStringToZip()
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val messageStr = String(message,0, message.size)
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","command $command")
            when (command){
                Constants.CMD_INI->{
                    bSyncroSequence = false
                    bSyncroProduct = false
                    bSyncroCorral = false
                    bSyncroRound = false
                    roundRunProductAdapter?.endLoad = false
                    val msg = "CMD${Constants.CMD_ACK}${Constants.CMD_INI}"
                    activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                }

                Constants.CMD_START_LOAD->{
                    Log.i(TAG,"Start load")
                    bSyncroSequence = false
                    bSyncroProduct = false
                    bSyncroCorral = false
                    bSyncroRound = false
                    roundRunProductAdapter?.endLoad = false
                    mBinding.tvTitleProduct.text = getString(R.string.cargar)
                    val msg = "CMD${Constants.CMD_ACK}${Constants.CMD_START_LOAD}"
                    activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                }

                Constants.CMD_START_DOWNLOAD->{
                    Log.i(TAG,"Start download")
                    bSyncroSequence = false
                    bSyncroProduct = false
                    bSyncroCorral = false
                    bSyncroRound = false
                    roundRunCorralAdapter?.endLoad = false
                    mBinding.tvTitleProduct.text = getString(R.string.descargar)
                    val msg = "CMD${Constants.CMD_ACK}${Constants.CMD_START_DOWNLOAD}"
                    activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                }

                Constants.CMD_WEIGHT->{
                    lastUpdate = LocalDateTime.now()
                    try{
                        val weight = String(message,5,8).toLong()

                        val progress = String(message,13,3).toInt()
                        Log.i("WEIGHT","CMD_WEIGHT ${String(message)}   mixerWeight $mixerWeight")
                        when(String(message,3,1)){
                            "L"->{
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
                            }
                            "D"->{
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
                            }
                            " "->{
                                countNoLoad++
                                if(countNoLoad>50){
                                    countNoLoad = 0
                                    mBinding.tvTitleProduct.text = getString(R.string.mixer)

                                    val mutableList = mutableListOf<ProductDetail>()
                                    val dietDetail = DietDetail (
                                        name = "",
                                        description = "",
                                        remoteId = 0L,
                                        updatedDate = "",
                                        "",
                                        usePercentage = false,
                                        products = mutableList,
                                        id = 0L
                                    )

                                    val emptyAdapter =  RoundRunProductAdapter(
                                        this@RemoteMixerFragment,
                                        dietDetail,
                                        defaultStep)

                                    mBinding.rvMixerProductsToLoad.adapter = emptyAdapter
                                }
                                mBinding.tvCurrentProductWeightPending.text = "${String(message,4,1)}${weight}Kg"
                                mBinding.pbCurrentProduct.progress = progress
                                bInLoad = false
                                bInDownload = false
                                mBinding.tvCurrentProduct.text = getString(R.string.mixer)
                            }
                            else->{
                                bInLoad = false
                                bInDownload = false
                                mBinding.tvCurrentProduct.text = getString(R.string.mixer)
                            }
                        }
                    }catch (e : Exception){
                        Log.i(TAG,"CMD_WEIGHT Exception $e")
                    }

                }

                Constants.CMD_ROUNDDETAIL->{
                    Log.i(TAG,"CMD_ROUNDDETAIL")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    val arraySize: Int
                    try{
                        arraySize = String(message,3,6).toInt()
                        val str : String = convertStringToZip.decompress(byteArrayUtil,arraySize)
                        Log.i("Json","arraySize $arraySize Json $str")
                        if(str.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : RoundRunDetail = gson.fromJson(str,  RoundRunDetail::class.java)
                            if(currentRoundRunDetail != null && currentRoundRunDetail!!.id == roundRunDetail.id){
                                Log.i(TAG,"notifyDataSetChanged roundRunDetail $roundRunDetail")
                                if(bInLoad)
                                    roundRunProductAdapter?.updateRound(roundRunDetail)
                                if(bInDownload)
                                    roundRunCorralAdapter?.updateRound(roundRunDetail)
                            }else{
                                Log.i(TAG,"new roundRunDetail $roundRunDetail")
                                currentRoundRunDetail = roundRunDetail
                                currentRoundRunDetail.let {
                                    val product = roundRunDetail.round.diet.products.firstOrNull{ productDetail->
                                        productDetail.remoteId == currentProductDetail?.remoteId
                                    }
                                    if (product != null) {
                                        currentProductDetail?.initialWeight = product.initialWeight
                                        roundRunProductAdapter?.updateInicial(product.initialWeight)
                                        val productIndex = roundRunProductAdapter?.selectedPosition
                                        productIndex.let {position->
                                            currentRoundRunDetail?.round?.diet?.products?.get(position!!)?.initialWeight = product.initialWeight
                                        }
                                    }
                                    val corral = roundRunDetail.round.corrals.firstOrNull{ corral->
                                        corral.remoteId == currentCorralDetail?.remoteId
                                    }
                                    if (corral != null) {
                                        currentCorralDetail?.initialWeight = corral.initialWeight
                                        roundRunCorralAdapter?.updateInicial(corral.initialWeight)
                                        val productIndex = roundRunProductAdapter?.selectedPosition
                                        productIndex.let {position->
                                            currentRoundRunDetail?.round?.corrals?.get(position!!)?.initialWeight = corral.initialWeight
                                        }
                                    }

                                    loadRoundDetail()
                                    it?.mixer.let {mixerDetail->
                                        val mixer = Mixer(
                                            mixerDetail!!.name,
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
                                        bSyncroMixer = true
                                    }
                                }
                            }
                            val title = "Mixer: ${mixerDetail?.name} - ${currentRoundRunDetail!!.round.name} : ${currentRoundRunDetail!!.round.diet.name}"
                            activity?.changeActionBarTitle(title)
                            bSyncroRound = true
                            if(!bSyncroRound){
                                requestProduct()
                            }
                        }
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"CMD_ROUNDDETAIL NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i(TAG,"CMD_ROUNDDETAIL Exception $e")
                    }

                }

                Constants.CMD_NXTPRODUCT->{
                    try{
                        Log.i(TAG,"CMD_NXTPRODUCT $messageStr")
                        val productPosition = messageStr.substring(3,7).toInt()
                        val productIndex = messageStr.substring(7,15).toLong()
                        val totalLoaded = messageStr.substring(15,23).toLong()
                        Log.i(TAG,"${productDetail?.name} total loaded $totalLoaded Next product productPosition $productPosition productIndex $productIndex")
                        if(productDetail?.remoteId == productIndex){
                            return
                        }
                        bSyncroProduct = false
                        bSyncroSequence = false
                        bSyncroRound = false
//                        mBinding.tvCurrentProductWeightPending.text = "Total: ${totalLoaded}Kg"
                        mBinding.tvCurrentProduct.text = "${productDetail?.name}"
                        roundRunProductAdapter?.selectProduct(productPosition)
                        requestRoundRunDetail()
                    }catch(e :  NumberFormatException){
                        Log.i(TAG,"CMD_NXTPRODUCT NumberFormatException $e ")
                    }catch ( e : Exception){
                        Log.i(TAG,"CMD_NXTPRODUCT Exception $e ")
                    }
                    Log.i(TAG,"CMD_NXTPRODUCT")

                }

                Constants.CMD_NXTCORRAL->{
                    try{
                        Log.i(TAG,"CMD_NXTCORRAL $messageStr")
                        val corralPosition = messageStr.substring(3,7).toInt()
                        val corralIndex = messageStr.substring(7,15).toLong()
                        val totalDownload = messageStr.substring(15,23).toLong()
                        Log.i(TAG,"${corralDetail?.name} total loaded $totalDownload Next corralt corralPosition $corralPosition productIndex $corralIndex")
                        if(corralDetail?.remoteId == corralIndex){
                            return
                        }
                        bSyncroCorral = false
                        bSyncroSequence = false
                        bSyncroRound = false
//                        mBinding.tvCurrentProductWeightPending.text = "Total: ${totalLoaded}Kg"
                        mBinding.tvCurrentProduct.text = "${corralDetail?.name}"
                        roundRunCorralAdapter?.selectCorral(corralPosition)
                        requestRoundRunDetail()
                    }catch(e :  NumberFormatException){
                        Log.i(TAG,"CMD_NXTCORRAL NumberFormatException $e ")
                    }catch ( e : Exception){
                        Log.i(TAG,"CMD_NXTCORRAL Exception $e ")
                    }
                    Log.i(TAG,"CMD_NXTCORRAL")

                }

                Constants.CMD_END->{
                    Log.i(TAG,"CMD_END $messageStr")
                    val msg = "CMD${Constants.CMD_ACK}${Constants.CMD_END}"
                    activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                    roundRunProductAdapter?.endLoad = true
                    totalWeightLoaded = 0.0
                    currentRoundRunDetail?.round?.diet?.products?.forEach { product ->
                        totalWeightLoaded += (product.finalWeight - product.initialWeight)
                        Log.i(TAG,"product $product totalWeightLoaded $totalWeightLoaded final ${product.finalWeight} inicial ${product.initialWeight}")
                    }

                    totalWeightLoaded = totalWeightLoaded.roundToLong().toDouble()
                    bSyncroSequence = false
                    bSyncroProduct = false
                    bSyncroCorral = false
                    currentRoundRunDetail = null
                    roundRunProductAdapter?.notifyDataSetChanged()

                    if(bInDownload){
                        cleanAll()
                        goToTabletMixerListFragment()
                    }
                }

                Constants.CMD_ACK->{
                    Log.i(TAG,"CMD_ACK")
                    when(String(message,3,3)){
                        Constants.CMD_USER-> {
                            Log.i(TAG,"CMD_USER ACK")
                            bSyncroUser = true
                            if(!bSyncroMixer){
                                requestMixer()
                            }
                        }
                    }
                }

                Constants.CMD_MIXER ->{
                    Log.i(TAG,"CMD_MIXER")
                    try{
                        val json = convertStringToZip.decompress(message.copyOfRange(7,message.size-1),String(message,3,4).toInt())
                        val gson = Gson()
                        mixerDetail = gson.fromJson(json,  MixerDetail::class.java)
                        Log.i(TAG,"mixer receibe $mixerDetail")
                        bSyncroMixer = true
                        if(!bSyncroRound){
                            Log.i(TAG,"CMD_MIXER y !bSybcroRound")
                            requestRoundRunDetail()
                        }
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

                Constants.CMD_PRODUCT ->{
                    Log.i(TAG,"CMD_PRODUCT")
                    try{
                        val json = convertStringToZip.decompress(message.copyOfRange(7,message.size-1),String(message,3,4).toInt())
                        val gson = Gson()
                        productDetail = gson.fromJson(json,  ProductDetail::class.java)
                        if(productDetail != null && productDetail!! != currentProductDetail){
                            previousProductDetail = currentProductDetail
                            currentProductDetail = productDetail
                            if(previousProductDetail == null ){
                                previousProductDetail =currentProductDetail
                            }
                        }

                        Log.i(TAG,"Product receibed $productDetail")
                        bSyncroProduct = true
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"bSyncroProduct NumberFormatException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"bSyncroProduct Exception $e")
                        return
                    }
                }


                Constants.CMD_CORRAL ->{
                    Log.i(TAG,"CMD_CORRAL")
                    try{
                        val json = convertStringToZip.decompress(message.copyOfRange(7,message.size-1),String(message,3,4).toInt())
                        val gson = Gson()
                       corralDetail = gson.fromJson(json,  CorralDetail::class.java)
                        if(corralDetail != null && corralDetail!! != currentCorralDetail){
                            previousCorralDetail = currentCorralDetail
                            currentCorralDetail = corralDetail
                            if(previousCorralDetail == null ){
                                previousCorralDetail =currentCorralDetail
                            }
                        }

                        Log.i(TAG,"Corral receibed $corralDetail")
                        bSyncroCorral = true
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"bSyncroCorral NumberFormatException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"bSyncroCorral Exception $e")
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
            if(message != null && message.equals("*")){
                deviceConnected()
                tickCountMessages = tick
            }
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("sent", "onMessageSent ${device?.address} $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i("sent", "onCommandSent ${device?.address} ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[MixRem] ACT onError")
            deviceDisconnected()
        }

        override fun onDeviceDisconnected() {
            deviceDisconnected()
            Log.i(TAG, "[MixRem]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[MixRem]onBondedDevices ${device?.size} \n$device")
            knowDevices = device
            knowDevices?.forEach{
                Log.i(TAG,"selectedTabletMixerInFragment $selectedTabletMixerInFragment \nbluetoothKnowed $it")
                if(selectedTabletMixerInFragment !=null && selectedTabletMixerInFragment!!.mac == it.address){
                    Log.i(TAG,"Se seleccionó ${it.name} : ${it.address}")
                    if(tabletMixerBluetoothDevice != it){
                        connectTable(selectedTabletMixerInFragment!!)
                    }
                    tabletMixerBluetoothDevice = it
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
        Log.i(TAG,"disconnectKnowDeviceWithTransfer L800")
        activity!!.mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }


    fun tare() {
    }

    fun deviceDisconnected() {
        if(isConnected){
            activity?.deviceDisconnected()
            Log.i(TAG,"deviceDisconnected")
            isConnected = false
        }
    }

    fun deviceConnected() {
        if(!isConnected){
            activity?.deviceConnected()
            Log.i(TAG,"deviceConnected")
            isConnected = true
        }
        activity?.hideCustomProgressDialog()
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

    private var antStatus : Boolean = false
    fun changeStatusConnection(bConnected: Boolean){
        activity?.runOnUiThread {
            if(antStatus != bConnected){
                Log.i(TAG, "changeStatusConnection $bConnected")
                antStatus = bConnected
            }
            if (bConnected) {
                deviceConnected()
            } else {
                deviceDisconnected()
            }
        }
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
        if(dialogCountDown != null && dialogCountDown!!.isShowing ){
            return dialogCountDown
        }
        var title: String? = null
        var message: String? = null
        val strkg : String =  strKg

        if(bInLoad)
            if(getCurrentProduct()!=null){
                title = getCurrentProduct()!!.name
                message = if(nextItem.isNotEmpty()){
                    "Se pasa a $nextItem"
                }else{
                    "Se va a pasar al proximo producto! "
                }
            }
        if(bInDownload)
            if(getCurrentCorral()!=null){
                title = getCurrentCorral()!!.name
                message = if(nextItem.isNotEmpty()){
                    "Se pasa a $nextItem"
                }else{
                    "Se va a pasar al proximo corral! "
                }
            }

        val builder = AlertDialog.Builder(requireActivity())
            .create()
        val  view = layoutInflater.inflate(R.layout.dialog_custom_target_weight_reached,null)
        val  btnAcept = view.findViewById<Button>(R.id.btn_dialog_acept)
        val  btnCancel = view.findViewById<Button>(R.id.btn_dialog_cancel)
        val  tvTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val  tvMessage = view.findViewById<TextView>(R.id.tv_dialog_message)
        val  tvCount = view.findViewById<TextView>(R.id.tv_dialog_count)


        if(title==null || message == null){
            return null
        }
        builder.setView(view)
        tvMessage.text = message
        tvTitle.text = title
        btnAcept.setOnClickListener {
            if(bInLoad && getCurrentProduct()!=null){
                sendNextProduct()
            }
            if(bInDownload && getCurrentCorral()!=null){
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
        return previousProductDetail
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
                menu?.findItem(R.id.menu_selected_remote_mixer)?.title = "  " + tabletMixer.name
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
            tickCountMessages = tick
            (requireActivity() as MainActivity).showCustomProgressDialog()
            Log.i(TAG,"connectKnowDeviceWithTransfer L1022 ${countConection++}")
            (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(deviceBluetooth)
        } else {
            Toast.makeText(requireActivity(), "No se pudo conectar", Toast.LENGTH_SHORT).show()
        }
    }


}