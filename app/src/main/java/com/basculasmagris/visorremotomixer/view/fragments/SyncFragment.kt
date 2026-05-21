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
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentSyncBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.TabletRemote
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.ApiError
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerData
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserRemoteViewModel
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

    private var liveData: MediatorLiveData<MergedLocalData>? = null
    private var mLocalTabletMixers: MutableList<TabletMixer>? = null
    
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
        getLocalData()
    }

    private fun getLocalData() {
        liveData = fetchLocalData()

        liveData?.observe(viewLifecycleOwner) { data ->
            when (data) {
                is TabletMixerData -> {
                    mLocalTabletMixers = data.tabletMixers
                    liveData?.removeObservers(viewLifecycleOwner)
                    liveData = null
                }

                else -> {}
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
        
        mRemoteTablets?.forEach {tabletRemote ->
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
                Log.i(TAG,"Tablet editada $tabletToUpdate" )
                mTabletMixerViewModel.update(tabletToUpdate)
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
                Log.i(TAG,"Tablet insertada $tabletToInsert" )
            }
        }
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

        // Users
        val totalUsers = (mRemoteUsers?.size ?: 0)
        mBinding.pbUsers.progress = 0
        mBinding.tvUsersPercentage.text =
            Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalUsers)"

        // Users
        val totalTablets = (mRemoteUsers?.size ?: 0)
        mBinding.pbUsers.progress = 0
        mBinding.tvUsersPercentage.text =
            Helper.getNumberWithDecimals(0.0, 2) + "% (0 / $totalTablets)"
    }

}