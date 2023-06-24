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
import androidx.recyclerview.widget.GridLayoutManager
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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.math.roundToLong

class RemoteMixerFragment : BottomSheetDialogFragment() {


    private var waitInit: Boolean = true
    private var tick: Long = 0L
    lateinit var mBinding: FragmentRemoteMixerBinding
    private val TAG: String = "DEBMixerVR"
    private var dialogResto: AlertDialog? = null
    private var targetReachedDialog: AlertDialog? = null
    private var estableTimer: Timer? = null
    private var mixerWeight = 0.0
    private var totalMixerWeight: Double = 0.0
    private var lastUpdate: LocalDateTime? = null
    private var timerTask: TimerTask? = null
    private var defaultStep = 5.0
    private var lastMessage = ""
    private var activity: MainActivity? = null
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentProductDetail: ProductDetail? = null
    private var previousProductDetail: ProductDetail? =null
    private var contTara: Int = 0
    private var mixerDetail: MixerDetail? = null
    var currentRoundRunDetail: RoundRunDetail? = null
    private var showSnackbar = true
    private var isConnected = false
    private var snackbar: Snackbar? = null
    private var dialogCountDown: AlertDialog? = null
    private var selectedMixerInFragment: Mixer? = null
    private var mixerBluetoothDevice : BluetoothDevice? = null
    private var knowDevices: List<BluetoothDevice>? = null
    private var messageTickRecord = 0L

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
            

        }

        mBinding.btnJump.setOnClickListener{
        }

        mBinding.btnRest.setOnClickListener{
            dialogResto = if(dialogResto==null){
                customDialog("Resto","${totalMixerWeight.roundToLong()}Kg",40f, Gravity.CENTER)
            }else{
                dialogResto?.dismiss()
                null
            }
        }

        currentRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
            val roundRunProductAdapter =  RoundRunProductAdapter(
                this@RemoteMixerFragment,
                dietDetail,
                defaultStep)
            currentRoundRunDetail?.round?.diet?.products?.let { products ->
                roundRunProductAdapter.productList(products)
            }
            mBinding.rvMixerProductsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))
            mBinding.rvMixerProductsToLoad.adapter = roundRunProductAdapter


            currentRoundRunDetail?.round?.diet?.products?.let { products ->
                products.forEach{productDetail ->
                    if(productDetail.finalWeight == 0.0){
                        selectProduct(productDetail)
                    }else{
                        nextProduct()
                    }
                }
            }
        }


        //Conectar bluetooth
        mixerBluetoothDevice?.let {
            activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
        }
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        currentRoundRunDetail?.state = Constants.STATE_LOAD
    }

    fun exitFragment(){
        cleanAll()
        requireActivity().finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRemoteMixerBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    private fun selectProduct(product: ProductDetail){
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
        mBinding
        mBinding.tvCurrentProductWeightPending.text = "${sign}${Helper.getNumberWithDecimals (value, 0)}Kg  ${product.name}"
        val percentage = (product.currentWeight-product.initialWeight)*100/targetProductWeight
        mBinding.pbCurrentProduct.progress = percentage.toInt()

        if(currentProductDetail!=null && activity!=null){
            if(!mBinding.btnPause.isChecked && currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight >= currentProductDetail!!.targetWeight){
                if(countGreaterThanTarget>10){
                    countGreaterThanTarget = 0
                    noPrevAlert = false
                    val adapterProduct = (mBinding.rvMixerProductsToLoad.adapter as RoundRunProductAdapter)
                    val nextProductName = adapterProduct.getProduct(adapterProduct.selectedPosition + 1)?.name
                    targetReachedDialog = if(nextProductName.isNullOrEmpty()){
                        if(adapterProduct.isLastProduct()){
                            dialogAlertTargetWeight("descarga")
                        }else{
                            dialogAlertTargetWeight()
                        }
                    } else{
                        dialogAlertTargetWeight(nextProductName)
                    }
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
        }

        override fun onCommandReceived(device: BluetoothDevice?, byteArray: ByteArray?) {
            val convertStringToZip = ConvertStringToZip()
            if(byteArray == null || byteArray.size<9){
                Log.i(TAG,"command not enough large (${byteArray?.size})")
                return
            }
            val byteArrayUtil = byteArray.copyOfRange(6,byteArray.size)
            var arraySize : Int =0
            try{
                val strToInt = String(byteArray,0,6)
                arraySize = strToInt.toInt()
                Log.i(TAG,"strToInt ${strToInt} - $strToInt")
            }catch (e: NumberFormatException){
                Log.i(TAG,"NumberFormatException")
               return
            }catch (e:Exception){
                Log.i(TAG,"Exception")
                return
            }
            val str : String = convertStringToZip.decompress(byteArrayUtil,arraySize)
            Log.i(TAG,"CMD Received (${str.length}): $str")
            if(waitInit){
                    waitInit = false
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            lastMessage = "No se están recibiendo datos"
            message?.let{
                lastMessage = it.replace("\n", "")
                if (it.length > 3){
                    activity?.hideCustomProgressDialog()
                }
            }
            if (lastUpdate == null){
                lastUpdate = LocalDateTime.now()
            }

            var currentMixerWeight = message?.substring(1, 7)?.toDoubleOrNull()
            if(currentMixerWeight != null){
                if(tick - messageTickRecord  > 20){
                    val msg = "CMD${Constants.CMD_CRRD}"
                    Log.i(TAG,"Send INIT_CMD $msg")
                    activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                    waitInit = true
                    messageTickRecord = tick
                }
            }

            if (currentMixerWeight != null && currentRoundRunDetail != null){
                currentMixerWeight = (currentMixerWeight- mixerDetail!!.tara)*mixerDetail!!.calibration
                totalMixerWeight = currentMixerWeight
                if(dialogResto!=null && dialogResto!!.isShowing){
                    dialogResto!!.setMessage("${totalMixerWeight.roundToLong()}Kg")
                }
                mixerWeight = currentMixerWeight - currentRoundRunDetail?.customTara!! - currentRoundRunDetail?.addedBlend!!
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
            currentRoundRunDetail?.round?.diet?.products?.forEach {
                actualWeight += it.currentWeight - it.initialWeight
            }

            if(contTara > 0){
                contTara--
                Log.i(TAG,"actualWeight: $actualWeight | mixerWeight: $mixerWeight | additional: ${currentProductDetail?.currentWeight}")
            }

            lastUpdate = LocalDateTime.now()
            var loaded = getCurrentWeight()// + additional
            var sign = ""
            if(loaded <0 ){
                loaded *= -1
                sign = "+"
            }

            if(targetReachedDialog !=null && targetReachedDialog!!.isShowing){
                val textView = targetReachedDialog?.window?.findViewById<TextView>(R.id.tv_dialog_kg)
                textView?.text = mBinding.tvCurrentProductWeightPending.text
            }
            mBinding.tvMixerLoaded.text = "${sign}${Helper.getNumberWithDecimals(loaded,0)}Kg"
            if(currentMixerWeight != null){
                mBinding.tvMixerTarget.text = "${Helper.getNumberWithDecimals(currentMixerWeight,0)}Kg"
            }
            val percentage = (getCurrentWeight())*100/getTargetWeight()
            mBinding.pbCurrentMixer.progress = percentage.toInt()

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

    fun nextProduct() {
    }

    fun tare() {
    }

    private fun getCurrentProduct(): ProductDetail? {
        return previousProductDetail
    }

    private fun lastProduct() {
        mBinding.btnJump.text = getString(R.string.descarga)
    }

    private fun noLastProduct() {
        mBinding.btnJump.text = getString(R.string.salto)
    }

    fun setProduct(product: ProductDetail) {
        currentProductDetail = product
        previousProductDetail = product
        selectProduct(product)
    }

    fun deviceDisconnected() {
        isConnected = false
    }

    fun deviceConnected() {
        isConnected = true
    }

    fun connectDevice(){
        if(!isConnected){
            snackbar?.dismiss()
            mixerBluetoothDevice?.let { _blueToothDevice->
                activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(_blueToothDevice)
            }
            activity?.showCustomProgressDialog()
            Timer().schedule(5000) {
                activity?.hideCustomProgressDialog()
                showSnackbar = true
                Log.i(TAG,"changeStatusConnection(false) MRV 453")
                changeStatusConnection(false)
            }
        }
    }

    private fun customDialog(title: String, message: String, fontSize : Float = 0F, gravity: Int = 0) : AlertDialog? {
        val dialogBuilder = AlertDialog.Builder(activity?.applicationContext)

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

    private fun dialogAlertTargetWeight(nextItem : String ="", strKg :String="") : AlertDialog? {
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

        val builder = AlertDialog.Builder(activity?.applicationContext)
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
                nextProduct()
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
                            nextProduct()
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

    private var antStatus : Boolean = false
    fun changeStatusConnection(bConnected: Boolean){
        activity?.runOnUiThread {
            if(antStatus != bConnected){
                Log.i(TAG, "changeStatusConnection $bConnected")
                antStatus = bConnected
            }
            if (bConnected) {
                showSnackbar = true
                deviceConnected()
                snackbar?.dismiss()
            } else {
                deviceDisconnected()
                if (showSnackbar){
                    showSnackbar = false
                    if(!mBinding.viewFragmentMixerRemoto.isAttachedToWindow){
                        return@runOnUiThread
                    }
                    snackbar =
                        Snackbar.make(mBinding.viewFragmentMixerRemoto, "Se perdió la conexión",
                            BaseTransientBottomBar.LENGTH_INDEFINITE
                        )
                    snackbar?.setAction(
                        "Reconectar"
                    ) {
                        Log.i(TAG,"mixerBluetoothDevice ${mixerBluetoothDevice} | selectedMixerInFragment $selectedMixerInFragment")
                        if(mixerBluetoothDevice != null){
                            mixerBluetoothDevice?.let {
                                activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                            }
                        }else{
                            selectedMixerInFragment?.let { mixerS ->
                                Log.i(TAG,"mixerBluetoothDevice null $selectedMixerInFragment")
                                setMixer(mixerS) }
                        }
                        activity?.showCustomProgressDialog()
                        Timer().schedule(5000) {
                            activity?.hideCustomProgressDialog()
                            showSnackbar = true
                            changeStatusConnection(false)
                            Log.i(TAG,"changeStatusConnection(false) MRV 606")
                        }
                    }
                    snackbar?.show()
                }

            }
        }
    }



    fun getCurrentWeight() : Double {
        var currentWeight = 0.0
        currentRoundRunDetail?.round?.diet?.products?.forEach {
            currentWeight += it.currentWeight-it.initialWeight
        }
        return currentWeight.roundToLong().toDouble()
    }

    fun getFinalLoad() : Double {
        var currentWeight = 0.0
        currentRoundRunDetail?.round?.diet?.products?.forEach {
            currentWeight += it.finalWeight - it.initialWeight
        }
        return currentWeight.roundToLong().toDouble()
    }

    fun getTargetWeight() : Double {
        var targetWeight = 0.0

        currentRoundRunDetail?.round?.corrals?.forEach {
            targetWeight += it.actualTargetWeight
        }
        return targetWeight.roundToLong().toDouble()
    }

    fun getLoadDifference() : Double {
        var totalLoadDiff = 0.0
        currentRoundRunDetail?.round?.diet?.products?.forEach { productDetail->
            totalLoadDiff += ((productDetail.finalWeight-productDetail.initialWeight)-productDetail.targetWeight )
        }
        return totalLoadDiff.roundToLong().toDouble()
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


}