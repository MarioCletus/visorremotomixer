package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentRoundListBinding
import com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.MinRound
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.RoundLocalData
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModelFactory


class RoundListFragment : Fragment() {

    private lateinit var mBinding: FragmentRoundListBinding
    private val TAG = "DEBRLF"

    private var bGoToRound: Boolean = true
    private var bBlockButton = false

    private val mRoundLocalViewModel: RoundLocalViewModel by viewModels {
        RoundLocalViewModelFactory((requireActivity().application as SpiMixerApplication).roundLocalRepository)
    }
    private var mLocalRoundsLocal: List<RoundLocal>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentRoundListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG,"onViewCreated")
        getLocalData()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_round_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_round)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvRoundsList.adapter as RoundRunAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvRoundsList.adapter as RoundRunAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_refresh_data ->{
                        refreshData()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        refreshData()
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    private fun refreshData() {
        if(!bBlockButton){
            bBlockButton = true
            (requireActivity() as MainActivity).requestListOfRounds()
            val handler = Handler(Looper.getMainLooper())
            val action = Runnable {
                getLocalRound()
                bBlockButton = false
            }
            handler.postDelayed(action, 500)
        }

    }



    private fun fetchLocalData(tabletMixer: TabletMixer): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mRoundLocalViewModel.allRoundLocalListByMac(tabletMixer.mac)) {
            if (it != null) {
                liveDataMerger.value = RoundLocalData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(){
        // Sync local data
        (requireActivity() as MainActivity).selectedTabletMixerInActivity?.let {
            val liveData = fetchLocalData(it)
            liveData.observe(viewLifecycleOwner, object : Observer<MergedLocalData> {
                override fun onChanged(it: MergedLocalData?) {
                    when (it) {
                        is RoundLocalData -> mLocalRoundsLocal = it.roundsLocal
                        else -> {}
                    }

                    if (mLocalRoundsLocal != null) {
                        liveData.removeObserver(this)
                        (requireActivity() as MainActivity).listOfMedRoundsRun.clear()
                        mLocalRoundsLocal?.let {roundsLocal->
                            roundsLocal.forEach {roundLocal->
                                val minRoundRun   = MinRound (
                                    name = roundLocal.name,
                                    description = roundLocal.description,
                                    remoteId = roundLocal.remoteId,
                                    id = roundLocal.id
                                )
                                val medRoundRunRun = MedRoundRunDetail(
                                    round = minRoundRun,
                                    startDate = roundLocal.startDate,
                                    endDate = roundLocal.endDate,
                                    state = roundLocal.state
                                )
                                (requireActivity() as MainActivity).listOfMedRoundsRun.add(medRoundRunRun)
                            }
                        }

                        getLocalRound()
                    }
                }
            })
            return
        }
        getLocalRound()
    }


    private fun getLocalRound(){

        mBinding.rvRoundsList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val roundAdapter =  RoundRunAdapter(this@RoundListFragment)
        mBinding.rvRoundsList.adapter = roundAdapter
        val mLocalRounds = (requireActivity() as MainActivity).listOfMedRoundsRun
        Log.i(TAG,"mLocalRounds $mLocalRounds")
        mLocalRounds.let{
            if (it.isEmpty()){
                mBinding.rvRoundsList.visibility = View.GONE
                mBinding.tvNoData.visibility = View.VISIBLE
            } else {
                Log.i("SYNC", "Se actualiza UI Roundas: ${it.size} ")
                mBinding.rvRoundsList.visibility = View.VISIBLE
                mBinding.tvNoData.visibility = View.GONE
                roundAdapter.roundList(it)
            }
        }

    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        init {
            Log.i(TAG,"create mBluetoothListener")
        }

        override fun onDiscoveryStarted() {
            Log.i(TAG, "[Sync] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[Sync] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[Sync] ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            var name = ""
            var address = ""
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (ActivityCompat.checkSelfPermission(
                        requireActivity() as MainActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    name = device?.name.toString()
                    address = device?.address.toString()
                }
            }else{
                name = device?.name.toString()
                address = device?.address.toString()
            }
            Log.i(TAG, "onDeviceConnected $name $address")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val messageStr = String(message,0, message.size)
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","command $command")
            Log.i("message", String(message))
            when (command){

                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS")
                    (requireActivity() as MainActivity).refreshRounds(message)
                }

                Constants.CMD_WEIGHT_LOAD->{
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }
                }
                Constants.CMD_WEIGHT_LOAD_FREE->{
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }
                }
                Constants.CMD_WEIGHT_DWNL->{
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }
                }
                Constants.CMD_WEIGHT_DWNL_FREE->{
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }
                }
                Constants.CMD_WEIGHT_CONFIG->{
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }
                }
                Constants.CMD_WEIGHT_RESUME->{
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }
                }
                else->{
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
        }

        override fun onMessageSent(device: BluetoothDevice?, message: String?) {
        }

        override fun onCommandSent(device: BluetoothDevice?, command: ByteArray?) {
        }

        override fun onError(message: String?) {
        }

        override fun onDeviceDisconnected() {
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
        }
    }
    override fun onDestroyView() {
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onDestroyView()
    }

    fun sendGoToRound(id: Long) {
        (requireActivity() as MainActivity).sendGoToRound(id)
    }
}