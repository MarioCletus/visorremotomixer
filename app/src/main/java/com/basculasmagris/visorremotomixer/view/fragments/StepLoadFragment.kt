package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentStepLoadBinding
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.MarginItemDecoration
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.roundToLong

class StepLoadFragment(mixerDetail: MixerDetail) : BottomSheetDialogFragment() {

    private var tick: Long = 0L
    lateinit var mBinding: FragmentStepLoadBinding
    private val TAG: String = "DEBLoad"
    private var dialogResto: AlertDialog? = null
    private var targetReachedDialog: AlertDialog? = null
    private var rotationTimer: Timer? = null
    private var estableTimer: Timer? = null
    private var mixerWeight = 0.0
    private var totalMixerWeight: Double = 0.0
    private var lastUpdate: LocalDateTime? = null
    private var timerTask: TimerTask? = null
    private var defaultStep = 5.0
    private var lastMessage = ""
    private var activity: RoundRunActivity? = null
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentProductDetail: ProductDetail? = null
    private var previousProductDetail: ProductDetail? =null
    private var contTara: Int = 0
    private var stepsDescription = arrayOf("Configuración","Carga", "Descarga", "Fin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = (requireActivity() as RoundRunActivity)
        activity?.showCustomProgressDialog()
        mBinding.spRoundRunStep.setStateDescriptionData(stepsDescription)
        mBinding.spRoundRunStep.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)

        mBinding.btnTara.setOnClickListener{
            activity?.
            customDialog("Tara","Seguro quiere hacer tara?")
        }

        mBinding.btnJump.setOnClickListener{
//            if((mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter).isLastProduct()){
//                targetReachedDialog = activity?.dialogAlertTargetWeight("descarga")
//            }else{
//                nextProduct()
//            }
        }

        mBinding.btnRest.setOnClickListener{
            dialogResto = if(dialogResto==null){
                activity?.customDialog("Resto","${totalMixerWeight.roundToLong()}Kg",40f,Gravity.CENTER)
            }else{
                dialogResto?.dismiss()
                null
            }
        }

//        activity?.currentRoundRunDetail?.round?.diet?.let { dietDetail ->
//            mBinding.rvRoundProductsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
//            val roundRunProductAdapter =  RoundRunProductAdapter(
//                this@StepLoadFragment,
//                dietDetail,
//                defaultStep)
//            activity?.currentRoundRunDetail?.round?.diet?.products?.let { products ->
////                roundRunProductAdapter.productList(products)
//            }
//            mBinding.rvRoundProductsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))
//            mBinding.rvRoundProductsToLoad.adapter = roundRunProductAdapter
//
//            activity?.startAutomaticSave()
//            activity?.currentRoundRunDetail?.round?.diet?.products?.let { products ->
//                products.forEach{productDetail ->
//                    if(productDetail.finalWeight == 0.0){
//                        selectProduct(productDetail)
//                    }else{
//                        nextProduct()
//                    }
//                }
//            }
//        }
        activity?.refreshLogo()

