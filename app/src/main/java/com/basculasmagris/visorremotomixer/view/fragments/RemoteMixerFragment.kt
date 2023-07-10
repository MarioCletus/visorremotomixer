package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.utils.*
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.getCurrentUser
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.math.roundToLong

class RemoteMixerFragment : BottomSheetDialogFragment() {

    lateinit var mBinding: FragmentRemoteMixerBinding
    private val TAG: String = "DEBMixerVR"

    private var dialogResto: AlertDialog? = null
    private var estableTimer: Timer? = null
    private var mixerWeight = 0.0
    private var totalMixerWeight: Double = 0.0
    private var lastUpdate: LocalDateTime? = null
    private var timerTask: TimerTask? = null
    private var defaultStep = 5.0
    private var activity: MainActivity? = null
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentProductDetail: ProductDetail? = null
    private var previousProductDetail: ProductDetail? =null
    private var mixerDetail: MixerDetail? = null
    private var productDetail : ProductDetail? = null
    var currentRoundRunDetail : RoundRunDetail? = null
    var roundRunProductAdapter : RoundRunProductAdapter? = null

    private var bSyncroSequence: Boolean = false
    private var bSyncroUser: Boolean = false
    private var bSyncroMixer: Boolean = false
    private var bSyncroRound: Boolean = false
    private var bSyncroProduct: Boolean = false
    private var bInLoad: Boolean = false
    private var bShowResume : Boolean = false

    private var targetWeight: Double = 0.0
    private var indexAnterior: Int = -1
    private var tick: Long = 0L
    private var tickCounterMessages : Long = 0
    private var countResume : Long = 0L
    private var totalWeightLoaded: Double = 0.0

    //    private var showSnackbar = true
    private var isConnected = false
//    private var snackbar: Snackbar? = null
    private var selectedMixerInFragment: Mixer? = null
    private var mixerBluetoothDevice : BluetoothDevice? = null
    private var knowDevices: List<BluetoothDevice>? = null

    private var targetReachedDialog: AlertDialog? = null
    private var dialogCountDown: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = (requireActivity() as MainActivity)
        activity?.showCustomProgressDialog()


        mBinding.btnTara.setOnClickListener{
            Log.i(TAG, "btnTara")
            val msg = "CMD${Constants.CMD_TARA}"
            activity?.mService?.LocalBinder()?.write(msg.toByteArray())
        }

        mBinding.btnJump.setOnClickListener{
            Log.i(TAG, "btnJump")
            targetReachedDialog = dialogAlertTargetWeight("descarga")
        }

        mBinding.btnPause.setOnClickListener{
            Log.i(TAG, "btnPause")
            val msg = "CMD${Constants.CMD_PAUSE}"
            activity?.mService?.LocalBinder()?.write(msg.toByteArray())
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
        mixerBluetoothDevice?.let {
            activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
        }
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        currentRoundRunDetail?.state = Constants.STATE_LOAD
        Log.i(TAG,"onViewCreated ready")
    }

    private fun loadRoundDetail() {
        currentRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = LinearLayoutManager(requireActivity(),0,false)
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
                products.forEach{productDetail ->
                    if(productDetail.finalWeight == 0.0){
                        currentProductDetail = productDetail
                        roundRunProductAdapter?.selectProduct(countProductPosition)
                        return@forEach
                    }else{
                        bShowResume = false
                        countResume = tick
                    }
                    countProductPosition++
                }
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRemoteMixerBinding.inflate(inflater, container, false)
        return mBinding.root
    }



    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
        activity = this.getActivity() as MainActivity?
        Log.i(TAG,"activity $activity")
        if(activity is MainActivity){
            Log.i(TAG,"activity is MainActivity $activity")
            activity!!.getSavedMixer()
        }

