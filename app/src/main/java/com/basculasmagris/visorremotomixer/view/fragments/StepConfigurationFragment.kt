package com.basculasmagris.visorremotomixer.view.fragments

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentStepConfigurationBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.MixerData
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.roundToLong

class StepConfigurationFragment(mixerDetail: MixerDetail?) : Fragment() {

    private val TAG : String = "DEBConf"
    private lateinit var mBinding: FragmentStepConfigurationBinding
    private var mLocalMixers: MutableList<Mixer>? = null
    private var defaultStep = 5.0
    private var defaultWeightStep = 10.0
    private var activity: RoundRunActivity? = null
    var lastWeight = 0.0
    private var estableTimer: Timer? = null
    private var timerTask: TimerTask? = null
    private var lastUpdate: LocalDateTime? = null
    private var countPressWeight : Int = -1
    private var countPressPercentage : Int = -1


    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((requireActivity().application as SpiMixerApplication).mixerRepository)
    }
    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((requireActivity().application as SpiMixerApplication).roundRepository)
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mMixerViewModel.allMixerList) {
            if (it != null) {
                liveDataMerger.value = MixerData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(viewLifecycleOwner, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is MixerData -> mLocalMixers = it.mixers
                    else -> {}
                }
                if (mLocalMixers != null) {
                    liveData.removeObserver(this)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        estableTimer= Timer(false)
        timerTask = object : TimerTask() {
            override fun run() {
                if(countPressWeight>0){
                    countPressWeight--
                }else if(countPressWeight == 0){
                    MainScope().launch {
                        mBinding.rgStepWeight.visibility = View.INVISIBLE
                    }
                    countPressWeight--
                }

                if(countPressPercentage>0){
                    countPressPercentage--
                }else if(countPressPercentage == 0){
                    MainScope().launch {
                        mBinding.rgStep.visibility = View.INVISIBLE
                    }
                    countPressPercentage--
                }

                //Check connection
                if (lastUpdate != null){
                    if (lastUpdate!!.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 5){
                        (requireActivity() as RoundRunActivity).changeStatusConnection(false)
                    } else {
                        if(isAdded){
                            Log.i("RUN", "Change status connection rt")
                            (requireActivity() as RoundRunActivity).changeStatusConnection(true)
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

        getLocalData()
        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail

        Log.i("Tara", "Valores mixer: lastWeight:${lastWeight} | customTara:${currentRound?.customTara} | Mixer Tara: ${currentRound?.mixer?.tara}")
        currentRound?.customTara?.let { customTara ->
            if (customTara > -1){
                mBinding.tvRoundCustomWeight.text = "${Helper.getNumberWithDecimals(lastWeight-customTara,0)}Kg"
            } else {
                mBinding.tvRoundCustomWeight.text = "${(Helper.getNumberWithDecimals(lastWeight-currentRound.mixer?.tara!!, 0))}Kg"
            }
        }

        mBinding.ibAutoCustomTara.setOnClickListener {
            tare()
        }

        mBinding.btnReturn.setOnClickListener {
            exitFragment()
        }
        mBinding.btnToLoad.setOnClickListener {
            (requireActivity() as RoundRunActivity).changeStep(1)
        }

        mBinding.tvRoundCustomPercentage.text = "${currentRound?.customPercentage.toString()}%"

        mBinding.ibUpCustomPer.setOnLongClickListener {
            mBinding.rgStep.visibility = View.VISIBLE
            countPressPercentage = 10
            return@setOnLongClickListener true
        }

        mBinding.ibDownCustomPer.setOnLongClickListener {
            mBinding.rgStep.visibility = View.VISIBLE
            countPressPercentage = 10
            return@setOnLongClickListener true
        }

        mBinding.ibUpCustomPer.setOnClickListener {
            if(countPressPercentage<0){
                mBinding.rgStep.visibility = View.VISIBLE
                countPressPercentage = 10
                return@setOnClickListener
            }else{
                mBinding.rgStep.visibility = View.INVISIBLE
            }
            countPressPercentage = 10

            var currentCustomPercentage = 0.0
            currentRound?.customPercentage?.let {
                currentCustomPercentage = it
            }
            val newCustomPercentage = currentCustomPercentage+defaultStep

            Log.i("run", "UP currentCustomPercentage: $currentCustomPercentage | newCustomPercentage: $newCustomPercentage")
            currentRound?.customPercentage = newCustomPercentage
            mBinding.tvRoundCustomPercentage.text = "$newCustomPercentage%"
            changePercentage()
        }

        mBinding.ibDownCustomPer.setOnClickListener {
            if(countPressPercentage<0){
                mBinding.rgStep.visibility = View.VISIBLE
                countPressPercentage = 10
                return@setOnClickListener
            }else{
                mBinding.rgStep.visibility = View.INVISIBLE
            }
            countPressPercentage = 10

            var currentCustomPercentage = 0.0

            currentRound?.customPercentage?.let {
                currentCustomPercentage = it
            }
            val newCustomPercentage = if (currentCustomPercentage-defaultStep < 0){
                0.0
            } else {
                currentCustomPercentage-defaultStep
            }

            Log.i("run", "DOWN currentCustomPercentage: $currentCustomPercentage | newCustomPercentage: $newCustomPercentage")
            currentRound?.customPercentage = newCustomPercentage
            mBinding.tvRoundCustomPercentage.text = "$newCustomPercentage%"
            changePercentage()
        }

//      Ajuste de peso
        mBinding.tvRoundCustomPercentage.text = "${currentRound?.customPercentage.toString()}%"

        mBinding.ibUpCustomWeight.setOnLongClickListener {
            mBinding.rgStepWeight.visibility = View.VISIBLE
            countPressWeight = 10
            return@setOnLongClickListener true
        }
        mBinding.ibDownCustomWeight.setOnLongClickListener {
            mBinding.rgStepWeight.visibility = View.VISIBLE
            countPressWeight = 10
            return@setOnLongClickListener true
        }

        mBinding.ibUpCustomWeight.setOnClickListener {
            if(countPressWeight<0){
                mBinding.rgStepWeight.visibility = View.VISIBLE
                countPressWeight = 10
                return@setOnClickListener
            }else{
                mBinding.rgStepWeight.visibility = View.INVISIBLE
            }
            countPressWeight = 10
            val oldTargetWeight = currentRound?.round?.corrals?.sumOf {
                it.actualTargetWeight
            }
            val totalTargetWeight = oldTargetWeight?.plus(defaultWeightStep)
            Log.i(TAG,"oldTargetWeight $oldTargetWeight   defaultWeightStep $defaultWeightStep   totalTargetWeight $totalTargetWeight")
            changeTargetWeight(totalTargetWeight)
        }

        mBinding.ibDownCustomWeight.setOnClickListener {
            if(countPressWeight<0){
                mBinding.rgStepWeight.visibility = View.VISIBLE
                countPressWeight = 10
                return@setOnClickListener
            }else{
                mBinding.rgStepWeight.visibility = View.INVISIBLE
            }
            countPressWeight = 10
            val oldTargetWeight = currentRound?.round?.corrals?.sumOf {
                it.actualTargetWeight
            }
            val totalTargetWeight = oldTargetWeight?.minus(defaultWeightStep)
            Log.i(TAG,"oldTargetWeight $oldTargetWeight   defaultWeightStep $defaultWeightStep   totalTargetWeight $totalTargetWeight")
            changeTargetWeight(totalTargetWeight)
        }

        mBinding.rgStepWeight.check(R.id.rb_10_kg)
        mBinding.rgStepWeight.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                Log.i("run", "Checked: ${checkedRadioButton.text}" + checkedRadioButton.text)
                defaultWeightStep = checkedRadioButton.text.toString().toDouble()
            }
            mBinding.rgStepWeight.visibility = View.GONE
        }

        (requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.let { roundDetail ->
            mBinding.rvRoundCorralToLoad.layoutManager = LinearLayoutManager(requireActivity(),0,false)
            val roundRunCorralAdapter =  RoundRunCorralAdapter(
                this@StepConfigurationFragment)
            roundDetail.corrals.let { corrals ->
                roundRunCorralAdapter.corralList(corrals)
            }
            mBinding.rvRoundCorralToLoad.adapter = roundRunCorralAdapter
        }

        mBinding.rgStep.check(R.id.rb_5)
        mBinding.rgStep.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                Log.i("run", "Checked: ${checkedRadioButton.text}" + checkedRadioButton.text)
                defaultStep = checkedRadioButton.text.toString().toDouble()
            }
            mBinding.rgStep.visibility = View.GONE
        }

        mBinding.tvMixer.text = currentRound?.mixer?.name
        mBinding.tvEstablishment.text = currentRound?.round?.corrals?.first()?.establishment?.name

        refreshCustomRoundRunTargetWeight()
        getCustomRoundRunPercentage()
        changePercentage()

        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)

        activity?.currentRoundRunDetail?.state = Constants.STATE_CONFIG
    }

    private fun changePercentage(){
        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail
        //(requireActivity() as RoundRunActivity).supportActionBar?.title = (requireActivity() as RoundRunActivity).currentRound?.round?.name
        var customTargetRoundWeight = 0.0
        var actualTargetRoundWeight = 0.0
        var currentPercentage = 0.0
        currentRound?.customPercentage?.let {
            currentPercentage = it
        }
        Log.i("run", "Nuevo porcentaje: $currentPercentage")
        currentRound?.round?.corrals?.forEachIndexed { index, corralDetail ->
            val newValue = (corralDetail.weight*currentPercentage/100)
            currentRound.round.corrals[index].customTargetWeight = newValue
            currentRound.round.corrals[index].actualTargetWeight = newValue
            customTargetRoundWeight += currentRound.round.corrals[index].customTargetWeight
            actualTargetRoundWeight += currentRound.round.corrals[index].actualTargetWeight
        }

        currentRound?.round?.corrals?.let {
            (mBinding.rvRoundCorralToLoad.adapter as RoundRunCorralAdapter).corralList(it)
        }

        mBinding.tvRoundTargetWeight.text = "${Helper.getNumberWithDecimals(actualTargetRoundWeight,0)}Kg"
        (requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.customRoundRunWeight = actualTargetRoundWeight
    }

    private fun changeTargetWeight(totalTargetWeight : Double?){
        if(totalTargetWeight==null){
            return
        }
        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail
        var newTotalTargetWeight = 0.0
        currentRound?.round?.corrals?.forEachIndexed {index,corralDetail->
            Log.i(TAG,"Corral ${corralDetail.name}  percentage ${corralDetail.percentage}  targetWeight ${corralDetail.actualTargetWeight}")
            currentRound.round.corrals[index].actualTargetWeight = (totalTargetWeight * (corralDetail.percentage/100)).roundToLong().toDouble()
            currentRound.round.corrals[index].customTargetWeight = currentRound.round.corrals[index].actualTargetWeight
            currentRound.round.corrals[index].weight = currentRound.round.corrals[index].actualTargetWeight
            newTotalTargetWeight += corralDetail.actualTargetWeight
            if(index == currentRound.round.corrals.size-1){
                currentRound.round.corrals[index].actualTargetWeight += (totalTargetWeight - newTotalTargetWeight)
                currentRound.round.corrals[index].customTargetWeight = currentRound.round.corrals[index].actualTargetWeight
                currentRound.round.corrals[index].weight = currentRound.round.corrals[index].actualTargetWeight
            }
        }

        changeProductTargetWeight(totalTargetWeight)
        currentRound?.round?.corrals?.let {
            (mBinding.rvRoundCorralToLoad.adapter as RoundRunCorralAdapter).corralList(it)
        }
        mBinding.tvRoundTargetWeight.text ="${Helper.getNumberWithDecimals(totalTargetWeight,0)}Kg"
    }

    private fun changeProductTargetWeight(totalTargetWeight : Double?) {
        if(totalTargetWeight==null){
            return
        }
        var greatherWeight = 0.0
        var greatherWeightIndex = 0
        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail
        var newTotalTargetWeight = 0.0
        currentRound?.round?.diet?.products?.forEachIndexed {index,productDetail->
            Log.i(TAG,"Producto ${productDetail.name}  targetWeight ${productDetail.targetWeight}")
            currentRound.round.diet.products[index].targetWeight = (totalTargetWeight * (productDetail.percentage/100)).roundToLong().toDouble()
            if(productDetail.targetWeight > greatherWeight){
                greatherWeight  = productDetail.targetWeight
                greatherWeightIndex = index
            }
            newTotalTargetWeight += productDetail.targetWeight
        }

        if((totalTargetWeight - newTotalTargetWeight).roundToLong() != 0L){
            currentRound?.round?.diet?.products?.get(greatherWeightIndex)?.targetWeight =
                currentRound?.round?.diet?.products?.get(greatherWeightIndex)?.targetWeight?.plus(
                    (totalTargetWeight - newTotalTargetWeight)
                )!!
        }

    }

    fun refreshCustomRoundRunTargetWeight(){
        var targetCustomRoundRunWeight = 0.0
        (requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.corrals?.forEach {
            targetCustomRoundRunWeight += it.actualTargetWeight
        }
        (requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.customRoundRunWeight = targetCustomRoundRunWeight
        mBinding.tvRoundTargetWeight.text = "${Helper.getNumberWithDecimals(targetCustomRoundRunWeight,0)}Kg"
        changeProductTargetWeight(targetCustomRoundRunWeight)
    }

    private fun getCustomRoundRunPercentage() : Double {
        var targetCustomRoundRunPercentage = 0.0
        (requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.let {
            targetCustomRoundRunPercentage = it.customRoundRunPercentage
        }
        return targetCustomRoundRunPercentage
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentStepConfigurationBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "[StepConfigurationFragment] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[StepConfigurationFragment] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[StepConfigurationFragment] ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            (activity as RoundRunActivity).deviceConnected()
            Log.i(TAG, "[StepConfigurationFragment] ACT onDeviceConnected")
        }
        override fun onCommandReceived(device: BluetoothDevice?, command: ByteArray?) {
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            lastUpdate = LocalDateTime.now()

            message?.let{
                if (it.length > 3){
                    activity?.hideCustomProgressDialog()
                    val currentMixerWeight = it.substring(1, 7).toDoubleOrNull()
                    currentMixerWeight?.let { value ->
                        val weight = (value - mixerDetail?.tara!!)*mixerDetail.calibration
                        lastWeight = weight
                        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail
                        currentRound?.customTara?.let{customTara->
                            mBinding.tvRoundCustomWeight.text = "${Helper.getNumberWithDecimals(lastWeight-customTara,0)}Kg"
                        }?:run{
                            mBinding.tvRoundCustomWeight.text = "${Helper.getNumberWithDecimals(lastWeight,0)}Kg"
                        }

                        (requireActivity() as RoundRunActivity).currentRoundRunDetail?.customTara?.let { customTara ->
                            mBinding.tvRoundCustomWeight.text = "${Helper.getNumberWithDecimals(lastWeight-customTara, 0)}Kg"
                        }?:run{
                            mBinding.tvRoundCustomWeight.text = "${Helper.getNumberWithDecimals(lastWeight,0)}Kg"
                        }
                    }
                }
            }
        }

        override fun onMessageSent(device: BluetoothDevice?) {
            Log.i(TAG, "[StepConfigurationFragment] ACT onMessageSent")
        }

        override fun onError(message: String?) {
            (activity as RoundRunActivity).deviceDisconnected()
            Log.i(TAG, "[StepConfigurationFragment] ACT onError")
        }

        override fun onDeviceDisconnected() {
            (activity as RoundRunActivity).deviceDisconnected()
            Log.i(TAG, "[StepConfigurationFragment]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[StepConfigurationFragment]ACT onBondedDevices")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanAll()
    }

    private fun cleanAll(){
        timerTask?.cancel()
        activity!!.mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    fun exitFragment(){
        var totalWeightLoad = 0.0
        activity?.currentRoundRunDetail?.round?.diet?.products?.forEach { productDetail ->
            totalWeightLoad += productDetail.currentWeight - productDetail.initialWeight
        }
        Log.i(TAG,"exitFragment totalWeightLoad $totalWeightLoad")
        if(totalWeightLoad<1 && !activity?.currentRoundRunDetail?.startDate.isNullOrEmpty() && activity?.currentRoundRunDetail?.endDate.isNullOrEmpty()){
            Log.i(TAG,"startDate before ${activity?.currentRoundRunDetail?.startDate}")
            activity?.currentRoundRunDetail?.id?.let {
                Log.i(TAG,"delete RoundRunById(${activity?.currentRoundRunDetail?.id})")
                mRoundViewModel.deleteRoundRunById(it)
            }
            Log.i(TAG,"exitFragment without saveRoundLoadStatus")
            cleanAll()
            requireActivity().finish()
            return
        }
        Log.i(TAG,"exitFragment with saveRoundLoadStatus")
        activity?.currentRoundRunDetail?.state = Constants.STATE_CONFIG
        activity?.saveRoundLoadStatus()
        cleanAll()
        requireActivity().finish()
    }

    fun tare() {
        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail
        mBinding.tvRoundCustomWeight.text = "0Kg"
        currentRound?.customTara = lastWeight
        Log.i("Tara", "Valores mixer: lastWeight:${lastWeight} | customTara:${currentRound?.customTara} | Mixer Tara: ${currentRound?.mixer?.tara}")
    }

    fun addInitialWeight() {
        val currentRound  = (requireActivity() as RoundRunActivity).currentRoundRunDetail
        currentRound?.addedBlend = lastWeight
        Log.i("Tara", "Valores mixer: lastWeight:${lastWeight} | addedBlend:${currentRound?.addedBlend}")
    }

}