        //Conectar bluetooth
        activity?.currentRoundRunDetail?.mixerBluetoothDevice?.let {
            activity?.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
        }
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        activity?.currentRoundRunDetail?.state = Constants.STATE_LOAD
    }

    fun exitFragment(){
        cleanAll()
        requireActivity().finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentStepLoadBinding.inflate(inflater, container, false)
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
//        if((mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter).isLastProduct()){
//            lastProduct()
//        }else{
//            noLastProduct()
//        }
        mBinding
        mBinding.tvCurrentProductWeightPending.text = "${sign}${Helper.getNumberWithDecimals (value, 0)}Kg  ${product.name}"
        val percentage = (product.currentWeight-product.initialWeight)*100/targetProductWeight
        mBinding.pbCurrentProduct.progress = percentage.toInt()

        if(currentProductDetail!=null && activity!=null){
            if(!mBinding.btnPause.isChecked && currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight >= currentProductDetail!!.targetWeight){
                if(countGreaterThanTarget>10){
                    countGreaterThanTarget = 0
                    noPrevAlert = false
//                    val adapterProduct = (mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter)
//                    val nextProductName = adapterProduct.getProduct(adapterProduct.selectedPosition + 1)?.name
//                    targetReachedDialog = if(nextProductName.isNullOrEmpty()){
//                        if(adapterProduct.isLastProduct()){
//                            activity?.dialogAlertTargetWeight("descarga")
//                        }else{
//                            activity?.dialogAlertTargetWeight()
//                        }
//                    } else{
//                        activity?.dialogAlertTargetWeight(nextProductName)
//                    }
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

        estableTimer= Timer(false)
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("RUN", "mixerWeight:$mixerWeight | lastUpdate: $lastUpdate |  Time:${lastUpdate?.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                tick ++
                if(tick == 3L){
                    MainScope().launch {
                        activity?.connectDevice()
                    }

                }
                if(currentProductDetail!=null && activity!=null){
                    if(currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight > currentProductDetail!!.targetWeight*0.9){
                        if(mBinding.tvCurrentProductWeightPending.currentTextColor==ContextCompat.getColor(activity!!, R.color.white)){
                            if(currentProductDetail!!.currentWeight-currentProductDetail!!.initialWeight > currentProductDetail!!.targetWeight){
                                mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!,
                                    R.color.color_yellow
                                ))
                            }else{
                                mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!,
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
                        activity?.changeStatusConnection(false)
                        Log.i(TAG,"changeStatusConnection(false) RRA 223")
                    } else {
                        Log.i("RUN", "Conexión: SI | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        if(isAdded){
                            Log.i("RUN", "Change status connection rt")
                            activity?.changeStatusConnection(true)
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
            Log.i(TAG, "[StepLoad] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[StepLoad] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[StepLoad] ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            (activity as RoundRunActivity).deviceConnected()
            Log.i(TAG, "[StepLoad] ACT onDeviceConnected")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
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
            if (currentMixerWeight != null){
                currentMixerWeight = (currentMixerWeight- mixerDetail.tara)*mixerDetail.calibration
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
//                    (mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter).updateInicial(mixerWeight)
                }
            }

            var actualWeight = 0.0
            activity?.currentRoundRunDetail?.round?.diet?.products?.forEach {
                actualWeight += it.currentWeight - it.initialWeight
            }

            if(contTara > 0){
                contTara--
                Log.i(TAG,"actualWeight: $actualWeight | mixerWeight: $mixerWeight | additional: ${currentProductDetail?.currentWeight}")
            }

            lastUpdate = LocalDateTime.now()
            var loaded = activity!!.getCurrentWeight()// + additional
            var sign = ""
            if(loaded <0 ){
                loaded *= -1
                sign = "+"
            }
            var signMixerTarget = ""
            var mixerTarget =  activity!!.getTargetWeight()
            if(mixerTarget<0){
                mixerTarget *= -1
                signMixerTarget = "+"
            }

            if(targetReachedDialog !=null && targetReachedDialog!!.isShowing){
                val textView = targetReachedDialog?.window?.findViewById<TextView>(R.id.tv_dialog_kg)
                textView?.text = mBinding.tvCurrentProductWeightPending.text
            }
            mBinding.tvMixerLoaded.text = "${sign}${Helper.getNumberWithDecimals(loaded,0)}Kg"
            mBinding.tvMixerTarget.text = "${signMixerTarget}${Helper.getNumberWithDecimals (mixerTarget, 0)}Kg"
            val percentage = (activity!!.getCurrentWeight())*100/activity!!.getTargetWeight()
            mBinding.pbCurrentMixer.progress = percentage.toInt()
//            (mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter).updateWeight(mixerWeight)
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i(TAG, "onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i(TAG, "onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            (activity as RoundRunActivity).deviceDisconnected()
            Log.i(TAG, "[StepLoad] ACT onError")
        }

        override fun onDeviceDisconnected() {
            (activity as RoundRunActivity).deviceDisconnected()
            Log.i(TAG, "[StepLoad]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[StepLoad]ACT onBondedDevices")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanAll()
        Log.i("RUN", "CANCELADO Simular bluetooth")
    }

    fun cleanAll(){
        timerTask?.cancel()
        rotationTimer?.cancel()
        activity!!.mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    fun nextProduct() {
        noPrevAlert = true
        mBinding.btnPause.isChecked = false
        countGreaterThanTarget = 0
        val adapteProduct =(mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter)
        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
//        if(adapteProduct.isLastProduct()){
//            adapteProduct.lastProductClose()
//        }else{
//            adapteProduct.nextProduct()
//        }
    }

    fun tare() {
        contTara = 5*35
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            Log.i(TAG, "[StepDownload] Tare - Initial: ${currentProductDetail?.initialWeight}    | MixerWeight $mixerWeight  | currentWeight ${currentProductDetail?.currentWeight}" )
            Log.i(TAG, "[StepDownload] Tare - currentProducDetail: $currentProductDetail" )

            activity?.currentRoundRunDetail?.round?.diet?.products?.forEach {
                if(it.id == currentProductDetail?.id){
                    it.currentWeight = mixerWeight
                    Log.i(TAG,"Tare $currentProductDetail initialWeight $mixerWeight")
                    it.initialWeight = mixerWeight
                    return@forEach
                }
            }
//            (mBinding.rvRoundProductsToLoad.adapter as RoundRunProductAdapter).tare(mixerWeight)
            delay(100)
            return@launch
        }
    }

    fun getCurrentProduct(): ProductDetail? {
        return previousProductDetail
    }


    fun lastProduct() {
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

}