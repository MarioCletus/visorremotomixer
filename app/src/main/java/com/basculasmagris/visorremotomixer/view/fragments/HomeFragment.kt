package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.getCurrentUser
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.setProgressDialog
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : BottomSheetDialogFragment() {
    private val TAG : String = "DEBHome"
    // Bluetooth
    private lateinit var mCustomListDialog: Dialog
//    private var selectedMixerInFragment: CustomListItem? = null
    private var selectedMixerInFragment: Mixer? = null
    private var knowDevices: List<BluetoothDevice>? = null
    var myMenu: Menu? = null
    private var dialog: AlertDialog? = null
    private var activity : MainActivity? = null

    private lateinit var mBinding: FragmentHomeBinding
    private var liveData: MediatorLiveData<MergedLocalData>? = null
    // -------------------
    // Establishment
    // -------------------
    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((requireActivity().application as SpiMixerApplication).establishmentRepository)
    }
    private var mLocalEstablishments: MutableList<Establishment>? = null

    // -------------------
    // Corral
    // -------------------
    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as SpiMixerApplication).corralRepository)
    }
    private var mLocalCorrals: MutableList<Corral>? = null

    // -------------------
    // ProductscorralViewModelRemoteObserver
    // -------------------
    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as SpiMixerApplication).productRepository)
    }
    private var mLocalProducts: MutableList<Product>? = null

    // -------------------
    // Mixer
    // -------------------
    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((requireActivity().application as SpiMixerApplication).mixerRepository)
    }
    private var mLocalMixers: MutableList<Mixer>? = null
    private val mLocalMixersCustomList: java.util.ArrayList<CustomListItem> =
        java.util.ArrayList<CustomListItem>()

    // -------------------
    // Diet
    // -------------------
    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((requireActivity().application as SpiMixerApplication).dietRepository)
    }
    private var mLocalDiets: MutableList<Diet>? = null
    private var mLocalDietProducts: MutableList<DietProduct>? = null

    // -------------------
    // Round
    // -------------------
    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((requireActivity().application as SpiMixerApplication).roundRepository)
    }
    private var mLocalRounds: MutableList<Round>? = null
    private var mLocalRoundCorrals: MutableList<RoundCorral>? = null
    private var mLocalDetailRound: MutableList<RoundRunDetail> = ArrayList()
    private var mLocalDetailRoundRunReport: MutableList<RoundRunDetail> = ArrayList()
    private var mLocalDetailRoundReport: MutableList<RoundDetail> = ArrayList()
    private var mLocalRoundRun: MutableList<RoundRun>? = null
    private var mLocalRoundRunToReport: MutableList<RoundRun>? = ArrayList()
    private var mLocalRoundRunProgressLoad: MutableList<RoundRunProgressLoad>? = null
    private var mLocalRoundRunProgressDownload: MutableList<RoundRunProgressDownload>? = null

    // All datsa
    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mEstablishmentViewModel.allEstablishmentList) {
            if (it != null) {
                liveDataMerger.value = EstablishmentData(it)
            }
        }
        liveDataMerger.addSource(mCorralViewModel.allCorralList) {
            if (it != null) {
                liveDataMerger.value = CorralData(it)
            }
        }
        liveDataMerger.addSource(mProductViewModel.allProductList) {
            if (it != null) {
                liveDataMerger.value = ProductData(it)
            }
        }
        liveDataMerger.addSource(mMixerViewModel.allMixerList) {
            if (it != null) {
                liveDataMerger.value = MixerData(it)
            }
        }
        liveDataMerger.addSource(mDietViewModel.allDietList) {
            if (it != null) {
                liveDataMerger.value = DietData(it)
            }
        }
        liveDataMerger.addSource(mDietViewModel.allDietProductList) {
            if (it != null) {
                liveDataMerger.value = DietProductData(it)
            }
        }

        liveDataMerger.addSource(mRoundViewModel.allRoundList) {
            if (it != null) {
                liveDataMerger.value = RoundData(it)
            }
        }

        liveDataMerger.addSource(mRoundViewModel.allRoundCorralList) {
            if (it != null) {
                liveDataMerger.value = RoundCorralData(it)
            }
        }

        liveDataMerger.addSource(mRoundViewModel.allRoundRunList) {
            if (it != null) {
                liveDataMerger.value = RoundRunData(it)
            }
        }

        liveDataMerger.addSource(mRoundViewModel.allRoundRunProgressLoadList) {
            if (it != null) {
                liveDataMerger.value = RoundRunProgressLoadData(it)
            }
        }

        liveDataMerger.addSource(mRoundViewModel.allRoundRunProgressDownloadList) {
            if (it != null) {
                liveDataMerger.value = RoundRunProgressDownloadData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(){
        // Sync local data
        Log.i("run", "getLocalData ${liveData?.value}")
        liveData = fetchLocalData()
        liveData?.observe(requireActivity(),  object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                Log.i("home", "it: ${it.toString()}")
                when (it) {
                    is EstablishmentData -> mLocalEstablishments = it.establishments
                    is CorralData -> mLocalCorrals = it.corrals
                    is ProductData -> mLocalProducts = it.products
                    is MixerData -> {mLocalMixers = it.mixers
                        if(mLocalMixers!=null && mLocalMixers!!.isEmpty()){
                            Log.i(TAG,"mLocalMixers: "+ mLocalMixers!!.size + "  "+ mLocalMixers)
                        }
                    }
                    is DietData -> mLocalDiets = it.diets
                    is DietProductData -> mLocalDietProducts = it.dietProducts
                    is RoundData -> mLocalRounds = it.rounds
                    is RoundCorralData -> mLocalRoundCorrals = it.roundCorrals
                    is RoundRunData -> mLocalRoundRun = it.roundRun
                    is RoundRunProgressLoadData -> mLocalRoundRunProgressLoad = it.roundRunProgressLoad
                    is RoundRunProgressDownloadData -> mLocalRoundRunProgressDownload = it.roundRunProgressDownload
                    else -> {}
                }

                if (mLocalEstablishments != null && mLocalCorrals != null &&
                    mLocalProducts != null && mLocalMixers != null && mLocalDiets != null &&
                    mLocalDietProducts != null && mLocalRounds != null && mLocalRoundCorrals != null &&
                    mLocalRoundRun != null && mLocalRoundRunProgressLoad != null && mLocalRoundRunProgressDownload != null) {
                    mLocalDetailRound.clear()
                    mLocalDetailRoundReport.clear()
                    mLocalDetailRoundRunReport.clear()
                    mLocalRoundRunToReport?.clear()
                    mLocalRoundRun?.let { list ->
                        mLocalRoundRunToReport = list.map { it.copy() }.filter { data -> data.endDate.isNotEmpty() }.toMutableList()
                    }

                    mLocalRoundRun?.forEach {
                        Log.i("DATA USER", "DATA USER TEST: ${it.userDisplayName}")
                    }
                    mLocalRoundRunToReport?.forEach {
                        Log.i("DATA USER", "mLocalRoundRunToReport DATA USER TEST: ${it.userDisplayName}")
                    }

                    Log.i("mLocalRoundRunProgressDownload", "----> ////////////////////////")
                    mLocalRoundRunProgressDownload?.forEach {  rrPD ->
                        Log.i("mLocalRoundRunProgressDownload", "ID: ${rrPD.roundRunId} | ${rrPD.corralId} | Actual: ${rrPD.currentWeight} | Real: ${rrPD.actualTargetWeight} | Plani: ${rrPD.customTargetWeight}")
                    }
                    Log.i("mLocalRoundRunProgressDownload", "----> ////////////////////////")

                    if (mLocalRounds?.size!! > 0){
                        mBinding.llRounds.visibility = VISIBLE
                        mBinding.tvNoData.visibility = GONE
                        setAdapters()
                    } else {
                        mBinding.llRounds.visibility = GONE
                        mBinding.tvNoData.visibility = VISIBLE
                    }

                    (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
                    dialog?.dismiss()
                    liveData?.removeObserver(this)
                    liveData = null
                }
            }
        })
    }

    fun setAdapters(){
        mLocalRounds?.forEach { round ->
            // Corrals
            val roundCorralsDetail: ArrayList<CorralDetail> = ArrayList()
            val roundCorrals = mLocalRoundCorrals?.filter { roundCorral ->
                roundCorral.roundId == round.id
            }
            roundCorrals?.forEach {  roundCorral ->

                val corral = mLocalCorrals?.firstOrNull { corral ->
                    corral.id == roundCorral.corralId
                }

                Log.i("Corral", "FER: ${corral?.name}")

                val establishment = mLocalEstablishments?.firstOrNull { establishment ->
                    establishment.id == corral?.establishmentId
                }

                establishment?.let { _establishment ->
                    val establishmentDetail = EstablishmentDetail(
                        _establishment.name,
                        _establishment.description,
                        _establishment.remoteId,
                        _establishment.updatedDate,
                        _establishment.archiveDate,
                        _establishment.id
                    )

                    corral?.let {  corral ->
                        val corralDetail = CorralDetail(
                            establishmentDetail,
                            corral.name,
                            corral.description,
                            corral.remoteId,
                            corral.updatedDate,
                            corral.archiveDate,
                            corral.animalQuantity,
                            corral.rfid,
                            roundCorral.order,
                            roundCorral.weight,
                            roundCorral.weight,
                            roundCorral.weight,
                            0.0,
                            0.0,
                            0.0,
                            roundCorral.percentage,
                            corral.id
                        )
                        Log.i("SETADAPTER", "corralDetail: ${corralDetail.name}")
                        roundCorralsDetail.add(corralDetail)
                    }
                }
            }

            // Products
            val roundDietProducts: ArrayList<ProductDetail> = ArrayList()
            val dietProducts = mLocalDietProducts?.filter { dietProduct ->
                dietProduct.dietId == round.dietId
            }
            dietProducts?.forEach {  dietProduct ->

                val products = mLocalProducts?.firstOrNull { product ->
                    product.id == dietProduct.productId
                }

                products?.let {  product ->
                    val productDetail = ProductDetail(
                        product.name,
                        product.description,
                        product.specificWeight,
                        product.rfid,
                        product.image,
                        product.imageSource,
                        product.remoteId,
                        product.updatedDate,
                        product.archiveDate,
                        dietProduct.order,
                        0.0,
                        0.0,
                        0.0,
                        dietProduct.weight,
                        dietProduct.percentage,
                        product.id
                    )
                    roundDietProducts.add(productDetail)
                }

            }

            // Diet
            val diet = mLocalDiets?.firstOrNull { diet ->
                diet.id == round.dietId
            }

            diet?.let { _diet ->
                val roundDietDetail = DietDetail(
                    _diet.name,
                    _diet.description,
                    _diet.remoteId,
                    _diet.updatedDate,
                    _diet.archiveDate,
                    _diet.usePercentage,
                    roundDietProducts,
                    _diet.id
                )

                val roundDetail = RoundDetail(
                    round.name,
                    round.description,
                    round.remoteId,
                    round.updatedDate,
                    round.archiveDate,
                    round.weight,
                    round.weight,
                    round.usePercentage,
                    round.customPercentage,
                    round.customPercentage,
                    roundDietDetail,
                    roundCorralsDetail,
                    round.id
                )

                Log.i("SETADAPTER", "mLocalRoundRun -----------------------------------------")
                mLocalRoundRun?.forEach {
                    Log.i("SETADAPTER", "RoundRun Name: ${it.id} Start: ${it.startDate}, End: ${it.endDate}")
                }

                var lastExecution: RoundRun? = null

                val finishedExecution = mLocalRoundRun?.sortedWith (
                    compareBy { it.endDate })?.lastOrNull { roundRun ->
                    roundRun.roundId == roundDetail.id && roundRun.endDate.isNotEmpty()
                }

                if (finishedExecution != null){
                    lastExecution = finishedExecution
                }

                val inProgressExecution = mLocalRoundRun?.sortedBy {
                    it.startDate
                }?.lastOrNull { roundRun ->
                    roundRun.roundId == roundDetail.id && roundRun.endDate.isEmpty()
                }

                if (inProgressExecution != null){
                    lastExecution = inProgressExecution
                }

                Log.i("SETADAPTER", "inProgressExecution: $inProgressExecution")
                Log.i("SETADAPTER", "finishedExecution: $finishedExecution")
                val selectedMixer = getSelectedMixer()
                val currentUser = getCurrentUser(requireActivity())

                var mixerDetailInRound : MixerDetail? = null
                var bluetothDeviceInRound : BluetoothDevice? = null
                val roundMixer = mLocalMixers?.firstOrNull{mixer ->
                    mixer.id == lastExecution?.mixerId
                }
                roundMixer?.let { mixer ->
                    mixerDetailInRound = MixerDetail(
                        mixer.name,
                        mixer.description,
                        mixer.mac,
                        mixer.btBox,
                        mixer.tara,
                        mixer.calibration,
                        mixer.rfid,
                        mixer.remoteId,
                        mixer.updatedDate,
                        mixer.archiveDate,
                        mixer.id
                    )

                    bluetothDeviceInRound = knowDevices?.firstOrNull{
                        mixerDetailInRound?.mac == it.address
                    }

                }

                val roundRunDetail = RoundRunDetail(
                    currentUser.id,
                    currentUser.displayName,
                    roundDetail,
                    mixerDetailInRound,//Originalmente en null
                    bluetothDeviceInRound,//Originalmente en null
                    lastExecution?.startDate ?: "",
                    lastExecution?.updatedDate ?: "",
                    lastExecution?.endDate ?: "",
                    lastExecution?.remoteId ?: 0L,
                    lastExecution?.customPercentage?: round.customPercentage,
                    lastExecution?.customTara ?: lastExecution?.customTara ?: 0.0,
                    lastExecution?.addedBlend ?: lastExecution?.addedBlend ?: 0.0,
                    lastExecution?.state ?: Constants.STATE_NONE,
                    lastExecution?.id ?: 0L,
                )


                //Complete product status
                roundRunDetail.round.diet.products.forEachIndexed { index, productDetail ->
                    Log.i("SETADAPTER", "producto: ${productDetail.name}")
                    val currentProductStatus = mLocalRoundRunProgressLoad?.firstOrNull { roundRunProgressLoad ->
                        roundRunProgressLoad.productId == productDetail.id && roundRunProgressLoad.roundRunId == roundRunDetail.id
                    }

                    if (currentProductStatus != null){
                        roundRunDetail.round.diet.products[index].initialWeight = currentProductStatus.initialWeight
                        roundRunDetail.round.diet.products[index].currentWeight = currentProductStatus.currentWeight
                        roundRunDetail.round.diet.products[index].finalWeight = currentProductStatus.finalWeight
                        roundRunDetail.round.diet.products[index].targetWeight = currentProductStatus.targetWeight
                    }

                }

                var customRoundRunWeight = 0.0
                roundRunDetail.round.corrals.forEachIndexed{ index, corralDetail ->
                    val currentCorralStatus = mLocalRoundRunProgressDownload?.firstOrNull { roundRunProgressDownload ->
                        roundRunProgressDownload.corralId == corralDetail.id && roundRunProgressDownload.roundRunId == roundRunDetail.id
                    }
                    Log.i("SETADAPTER", "currentCorralStatus actualWeight: ${currentCorralStatus?.currentWeight} | customTargetWeight: ${currentCorralStatus?.customTargetWeight}")
                    if (currentCorralStatus != null){
                        roundRunDetail.round.corrals[index].initialWeight = currentCorralStatus.initialWeight
                        roundRunDetail.round.corrals[index].currentWeight = currentCorralStatus.currentWeight
                        roundRunDetail.round.corrals[index].finalWeight = currentCorralStatus.finalWeight
                        roundRunDetail.round.corrals[index].customTargetWeight = currentCorralStatus.customTargetWeight
                        roundRunDetail.round.corrals[index].actualTargetWeight = currentCorralStatus.actualTargetWeight
                        customRoundRunWeight += currentCorralStatus.customTargetWeight

                    }
                }

                //Complete general round status
                roundRunDetail.round.customRoundRunWeight = customRoundRunWeight
                lastExecution?.customPercentage?.let {
                    roundRunDetail.round.customRoundRunPercentage = it
                }

                // Se actualiza con los datos
                Log.i("SETADAPTER", "roundRunDetail.round.corrals--------------| ${roundRunDetail.round.corrals.size} | Round detail: $roundDetail | RoundRundetail $roundRunDetail")
                roundRunDetail.round.corrals.forEach {
                    Log.i("SETADAPTER", "CorralDetail current: ${it.currentWeight} | actual: ${it.actualTargetWeight}")
                }
                mLocalDetailRound.add(roundRunDetail)
                mLocalDetailRoundReport.add(roundRunDetail.round.copy())
            }
        }
        Log.i("SETADAPTER", "mLocalDetailRound: ${mLocalDetailRound.size} | Report Size: ${mLocalDetailRoundReport.size}")

        val lastRounds = mLocalDetailRound.filter {  roundRunDetail ->
            roundRunDetail.id > 0L
        }.sortedWith (
            compareBy(
                {it.endDate},
                {LocalDateTime.parse(it.startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
            ))

        val otherRounds = mLocalDetailRound.filter {  roundRunDetail ->
            roundRunDetail.id == 0L
        }.sortedBy {
            it.round.id
        }

        mBinding.rvLastRounds.layoutManager = GridLayoutManager(requireActivity(), 3)
        val lastRoundRunAdapter =  RoundRunAdapter(this@HomeFragment)
        lastRoundRunAdapter.roundList((lastRounds+otherRounds).sortedByDescending { it.startDate })
        mBinding.rvLastRounds.adapter = lastRoundRunAdapter
        setDataForReport()

    }

    private fun setDataForReport() {
        // REPORT

        mLocalDetailRoundRunReport.clear()

        Log.i("mLocalRoundRunToReport.size", "mLocalRoundRunToReport.size ${mLocalRoundRunToReport?.size}")
        mLocalRoundRunToReport?.forEach { roundRun ->
            var copyData : MutableList<RoundDetail> = ArrayList()
            var copyDataCorral : MutableList<CorralDetail> = ArrayList()
            var copyDataProducts : MutableList<ProductDetail> = ArrayList()
//            var copyDiet : DietDetail? = null
            mLocalDetailRoundReport.let { list ->
                copyData = list.map { it.copy() }.toMutableList()
            }

            val rDetail = copyData.firstOrNull { rd ->
                rd.id == roundRun.roundId
            }

            rDetail?.corrals?.let { list ->
                copyDataCorral = list.map { it.copy() }.toMutableList()
            }
            rDetail?.corrals = copyDataCorral

            rDetail?.diet = rDetail?.diet?.copy()!!
            rDetail.diet.products.let { list ->
                copyDataProducts = list.map { it.copy() }.toMutableList()
            }
            rDetail.diet.products = copyDataProducts

            // Seteamos valores del corral
            rDetail.corrals.forEachIndexed { index, corralDetail ->
                val currentCorralStatus =
                    mLocalRoundRunProgressDownload?.firstOrNull { roundRunProgressDownload ->
                        roundRunProgressDownload.corralId == corralDetail.id && roundRunProgressDownload.roundRunId == roundRun.id
                    }
                if (currentCorralStatus != null) {
                    rDetail.corrals[index].initialWeight = currentCorralStatus.initialWeight
                    rDetail.corrals[index].currentWeight = currentCorralStatus.currentWeight
                    rDetail.corrals[index].finalWeight = currentCorralStatus.finalWeight
                    rDetail.corrals[index].customTargetWeight = currentCorralStatus.customTargetWeight
                    rDetail.corrals[index].actualTargetWeight = currentCorralStatus.actualTargetWeight
                }
            }
            rDetail.corrals.forEach { corralDetail ->
                Log.i(
                    "currentWeight updated:",
                    "ID: ${roundRun.id} | ${corralDetail.id} | currentWeight: ${corralDetail.currentWeight} | real: ${corralDetail.actualTargetWeight} | plani: ${corralDetail.customTargetWeight}"
                )
            }

            // Seteamos valores del producto
            rDetail.diet.products.forEachIndexed { index, productDetail ->
                val currentProductStatus =
                    mLocalRoundRunProgressLoad?.firstOrNull { roundRunProgressLoad ->
                        roundRunProgressLoad.productId == productDetail.id && roundRunProgressLoad.roundRunId == roundRun.id
                    }
                if (currentProductStatus != null) {
                    rDetail.diet.products[index].initialWeight = currentProductStatus.initialWeight
                    rDetail.diet.products[index].currentWeight = currentProductStatus.currentWeight
                    rDetail.diet.products[index].finalWeight = currentProductStatus.finalWeight
                    rDetail.diet.products[index].targetWeight = currentProductStatus.targetWeight
                }
            }


            var mixerDetailInRound : MixerDetail? = null
            var bluetothDeviceInRound : BluetoothDevice? = null
            val roundMixer = mLocalMixers?.firstOrNull{mixer ->
                mixer.id == roundRun.mixerId
            }
            roundMixer?.let { mixer->
                mixerDetailInRound = MixerDetail(
                    mixer.name,
                    mixer.description,
                    mixer.mac,
                    mixer.btBox,
                    mixer.tara,
                    mixer.calibration,
                    mixer.rfid,
                    mixer.remoteId,
                    mixer.updatedDate,
                    mixer.archiveDate,
                    mixer.id
                )

                bluetothDeviceInRound = knowDevices?.firstOrNull{
                    mixerDetailInRound?.mac == it.address
                }

            }

            val roundRunDetail = RoundRunDetail(
                roundRun.remoteUserId,
                roundRun.userDisplayName,
                rDetail,
                mixerDetailInRound,//Originalmente en null
                bluetothDeviceInRound,//Originalmente en null
                roundRun.startDate,
                roundRun.updatedDate,
                roundRun.endDate,
                roundRun.remoteId,
                roundRun.customPercentage,
                roundRun.customTara,
                roundRun.addedBlend,
                roundRun.state,
                roundRun.id,
            )
            mLocalDetailRoundRunReport.add(roundRunDetail)
        }

        val reportsRounds = mLocalDetailRoundRunReport.sortedWith (
            compareBy(
                {it.endDate},
                {LocalDateTime.parse(it.startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
            ))

        (requireActivity() as MainActivity).mLocalDetailRound = reportsRounds.toMutableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.i("home", "---->onCreateView")
        if (dialog == null){
            dialog = setProgressDialog(requireActivity(), "Buscando rondas...")
        }
        dialog?.show()
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

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                myMenu = menu
                menuInflater.inflate(R.menu.menu_home, menu)
                val changeMixer = menu.findItem(R.id.action_change_mixer)
                val selectedMixer = getSelectedMixer()
                if (selectedMixer != null){
                    changeMixer.title = "   "+ selectedMixer.name
                } else {
                    changeMixer.title = "   "+ "Elegir Mixer"
                }

                val search = menu.findItem(R.id.search_round)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvLastRounds.adapter as RoundRunAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvLastRounds.adapter as RoundRunAdapter).filter.filter(newText)
                        return true
                    }
                })

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_change_mixer -> {
                        if (mLocalMixersCustomList.size > 0){
                            customItemsLDialog("Mixer", mLocalMixersCustomList,Constants.MIXER_REF)
                        } else {
                            alertMixer("No hay ningún mixer vinculado en el dispositvo.")
                        }
                        return true
                    }
                    R.id.action_add_round -> {
                        goToAddUpdateRound()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("run", "onViewCreated")
        mRoundViewModel.allRoundRunProgressDownloadList.observe(this){
            Log.i("run", "OBSERVE allRoundRunProgressDownloadList")
            activity = (requireActivity() as MainActivity)
            activity?.mLocalDetailRound?.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalRoundRunProgressDownload = it
            setAdapters()
        }

        mRoundViewModel.allRoundRunProgressLoadList.observe(this){
            Log.i("run", "OBSERVE allRoundRunProgressDownloadList")
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalRoundRunProgressLoad = it
            setAdapters()
        }

        mRoundViewModel.allRoundRunList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalRoundRun = it
            setAdapters()
        }

        mMixerViewModel.allMixerList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalMixers = it
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
            setAdapters()
        }

        mEstablishmentViewModel.allEstablishmentList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalEstablishments = it
            setAdapters()
        }

        mCorralViewModel.allCorralList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalCorrals = it
            setAdapters()
        }

        mProductViewModel.allProductList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalProducts = it
            setAdapters()
        }

        mDietViewModel.allDietList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalDiets = it
            setAdapters()
        }

        mDietViewModel.allDietProductList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalDietProducts = it
            setAdapters()
        }

        mRoundViewModel.allRoundCorralList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalRoundCorrals = it
            setAdapters()
        }

        mRoundViewModel.allRoundList.observe(this){
            (requireActivity() as MainActivity).mLocalDetailRound.clear()
            mLocalDetailRound.clear()
            mLocalDetailRoundRunReport.clear()
            mLocalRounds = it

            if (mLocalRounds != null  && mLocalRounds?.size!! > 0){
                mBinding.llRounds.visibility = VISIBLE
                mBinding.tvNoData.visibility = GONE
            } else {
                mBinding.llRounds.visibility = GONE
                mBinding.tvNoData.visibility = VISIBLE
            }
            setAdapters()
        }
    }

    override fun onResume() {
        super.onResume()
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        getLocalData()
        Log.i(TAG,"onResume")
        activity = this.getActivity() as MainActivity?
        if(activity is MainActivity){
            Log.i(TAG,"activity as MainActivity $activity")
            (activity as MainActivity).getSavedMixer()
        }
    }

    private fun alertMixer(msg: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Alerta")
        builder.setMessage(msg)
        builder.setPositiveButton("Vincular") { dialog, _ ->
            val options = NavOptions.Builder()
                .setPopUpTo(R.id.nav_home, true)
                .build()
            findNavController().navigate(R.id.nav_mixer, null, options)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun goToRoundDetails(round: Round){
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToRoundDetailFragment(
            round
        ))
    }

    fun goToAddUpdateRound(){
//        val intent = Intent(activity, AddUpdateRoundActivity::class.java)
//        startActivity(intent)
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToAddUpdateRoundActivity())
    }

    fun goToRoundRun(roundRunDetail: RoundRunDetail,toStep : Int){
        val selectedMixer = getSelectedMixer()

        val selecetedBluetoothDevice = getSelectedMixerBluetoothDevice()
        roundRunDetail.mixerBluetoothDevice =  selecetedBluetoothDevice

        selectedMixer?.let {
            Log.i("run", "selectedMixer: ${selectedMixer.name}")
            val mixer = MixerDetail(
                it.name,
                it.description,
                it.mac,
                it.btBox,
                it.tara,
                it.calibration,
                it.rfid,
                it.remoteId,
                it.updatedDate,
                it.archiveDate,
                it.id
            )

            roundRunDetail.mixer = mixer

            if (roundRunDetail.endDate.isNotEmpty()) {
                Log.i("run", "[Nueva] Ronda con id: ${roundRunDetail.id} | ${roundRunDetail.round.name}")
                roundRunDetail.round.diet.products.forEach {  prodcutDetail ->
                    prodcutDetail.initialWeight = 0.0
                    prodcutDetail.currentWeight = 0.0
                    prodcutDetail.finalWeight = 0.0
                }
                roundRunDetail.round.corrals.forEach { corralDetail ->
                    corralDetail.initialWeight = 0.0
                    corralDetail.currentWeight = 0.0
                    corralDetail.finalWeight = 0.0
                    corralDetail.actualTargetWeight = corralDetail.customTargetWeight
                }
                roundRunDetail.round.customRoundRunPercentage = roundRunDetail.round.customPercentage
                roundRunDetail.round.customRoundRunWeight = roundRunDetail.round.weight
                roundRunDetail.customTara = 0.0
                roundRunDetail.addedBlend = 0.0

                val newRoundRunDetail = RoundRunDetail(
                    roundRunDetail.userId,
                    roundRunDetail.userDisplayName,
                    roundRunDetail.round,
                    mixer = mixer,
                    selecetedBluetoothDevice,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.APP_DB_FORMAT_DATE)),
                    "",
                    "",
                    0L,
                    roundRunDetail.round.customPercentage,
                    roundRunDetail.customTara,
                    roundRunDetail.addedBlend,
                    roundRunDetail.state,
                    0L,
                )
                Log.i(TAG, "Ronda: $roundRunDetail")

                Helper.logRoundRunComplete(newRoundRunDetail)
                cleanObservers()
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToRoundRunActivity(newRoundRunDetail))

            } else {
                Log.i(TAG, "[Continuar] Ronda con id: ${roundRunDetail.id} | ${roundRunDetail.round.name}")
                if (roundRunDetail.startDate.isEmpty()){
                    roundRunDetail.startDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.APP_DB_FORMAT_DATE))
                    roundRunDetail.customTara = 0.0//TODO selectedMixer.tara Para mi estas dos lineas estan mal
                    roundRunDetail.addedBlend = 0.0//TODO selectedMixer.tara
                }
                cleanObservers()
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToRoundRunActivity(roundRunDetail,toStep))
            }
        }?: run {
            alertMixer("No se puede iniciar la ronda porque no hay ningún mixer vinculado en el dispositvo.")
        }

    }

    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapterFragment(this, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){

        when(selection){
            Constants.MIXER_REF->{
                (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                //mBinding.men men.etMixer.setText(selectedMixer.name, TextView.BufferType.EDITABLE)
                mCustomListDialog.dismiss()

                val localMixer = mLocalMixers?.first { mixer ->
                    mixer.id == item.id
                }

                val changeMixer = myMenu?.findItem(R.id.action_change_mixer)
                changeMixer?.title = "   "+ localMixer?.name
                localMixer.let {
                    Log.i(TAG, "1Se seleccionó mixer $localMixer")
                    (requireActivity() as MainActivity).saveMixer(localMixer!!)
                }

                Log.i(TAG, "Local mixer selected: ${localMixer?.name} | ${localMixer?.mac}")
                val localKnowDevice = knowDevices?.firstOrNull {
                    Log.d("DEBUG", "selected item from mixer 1 $localMixer  BT: $it")
                    it.address == localMixer?.mac
                }
                Log.i(TAG, "localKnowDevice: ${localKnowDevice?.name} | ${localKnowDevice?.address}")

                if (localKnowDevice != null){
                    selectedMixerInFragment = mLocalMixers?.firstOrNull {
                        it.id == item.id
                    }
                }
            }
        }


    }

    fun getSelectedMixer() : Mixer? {
        return selectedMixerInFragment
    }

    private fun getSelectedMixerBluetoothDevice() : BluetoothDevice? {//TODO Cuando hay mas de un  mixer con el mismo bluetooth esto no funciona. Hay que arreglarlo
        Log.i(TAG, "[getSelectedMixerBluetoothDevice] Local mixer: ${knowDevices?.size} | mLocalMixers: ${mLocalMixers?.size} \nselectedMixerInFragment $selectedMixerInFragment \nknowDevices $knowDevices")
        knowDevices?.forEach { bluetoothDevice ->
            if(selectedMixerInFragment?.mac == bluetoothDevice.address){
                return bluetoothDevice
            }
        }

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("run", "onDestroyView")
        cleanObservers()
        // Unregister Listener
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    private fun cleanObservers(){
        mLocalDetailRound.clear()
        Log.i("home", "mLocalDetailRound cleanObservers ${mLocalDetailRound.size}")

        mLocalEstablishments = null
        mLocalDiets = null
        mLocalMixers = null
        mLocalProducts = null
        mLocalCorrals = null
        mLocalRounds = null
        mLocalDietProducts = null
        mLocalRoundCorrals = null
        mLocalRoundRunProgressLoad = null
        mLocalRoundRunProgressDownload = null
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            Log.i(TAG, "ACT onDeviceConnected")
            connected()
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("${this.javaClass.name} BLUE", "ACT onCommandReceived")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i(TAG, "ACT onMessageReceived DEVICE: ${device?.address} | MSG: $message")
        }

        override fun onMessageSent(device: BluetoothDevice?) {
            Log.i(TAG, "ACT onMessageSent")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "ACT onError")
            disconnected()
        }

        override fun onDeviceDisconnected() {
            Log.i(TAG, "ACT onDeviceDisconnected")
            disconnected()
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "ACT onBondedDevices")
            Log.i(TAG,"selectedMixerInFragment $selectedMixerInFragment")
            knowDevices?.forEach {
                Log.i(TAG, "knowDevices: ${it.name} | ${it.address}")
            }
            knowDevices = device
            val selectedMixer = getSelectedMixer()
            if (selectedMixer != null){
                myMenu?.findItem(R.id.action_change_mixer)?.title = "   " +selectedMixer.name
            }

            mLocalMixers?.forEach {
                val alreadyExist = mLocalMixersCustomList.firstOrNull {  customItem ->
                    customItem.id == it.id
                }

                val isLinked = knowDevices?.any { knowDevice -> knowDevice.address == it.mac } == true
                if (alreadyExist == null && isLinked){
                    val customList = CustomListItem(it.id, it.remoteId, it.name)
                    mLocalMixersCustomList.add(customList)
                }
            }
        }
    }

    fun connected(){
        val icBTStat = myMenu?.findItem(R.id.bluetooth_mixer_status)
        icBTStat?.icon?.setTint(R.color.white)
    }

    fun disconnected(){
        val icBTStat = myMenu?.findItem(R.id.bluetooth_mixer_status)
        icBTStat?.icon?.setTint(R.color.color_full_red)
    }

    fun deleteRound(round: Round){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_round))
        builder.setMessage(resources.getString(R.string.msg_delete_round_dialog, round.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        if(round.remoteId != 0L){
            builder.setMessage(resources.getString(R.string.msg_delete_round_sincronized_dialog, round.name))
            builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            builder.setCancelable(false)
        }

        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mRoundViewModel.delete(round)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun setMixer(mixer : Mixer?) {
        myMenu?.findItem(R.id.action_change_mixer)?.title = "  " + mixer?.name
    }
}

