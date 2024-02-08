package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentStepDownloadBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.MarginItemDecoration
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.roundToLong

class StepDownloadFragment(mixerDetail: MixerDetail) : Fragment() {

    private var tick: Long = 0L
    private var TAG : String = "DEBDown"
    private lateinit var mBinding: FragmentStepDownloadBinding
    private var activity: RoundRunActivity? = null
    private var lastMessage = ""
    private var lastUpdate: LocalDateTime? = null
    private var mixerWeight = 0.0
    private var totalMixerWeight: Double = 0.0
    private var estableTimer: Timer? = null
    private var timerTask: TimerTask? = null
    private var initialMixerWeight = 0.0
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentCorralDetail: CorralDetail? = null
    private var previousCorralDetail: CorralDetail? =null
    private var stepsDescription = arrayOf("Configuración","Carga", "Descarga", "Fin")
    private var dialogResto: AlertDialog? = null
    private var targetReachedDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onResume() {
        super.onResume()

        estableTimer= Timer(false)
        timerTask = object : TimerTask() {
            override fun run() {

                tick ++
                if(tick == 3L){
                    MainScope().launch {
                        activity?.connectDevice()
                    }
                }

                if(currentCorralDetail!=null && activity!=null){
                    if(currentCorralDetail!!.initialWeight - currentCorralDetail!!.currentWeight > currentCorralDetail!!.actualTargetWeight*0.9){
                        if(mBinding.tvCurrentCorralWeightPending.currentTextColor== ContextCompat.getColor(activity!!, R.color.white)){
                            if(currentCorralDetail!!.initialWeight - currentCorralDetail!!.currentWeight > currentCorralDetail!!.actualTargetWeight){
                                mBinding.tvCurrentCorralWeightPending.setTextColor(
                                    ContextCompat.getColor(activity!!,
                                        R.color.color_yellow
                                    ))
                            }else{
                                mBinding.tvCurrentCorralWeightPending.setTextColor(
                                    ContextCompat.getColor(activity!!,
                                        R.color.color_orange
                                    ))
                            }
                        }else{
                            mBinding.tvCurrentCorralWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                        }
                    }else{
                        mBinding.tvCurrentCorralWeightPending.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    }

                    if(currentCorralDetail!!.initialWeight - currentCorralDetail!!.currentWeight > currentCorralDetail!!.actualTargetWeight && noPrevAlert){
                        Log.i(TAG, "countGreaterThanTarget: $countGreaterThanTarget")
                        countGreaterThanTarget++
                    }
                }
                
                //Check connection
                if (lastUpdate != null){
                    if (lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 5){
                        Log.i("RUN", "Conexión: NO | Tiempo: ${lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS)}")
                        Log.i(TAG,"changeStatusConnection(false) STD 103")
                        activity?.changeStatusConnection(false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = (requireActivity() as RoundRunActivity)
        activity?.showCustomProgressDialog()
        mBinding.spRoundRunStep.setStateDescriptionData(stepsDescription)
        mBinding.spRoundRunStep.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)


        //Listeners de botones
        mBinding.btnTara.setOnClickListener{
            activity?.
            customDialog("Tara","Seguro quiere hacer tara?")
        }
        mBinding.btnJump.setOnClickListener{
//            if((mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter).isLastCorral()){
//                targetReachedDialog = activity?.dialogAlertTargetWeight("resumen")
//            }else{
//                nextCorral()
//            }

        }

        mBinding.btnRest.setOnClickListener{
            dialogResto = if(dialogResto==null){
                activity?.customDialog("Resto","${totalMixerWeight.roundToLong()}Kg",40F,Gravity.CENTER)
            }else{
                dialogResto?.dismiss()
                null
            }
        }


        mBinding.rvRoundCorralsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
        val roundRunCorralAdapter =  RoundRunCorralDownloadAdapter(
            this@StepDownloadFragment)
        activity?.currentRoundRunDetail?.round?.corrals?.let { corrals ->
//            roundRunCorralAdapter.corralList(corrals)
        }
        mBinding.rvRoundCorralsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))
        mBinding.rvRoundCorralsToLoad.adapter = roundRunCorralAdapter

        activity?.startAutomaticSave()
        activity?.currentRoundRunDetail?.round?.corrals.let { corrals ->
            corrals?.forEach{corralDetail ->
                if(corralDetail.finalWeight == 0.0){
                    selectCorral(corralDetail)
                }else{
                    nextCorral()
                }
            }
        }
        //Conectar bluetooth
        activity!!.currentRoundRunDetail?.mixerBluetoothDevice?.let {
            activity!!.mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
        }
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        activity?.currentRoundRunDetail?.state = Constants.STATE_DOWNLOAD
    }

    fun selectCorral(corral: CorralDetail){
        val targetCorralWeight =  corral.actualTargetWeight
        var sign = ""
        var value = targetCorralWeight - (corral.initialWeight - corral.currentWeight)
        if( value < 0){
            value *= -1
            sign = "+"
        }

//        if((mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter).isLastCorral()){
//            lastCorral()
//        }else{
//            noLastCorral()
//        }

        mBinding.tvCurrentCorralWeightPending.text = "${sign}${Helper.getNumberWithDecimals (value, 0)}Kg  ${corral.name}"
        var percentage = 0.0
        if (targetCorralWeight > 0) {
            percentage = (corral.initialWeight - corral.currentWeight)*100/targetCorralWeight
        }

        mBinding.pbCurrentCorral.progress = percentage.toInt()

        if(currentCorralDetail==null){
            Log.i(TAG, "Corral inicial ${corral.name} | ${corral.actualTargetWeight} + ${corral.currentWeight} + ${corral.initialWeight}")
            currentCorralDetail = corral
            previousCorralDetail = corral
        }
        currentCorralDetail = corral

        if(currentCorralDetail !=null && activity !=null ){
            if(!mBinding.btnPause.isChecked && currentCorralDetail!!.initialWeight - currentCorralDetail!!.currentWeight >= currentCorralDetail!!.actualTargetWeight){
                if(countGreaterThanTarget > 10){
                    Log.i(TAG,"DialgoAlertTargetWeight")
                    countGreaterThanTarget = 0
                    noPrevAlert = false
//                    val adapterCorral = (mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter)
//                    val nextCorralName = adapterCorral.getCorral(adapterCorral.selectedPosition + 1)?.name
//                    targetReachedDialog = if(nextCorralName.isNullOrEmpty()){
//                        if(adapterCorral.isLastCorral()){
//                            activity?.dialogAlertTargetWeight("resumen")
//                        }else{
//                            activity?.dialogAlertTargetWeight()
//                        }
//                    } else{
//                        activity?.dialogAlertTargetWeight(nextCorralName)
//                    }
                }
            }
        }
    }


    fun setCorral(corral: CorralDetail){
        previousCorralDetail = corral
        currentCorralDetail = corral
        selectCorral(corral)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentStepDownloadBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "[StepDownload] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[StepDownload] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[StepDownload] ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            deviceConnected()
            Log.i(TAG, "[StepDownload] ACT onDeviceConnected")
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

            //Se extrae valor de peso del mensaje resibido.
            var currentMixerWeight = message?.substring(1, 7)?.toDoubleOrNull()
            if (currentMixerWeight != null){
                currentMixerWeight = (currentMixerWeight- mixerDetail.tara)*mixerDetail.calibration
                totalMixerWeight = currentMixerWeight
                if(dialogResto!=null && dialogResto!!.isShowing){
                    dialogResto!!.setMessage("${totalMixerWeight.roundToLong()}Kg")
                }
                mixerWeight = currentMixerWeight - activity?.currentRoundRunDetail?.customTara!!
                if(previousCorralDetail !=null && currentCorralDetail !=null &&
                    previousCorralDetail!!.id != currentCorralDetail!!.id){
                    Log.i(TAG,"currentCorralDetail ${currentCorralDetail!!.name}")
                    previousCorralDetail = currentCorralDetail
                    currentCorralDetail?.initialWeight = mixerWeight
//                    (mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter).updateInicial(mixerWeight)
                }
            }else{
                //Si no se puede extraer un valor numerico del mensaje retorno
                return
            }

            //Se ingresa solo la primera vez si es que el valor del mixer es > 0
            if (initialMixerWeight == 0.0 && mixerWeight >= 0){//initialMixerWeight solo es 0 al inicio
                var actualProductsWeight = activity?.currentRoundRunDetail?.addedBlend ?: 0.0
                (activity as RoundRunActivity).currentRoundRunDetail?.round?.diet?.products?.forEach {
                    actualProductsWeight += (it.finalWeight - it.initialWeight)
                }
                initialMixerWeight = if(actualProductsWeight>0){
                    Log.i(TAG,"actualProductsWeight $actualProductsWeight sum of products")
                    actualProductsWeight
                }else{
                    Log.i(TAG,"actualProductsWeight $mixerWeight peso actual del mixer")
                    mixerWeight
                }
                Log.i(TAG, "Primer ingreso\nmixerWeight $mixerWeight | actualProductsWeight $actualProductsWeight | initialMixerWeight $initialMixerWeight ")
                activity?.initialMixer = initialMixerWeight

                activity?.let { roundRunActivity ->
                    Log.i(TAG, "Descarga actual: ${roundRunActivity.getCurrentDownload()}")
                    roundRunActivity.currentRoundRunDetail?.round?.corrals?.get(0)?.let {
                        it.initialWeight = actualProductsWeight
                        selectCorral(it)
                    }

                    // Si aún no se descargó nada actualizamos los kg que hay que descargar en
                    // los corrales segun los kg de mezcla cargados. No se tiene en cuenta si el mixer estaba cargado o si se hace Tara
                    // entre productos.
//                    (mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter).updateCorralTargetWeight()
                }

            }

            //Se actualizan las vistas
            val download = activity!!.getCurrentDownload()

            var signMixerTarget = ""
            var mixerTarget =  activity!!.getTargetDownload()
            if(mixerTarget<0){
                mixerTarget *= -1
                signMixerTarget = "+"
            }

            if(targetReachedDialog !=null && targetReachedDialog!!.isShowing){
                val textView = targetReachedDialog?.window?.findViewById<TextView>(R.id.tv_dialog_kg)
                textView?.text = mBinding.tvCurrentCorralWeightPending.text
            }
            mBinding.tvMixerDownloaded.text = "${Helper.getNumberWithDecimals(download,0)}Kg"
            mBinding.tvMixerTarget.text = "${signMixerTarget}${Helper.getNumberWithDecimals (mixerTarget, 0)}Kg"
            val percentage = (activity!!.getCurrentDownload())*100/activity!!.getTargetDownload()
            mBinding.pbCurrentMixer.progress = 100-percentage.toInt()
//            (mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter).updateCorralWeight(mixerWeight)

            //Se marca la fecha de actualizacion
            lastUpdate = LocalDateTime.now()
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i(TAG, "onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i(TAG, "onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            deviceDisconnected()
            Log.i(TAG, "[StepDownload] ACT onError")
        }

        override fun onDeviceDisconnected() {
            deviceDisconnected()
            Log.i(TAG, "[StepDownload]ACT onDeviceDisconnected")

        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[StepDownload]ACT onBondedDevices")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanAll()
    }

    fun cleanAll(){
        timerTask?.cancel()
        activity!!.mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    fun nextCorral() {
        val adapteCorral =(mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter)
        mBinding.btnPause.isChecked = false
        noPrevAlert = true
        countGreaterThanTarget = 0
        mBinding.tvCurrentCorralWeightPending.setTextColor(ContextCompat.getColor(activity!!,R.color.white)
        )
//        if(adapteCorral.isLastCorral()){
//            adapteCorral.lastCorralClose()
//        }else {
//            adapteCorral.selectNextCorral()
//        }
    }


    private fun deviceConnected() {
        (activity as RoundRunActivity).deviceConnected()
    }

    private fun deviceDisconnected() {
        (activity as RoundRunActivity).deviceDisconnected()
    }

    fun lastCorral() {
        mBinding.btnJump.text = "Fin"
    }

    private fun noLastCorral() {
        mBinding.btnJump.text = getString(R.string.salto)
    }

    fun exitFragment(){
        cleanAll()
        requireActivity().finish()
    }

    fun getCurrentCorral(): CorralDetail? {
        return previousCorralDetail
    }

    fun tare() {
//        (mBinding.rvRoundCorralsToLoad.adapter as RoundRunCorralDownloadAdapter).tare(mixerWeight)
    }

}