package com.basculasmagris.visorremotomixer.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentSyncBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class SyncFragment : Fragment() {

    private lateinit var mBinding: FragmentSyncBinding
    private lateinit var username: String
    private lateinit var codeClient: String
    private var userId by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentSyncBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isAvailableInternet = Helper.checkForInternet(requireActivity())
        val sharedpreferences: SharedPreferences?
        sharedpreferences = requireActivity().getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE)
        username = sharedpreferences.getString(Constants.PREF_LOGIN_KEY_USERNAME, "").toString()
        codeClient = sharedpreferences.getString(Constants.PREF_LOGIN_KEY_CODE_CLIENT, "").toString()
        userId = sharedpreferences.getLong(Constants.PREF_LOGIN_KEY_ID, 0)
        mBinding.btnSync.isEnabled = false
        mBinding.btnSync.setOnClickListener{
            GlobalScope.launch (Dispatchers.Main) {
                syncData()
            }
        }

        if (isAvailableInternet) {
            mBinding.llSyncData.visibility = VISIBLE
            mBinding.llSyncNoInternet.visibility = GONE
            getLocalData()
        } else {
            mBinding.llSyncData.visibility = GONE
            mBinding.llSyncNoInternet.visibility = VISIBLE

            mBinding.btnRetry.setOnClickListener{
                if (Helper.checkForInternet(requireActivity())) {
                    mBinding.llSyncData.visibility = VISIBLE
                    mBinding.llSyncNoInternet.visibility = GONE
                    getLocalData()
                }
            }
        }


    }

    // -------------------
    // Establishment
    // -------------------
    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((requireActivity().application as SpiMixerApplication).establishmentRepository)
    }
    private var mEstablishmentViewModelRemote: EstablishmentRemoteViewModel? = null
    private var mLocalEstablishments: MutableList<Establishment>? = null
    private var mRemoteEstablishments: MutableList<EstablishmentRemote>? = null

    // -------------------
    // Corral
    // -------------------
    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as SpiMixerApplication).corralRepository)
    }
    private var mCorralViewModelRemote: CorralRemoteViewModel? = null
    private var mLocalCorrals: MutableList<Corral>? = null
    private var mRemoteCorrals: MutableList<CorralRemote>? = null

    // -------------------
    // ProductscorralViewModelRemoteObserver
    // -------------------
    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as SpiMixerApplication).productRepository)
    }
    private var mProductViewModelRemote: ProductRemoteViewModel? = null
    private var mLocalProducts: MutableList<Product>? = null
    private var mRemoteProducts: MutableList<ProductRemote>? = null

    // -------------------
    // Mixer
    // -------------------
    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((requireActivity().application as SpiMixerApplication).mixerRepository)
    }
    private var mMixerViewModelRemote: MixerRemoteViewModel? = null
    private var mLocalMixers: MutableList<Mixer>? = null
    private var mRemoteMixers: MutableList<MixerRemote>? = null

    // -------------------
    // Diet
    // -------------------
    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((requireActivity().application as SpiMixerApplication).dietRepository)
    }
    private var mDietViewModelRemote: DietRemoteViewModel? = null
    private var mLocalDiets: MutableList<Diet>? = null
    private var mLocalDietProducts: MutableList<DietProduct>? = null
    private var mRemoteDiets: MutableList<DietRemote>? = null

    // -------------------
    // Round
    // -------------------
    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((requireActivity().application as SpiMixerApplication).roundRepository)
    }
    private var mRoundViewModelRemote: RoundRemoteViewModel? = null
    private var mLocalRounds: MutableList<Round>? = null
    private var mLocalRoundCorrals: MutableList<RoundCorral>? = null
    private var mRemoteRounds: MutableList<RoundRemote>? = null

    // -------------------
    // Users
    // -------------------
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as SpiMixerApplication).userRepository)
    }
    private var mUserViewModelRemote: UserRemoteViewModel? = null
    private var mLocalUsers: MutableList<User>? = null
    private var mRemoteUsers: MutableList<UserRemote>? = null

    // -------------------
    // Round Run
    // -------------------
    private var mRoundRunViewModelRemote: RoundRemoteViewModel? = null

    private var mLocalRoundRunRemote: MutableList<RoundRunRemote>? = null
    private var mLocalRoundRunProgressLoadRemote: MutableList<RoundRunProgressLoad>? = null
    private var mLocalRoundRunProgressDownloadRemote: MutableList<RoundRunProgressDownload>? = null

    private var mLocalRoundRun: MutableList<RoundRun>? = null
    private var mLocalRoundRunProgressLoad: MutableList<RoundRunProgressLoad>? = null
    private var mLocalRoundRunProgressDownload: MutableList<RoundRunProgressDownload>? = null


    private fun checkSyncStatus(){
        mBinding.btnSync.isEnabled = (mRemoteCorrals != null && mRemoteEstablishments != null
                && mRemoteMixers != null && mRemoteProducts != null && mRemoteDiets != null
                && mRemoteRounds != null && mRemoteUsers != null)

        if (mBinding.btnSync.isEnabled){
            Log.i("SYNC", "\n-----------REMOTOS\nEstableciemientos: ${mRemoteEstablishments?.size}\n" +
                    "Corrales: ${mRemoteCorrals?.size}\n" +
                    "Mixers: ${mRemoteMixers?.size}\n" +
                    "Productos: ${mRemoteProducts?.size}\n" +
                    "Diets: ${mRemoteDiets?.size}\n" +
                    "Rounds: ${mRemoteRounds?.size}\n" +
                    "Usuarios: ${mRemoteUsers?.size}\n"
            )

            mRemoteUsers?.forEach {
                Log.i("SYNC", "Users")
                Log.i("SYNC", "UserName: ${it.username} | ${it.id} | ${it.updatedDate}")
                Log.i("SYNC", "Client: ${it.codeClient} | Role: ${it.codeRole}")
            }

            val totalEstablishment = mLocalEstablishments!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteEstablishments!!.size
            mBinding.pbEstablishments.progress = 0
            mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalEstablishment)"

            val totalProducts = mLocalProducts!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteProducts!!.size
            mBinding.pbProducts.progress = 0
            mBinding.tvProductsPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalProducts)"

            val totalCorrals = mLocalCorrals!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteCorrals!!.size
            mBinding.pbCorrals.progress = 0
            mBinding.tvCorralsPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalCorrals)"

            val totalMixer = mLocalMixers!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteMixers!!.size
            mBinding.pbMixers.progress = 0
            mBinding.tvMixersPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalMixer)"

            val totalDiets = mLocalDiets!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteDiets!!.size
            mBinding.pbDiets.progress = 0
            mBinding.tvDietsPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalDiets)"


            val totalRounds = mLocalRounds!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteRounds!!.size
            mBinding.pbRounds.progress = 0
            mBinding.tvRoundsPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalRounds)"

            val totalUsers= mLocalUsers!!.filter {
                it.remoteId == 0L || it.updatedDate.isNotEmpty()
            }.size + mRemoteRounds!!.size
            mBinding.pbUsers.progress = 0
            mBinding.tvUsersPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalUsers)"

            val totalRoundRun= mLocalRoundRun!!.filter {
                it.remoteId == 0L && it.endDate.isNotEmpty() //TODO
            }.size
            mBinding.pbReports.progress = 0
            mBinding.tvReportsPercentage.text = Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalRoundRun)"

        }

    }
    private fun setupSync(){
        // Observers to API
        // Establishment
        mEstablishmentViewModelRemote = ViewModelProvider(requireActivity())[EstablishmentRemoteViewModel::class.java]
        establishmentViewModelRemoteObserver()
        mEstablishmentViewModelRemote?.getEstablishmentsFromAPI()

        // Corral
        mCorralViewModelRemote = ViewModelProvider(requireActivity())[CorralRemoteViewModel::class.java]
        corralViewModelRemoteObserver()
        mCorralViewModelRemote?.getCorralsFromAPI()

        // Products
        mProductViewModelRemote = ViewModelProvider(requireActivity())[ProductRemoteViewModel::class.java]
        productViewModelRemoteObserver()
        mProductViewModelRemote?.getProductsFromAPI()

        // Mixers
        mMixerViewModelRemote = ViewModelProvider(requireActivity())[MixerRemoteViewModel::class.java]
        mixerViewModelRemoteObserver()
        mMixerViewModelRemote?.getMixersFromAPI()

        // Diets
        mDietViewModelRemote = ViewModelProvider(requireActivity())[DietRemoteViewModel::class.java]
        dietViewModelRemoteObserver()
        mDietViewModelRemote?.getDietsFromAPI()

        // Rounds
        mRoundViewModelRemote = ViewModelProvider(requireActivity())[RoundRemoteViewModel::class.java]
        roundViewModelRemoteObserver()
        mRoundViewModelRemote?.getRoundsFromAPI()

        // Users
        mUserViewModelRemote = ViewModelProvider(requireActivity())[UserRemoteViewModel::class.java]
        userViewModelRemoteObserver()
        mUserViewModelRemote?.getUsersFromAPI()

        // Round Run
        mRoundRunViewModelRemote = ViewModelProvider(requireActivity())[RoundRemoteViewModel::class.java]
        roundRunViewModelRemoteObserver()
    }

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

        liveDataMerger.addSource(mUserViewModel.allUserList) {
            if (it != null) {
                liveDataMerger.value = UserData(it)
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
        val liveData = fetchLocalData()
        liveData.observe(requireActivity(),  object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is EstablishmentData -> mLocalEstablishments = it.establishments
                    is CorralData -> mLocalCorrals = it.corrals
                    is ProductData -> mLocalProducts = it.products
                    is MixerData -> mLocalMixers = it.mixers
                    is DietData -> mLocalDiets = it.diets
                    is DietProductData -> mLocalDietProducts = it.dietProducts
                    is RoundData -> mLocalRounds = it.rounds
                    is RoundCorralData -> mLocalRoundCorrals = it.roundCorrals
                    is UserData -> mLocalUsers = it.users
                    is RoundRunData -> mLocalRoundRun = it.roundRun
                    is RoundRunProgressLoadData -> mLocalRoundRunProgressLoad = it.roundRunProgressLoad
                    is RoundRunProgressDownloadData -> mLocalRoundRunProgressDownload = it.roundRunProgressDownload
                    else -> {}
                }

                if (mLocalEstablishments != null && mLocalCorrals != null &&
                    mLocalProducts != null && mLocalMixers != null && mLocalDiets != null &&
                    mLocalDietProducts != null && mLocalRounds != null &&
                    mLocalRoundCorrals != null && mLocalUsers != null
                    && mLocalRoundRun != null && mLocalRoundRunProgressLoad != null && mLocalRoundRunProgressDownload != null) {

                    Log.i("SYNC", "----- LOCALES ------")
                    Log.i("SYNC", "-----------\nEstableciemientos: ${mLocalEstablishments?.size}\n" +
                            "Corrales: ${mLocalCorrals?.size}\n" +
                            "Mixers: ${mLocalMixers?.size}\n" +
                            "Productos: ${mLocalProducts?.size}\n" +
                            "Diets: ${mLocalDiets?.size}\n" +
                            "Diets/Products: ${mLocalDietProducts?.size}\n" +
                            "Round: ${mLocalRounds?.size}\n" +
                            "Round/Corral: ${mLocalRoundCorrals?.size}\n" +
                            "Usuarios: ${mLocalUsers?.size}\n" +
                            "Ejecuciones: ${mLocalRoundRun?.size}\n" +
                            "    Cargas: ${mLocalRoundRunProgressLoad?.size}\n" +
                            "    Descargas: ${mLocalRoundRunProgressDownload?.size}\n"
                    )

                    mLocalUsers?.forEach {
                        Log.i("SYNC", "Users")
                        Log.i("SYNC", "UserName: ${it.username} | ${it.remoteId}")
                        Log.i("SYNC", "Client: ${it.codeClient} | Role: ${it.codeRole}")

                    }

                    mLocalRoundRun?.forEach {
                        Log.i("SYNC", "mLocalRoundRun")
                        Log.i("SYNC", "id: ${it.id} | ${it.remoteId}")
                        Log.i("SYNC", "endDate: ${it.endDate}")

                    }

                    setupSync()
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    private suspend fun syncData(){

        // Sync Users
        // -------------------
        syncUsers()

        // Sync Products
        // -------------------
        syncProducts()

        // Sync Mixer
        // -------------------
        syncMixers()

        // Sync Establishment
        // -------------------
        syncEstablishment()

        // Sync Corral
        // -------------------
        syncCorral()

        // Sync Diets
        // -------------------
        syncDiets()

        // Sync Rounds
        // -------------------
        syncRounds()

        // Sync Round Run
        // -------------------
        syncRoundRun()

    }

    private fun updateEstablishmentReferences(establishmentAppId: Long, remoteEstablishmentId: Long){
        // Corral references
        val localCorrals = mLocalCorrals?.filter { corral -> corral.establishmentId == establishmentAppId && corral.establishmentRemoteId == 0L}
        localCorrals?.forEach { corral ->
            mCorralViewModel.setUpdatedEstablishmentRemoteId(corral.id, remoteEstablishmentId)
            val index = mLocalCorrals?.indexOf(corral)
            index?.let { _index ->
                mLocalCorrals!![_index].establishmentRemoteId = remoteEstablishmentId
            }
        }
        updateEstablishmentRemoteId(establishmentAppId, remoteEstablishmentId)
    }
    
    // Establishment
    // -----------------
    private suspend fun syncEstablishment(){
        Log.i("SYNC", "-----> Start Establishments")

        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate : List<Establishment> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalEstablishments?.let {
            itemsToAddOrUpdate = it.filter { establishment ->
                establishment.remoteId == 0L || establishment.updatedDate.isNotEmpty()
            }
            origianlItemsToAddOrUpdate = itemsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando establecimientos: ${mRemoteEstablishments?.size}")
        mRemoteEstablishments?.let {
            totalSize = it.size + itemsToAddOrUpdate.size
            it.forEach{ item ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteEstablishmentInDatabase(item)
            }
        }

        mLocalEstablishments?.let {
            itemsToAddOrUpdate = it.filter { establishment ->
                establishment.remoteId == 0L || establishment.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - itemsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando establecimientos: ${itemsToAddOrUpdate.size}")

        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbEstablishments.progress = percentageValue.roundToInt()
            mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            saveLocalEstablishmentInServer(item)
        }
        Log.i("SYNC", "      --------------------------------")
    }

    private fun establishmentViewModelRemoteObserver(){
            // Get observers
            mEstablishmentViewModelRemote?.establishmentsResponse?.observe(viewLifecycleOwner
            ) { remoteEstablishment ->
                remoteEstablishment?.let { remoteEstablishments ->
                    mRemoteEstablishments = remoteEstablishments
                    checkSyncStatus()
                }
            }
        mEstablishmentViewModelRemote?.establishmentsLoadingError?.observe(viewLifecycleOwner
            ) { errorData ->
                errorData?.let { hasError ->
                    if (hasError) {
                        Log.e("Establishments [establishmentsLoadingError]", "$errorData")
                    }
                }
            }

        // Observers Add
            // -------------
            mEstablishmentViewModelRemote?.addEstablishmentsResponse?.observe(viewLifecycleOwner
            ) { remoteEstablishment ->
                remoteEstablishment?.let {
                    updateEstablishmentReferences(it.appId, it.id)
                }
            }
        mEstablishmentViewModelRemote?.addEstablishmentErrorResponse?.observe(viewLifecycleOwner
        ) { errorData ->
            errorData?.let { hasError ->
                if (hasError) {
                    Log.e("Establishments [addEstablishmentErrorResponse]", "$errorData")
                }
            }
        }

        // Observers Update
            // -------------
            mEstablishmentViewModelRemote?.updateEstablishmentsResponse?.observe(viewLifecycleOwner
            ) { remoteEstablishment ->
                remoteEstablishment?.let {
                    updateEstablishmentReferences(it.appId, it.id)
                    mEstablishmentViewModel.setUpdatedDate(it.appId, "")
                }
            }

    }

    private fun saveLocalEstablishmentInServer(establishment: Establishment){

            // Si no tiene remoteID significa que es un establishmento que
            // fue generado desde la APP pero que nunca pudo ser sincronizado
            if (establishment.remoteId == 0L) {
                Log.i(
                    "SYNC",
                    "        - Added: ${establishment.id} (${establishment.name})"
                )
                mEstablishmentViewModelRemote?.addEstablishmentFromAPI(establishment)
            } else if (establishment.updatedDate.isNotEmpty()) {
                // Si no tiene updatedDate significa que es un establishmento que
                // fue actualizado desde la APP pero que no pudieron sincronizarse los cambios.

                Log.i(
                    "SYNC",
                    "        - Updated: ${establishment.id} (${establishment.name})"
                )
                mEstablishmentViewModelRemote?.updateEstablishmentFromAPI(establishment)
            }
    }
    private suspend fun saveRemoteEstablishmentInDatabase(remoteEstablishment: EstablishmentRemote){

        val localEstablishment = mLocalEstablishments?.filter {
            it.remoteId == remoteEstablishment.id
        }

        localEstablishment?.let {  it ->
            if (it.isNotEmpty()){

                if (it[0].updatedDate.isNotEmpty()){
                    Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteEstablishment.updatedDate)}")
                    Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(it[0].updatedDate)}")
                    Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteEstablishment.updatedDate)))}")
                }
                // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
                // o bien si la modificación es más reciente es de la WEB
                if (it[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteEstablishment.updatedDate)))) {
                    val establishment = Establishment(
                        remoteEstablishment.name,
                        remoteEstablishment.description,
                        remoteEstablishment.id,
                        "",
                        remoteEstablishment.archiveDate,
                        localEstablishment[0].id)

                    mEstablishmentViewModel.update(establishment)
                    val index = mLocalEstablishments?.indexOf(localEstablishment[0])
                    mLocalEstablishments?.get(index!!)?.updatedDate  = ""
                    
                    Log.i(
                        "SYNC",
                        "        - Updated: ${establishment.id} (${establishment.name}) | Remote: ${remoteEstablishment.id}"
                    )

                } else {
                    Log.i(
                        "SYNC",
                        "        - Last change APP: ${it[0].id} (${remoteEstablishment.name}) | Remote: ${remoteEstablishment.id}"
                    )
                }

            } else {
                val establishment = Establishment(
                    remoteEstablishment.name,
                    remoteEstablishment.description,
                    remoteEstablishment.id,
                    "",
                    remoteEstablishment.archiveDate
                )

                val establishmentId = mEstablishmentViewModel.insertSync(establishment)

                establishment.id = establishmentId
                Log.i(
                    "SYNC",
                    "        - Added: ${establishment.id} (${establishment.name}) | Remote: ${remoteEstablishment.id}"
                )
                mLocalEstablishments?.add(establishment)

            }
        }

    }
    private fun updateEstablishmentRemoteId(id: Long, remoteId: Long){
        mEstablishmentViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalEstablishment = mLocalEstablishments?.first { _establishment ->
            _establishment.id == id
        }
        if (currentLocalEstablishment != null){
            val index = mLocalEstablishments?.indexOf(currentLocalEstablishment)
            index?.let { _index ->
                mLocalEstablishments!![_index].remoteId = remoteId
            }
        }
    }

    // Corral
    // -----------------
    private suspend fun syncCorral(){
        Log.i("SYNC", "-----> Start Corrals")

        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate : List<Corral> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalCorrals?.let {
            itemsToAddOrUpdate = it.filter { mixer ->
                mixer.remoteId == 0L || mixer.updatedDate.isNotEmpty()
            }
            origianlItemsToAddOrUpdate = itemsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando Corrales: ${mRemoteCorrals?.size}")
        mRemoteCorrals?.let {
            totalSize = it.size + itemsToAddOrUpdate.size
            it.forEach{ item ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbCorrals.progress = percentageValue.roundToInt()
                mBinding.tvCorralsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteCorralInDatabase(item)
            }
        }

        mLocalCorrals?.let {
            itemsToAddOrUpdate = it.filter { mixer ->
                mixer.remoteId == 0L || mixer.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - itemsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando Corrales: ${itemsToAddOrUpdate.size}")
        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbCorrals.progress = percentageValue.roundToInt()
            mBinding.tvCorralsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            saveLocalCorralInServer(item)
        }
    }
    private fun corralViewModelRemoteObserver(){

            // Get observers
            mCorralViewModelRemote?.corralsResponse?.observe(viewLifecycleOwner) { remoteCorral ->
                remoteCorral?.let { remoteCorrals ->
                    mRemoteCorrals = remoteCorrals
                    checkSyncStatus()
                }
            }
        mCorralViewModelRemote?.corralsLoadingError?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
            }
        }

        // Observers Add
            // -------------
            mCorralViewModelRemote?.addCorralsResponse?.observe(viewLifecycleOwner) { remoteCorral ->
                remoteCorral?.let {
                    updateCorralRemoteId(remoteCorral.appId, it.id)
                }
            }
        mCorralViewModelRemote?.addCorralErrorResponse?.observe(viewLifecycleOwner) { errorData ->
                errorData?.let {
                    if (errorData) {
                        Log.e("Corrals [addCorralErrorResponse]", "$errorData")
                    }
                }
            }

        // Observers Update
            // -------------
            mCorralViewModelRemote?.updateCorralsResponse?.observe(viewLifecycleOwner
            ) { remoteCorral ->
                remoteCorral?.let {
                    mCorralViewModel.setUpdatedDate(it.appId, "")
                }
            }
    }

    private fun saveLocalCorralInServer(corral: Corral){

            // Se actualiza el ID Remoto de la dependencia
            val localEstablishment = mLocalEstablishments?.firstOrNull {
                it.id == corral.establishmentId
            }
            localEstablishment?.let {
                corral.establishmentRemoteId = it.remoteId
            }

            // Si no tiene remoteID significa que es un corralo que
            // fue generado desde la APP pero que nunca pudo ser sincronizado
            if (corral.remoteId == 0L) {
                Log.i(
                    "SYNC",
                    "        - Added: ${corral.id} (${corral.name}) | Est. ${corral.establishmentId}/${corral.establishmentRemoteId}"
                )
                mCorralViewModelRemote?.addCorralFromAPI(corral)
            }

            // Si no tiene updatedDate significa que es un corralo que
            // fue actualizado desde la APP pero que no pudieron sincronizarse los cambios.
            if (corral.updatedDate.isNotEmpty()){
                Log.i(
                    "SYNC",
                    "        - Updated: ${corral.id} (${corral.name})"
                )
                mCorralViewModelRemote?.updateCorralFromAPI(corral)
            }
    }
    private suspend fun saveRemoteCorralInDatabase(remoteCorral: CorralRemote){

        val localCorral = mLocalCorrals?.filter {
            it.remoteId == remoteCorral.id
        }

        val localEstablishment = mLocalEstablishments?.filter {
            it.remoteId == remoteCorral.establishmentId
        }

        localEstablishment?.let { _localEstablishment ->
            if (_localEstablishment.isNotEmpty()){
                localCorral?.let {
                    if (it.isNotEmpty()){

                        if (it[0].updatedDate.isNotEmpty()){
                            Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteCorral.updatedDate)}")
                            Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(it[0].updatedDate)}")
                            Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteCorral.updatedDate)))}")
                        }
                        // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
                        // o bien si la modificación es más reciente es de la WEB
                        if (it[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteCorral.updatedDate)))) {
                            val corral = Corral(
                                _localEstablishment[0].id,
                                _localEstablishment[0].remoteId,
                                remoteCorral.name,
                                remoteCorral.description,
                                remoteCorral.id,
                                "",
                                remoteCorral.archiveDate,
                                remoteCorral.animalQuantity,
                                remoteCorral.rfid,
                                localCorral[0].id
                            )

                            Log.i(
                                "SYNC",
                                "        - Updated: ${localCorral[0].id} / Establishment: ${_localEstablishment[0].id} (${_localEstablishment[0].name}) | Remote Est.: ${_localEstablishment[0].remoteId} | Remote Corral.: ${remoteCorral.id}"
                            )
                            updateCorralDependencies(localCorral[0].id, remoteCorral.id)

                            mCorralViewModel.update(corral)
                            val index = mLocalCorrals?.indexOf(localCorral[0])
                            mLocalCorrals?.get(index!!)?.updatedDate  = ""
                        } else {
                            Log.i(
                                "SYNC",
                                "        - Last change APP: ${it[0].id} (${remoteCorral.name}) | Remote: ${remoteCorral.id}"
                            )
                        }
                    } else {
                        val corral = Corral(
                            _localEstablishment[0].id,
                            _localEstablishment[0].remoteId,
                            remoteCorral.name,
                            remoteCorral.description,
                            remoteCorral.id,
                            "",
                            remoteCorral.archiveDate,
                            remoteCorral.animalQuantity,
                            remoteCorral.rfid
                        )

                        val corralId = mCorralViewModel.insertSync(corral)
                        corral.id = corralId
                        Log.i(
                            "SYNC",
                            "        - Added: $corralId (${corral.name}) / Establishment: ${_localEstablishment[0].id} (${_localEstablishment[0].name}) | Remote Est.: ${_localEstablishment[0].remoteId} | Remote Corral.: ${remoteCorral.id}"
                        )
                        mLocalCorrals?.add(corral)
                    }
                }
            }
        }
    }
    private fun updateCorralRemoteId(id: Long, remoteId: Long){
        mCorralViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalCorral = mLocalCorrals?.first { _corral ->
            _corral.id == id
        }
        if (currentLocalCorral != null){
            val index = mLocalCorrals?.indexOf(currentLocalCorral)
            index?.let { _index ->
                mLocalCorrals!![_index].remoteId = remoteId
            }
        }
    }
    private fun updateCorralDependencies(corralAppId: Long, corralRemoteId: Long){
        //Se actualizan las dependencias
        mLocalRoundRunProgressDownload?.forEach { roundRunDownload ->
            if (roundRunDownload.corralId == corralAppId){
                roundRunDownload.remoteCorralId = corralRemoteId
            }
        }
    }
    // Product
    // ----------------
    private suspend fun syncProducts(){
        Log.i("SYNC", "-----> Start Products")
        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var productsToAddOrUpdate : List<Product> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalProducts?.let {
            productsToAddOrUpdate = it.filter { product ->
                product.remoteId == 0L || product.updatedDate.isNotEmpty()
            }
            origianlItemsToAddOrUpdate = productsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando productos: ${mLocalProducts?.size}")
        mRemoteProducts?.let {
            totalSize = it.size + productsToAddOrUpdate.size
            it.forEach{ product ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbProducts.progress = percentageValue.roundToInt()
                mBinding.tvProductsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteProductInDatabase(product)
            }
        }
        

        mLocalProducts?.let {
            productsToAddOrUpdate = it.filter { product ->
                product.remoteId == 0L || product.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - productsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando productos: ${productsToAddOrUpdate.size}")

        productsToAddOrUpdate.forEach{ product ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbProducts.progress = percentageValue.roundToInt()
            mBinding.tvProductsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            saveLocalProductInServer(product)
        }
    }
    private fun productViewModelRemoteObserver(){
        mProductViewModelRemote?.productsResponse?.observe(viewLifecycleOwner) { remoteProduct ->
            remoteProduct?.let {
                mRemoteProducts = it
                checkSyncStatus()
            }
        }
        mProductViewModelRemote?.productsLoadingError?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                if (errorData) {
                    Log.e("Products [productsLoadingError]", "$errorData")
                }
            }
        }

        // Observers Add
        // -------------
        mProductViewModelRemote?.addProductsResponse?.observe(viewLifecycleOwner) { remoteProduct ->
            remoteProduct?.let {
                Log.i("SYNC", "***** addProductsResponse")
                Log.i("SYNC", "***** Se actualiza id remoto en dieta")
                Log.i(
                    "SYNC",
                    "***** App ID: ${it.appId} | Remote Id: ${it.id}"
                )
                updateProductRemoteId(it.appId, it.id)
            }
        }
        mProductViewModelRemote?.addProductErrorResponse?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                Log.e("Products [addProductErrorResponse]", "$errorData")
            }
        }

        // Observers Update
        // -------------
        mProductViewModelRemote?.updateProductsResponse?.observe(viewLifecycleOwner) { remoteProduct ->

            Log.i("SYNC", "***** updateProductsResponse")

            remoteProduct?.let {
                mProductViewModel.setUpdatedDate(it.appId, "")
                Log.i("SYNC", "***** Se actualiza id remoto en dieta")
                Log.i("SYNC", "***** App ID: ${it.appId} | Remote Id: ${it.id}")
                updateProductRemoteId(it.appId, it.id)
            }
        }


    }

    private fun saveLocalProductInServer(product: Product){

        // Si no tiene remoteID significa que es un producto que
        // fue generado desde la APP pero que nunca pudo ser sincronizado
        if (product.remoteId == 0L) {
            Log.i(
                "SYNC",
                "        - Added: ${product.id} (${product.name})"
            )
            mProductViewModelRemote?.addProductFromAPI(product)
        } else if (product.updatedDate.isNotEmpty()){
            // Si no tiene updatedDate significa que es un producto que
            // fue actualizado desde la APP pero que no pudieron sincronizarse los cambios.
            Log.i(
                "SYNC",
                "        - Updated: ${product.id} (${product.name})"
            )
            mProductViewModelRemote?.updateProductFromAPI(product)
        }
    }

    private suspend fun saveRemoteProductInDatabase(remoteProduct: ProductRemote){

        val localProduct = mLocalProducts?.filter {
            it.remoteId == remoteProduct.id
        }
        
        localProduct?.let {  it ->
            if (it.isNotEmpty()) {
                if (it[0].updatedDate.isNotEmpty()){
                    Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteProduct.updatedDate)}")
                    Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(it[0].updatedDate)}")
                    Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteProduct.updatedDate)))}")
                }
                // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
                // o bien si la modificación es más reciente es de la WEB
                if (it[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteProduct.updatedDate)))) {
                    val product = Product(
                        remoteProduct.name,
                        remoteProduct.description,
                        remoteProduct.specificWeight,
                        remoteProduct.rfid,
                        localProduct[0].image,
                        localProduct[0].imageSource,
                        remoteProduct.id,
                        "",
                        remoteProduct.archiveDate,
                        localProduct[0].id
                    )

                    mProductViewModel.update(product)
                    val index = mLocalProducts?.indexOf(localProduct[0])
                    mLocalProducts?.get(index!!)?.updatedDate  = ""

                    updateProductDependencies(localProduct[0].id, remoteProduct.id)

                    Log.i(
                        "SYNC",
                        "        - Updated: ${product.id} (${remoteProduct.name}) | Remote: ${remoteProduct.id}"
                    )
                } else {
                    Log.i(
                        "SYNC",
                        "        - Last change APP: ${it[0].id} (${remoteProduct.name}) | Remote: ${remoteProduct.id}"
                    )
                }
            } else {
                val product = Product(
                    remoteProduct.name,
                    remoteProduct.description,
                    remoteProduct.specificWeight,
                    remoteProduct.rfid,
                    remoteProduct.image,
                    remoteProduct.imageSource,
                    remoteProduct.id,
                    "",
                    remoteProduct.archiveDate
                )

                val productId = mProductViewModel.insertSync(product)
                product.id = productId
                mLocalProducts?.add(product)
                Log.i(
                    "SYNC",
                    "        - Added: ${product.id} (${remoteProduct.name}) | Remote: ${remoteProduct.id}"
                )
            }
        }

    }
    private fun updateProductRemoteId(id: Long, remoteId: Long){
        mProductViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalProduct = mLocalProducts?.first { _product ->
            _product.id == id
        }

        if (currentLocalProduct != null){
            val index = mLocalProducts?.indexOf(currentLocalProduct)
            index?.let { _index ->
                mLocalProducts!![_index].remoteId = remoteId
            }
        }

        val currentLocalDietProduct = mLocalDietProducts?.first { _product ->
            _product.productId == id
        }
        if (currentLocalDietProduct != null){
            val index = mLocalDietProducts?.indexOf(currentLocalDietProduct)
            index?.let { _index ->
                mLocalDietProducts!![_index].remoteProductId = remoteId
            }
        }

    }

    // Mixer
    // ---------------
    private suspend fun syncMixers() {
        Log.i("SYNC", "-----> Start Mixers")

        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate : List<Mixer> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalMixers?.let {
            itemsToAddOrUpdate = it.filter { mixer ->
                mixer.remoteId == 0L || mixer.updatedDate.isNotEmpty()
            }

            origianlItemsToAddOrUpdate = itemsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando Mixer: ${mRemoteMixers?.size}")
        mRemoteMixers?.let {
            totalSize = it.size + itemsToAddOrUpdate.size
            it.forEach{ item ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbMixers.progress = percentageValue.roundToInt()
                mBinding.tvMixersPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteMixerInDatabase(item)
            }
        }

        mLocalMixers?.let {
            itemsToAddOrUpdate = it.filter { mixer ->
                mixer.remoteId == 0L || mixer.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - itemsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando Mixer: ${itemsToAddOrUpdate.size}")
        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbMixers.progress = percentageValue.roundToInt()
            mBinding.tvMixersPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            saveLocalMixerInServer(item)
        }
    }
    private fun mixerViewModelRemoteObserver(){

            // Get observers
            mMixerViewModelRemote?.mixersResponse?.observe(viewLifecycleOwner) { remoteMixer ->
                remoteMixer?.let { remoteMixers ->
                    mRemoteMixers = remoteMixers
                    checkSyncStatus()
                }
            }
        mMixerViewModelRemote?.mixersLoadingError?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                if (errorData) {
                    Log.e("Mixers [mixersLoadingError]", "$errorData")
                }
            }
        }

        // Observers Add
            // -------------
            mMixerViewModelRemote?.addMixersResponse?.observe(viewLifecycleOwner) { remoteMixer ->
                remoteMixer?.let {
                    updateMixerRemoteId(it.appId, it.id)
                }
            }
        mMixerViewModelRemote?.addMixerErrorResponse?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                Log.e("Mixers [addMixerErrorResponse]", "$errorData")
            }
        }

        // Observers Update
            // -------------
            mMixerViewModelRemote?.updateMixersResponse?.observe(viewLifecycleOwner) { remoteMixer ->
                remoteMixer?.let {
                    mMixerViewModel.setUpdatedDate(it.appId, "")
                }
            }
    }

    private fun saveLocalMixerInServer(mixer: Mixer){

            // Si no tiene remoteID significa que es un mixero que
            // fue generado desde la APP pero que nunca pudo ser sincronizado
            if (mixer.remoteId == 0L) {
                Log.i(
                    "SYNC",
                    "        - Added: ${mixer.id} (${mixer.name})"
                )
                mMixerViewModelRemote?.addMixerFromAPI(mixer)
            }

            // Si no tiene updatedDate significa que es un mixero que
            // fue actualizado desde la APP pero que no pudieron sincronizarse los cambios.
            if (mixer.updatedDate.isNotEmpty()){
                Log.i(
                    "SYNC",
                    "        - Updated: ${mixer.id} (${mixer.name})"
                )
                mMixerViewModelRemote?.updateMixerFromAPI(mixer)
            }

    }
    private suspend fun saveRemoteMixerInDatabase(remoteMixer: MixerRemote){

        val localMixer = mLocalMixers?.filter {
            it.remoteId == remoteMixer.id
        }

        localMixer?.let {  it ->
            if (it.isNotEmpty()) {

                if (it[0].updatedDate.isNotEmpty()){
                    Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteMixer.updatedDate)}")
                    Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(it[0].updatedDate)}")
                    Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteMixer.updatedDate)))}")
                }
                // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
                // o bien si la modificación es más reciente es de la WEB
                if (it[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteMixer.updatedDate)))) {
                    val mixer = Mixer(
                        remoteMixer.name,
                        remoteMixer.description,
                        remoteMixer.mac,
                        remoteMixer.btBox,
                        remoteMixer.tara,
                        remoteMixer.calibration,
                        remoteMixer.rfid,
                        remoteMixer.id,
                        "",
                        remoteMixer.archiveDate,
                        false,
                        localMixer[0].id
                    )


                    mMixerViewModel.update(mixer)
                    val index = mLocalMixers?.indexOf(localMixer[0])
                    mLocalMixers?.get(index!!)?.updatedDate  = ""
                    Log.i(
                        "SYNC",
                        "        - Updated: ${localMixer[0].id} (${remoteMixer.name}) | Remote: ${remoteMixer.id}"
                    )

                    updateMixerDependencies(localMixer[0].id, remoteMixer.id)
                } else {
                    Log.i(
                        "SYNC",
                        "        - Last change APP: ${it[0].id} (${remoteMixer.name}) | Remote: ${remoteMixer.id}"
                    )
                    updateMixerDependencies(localMixer[0].id, remoteMixer.id)
                }

            } else {
                val mixer = Mixer(
                    remoteMixer.name,
                    remoteMixer.description,
                    remoteMixer.mac,
                    remoteMixer.btBox,
                    remoteMixer.tara,
                    remoteMixer.calibration,
                    remoteMixer.rfid,
                    remoteMixer.id,
                    "",
                    remoteMixer.archiveDate,
                    false
                )

                val mixerId = mMixerViewModel.insertSync(mixer)
                mixer.id = mixerId
                mLocalMixers?.add(mixer)
                Log.i(
                    "SYNC",
                    "        - Added: ${mixer.id} (${remoteMixer.name}) | Remote: ${remoteMixer.id}"
                )
            }
        }

    }
    private fun updateMixerRemoteId(id: Long, remoteId: Long){
        mMixerViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalMixer = mLocalMixers?.first { _mixer ->
            _mixer.id == id
        }
        if (currentLocalMixer != null){
            val index = mLocalMixers?.indexOf(currentLocalMixer)
            index?.let { _index ->
                mLocalMixers!![_index].remoteId = remoteId
            }
        }
    }
    private fun updateMixerDependencies(mixerAppId: Long, mixerRemoteId: Long){
        //Se actualizan las dependencias
        mLocalRoundRun?.forEach { roundRun ->
            if (roundRun.mixerId == mixerAppId){
                roundRun.remoteMixerId = mixerRemoteId
            }
        }
    }
    // Diets
    // ---------------
    private suspend fun syncDiets() {
        Log.i("SYNC", "-----> Start Diets")

        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate: List<Diet> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalDiets?.let {
            itemsToAddOrUpdate = it.filter { diet ->
                diet.remoteId == 0L || diet.updatedDate.isNotEmpty()
            }
            origianlItemsToAddOrUpdate = itemsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando Dietas: ${mRemoteDiets?.size}")
        mRemoteDiets?.let {
            totalSize = it.size + itemsToAddOrUpdate.size
            it.forEach{ item ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbDiets.progress = percentageValue.roundToInt()
                mBinding.tvDietsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteDietInDatabase(item)
            }
        }

        mLocalDiets?.let {
            itemsToAddOrUpdate = it.filter { diet ->
                diet.remoteId == 0L || diet.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - itemsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando Dietas: ${itemsToAddOrUpdate.size}")

        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbDiets.progress = percentageValue.roundToInt()
            mBinding.tvDietsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"

            val dietRemote = DietRemote(
                item.name,
                item.description,
                item.remoteId,item.id,"","",
                item.usePercentage, ArrayList()
            )

            val dietProducts = mLocalDietProducts?.filter {
                it.dietId == item.id
            }

            dietProducts?.forEach {

                //Busca el ID remoto del producto
                val localProduct = mLocalProducts?.firstOrNull { localProduct ->
                    localProduct.id == it.productId
                }

                //Busca el ID remoto de la dieta
                val localDiet = mLocalDiets?.firstOrNull { localDiet ->
                    localDiet.id == it.dietId
                }

                if (localProduct != null && localDiet != null){
                    val dpd = DietProductDetail(
                        it.productId,
                        localProduct.remoteId,
                        "",
                        "",
                        it.dietId,
                        localDiet.remoteId,
                        it.percentage,
                        it.weight,
                        it.order)

                    dietRemote.products.add(dpd)
                }


            }

            saveLocalDietInServer(dietRemote, (dietRemote.id == 0L))
        }
    }
    private fun dietViewModelRemoteObserver(){
            // Get observers
            mDietViewModelRemote?.dietsResponse?.observe(viewLifecycleOwner) { remoteDiet ->
                remoteDiet?.let { remoteDiets ->
                    mRemoteDiets = remoteDiets
                    Log.i("SYNC", "**** mDietViewModelRemote.dietsResponse")
                    checkSyncStatus()
                }
            }

        mDietViewModelRemote?.dietsLoadingError?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                if (errorData) {
                    Log.e("Diets [dietsLoadingError]", "$errorData")
                }
            }
        }

        // Observers Add
            // -------------
            mDietViewModelRemote?.addDietsResponse?.observe(viewLifecycleOwner) { remoteDiet ->
                remoteDiet?.let {
                    updateDietRemoteId(it.appId, it.id)
                }
            }
        mDietViewModelRemote?.addDietErrorResponse?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                Log.e("Diets [addDietErrorResponse]", "$errorData")
            }
        }

        // Observers Update
            // -------------
            mDietViewModelRemote?.updateDietsResponse?.observe(viewLifecycleOwner) { remoteDiet ->
                remoteDiet?.let {
                    mDietViewModel.setUpdatedDate(it.appId, "")
                }
            }
    }

    private fun saveLocalDietInServer(diet: DietRemote, isNew: Boolean){

            // Si no tiene remoteID significa que es un mixero que
            // fue generado desde la APP pero que nunca pudo ser sincronizado
            if (isNew) {
                Log.i(
                    "SYNC",
                    "        - Added: (${diet.name}) | Desc: (${diet.description})"
                )
                diet.products.forEach {
                    Log.i("SYNC",
                        "            - Product in diet: ${it.productId} | ${it.remoteProductId}")
                }
                mDietViewModelRemote?.addDietFromAPI(diet)
            } else {
                Log.i(
                    "SYNC",
                    "        - Updated: (${diet.name}) | Desc: (${diet.description})"
                )
                diet.products.forEach {
                    Log.i(
                        "SYNC",
                        "            - Product in diet: ${it.productId} | ${it.remoteProductId}"
                    )

                }
                mDietViewModelRemote?.updateDietFromAPI(diet)
            }
    }

    private fun saveRemoteDietProductInDatabase(localDietId: Long, remoteDietId: Long, localProductId: Long, remoteProductId: Long, remoteProduct: DietProductDetail){
        val dietProduct = DietProduct(
                localDietId,
                localProductId,
                remoteDietId,
                remoteProductId,
                remoteProduct.order,
                remoteProduct.weight,
                remoteProduct.percentage,
                0,
                "",
                null)

        mDietViewModel.insertDietProduct(dietProduct)
        mLocalDietProducts?.add(dietProduct)
    }
    private suspend fun saveRemoteDietInDatabase(remoteDiet: DietRemote){

        val localDiet = mLocalDiets?.filter {
            it.remoteId == remoteDiet.id
        }

        if (localDiet?.isNotEmpty() == true) {

            if (localDiet[0].updatedDate.isNotEmpty()){
                Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteDiet.updatedDate)}")
                Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(localDiet[0].updatedDate)}")
                Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(localDiet[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteDiet.updatedDate)))}")
            }
            // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
            // o bien si la modificación es más reciente es de la WEB
            if (localDiet[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(localDiet[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteDiet.updatedDate)))) {
                val diet = Diet(
                    remoteDiet.name,
                    remoteDiet.description,
                    remoteDiet.id,
                    "",
                    remoteDiet.archiveDate,
                    remoteDiet.usePercentage,
                    localDiet[0].id
                )

                Log.i("SYNC", "        Updated: ${localDiet[0].id} (${remoteDiet.name}) | Remote ${remoteDiet.id} | Cant. Prods: ${remoteDiet.products.size}")
                mDietViewModel.update(diet)
                val index = mLocalDiets?.indexOf(localDiet[0])
                mLocalDiets?.get(index!!)?.updatedDate  = ""

                updateDietDependencies(localDiet[0].id, remoteDiet.id)

                mDietViewModel.deleteProductByDiet(localDiet[0].id)
                remoteDiet.products.forEach{ remoteProduct ->
                    val localProduct = mLocalProducts?.firstOrNull {
                        it.remoteId == remoteProduct.productId
                    }

                    if (localProduct != null) {
                        Log.i(
                            "SYNC",
                            "        - Added Product: ${localProduct.id} (${localProduct.name}) | Remote: ${remoteProduct.productId} / ${remoteDiet.id}"
                        )
                        saveRemoteDietProductInDatabase(
                            localDiet[0].id,
                            remoteDiet.id,
                            localProduct.id,
                            remoteProduct.productId,
                            remoteProduct
                        )
                    }
                }

            }else {
                Log.i(
                    "SYNC",
                    "        - Last change APP: ${localDiet[0].id} (${remoteDiet.name}) | Remote: ${remoteDiet.id}"
                )
            }
            
        } else {

            val diet = Diet(
                remoteDiet.name,
                remoteDiet.description,
                remoteDiet.id,
                "",
                remoteDiet.archiveDate,
                remoteDiet.usePercentage
            )

            Log.i("SYNC", "        Added: ${remoteDiet.name} / RemoteID: ${remoteDiet.id} / Cant. Prod: ${remoteDiet.products.size}")
            val newDietId = mDietViewModel.insertSync(diet)
            diet.id = newDietId
            mLocalDiets?.add(diet)

            remoteDiet.products.forEach{ remoteProduct ->
                val localProduct = mLocalProducts?.firstOrNull {
                    it.remoteId == remoteProduct.productId
                }
                if (localProduct != null){
                    Log.i("SYNC", "        - Added Product: ${localProduct.id} (${localProduct.name}) | Remote: ${remoteProduct.productId} / ${remoteDiet.id}")
                    saveRemoteDietProductInDatabase(
                        newDietId,
                        remoteDiet.id,
                        localProduct.id,
                        remoteProduct.productId,
                        remoteProduct)

                }

            }
        }

    }
    private fun updateDietRemoteId(id: Long, remoteId: Long){
        mDietViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalDiet = mLocalDiets?.firstOrNull { localDiet ->
            localDiet.id == id
        }
        if (currentLocalDiet != null){
            val index = mLocalDiets?.indexOf(currentLocalDiet)
            index?.let { _index ->
                mLocalDiets!![_index].remoteId = remoteId
            }
        }
    }
    private fun updateProductDependencies(productAppId: Long, productRemoteId: Long){
        //Se actualizan las dependencias
        mLocalDietProducts?.forEach { dietProduct ->
            if (dietProduct.productId == productAppId){
                dietProduct.remoteProductId = productRemoteId
            }
        }

        mLocalRoundRunProgressLoad?.forEach { roundRunProgessLoad ->
            if (roundRunProgessLoad.productId == productAppId){
                roundRunProgessLoad.remoteProductId = productRemoteId
            }
        }


        /*
        val currentLocalDietProduct = mLocalDietProducts?.firstOrNull { _product ->
            _product.productId == productAppId
        }
        if (currentLocalDietProduct != null){
            val index = mLocalDietProducts?.indexOf(currentLocalDietProduct)
            index?.let { _index ->
                mLocalDietProducts!![_index].remoteProductId = productRemoteId
            }
        }*/
    }

    private fun updateDietDependencies(dietAppId: Long, dietRemoteId: Long){
        //Se actualizan las dependencias
        mLocalDietProducts?.forEach { dietProduct ->
            if (dietProduct.dietId == dietAppId){
                dietProduct.remoteDietId = dietRemoteId
            }
        }

        mLocalRoundRunProgressLoad?.forEach { roundRunProgessLoad ->
            if (roundRunProgessLoad.dietId == dietAppId){
                roundRunProgessLoad.remoteDietId = dietRemoteId
            }
        }
    }

    // Rounds
    // ---------------
    private suspend fun syncRounds() {
        Log.i("SYNC", "-----> Start Rounds")

        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate: List<Round> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalRounds?.let {
            itemsToAddOrUpdate = it.filter { diet ->
                diet.remoteId == 0L || diet.updatedDate.isNotEmpty()
            }
            origianlItemsToAddOrUpdate = itemsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando Rondas: ${mRemoteRounds?.size}")
        mRemoteRounds?.let {
            totalSize = it.size + itemsToAddOrUpdate.size
            it.forEach{ item ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbRounds.progress = percentageValue.roundToInt()
                mBinding.tvRoundsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteRoundInDatabase(item)
            }
        }

        mLocalRounds?.let {
            itemsToAddOrUpdate = it.filter { round ->
                round.remoteId == 0L || round.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - itemsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbEstablishments.progress = percentageValue.roundToInt()
                mBinding.tvEstablishmentsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando Rondas: ${itemsToAddOrUpdate.size}")

        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbRounds.progress = percentageValue.roundToInt()
            mBinding.tvRoundsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"

            val roundRemote = RoundRemote(
                item.name,
                item.description,
                item.remoteId,
                item.id,
                "",
                "",
                item.weight,
                item.usePercentage,
                item.customPercentage,
                item.remoteDietId,
                ArrayList()
            )

            val roundCorrals = mLocalRoundCorrals?.filter {
                it.roundId == item.id
            }

            roundCorrals?.forEach {

                //Busca el ID remoto del corral
                val localCorral = mLocalCorrals?.firstOrNull { localCorral ->
                    localCorral.id == it.corralId
                }

                //Busca el ID remoto de la ronda
                val localRound = mLocalRounds?.firstOrNull { localRound ->
                    localRound.id == it.roundId
                }

                if (localCorral != null && localRound != null){
                    val rcd = RoundCorralDetail(
                        it.corralId,
                        localCorral.remoteId,
                        "",
                        "",
                        it.roundId,
                        localRound.remoteId,
                        it.percentage,
                        it.weight,
                        it.order,
                        localCorral.animalQuantity)

                    roundRemote.corrals.add(rcd)
                }


            }

            saveLocalRoundInServer(roundRemote, (roundRemote.id == 0L))
        }
    }
    private fun roundViewModelRemoteObserver(){
        // Get observers
        mRoundViewModelRemote?.roundsResponse?.observe(viewLifecycleOwner) { remoteRound ->
            remoteRound?.let {
                mRemoteRounds = it
                checkSyncStatus()
            }
        }

        mRoundViewModelRemote?.roundsLoadingError?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                if (errorData) {
                    Log.e("Diets [dietsLoadingError]", "$errorData")
                }
            }
        }

        // Observers Add
        // -------------
        mRoundViewModelRemote?.addRoundsResponse?.observe(viewLifecycleOwner) { remoteRound ->
            remoteRound?.let {
                updateRoundRemoteId(it.appId, it.id)
            }
        }
        mRoundViewModelRemote?.addRoundErrorResponse?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                Log.e("Diets [addDietErrorResponse]", "$errorData")
            }
        }

        // Observers Update
        // -------------
        mRoundViewModelRemote?.updateRoundsResponse?.observe(viewLifecycleOwner) { remoteRound ->
            remoteRound?.let {
                mRoundViewModel.setUpdatedDate(it.appId, "")
            }
        }
    }

    private fun saveLocalRoundInServer(round: RoundRemote, isNew: Boolean){

        // Si no tiene remoteID significa que es un mixero que
        // fue generado desde la APP pero que nunca pudo ser sincronizado
        if (isNew) {
            Log.i(
                "SYNC",
                "        - Added: (${round.name}) | Desc: (${round.description})"
            )
            round.corrals.forEach {
                Log.i("SYNC",
                    "            - Corral in round: ${it.corralId} | ${it.remoteCorralId}")

            }
            mRoundViewModelRemote?.addRoundFromAPI(round)
        } else {
            Log.i(
                "SYNC",
                "        - Updated: (${round.name}) | Desc: (${round.description})"
            )
            round.corrals.forEach {
                Log.i("SYNC",
                    "            - Corral in round: ${it.corralId} | ${it.remoteCorralId}")

            }
            mRoundViewModelRemote?.updateRoundFromAPI(round)
        }
    }

    private fun saveRemoteRoundCorralInDatabase(localRoundId: Long, remoteRoundId: Long, localCorralId: Long, remoteCorralId: Long, remoteCorral: RoundCorralDetail){
        val roundCorral = RoundCorral(
            localRoundId,
            localCorralId,
            remoteRoundId,
            remoteCorralId,
            remoteCorral.order,
            remoteCorral.weight,
            remoteCorral.percentage,
            0,
            "",
            null)

        mRoundViewModel.insertRoundCorral(roundCorral)
        mLocalRoundCorrals?.add(roundCorral)
    }
    private suspend fun saveRemoteRoundInDatabase(remoteRound: RoundRemote){

        val localRound = mLocalRounds?.filter {
            it.remoteId == remoteRound.id
        }

        val localDiet = mLocalDiets?.firstOrNull {
            it.remoteId == remoteRound.dietId
        }

        if (localRound?.isNotEmpty() == true) {

            if (localRound[0].updatedDate.isNotEmpty()){
                Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteRound.updatedDate)}")
                Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(localRound[0].updatedDate)}")
                Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(localRound[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteRound.updatedDate)))}")
            }
            // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
            // o bien si la modificación es más reciente es de la WEB
            if (localRound[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(localRound[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteRound.updatedDate)))) {
                val round = Round(
                    remoteRound.name,
                    remoteRound.description,
                    remoteRound.id,
                    "",
                    remoteRound.archiveDate,
                    remoteRound.weight,
                    remoteRound.usePercentage,
                    remoteRound.customPercentage,
                    localDiet?.id ?: 0,
                    remoteRound.dietId,
                    localRound[0].id
                )

                Log.i("SYNC", "        Updated: ${localRound[0].id} (${remoteRound.name}) | Remote ${remoteRound.id} | Cant. Corrales: ${remoteRound.corrals.size}")
                mRoundViewModel.update(round)
                val index = mLocalRounds?.indexOf(localRound[0])
                mLocalRounds?.get(index!!)?.updatedDate  = ""

                updateRoundDependencies(localRound[0].id, remoteRound.id)

                mRoundViewModel.deleteCorralByRound(localRound[0].id)
                remoteRound.corrals.forEach{ remoteCorral ->
                    val localCorral = mLocalCorrals?.firstOrNull {
                        it.remoteId == remoteCorral.corralId
                    }

                    if (localCorral != null) {
                        Log.i(
                            "SYNC",
                            "        - Added Corral: ${localCorral.id} (${localCorral.name} | ${remoteCorral.percentage} | ${remoteCorral.weight}) | Remote: ${remoteCorral.corralId} / ${remoteRound.id}"
                        )
                        saveRemoteRoundCorralInDatabase(
                            localRound[0].id,
                            remoteRound.id,
                            localCorral.id,
                            remoteCorral.corralId,
                            remoteCorral
                        )
                    }
                }
            }else {
                Log.i(
                    "SYNC",
                    "        - Last change APP: ${localRound[0].id} (${remoteRound.name}) | Remote: ${remoteRound.id}"
                )
            }

        } else {

            val round = Round(
                remoteRound.name,
                remoteRound.description,
                remoteRound.id,
                "",
                "",
                remoteRound.weight,
                remoteRound.usePercentage,
                remoteRound.customPercentage,
                localDiet?.id ?: 0,
                remoteRound.dietId
            )

            Log.i("SYNC", "        Added: ${remoteRound.name} / RemoteID: ${remoteRound.id} / Diet: Local->${localDiet?.id} | Remote->${remoteRound.dietId} / Cant. Corrales: ${remoteRound.corrals.size}")
            val newRoundId = mRoundViewModel.insertSync(round)
            round.id = newRoundId
            mLocalRounds?.add(round)

            remoteRound.corrals.forEach{ remoteCorral ->
                val localCorral = mLocalCorrals?.firstOrNull {
                    it.remoteId == remoteCorral.corralId
                }
                if (localCorral != null){
                    Log.i("SYNC", "        - Added Corral: ${localCorral.id} (${localCorral.name} | ${remoteCorral.percentage}) | Remote: ${remoteCorral.corralId} / ${remoteRound.id}")
                    saveRemoteRoundCorralInDatabase(
                        newRoundId,
                        remoteRound.id,
                        localCorral.id,
                        remoteCorral.corralId,
                        remoteCorral)
                }

            }
        }

    }
    private fun updateRoundRemoteId(id: Long, remoteId: Long){
        mRoundViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalRound = mLocalRounds?.firstOrNull { localRound ->
            localRound.id == id
        }
        if (currentLocalRound != null){
            val index = mLocalRounds?.indexOf(currentLocalRound)
            index?.let { _index ->
                mLocalRounds!![_index].remoteId = remoteId
            }
        }
    }

    private fun updateRoundDependencies(roundAppId: Long, roundRemoteId: Long) {
        //Se actualizan las dependencias
        mLocalRoundCorrals?.forEach { roundCorral ->
            if (roundCorral.roundId == roundAppId) {
                //roundCorral.remoteRoundId = roundRemoteId
            }
        }

        mLocalRoundRun?.forEach { roundRun ->
            if (roundRun.roundId == roundAppId){
                roundRun.remoteRoundId = roundRemoteId
            }
        }
    }


    // Users
    // ---------------
    private suspend fun syncUsers() {
        Log.i("SYNC", "-----> Start Users")

        var totalSize = 0
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate : List<User> = ArrayList()
        var origianlItemsToAddOrUpdate = 0

        mLocalUsers?.let {
            itemsToAddOrUpdate = it.filter { user ->
                user.remoteId == 0L || user.updatedDate.isNotEmpty()
            }

            origianlItemsToAddOrUpdate = itemsToAddOrUpdate.size
        }

        Log.i("SYNC", "      Descargando Users: ${mRemoteUsers?.size}")
        mRemoteUsers?.let {
            totalSize = it.size + itemsToAddOrUpdate.size
            it.forEach{ item ->
                currentProcessedItem += 1
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbUsers.progress = percentageValue.roundToInt()
                mBinding.tvUsersPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
                saveRemoteUserInDatabase(item)
            }
        }

        mLocalUsers?.let {
            itemsToAddOrUpdate = it.filter { user ->
                user.remoteId == 0L || user.updatedDate.isNotEmpty()
            }

            if (totalSize > 0){
                currentProcessedItem += (origianlItemsToAddOrUpdate - itemsToAddOrUpdate.size)
                percentageValue = (currentProcessedItem)*100.0/totalSize
                mBinding.pbUsers.progress = percentageValue.roundToInt()
                mBinding.tvUsersPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            }
        }

        Log.i("SYNC", "      Cargando User: ${itemsToAddOrUpdate.size}")
        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbUsers.progress = percentageValue.roundToInt()
            mBinding.tvUsersPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"
            saveLocalUserInServer(item)
        }
    }
    private fun userViewModelRemoteObserver(){

        // Get observers
        mUserViewModelRemote?.usersResponse?.observe(viewLifecycleOwner) { remoteUsers ->
            remoteUsers?.let {
                mRemoteUsers = it
                checkSyncStatus()
            }
        }
        mUserViewModelRemote?.usersLoadingError?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                if (errorData) {
                    Log.e("Users [usersLoadingError]", "$errorData")
                }
            }
        }

        // Observers Add
        // -------------
        mUserViewModelRemote?.addUsersResponse?.observe(viewLifecycleOwner) { remoteUser ->
            remoteUser?.let {
                updateUserRemoteId(it.appId, it.id)
            }
        }
        mUserViewModelRemote?.addUserErrorResponse?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                Log.e("Users [addUserErrorResponse]", "$errorData")
            }
        }

        // Observers Update
        // -------------
        mUserViewModelRemote?.updateUsersResponse?.observe(viewLifecycleOwner) { remoteUser ->
            remoteUser?.let {
                mUserViewModel.setUpdatedDate(it.appId, "")
            }
        }
    }

    private fun saveLocalUserInServer(user: User){

        // Si no tiene remoteID significa que es un usero que
        // fue generado desde la APP pero que nunca pudo ser sincronizado
        if (user.remoteId == 0L) {
            Log.i(
                "SYNC",
                "        - Added: ${user.id} (${user.name}) | $user"
            )
            mUserViewModelRemote?.addUserFromAPI(user)
        }

        // Si no tiene updatedDate significa que es un usero que
        // fue actualizado desde la APP pero que no pudieron sincronizarse los cambios.
        if (user.updatedDate.isNotEmpty()){
            Log.i(
                "SYNC",
                "        - Updated: ${user.id} (${user.name})"
            )
            mUserViewModelRemote?.updateUserFromAPI(user)
        }

    }
    private suspend fun saveRemoteUserInDatabase(remoteUser: UserRemote){

        val localUser = mLocalUsers?.filter {
            it.remoteId == remoteUser.id
        }

        localUser?.let {  it ->
            if (it.isNotEmpty()) {

                if (it[0].updatedDate.isNotEmpty()){
                    Log.i("SYNC", "         Remote Date: ${Helper.getApiDateFromString(remoteUser.updatedDate)}")
                    Log.i("SYNC", "         App Date   : ${Helper.getAppDateFromString(it[0].updatedDate)}")
                    Log.i("SYNC", "         Update with remote: ${(Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteUser.updatedDate)))}")
                }
                // Se actualiza solo si no tiene modificaciones locales pendientes de sincronizar
                // o bien si la modificación es más reciente es de la WEB
                if (it[0].updatedDate.isEmpty() || (Helper.getAppDateFromString(it[0].updatedDate).isBefore(Helper.getApiDateFromString(remoteUser.updatedDate)))) {
                    val user = User(
                        remoteUser.username,
                        remoteUser.name,
                        remoteUser.lastname,
                        remoteUser.mail,
                        remoteUser.password,
                        remoteUser.id,
                        "",
                        "",
                        remoteUser.codeRole,
                        remoteUser.codeClient,
                        localUser[0].id
                    )

                    mUserViewModel.update(user)
                    val index = mLocalUsers?.indexOf(localUser[0])
                    mLocalUsers?.get(index!!)?.updatedDate  = ""
                    Log.i(
                        "SYNC",
                        "        - Updated: ${localUser[0].id} (${remoteUser.name}) | Remote: ${remoteUser.id}"
                    )
                } else {
                    Log.i(
                        "SYNC",
                        "        - Last change APP: ${it[0].id} (${remoteUser.name}) | Remote: ${remoteUser.id}"
                    )
                }

            } else {
                val user = User(
                    remoteUser.username,
                    remoteUser.name,
                    remoteUser.lastname,
                    remoteUser.mail,
                    remoteUser.password,
                    remoteUser.id,
                    "",
                    "",
                    remoteUser.codeRole,
                    remoteUser.codeClient
                )

                val userId = mUserViewModel.insertSync(user)
                user.id = userId
                mLocalUsers?.add(user)
                Log.i(
                    "SYNC",
                    "        - Added: ${user.id} (${remoteUser.name}) | Remote: ${remoteUser.id}"
                )
            }
        }

    }
    private fun updateUserRemoteId(id: Long, remoteId: Long){
        mUserViewModel.setUpdatedRemoteId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalUser = mLocalUsers?.first { _user ->
            _user.id == id
        }
        if (currentLocalUser != null){
            val index = mLocalUsers?.indexOf(currentLocalUser)
            index?.let { _index ->
                mLocalUsers!![_index].remoteId = remoteId
            }
        }
    }


    // Round Run
    // ---------------
    private fun syncRoundRun() {
        Log.i("SYNC", "-----> Start RoundRun")

        /* RoundRun */
        val totalSize: Int
        var currentProcessedItem = 0
        var percentageValue: Double
        var itemsToAddOrUpdate : List<RoundRun> = ArrayList()

        // Solo tenemos en cuenta las ejecuciones que están finalizadas y aún no se han sicronizado
        // Para determinar si se sincrinizó o no nos fijamos en la fecha de fin
        mLocalRoundRun?.let {
            itemsToAddOrUpdate = it.filter { report ->
                report.remoteId == 0L && report.endDate.isNotEmpty() //TODO
            }
        }

        Log.i("SYNC", "      Descargando RoundRun: NO APLICA")


        Log.i("SYNC", "      Cargando Report: ${itemsToAddOrUpdate.size}")
        totalSize = itemsToAddOrUpdate.size
        itemsToAddOrUpdate.forEach{ item ->
            currentProcessedItem += 1
            percentageValue = (currentProcessedItem)*100.0/totalSize
            mBinding.pbReports.progress = percentageValue.roundToInt()
            mBinding.tvReportsPercentage.text = Helper.getNumberWithDecimals(percentageValue, 2) + "% ($currentProcessedItem / $totalSize)"

            val roundRunLoadProgress = mLocalRoundRunProgressLoad?.filter { roundRunProgressLoad ->
                roundRunProgressLoad.roundRunId == item.id
            }?.toMutableList() ?: ArrayList()

            val roundRunDownloadProgress = mLocalRoundRunProgressDownload?.filter {  roundRunProgressDownload ->
                roundRunProgressDownload.roundRunId == item.id
            }?.toMutableList() ?: ArrayList()

            saveLocalRoundRunInServer(item, roundRunLoadProgress, roundRunDownloadProgress)
        }
    }

    private fun roundRunViewModelRemoteObserver(){

        // Observers Add
        // -------------
        mRoundRunViewModelRemote?.addRoundsRunResponse?.observe(viewLifecycleOwner) { remoteReport ->
            remoteReport?.let {
                updateRoundRunRemoteId(it.appId, it.id)
            }
        }
        mRoundRunViewModelRemote?.addRoundRunErrorResponse?.observe(viewLifecycleOwner) { errorData ->
            errorData?.let {
                Log.e("Reports [addRoundRunErrorResponse]", "$errorData")
            }
        }
    }

    private fun saveLocalRoundRunInServer(
        roundRun: RoundRun,
        roundRunProgressLoad: List<RoundRunProgressLoad>,
        roundRunProgressDownload: List<RoundRunProgressDownload>){

        // Si no tiene remoteID significa que es un reporto que
        // fue generado desde la APP pero que nunca pudo ser sincronizado
        if (roundRun.remoteId == 0L) { //TODO
            Log.i(
                "SYNC",
                "        - Added: ${roundRun.id} (${roundRun.startDate})"
            )
            val roundRunBody = RoundRunBody(
                roundRun,
                roundRunProgressLoad,
                roundRunProgressDownload
            )
            mRoundViewModelRemote?.addRoundRunFromAPI(roundRunBody)
        }
    }
    private fun updateRoundRunRemoteId(id: Long, remoteId: Long){
        mRoundViewModel.setUpdatedRemoteRoundRunId(id, remoteId)

        // Actualizamos el remoteID en el objeto local
        val currentLocalRoundRun = mLocalRoundRun?.first { roundRound ->
            roundRound.id == id
        }

        if (currentLocalRoundRun != null) {
            val index = mLocalRoundRun?.indexOf(currentLocalRoundRun)
            index?.let { _index ->
                mLocalRoundRun!![_index].remoteId = remoteId
            }
        }
    }

    override fun onDestroyView() {
     super.onDestroyView()
     Log.i("SYNC", "----> onDestroyView Se limpia")
     cleanObservers()
    }



    private fun cleanObservers(){
     mEstablishmentViewModelRemote?.establishmentsResponse?.value = null
     mEstablishmentViewModelRemote?.establishmentsLoadingError?.value = null
     mEstablishmentViewModelRemote?.loadEstablishment?.value = null
     mEstablishmentViewModelRemote?.addEstablishmentsResponse?.value = null
     mEstablishmentViewModelRemote?.addEstablishmentErrorResponse?.value = null
     mEstablishmentViewModelRemote?.addEstablishmentsLoad?.value = null
     mEstablishmentViewModelRemote?.updateEstablishmentsResponse?.value = null
     mEstablishmentViewModelRemote?.updateEstablishmentsErrorResponse?.value = null
     mEstablishmentViewModelRemote?.updateEstablishmentsLoad?.value = null
     mEstablishmentViewModelRemote = null

     mCorralViewModelRemote?.corralsResponse?.value = null
     mCorralViewModelRemote?.corralsLoadingError?.value = null
     mCorralViewModelRemote?.loadCorral?.value = null
     mCorralViewModelRemote?.addCorralsResponse?.value = null
     mCorralViewModelRemote?.addCorralErrorResponse?.value = null
     mCorralViewModelRemote?.addCorralsLoad?.value = null
     mCorralViewModelRemote?.updateCorralsResponse?.value = null
     mCorralViewModelRemote?.updateCorralsErrorResponse?.value = null
     mCorralViewModelRemote?.updateCorralsLoad?.value = null
     mCorralViewModelRemote = null

     mProductViewModelRemote?.productsResponse?.value = null
     mProductViewModelRemote?.productsLoadingError?.value = null
     mProductViewModelRemote?.loadProduct?.value = null
     mProductViewModelRemote?.addProductsResponse?.value = null
     mProductViewModelRemote?.addProductErrorResponse?.value = null
     mProductViewModelRemote?.addProductsLoad?.value = null
     mProductViewModelRemote?.updateProductsResponse?.value = null
     mProductViewModelRemote?.updateProductsErrorResponse?.value = null
     mProductViewModelRemote?.updateProductsLoad?.value = null
     mProductViewModelRemote = null

     mMixerViewModelRemote?.mixersResponse?.value = null
     mMixerViewModelRemote?.mixersLoadingError?.value = null
     mMixerViewModelRemote?.loadMixer?.value = null
     mMixerViewModelRemote?.addMixersResponse?.value = null
     mMixerViewModelRemote?.addMixerErrorResponse?.value = null
     mMixerViewModelRemote?.addMixersLoad?.value = null
     mMixerViewModelRemote?.updateMixersResponse?.value = null
     mMixerViewModelRemote?.updateMixersErrorResponse?.value = null
     mMixerViewModelRemote?.updateMixersLoad?.value = null
     mMixerViewModelRemote = null

     mDietViewModelRemote?.dietsResponse?.value = null
     mDietViewModelRemote?.dietsLoadingError?.value = null
     mDietViewModelRemote?.loadDiet?.value = null
     mDietViewModelRemote?.addDietsResponse?.value = null
     mDietViewModelRemote?.addDietErrorResponse?.value = null
     mDietViewModelRemote?.addDietsLoad?.value = null
     mDietViewModelRemote?.updateDietsResponse?.value = null
     mDietViewModelRemote?.updateDietsErrorResponse?.value = null
     mDietViewModelRemote?.updateDietsLoad?.value = null

     mUserViewModelRemote?.usersResponse?.value = null
     mUserViewModelRemote?.usersLoadingError?.value = null
     mUserViewModelRemote?.loadUser?.value = null
     mUserViewModelRemote?.addUsersResponse?.value = null
     mUserViewModelRemote?.addUserErrorResponse?.value = null
     mUserViewModelRemote?.addUsersLoad?.value = null
     mUserViewModelRemote?.updateUsersResponse?.value = null
     mUserViewModelRemote?.updateUsersErrorResponse?.value = null
     mUserViewModelRemote?.updateUsersLoad?.value = null

     mEstablishmentViewModelRemote = null
     mCorralViewModelRemote = null
     mProductViewModelRemote = null
     mMixerViewModelRemote = null
     mDietViewModelRemote = null
     mUserViewModelRemote = null

     mLocalEstablishments = null
     mLocalCorrals = null
     mLocalProducts = null
     mLocalMixers = null
     mLocalDiets = null
     mLocalUsers = null
     mLocalRoundRun = null

     mRemoteCorrals = null
     mRemoteEstablishments = null
     mRemoteMixers = null
     mRemoteProducts = null
     mRemoteDiets = null
     mRemoteUsers = null
        mRemoteRounds = null
    }



}