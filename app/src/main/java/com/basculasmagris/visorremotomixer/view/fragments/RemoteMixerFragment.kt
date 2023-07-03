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
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.math.round
import kotlin.math.roundToLong

class RemoteMixerFragment : BottomSheetDialogFragment() {

    lateinit var mBinding: FragmentRemoteMixerBinding
    private val TAG: String = "DEBMixerVR"
    private var targetWeight: Double = 0.0
    private var indexAnterior: Int = -1
    private var tick: Long = 0L
    private var tickCounterMessages : Long = 0

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

//    private var showSnackbar = true
    private var isConnected = false
//    private var snackbar: Snackbar? = null
    private var selectedMixerInFragment: Mixer? = null
    private var mixerBluetoothDevice : BluetoothDevice? = null
    private var knowDevices: List<BluetoothDevice>? = null

    private var targetReachedDialog: AlertDialog? = null
    private var dialogCountDown: AlertDialog? = null
    private var refresh: Boolean = false

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
        activity?.currentRoundRunDetail?.state = Constants.STATE_LOAD
    }

    private fun loadRoundDetail() {
        activity?.currentRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = LinearLayoutManager(requireActivity(),0,false)
            val roundRunProductAdapter =  RoundRunProductAdapter(
                this@RemoteMixerFragment,
                dietDetail,
                defaultStep)
            activity?.currentRoundRunDetail?.round?.diet?.products?.let { products ->
                roundRunProductAdapter.productList(products)
            }
            mBinding.rvMixerProductsToLoad.adapter = roundRunProductAdapter


            activity?.currentRoundRunDetail?.round?.diet?.products?.let { products ->
                products.forEach{productDetail ->
                    if(productDetail.finalWeight == 0.0){
                        selectProduct(productDetail)
                    }else{
                        nextProduct()
                    }
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


    fun selectProduct(product: ProductDetail){
        val targetProductWeight = product.targetWeight//Helper.getNumberWithDecimals(product.percentage*roundTargetWeight/100,0).toDouble()
        var value = targetProductWeight - (product.currentWeight-product.initialWeight)
        var sign = ""
        if(value < 0){
            sign = "+"
            value *= -1
        }
        if((mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).isLastProduct()){
            lastProduct()
        }else{
            noLastProduct()
        }
        mBinding.tvCurrentProductWeightPending.text = "${sign}${Helper.getNumberWithDecimals (value, 0)}Kg"
        mBinding.tvCurrentProduct.text = product.name
        val percentage = (product.currentWeight-product.initialWeight)*100/targetProductWeight
        mBinding.pbCurrentProduct.progress = percentage.toInt()

        if(currentProductDetail!=null && activity!=null){
            if(currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight >= currentProductDetail!!.targetWeight){
                if(countGreaterThanTarget>10){
                    countGreaterThanTarget = 0
                    noPrevAlert = false
                }
            }
        }

        if(currentProductDetail == null){
            Log.i(TAG, "Producto inicial ${product.name} | ${product.targetWeight} + ${product.currentWeight} + ${product.initialWeight}")
            currentProductDetail = product
            previousProductDetail = product
        }
        currentProductDetail = product
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
                if(currentProductDetail!=null && activity!=null){
                    if(currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight > currentProductDetail!!.targetWeight*0.9){
                        if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(activity!!, R.color.white)){
                            if(currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight > currentProductDetail!!.targetWeight){
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

                    if(currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight > currentProductDetail!!.targetWeight && noPrevAlert){
                        countGreaterThanTarget++
                    }
                }

                //Check connection
                if (lastUpdate != null){
                    if (lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 5){
                        Log.i("RUN", "Conexión: NO | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        changeStatusConnection(false)
                    } else {
                        Log.i("RUN", "Conexión: SI | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        if(isAdded){
                            Log.i("RUN", "Change status connection rt")
                            changeStatusConnection(true)
                        }else{
                            Log.i("RUN", "Conexión: NO | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
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
            requestRoundRunDetail()
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            val convertStringToZip = ConvertStringToZip()
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val comando = String(message.copyOfRange(0,3))
            when (comando){
                Constants.CMD_INI->{
                    Log.i(TAG,"CMD_INI")
                }

                Constants.CMD_ROUNDDETAIL->{
                    Log.i(TAG,"CMD_ROUNDDETAIL")
                    val byteArrayUtil = message.copyOfRange(9,message.size-9)
                    val arraySize: Int
                    try{
                        val strToInt = String(message,3,6)
                        arraySize = strToInt.toInt()
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"NumberFormatException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"Exception $e")
                        return
                    }
                    val str : String = convertStringToZip.decompress(byteArrayUtil,arraySize)
                    if(str.isNotEmpty()){
                        val gson = Gson()
                        val roundRunDetail : RoundRunDetail = gson.fromJson(str,  RoundRunDetail::class.java)
                        if(activity?.currentRoundRunDetail != null && activity?.currentRoundRunDetail!!.id == roundRunDetail.id){
                            Log.i(TAG,"notifyDataSetChanged roundRunDetail $roundRunDetail")
                            activity?.currentRoundRunDetail = roundRunDetail

                            val product = roundRunDetail.round.diet.products.firstOrNull{ productDetail->
                                productDetail.id == currentProductDetail?.id
                            }
                            if (product != null) {
                                currentProductDetail?.initialWeight = product.initialWeight
                                (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).updateInicial(product.initialWeight)
                            }
                            mBinding.rvMixerProductsToLoad.adapter?.notifyDataSetChanged()
                        }else{
                            Log.i(TAG,"new roundRunDetail $roundRunDetail")
                            activity?.currentRoundRunDetail = roundRunDetail
                            activity?.currentRoundRunDetail.let { it ->
                                val product = roundRunDetail.round.diet.products.firstOrNull{ productDetail->
                                    productDetail.id == currentProductDetail?.id
                                }
                                if (product != null) {
                                    currentProductDetail?.initialWeight = product.initialWeight
                                    (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).updateInicial(product.initialWeight)
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
                                }
                            }
                        }
                    }
                }

                Constants.CMD_NXTPRODUCT->{
                    Log.i(TAG,"CMD_NXTPRODUCT")
                    nextProduct()
                    requestRoundRunDetail()
                }
                Constants.CMD_END->{
                    Log.i(TAG,"CMD_END")
                    (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).endLoad = true
                    (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).notifyDataSetChanged()
                }
                Constants.CMD_UPDATE->{
                    Log.i(TAG,"CMD_UPDATE")
                }
                Constants.CMD_ACK->{
                    Log.i(TAG,"CMD_ACK")
                }
                Constants.CMD_WEIGHT->{
                    activity?.hideCustomProgressDialog()
                    try{
                        val messageStr = String(message)
                        val roundIndex= messageStr.substring(3,9).toLong()
                        val productIndexInRound= messageStr.substring(9,11).toInt()
                        val currentProductWeight= messageStr.substring(11,17).toLong()
                        if((activity?.currentRoundRunDetail == null || (activity?.currentRoundRunDetail != null && activity?.currentRoundRunDetail?.id != roundIndex)) && tick - tickCounterMessages > 3){
                            Log.i(TAG,"message $messageStr\nNot match roundIndex $roundIndex activity?.currentRoundRunDetail.id ${activity?.currentRoundRunDetail?.id}")
                            requestRoundRunDetail()
                            tickCounterMessages = tick
                            return
                        }

                        if(activity?.currentRoundRunDetail != null && productIndexInRound != indexAnterior && tick - tickCounterMessages > 0){
                            Log.i(TAG,"productIndexInRound $productIndexInRound not match indexAnterio $indexAnterior ")
                            indexAnterior = productIndexInRound
                            val product : ProductDetail = activity?.currentRoundRunDetail!!.round.diet.products[productIndexInRound]
                            mBinding.tvCurrentProduct.text = product.name
                            targetWeight = product.targetWeight
                            activity?.changeActionBarTitle("${getString(R.string.mixer_remoto)} - ${activity?.currentRoundRunDetail!!.round.name}: ${activity?.currentRoundRunDetail!!.round.diet.name}")
                            requestRoundRunDetail()
                        }
                        val percentage = (targetWeight-currentProductWeight)*100/targetWeight
                        mBinding.tvCurrentProductWeightPending.text =   if(currentProductWeight<0)
                            "+${-1*currentProductWeight}Kg"
                        else
                            "${currentProductWeight}Kg"
                        mBinding.pbCurrentProduct.progress = percentage.toInt()
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"NumberFormatException $e")
                        return
                    }catch (e:StringIndexOutOfBoundsException){
                        Log.i(TAG,"StringIndexOutOfBoundsException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"Exception $e")
                        return
                    }
                    lastUpdate = LocalDateTime.now()
                }

                Constants.CMD_REFRESH->{
                    refresh = true
//                    Log.i(TAG,"CMD_REFRESH")
                }

                else->{
                    Log.i(TAG,"else $comando")
                }
            }

        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            if(message == null  || message.length < 7)
                return
            activity?.hideCustomProgressDialog()
            if (lastUpdate == null){
                lastUpdate = LocalDateTime.now()
            }

            var currentMixerWeight = message.substring(1, 7).toDoubleOrNull()


            if (currentMixerWeight != null && activity?.currentRoundRunDetail != null){
                currentMixerWeight = (currentMixerWeight- mixerDetail!!.tara)*mixerDetail!!.calibration
                totalMixerWeight = currentMixerWeight
                if(dialogResto!=null && dialogResto!!.isShowing){
                    dialogResto!!.setMessage("${totalMixerWeight.roundToLong()}Kg")
                }
                mixerWeight = currentMixerWeight - activity?.currentRoundRunDetail?.customTara!! - activity?.currentRoundRunDetail?.addedBlend!!
                if(previousProductDetail != null && currentProductDetail != null &&
                    previousProductDetail!!.id != currentProductDetail!!.id
                ){
                    Log.i(TAG,"previousProductDetail $previousProductDetail" +
                            "\n!=\ncurrentProductDetail $currentProductDetail |\ninitialWeight $mixerWeight")
                    previousProductDetail = currentProductDetail
                    currentProductDetail?.initialWeight = mixerWeight
                    (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).updateInicial(mixerWeight)
                }
            }

            var actualWeight = 0.0
            activity?.currentRoundRunDetail?.round?.diet?.products?.forEach {
                actualWeight += it.currentWeight - it.initialWeight
            }

            lastUpdate = LocalDateTime.now()

            if(targetReachedDialog !=null && targetReachedDialog!!.isShowing){
                val textView = targetReachedDialog?.window?.findViewById<TextView>(R.id.tv_dialog_kg)
                textView?.text = mBinding.tvCurrentProductWeightPending.text
            }
            if(refresh && mixerWeight != null && mBinding!=null && (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter) != null){
                (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).updateWeight(mixerWeight)
                refresh = false
            }

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

    private fun requestRoundRunDetail() {
        val msg = "CMD${Constants.CMD_ROUNDDETAIL}"
        Log.i(TAG,"Send requestRoundRunDetail $msg")
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
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

    fun nextProduct() {
        (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter).nextProduct()
    }

    fun tare() {
    }

    private fun lastProduct() {
        mBinding.btnJump.text = getString(R.string.descarga)
    }

    private fun noLastProduct() {
        mBinding.btnJump.text = getString(R.string.salto)
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


}