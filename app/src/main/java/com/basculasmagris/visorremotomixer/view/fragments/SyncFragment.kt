package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentSyncBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.TabletRemote
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.ApiError
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerData
import com.basculasmagris.visorremotomixer.view.activities.UserData
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.CancellationException

class SyncFragment : Fragment() {

    private lateinit var mBinding: FragmentSyncBinding
    private val TAG = "DEBSync"

    private val mTabletMixerViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerVRApplication).tabletMixerRepository)
    }

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as SpiMixerVRApplication).userRepository)
    }

    private var liveData: MediatorLiveData<MergedLocalData>? = null
    private var mLocalTabletMixers: MutableList<TabletMixer>? = null
    private var mLocalUsers: MutableList<User>? = null

    private var mUserViewModelRemote: UserRemoteViewModel? = null
    private var mRemoteUsers: MutableList<UserRemote>? = null

    private var mTabletViewModelRemote: TabletRemoteViewModel? = null
    private var mRemoteTablets: MutableList<TabletRemote>? = null
    var currentUser: UserRemote? = null
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


        if(isAdded){
            (requireActivity() as MainActivity).clearbShowDevice()
        }
        mBinding.llSyncNoInternet.visibility = View.INVISIBLE
        currentUser = Helper.getCurrentUser(requireContext())
        mBinding.llRounds.visibility = View.INVISIBLE

        mBinding.btnSync.setOnClickListener {
            runSync()
        }

        mBinding.btnBack.setOnClickListener {
            if (isAdded) findNavController().popBackStack(R.id.nav_home, false)
        }

        getLocalData()
    }

    private fun getLocalData() {
        if (liveData != null) return

        liveData = fetchLocalData()

        liveData?.observe(viewLifecycleOwner) { data ->
            when (data) {
                is TabletMixerData -> mLocalTabletMixers = data.tabletMixers
                is UserData -> mLocalUsers = data.users
                else -> {}
            }

            if (mLocalTabletMixers != null && mLocalUsers != null) {
                liveData?.removeObservers(viewLifecycleOwner)
                liveData = null
            }
        }
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()

        liveDataMerger.addSource(mTabletMixerViewModel.allTabletMixerList) {
            if (it != null) {
                liveDataMerger.value = TabletMixerData(it)
            }
        }

        liveDataMerger.addSource(mUserViewModel.allUserList) {
            if (it != null) {
                liveDataMerger.value = UserData(it)
            }
        }

        return liveDataMerger
    }

    private fun runSync(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                runSync_SUSPEND()

            } catch (e: CancellationException) {
                // La corrutina fue cancelada porque saliste del Fragment.
                // No es un error real.
                throw e

            } catch (t: Throwable) {
                Log.e(TAG, "setupSync_SUSPEND error", t)

                val context = context ?: return@launch

                val apiError = parseApiError(t)

                val messageToShow = when (apiError?.code) {
                    "DEVICE_NOT_REGISTERED" -> "Esta tablet no está registrada"
                    "DEVICE_NOT_SUBSCRIBED" -> "Esta tablet no tiene suscripción activa"
                    "DEVICE_SERIAL_REQUIRED" -> "Falta el número de serie de la tablet"
                    "INVALID_DEVICE_SERIAL" -> "El número de serie de la tablet es inválido"
                    else -> apiError?.message ?: context.getString(R.string.error_descargando_datos_servidor)
                }

                Toast.makeText(
                    context,
                    messageToShow,
                    Toast.LENGTH_LONG
                ).show()
            } finally {

            }
        }
    }

    private suspend fun runSync_SUSPEND() = kotlinx.coroutines.coroutineScope {
        Log.v(TAG, "runSync_SUSPEND")

        refreshLocalDataBeforeSync()

        val mainActivity = (activity as? MainActivity) ?: return@coroutineScope

        val userVm          = ViewModelProvider(mainActivity)[UserRemoteViewModel::class.java]
        val tabletVm    = ViewModelProvider(mainActivity)[TabletRemoteViewModel::class.java]

        // (Opcional) guardar en properties
        mUserViewModelRemote = userVm
        mTabletViewModelRemote = tabletVm

        // GETs en paralelo (fail fast)
        Log.i(TAG,"tabletAsync ${currentUser?.codeClient}")
        val tabletAsync         = async { tabletVm.getTabletFromAPI_SUSPEND(currentUser?.codeClient?:"") }
        Log.i(TAG,"userAsync")
        val usersAsync          = async { userVm.getUsersFromAPI_SUSPEND() }

        // Await
        mRemoteUsers          = usersAsync.await()
        mRemoteTablets          = tabletAsync.await()

        initSyncUiAfterRemoteLoaded()

        Log.i(TAG,"users: $mRemoteUsers ")
        Log.i(TAG,"tablets: $mRemoteTablets ")
        
        mRemoteTablets?.forEachIndexed() {index,tabletRemote ->
            val tabletMixer = mLocalTabletMixers?.firstOrNull{
                it.serial == tabletRemote.serial
            }
            if(tabletMixer != null){
                val tabletToUpdate = TabletMixer(
                    name = tabletRemote.name,
                    mixerName = tabletMixer.mixerName,
                    mac = tabletMixer.mac,
                    serial = tabletRemote.serial?:tabletMixer.serial,
                    btName = tabletRemote.bluetoothName?:tabletMixer.btName,
                    updatedDate = Helper.getCurrentDateTime(),
                    id = tabletMixer.id
                )
                mTabletMixerViewModel.update(tabletToUpdate)

                mLocalTabletMixers = mLocalTabletMixers?.map {
                    if (it.id == tabletToUpdate.id) tabletToUpdate else it
                }?.toMutableList()
            }else{
                val tabletToInsert = TabletMixer(
                    name = tabletRemote.name,
                    mixerName = "",
                    mac = "",
                    serial = tabletRemote.serial?:"",
                    btName = tabletRemote.bluetoothName?:"",
                    updatedDate = Helper.getCurrentDateTime()
                )
                mTabletMixerViewModel.insert(tabletToInsert)
                mLocalTabletMixers?.add(tabletToInsert)
            }
            updateTabletsProgress(index + 1, mRemoteTablets?.size ?: 0)
        }

        Log.i(TAG,"mLocalUsers: $mLocalUsers")
        mRemoteUsers?.forEachIndexed() {index, userRemote ->

            val userLocal = mLocalUsers?.firstOrNull { userLocal ->
                userLocal.remoteId == userRemote.id ||
                        userLocal.username == userRemote.username
            }

            if (userLocal != null) {
                val userToUpdate = User(
                    username = userRemote.username,
                    name = userRemote.name,
                    lastname = userRemote.lastname,
                    mail = userRemote.mail,
                    password = userRemote.password,
                    remoteId = userRemote.id,
                    updatedDate = Helper.getCurrentDateTime(),
                    archiveDate = userLocal.archiveDate,
                    codeRole = userRemote.codeRole,
                    codeClient = userRemote.codeClient,
                    id = userLocal.id
                )
                mUserViewModel.update(userToUpdate)
                mLocalUsers = mLocalUsers?.map {
                    if (it.id == userToUpdate.id) userToUpdate else it
                }?.toMutableList()
            } else {
                val userToInsert = User(
                    username = userRemote.username,
                    name = userRemote.name,
                    lastname = userRemote.lastname,
                    mail = userRemote.mail,
                    password = userRemote.password,
                    remoteId = userRemote.id,
                    updatedDate = Helper.getCurrentDateTime(),
                    archiveDate = null,
                    codeRole = userRemote.codeRole,
                    codeClient = userRemote.codeClient
                )
                mUserViewModel.insertSync(userToInsert)
                mLocalUsers?.add(userToInsert)
            }
            updateUsersProgress(index + 1, mRemoteUsers?.size ?: 0)
        }

        Log.i(TAG,"usuarios actualizados")
    }


    fun parseApiError(t: Throwable): ApiError? {
        return try {
            if (t is HttpException) {
                val errorBody = t.response()?.errorBody()?.string()
                if (!errorBody.isNullOrEmpty()) {
                    Gson().fromJson(errorBody, ApiError::class.java)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }



    private fun initSyncUiAfterRemoteLoaded() {
        val totalUsers = mRemoteUsers?.size ?: 0
        mBinding.pbUsers.progress = 0
        mBinding.tvUsersPercentage.text = "0% (0 / $totalUsers)"

        val totalTablets = mRemoteTablets?.size ?: 0
        mBinding.pbTablet.progress = 0
        mBinding.tvTabletPercentage.text = "0% (0 / $totalTablets)"
    }

    private fun updateUsersProgress(current: Int, total: Int) {
        val progress = if (total == 0) 100 else (current * 100) / total
        mBinding.pbUsers.progress = progress
        mBinding.tvUsersPercentage.text = "$progress% ($current / $total)"
    }

    private fun updateTabletsProgress(current: Int, total: Int) {
        val progress = if (total == 0) 100 else (current * 100) / total
        mBinding.pbTablet.progress = progress
        mBinding.tvTabletPercentage.text = "$progress% ($current / $total)"
    }

    private suspend fun refreshLocalDataBeforeSync() {
        kotlinx.coroutines.suspendCancellableCoroutine<Unit> { cont ->

            val localLiveData = fetchLocalData()

            localLiveData.observe(viewLifecycleOwner) { data ->
                when (data) {
                    is TabletMixerData -> mLocalTabletMixers = data.tabletMixers
                    is UserData -> mLocalUsers = data.users
                    else -> {}
                }

                if (mLocalTabletMixers != null && mLocalUsers != null) {
                    localLiveData.removeObservers(viewLifecycleOwner)

                    if (cont.isActive) {
                        cont.resume(Unit) {}
                    }
                }
            }
        }
    }
}