        estableTimer = Timer(false)
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("RUN", "mixerWeight:$mixerWeight | lastUpdate: $lastUpdate |  Time:${lastUpdate?.until(
                    LocalDateTime.now(), ChronoUnit.SECONDS)}")
                tick ++
                if(tick == 3L){
                    MainScope().launch {
                        connectDevice()
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
                    }else if(!bSyncroRound && bInLoad){
                        Log.i(TAG,"bSyncroRound = false")
                        requestRoundRunDetail()
                    }else if(!bSyncroProduct && bInLoad){
                        requestProduct()
                    }else if(bSyncroProduct && bSyncroRound && bSyncroMixer && bSyncroUser){
                        bSyncroSequence = true
                    }
                }

                if(productDetail!=null && activity!=null){
                    if(productDetail!!.currentWeight-productDetail!!.initialWeight > productDetail!!.targetWeight*0.9){
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

                //Check connection
                if (lastUpdate != null){
                    if (lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 5){
                        Log.i("CONEXION", "Conexión: NO Aca? | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        changeStatusConnection(false)
                        if(mixerBluetoothDevice != null){
                            mixerBluetoothDevice?.let {
                                Log.i(TAG,"La puta que te pario ${mixerBluetoothDevice}")
                                activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                            }
                        }
                    } else {
                        Log.i("RUN", "Conexión: SI | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        if(isAdded){
                            Log.i("RUN", "Change status connection rt")
                            changeStatusConnection(true)
                        }else{
                            Log.i("CONEXION", "Conexión: NO | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        }
                    }
                }
            }
        }
        estableTimer?.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
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
            deviceConnected()
            Log.i(TAG, "[MixRem] ACT onDeviceConnected")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            val convertStringToZip = ConvertStringToZip()
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val messageStr : String = message.size.let { size-> String(message,0, size)}
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","command $command")
            when (command){
                Constants.CMD_INI->{
                    bSyncroSequence = false
                    bSyncroProduct = false
                    bSyncroRound = false
                    roundRunProductAdapter?.endLoad = false
                    val msg = "CMD${Constants.CMD_ACK}${Constants.CMD_INI}"
                    activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                }

                Constants.CMD_INLOAD->{
                    bInLoad = true
                }

                Constants.CMD_ROUNDDETAIL->{
                    Log.i(TAG,"CMD_ROUNDDETAIL")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    val arraySize: Int
                    try{
                        arraySize = String(message,3,6).toInt()
                        val str : String = convertStringToZip.decompress(byteArrayUtil,arraySize)
                        if(str.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : RoundRunDetail = gson.fromJson(str,  RoundRunDetail::class.java)
                            if(currentRoundRunDetail != null && currentRoundRunDetail!!.id == roundRunDetail.id){
                                Log.i(TAG,"notifyDataSetChanged roundRunDetail $roundRunDetail")
                                roundRunProductAdapter?.updateRound(roundRunDetail)
                            }else{
                                Log.i(TAG,"new roundRunDetail $roundRunDetail")
                                currentRoundRunDetail = roundRunDetail
                                currentRoundRunDetail.let { it ->
                                    val product = roundRunDetail.round.diet.products.firstOrNull{ productDetail->
                                        productDetail.id == currentProductDetail?.id
                                    }
                                    if (product != null) {
                                        currentProductDetail?.initialWeight = product.initialWeight
                                        roundRunProductAdapter?.updateInicial(product.initialWeight)
                                        val productIndex = roundRunProductAdapter?.selectedPosition
                                        productIndex.let {position->
                                            currentRoundRunDetail?.round?.diet?.products?.get(position!!)?.initialWeight = product.initialWeight
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
                            val title : String = "Mixer: ${mixerDetail?.name} - ${currentRoundRunDetail!!.round.name} : ${currentRoundRunDetail!!.round.diet.name}"
                            activity?.changeActionBarTitle(title)
                            bSyncroRound = true
                            if(!bSyncroRound){
                                requestProduct()
                            }
                        }
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i(TAG,"Exception $e")
                    }

                }

                Constants.CMD_NXTPRODUCT->{
                    try{
                        val productPosition = messageStr.substring(3,7).toLong().toInt()
                        val productIndex = messageStr.substring(7,15).toLong()
                        if(productDetail?.remoteId == productIndex){
                            return
                        }
                        bSyncroProduct = false
                        bSyncroSequence = false
                        bSyncroRound = false
                        countResume = tick
                        bShowResume = true
                        roundRunProductAdapter?.selectProduct(productPosition)
                        requestRoundRunDetail()
                    }catch(e :  NumberFormatException){
                        Log.i(TAG,"CMD_NXTPRODUCT NumberFormatException $e ")
                    }catch ( e : Exception){
                        Log.i(TAG,"CMD_NXTPRODUCT Exception $e ")
                    }
                    Log.i(TAG,"CMD_NXTPRODUCT")

                }

                Constants.CMD_END->{
                    Log.i(TAG,"CMD_END")
                    roundRunProductAdapter?.endLoad = true
                    roundRunProductAdapter?.notifyDataSetChanged()
                    totalWeightLoaded = 0.0
                    currentRoundRunDetail?.round?.diet?.products?.forEach { product ->
                        totalWeightLoaded += (product.finalWeight - product.initialWeight)
                    }
                    totalWeightLoaded = totalWeightLoaded.roundToLong().toDouble()
                    bSyncroSequence = false
                    bSyncroProduct = false
                    bShowResume = true
                    countResume = tick
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
                        val title : String = "Mixer: ${mixerDetail?.name}"
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
                        if(productDetail != null && !productDetail!!.equals(currentProductDetail)){
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

                else->{
                    Log.i(TAG,"else $command")
                }
            }

        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            if(message == null  || message.length < 7)
                return
            activity?.hideCustomProgressDialog()
            lastUpdate = LocalDateTime.now()

            var currentMixerWeight = message.substring(1, 7).toDoubleOrNull()

            if(bShowResume){
                if(tick - countResume < 5){
                    if(roundRunProductAdapter?.endLoad == true){
                        mBinding.tvCurrentProductWeightPending.text = "${Helper.getNumberWithDecimals(totalWeightLoaded,0)}Kg"
                        mBinding.tvCurrentProduct.text = getString(R.string.carga_total)
                    }else{
                        if(productDetail!=null && previousProductDetail != null){
                            mBinding.tvCurrentProductWeightPending.text = "Total: ${Helper.getNumberWithDecimals(previousProductDetail?.finalWeight!!-previousProductDetail?.initialWeight!!,0)}Kg"
                            mBinding.tvCurrentProduct.text = "Resumen: ${previousProductDetail?.name}"
                        }
                    }
                    bInLoad = false
                    return
                }
                bShowResume = false
            }

            if(!bSyncroSequence && bSyncroMixer && currentMixerWeight != null){
                currentMixerWeight = (currentMixerWeight - mixerDetail!!.tara)*mixerDetail!!.calibration
                mBinding.tvCurrentProduct.text = getString(R.string.mixer)
                mBinding.tvCurrentProductWeightPending.text = Helper.getNumberWithDecimals(currentMixerWeight,0)
            }

            if(bInLoad && bSyncroSequence &&  currentMixerWeight != null && productDetail != null){
                currentMixerWeight = (currentMixerWeight - mixerDetail!!.tara)*mixerDetail!!.calibration
                mixerWeight = currentMixerWeight - currentRoundRunDetail?.customTara!! - currentRoundRunDetail?.addedBlend!!
                var value = productDetail?.targetWeight?.minus(mixerWeight - productDetail?.initialWeight!!)!!
                productDetail?.currentWeight = mixerWeight
                val percentage = (productDetail?.targetWeight!!-value)*100/ productDetail?.targetWeight!!
                mBinding.pbCurrentProduct.progress = percentage.toInt()
                var sign = ""
                if(value < 0){
                    sign = "+"
                    value *= -1
                }
                mBinding.tvCurrentProduct.text = productDetail?.name
                mBinding.tvCurrentProductWeightPending.text = "$sign${Helper.getNumberWithDecimals(value,0 )}Kg"
                roundRunProductAdapter?.updateWeight(mixerWeight)
            }
            bInLoad = false

        }

        override fun onMessageSent(device: BluetoothDevice?) {
            Log.i(TAG, "[MixRem] ACT onMessageSent")
        }

        override fun onError(message: String?) {
            deviceDisconnected()
            Log.i(TAG, "[MixRem] ACT onError")
        }

        override fun onDeviceDisconnected() {
            deviceDisconnected()
            Log.i(TAG, "[MixRem]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[MixRem]ACT onBondedDevices ${device?.size} \n$device")
            knowDevices = device

            knowDevices?.forEach{
                Log.i(TAG,"selectedMixerInFragment $selectedMixerInFragment \nbluetoothKnowed $it")
                if(selectedMixerInFragment !=null && selectedMixerInFragment!!.mac == it.address){
                    mixerBluetoothDevice = it
                    Log.i(TAG,"Se seleccionó ${it.name} : ${it.address}")
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanAll()
        Log.i("RUN", "CANCELADO Simular bluetooth")
    }

    private fun cleanAll(){
        estableTimer?.cancel()
        timerTask?.cancel()
        activity!!.mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }


    fun tare() {
    }

    fun deviceDisconnected() {
        isConnected = false
    }

    fun deviceConnected() {
        isConnected = true
    }

    fun connectDevice(){
        if(!isConnected){
            mixerBluetoothDevice?.let { _blueToothDevice->
                activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(_blueToothDevice)
            }
            activity?.showCustomProgressDialog()
            Timer().schedule(5000) {
                activity?.hideCustomProgressDialog()
                Log.i(TAG,"changeStatusConnection(false) MRV 453")
                changeStatusConnection(false)
            }
        }
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

    fun dialogAlertTargetWeight(nextItem : String ="",strKg :String="") : AlertDialog? {
        if(dialogCountDown != null && dialogCountDown!!.isShowing ){
            return dialogCountDown
        }
        var title: String? = null
        var message: String? = null
        val strkg : String =  strKg
        if(getCurrentProduct()!=null){
            title = getCurrentProduct()!!.name
            message = if(nextItem.isNotEmpty()){
                "Se pasa a $nextItem"
            }else{
                "Se va a pasar al proximo producto! "
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
        if(getCurrentProduct()!=null){
            title = getCurrentProduct()!!.name
            message = if(nextItem.isNotEmpty()){
                "Se pasa a $nextItem"
            }else{
                "Se va a pasar al proximo producto! "
            }
        }

        if(title==null || message == null){
            return null
        }
        builder.setView(view)
        tvMessage.text = message
        tvTitle.text = title
        btnAcept.setOnClickListener {
            if(getCurrentProduct()!=null){
                sendNextProduct()
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
                            sendNextProduct()
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

    fun getCurrentProduct(): ProductDetail? {
        return previousProductDetail
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

}