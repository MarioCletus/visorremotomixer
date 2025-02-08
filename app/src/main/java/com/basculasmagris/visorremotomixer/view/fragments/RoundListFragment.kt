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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentRoundListBinding
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.gson.Gson


class RoundListFragment : Fragment() {

    private lateinit var mBinding: FragmentRoundListBinding
    private val TAG = "DEBRLF"

    private var bBlockButton = false

    private var bGoToRound = false
    private var fragmentRunning = false

    private val REFRESH_VIEW_TIME = 20
    private val TIME_REFRESH_ROUNDS = 2000L
    private var countMsg: Int = REFRESH_VIEW_TIME

    private var menu:Menu? = null
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null

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
                this@RoundListFragment.menu = menu
                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_round)
                val searchView = search.actionView as SearchView
                if(isAdded){
                    (requireActivity() as MainActivity).clearbShowDevice()
                }
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
                Log.i(TAG,"Menu pressed ${menuItem.itemId}\n$menuItem")
                return when (menuItem.itemId) {
                    R.id.menu_refresh_data ->{
                        refreshData()
                        return true
                    }
                    R.id.menu_selected_remote_tablet -> {
                        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
                            bd.address == selectedTabletMixerInFragment?.mac
                        }
                        Log.v(TAG,"Force connection $deviceBluetooth")
                        deviceBluetooth?.let {
                            val name = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                                if (ActivityCompat.checkSelfPermission(
                                        requireActivity() as MainActivity,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    if (it.name == null) "" else it.name
                                }else{
                                    ""
                                }
                            }else{
                                if (it.name == null) "" else it.name
                            }
                            Log.v(TAG,"Force connection $name")
                            (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                            (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                            (requireActivity() as MainActivity).showCustomProgressDialog()
                        }
                        return true
                    }


                    R.id.bluetooth_balance -> {
                        (requireActivity() as MainActivity).sendReconnectBalance()
                        (requireActivity() as MainActivity).showCustomProgressDialog()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    private fun refreshData() {
        if(!bBlockButton){
            bBlockButton = true
            (requireActivity() as MainActivity).sendRequestListOfRounds()
            val handler = Handler(Looper.getMainLooper())
            val action = Runnable {

                bBlockButton = false
            }
            handler.postDelayed(action, TIME_REFRESH_ROUNDS)
        }

    }


    private fun getLocalData(){
        // Sync local data
        Log.i(TAG,"getLocalData")
        getLocalRound()
        refreshData()
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
                Log.i(TAG, "Se actualiza UI Roundas: ${it.size} ")
                mBinding.rvRoundsList.visibility = View.VISIBLE
                mBinding.tvNoData.visibility = View.GONE
                Log.i(TAG,"roundList $it")
                Log.i("MEP","roundAdapter.roundList(it) $it")
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
            if(!fragmentRunning){
                return
            }
            val messageStr = String(message,0, message.size)
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","RLF command $command")
            Log.i("message", "RLF message ${String(message)}")
            when (command){

                Constants.CMD_ROUNDDETAIL->{
                    val convertZip = ConvertZip()
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i("showCommand","CMD_ROUNDDETAIL ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                    try{
                        val jsonString : String = convertZip.decompressText(byteArrayUtil)
                        Log.i("Json","jsonString * ${jsonString}")
                        if(jsonString.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : MinRoundRunDetail = gson.fromJson(jsonString,  MinRoundRunDetail::class.java)
                            (requireActivity() as MainActivity).updateRoundDetail(roundRunDetail)
                            (mBinding.rvRoundsList.adapter as RoundRunAdapter).notifyDataSetChanged()
                            }
                    }catch (e: NumberFormatException){
                        Log.i("showCommand","CMD_ROUNDDETAIL NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i("showCommand","CMD_ROUNDDETAIL Exception $e")
                    }
                }


                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS RLF")
                    Log.i("MEP","CMD_ROUNDS")
                    if(isAdded)
                        (requireActivity() as MainActivity).refreshRounds(message)
                    Log.i("MEP","getLocalRound() 3 ")
                    getLocalRound()
                }

                Constants.CMD_WEIGHT->{
                    Log.i("CMD_WEIGHT","RoundListFragment CMD_WEIGHT $message")
                    sincroRound(false)
                    refreshData()
                    refreshWeight(message)
                }

                Constants.CMD_WEIGHT_LOAD->{
                    Log.i("CMD_WEIGHT","CMD_WEIGHT_LOAD $message")
                    if(countMsg++ > REFRESH_VIEW_TIME){
                        refreshRound()
                    }
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }else{
                        sincroRound(true)
                    }
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_LOAD_FREE->{
                    Log.i("CMD_WEIGHT","CMD_WEIGHT_LOAD_FREE $message")
                    if(countMsg++ > REFRESH_VIEW_TIME){
                        refreshRound()
                    }
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }else{
                        sincroRound(true)
                    }
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_DWNL->{
                    Log.i("CMD_WEIGHT","CMD_WEIGHT_DWNL $message")
                    if(countMsg++ > REFRESH_VIEW_TIME){
                        refreshRound()
                    }
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }else{
                        sincroRound(true)
                    }
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_DWNL_FREE->{
                    if(countMsg++ > REFRESH_VIEW_TIME){
                        refreshRound()
                    }
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }else{
                        sincroRound(true)
                    }
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_CONFIG->{
                    Log.i("CMD_WEIGHT","CMD_WEIGHT_CONFIG")
                    if(countMsg++ > REFRESH_VIEW_TIME){
                        refreshRound()
                    }
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }else{
                        sincroRound(true)
                    }
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_RESUME->{
                    if(countMsg++ > REFRESH_VIEW_TIME){
                        refreshRound()
                    }
                    if(bGoToRound){
                        bGoToRound = false
                        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), this)
                        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRemoteMixerFragment())
                    }else{
                        sincroRound(true)
                    }
                    refreshWeight(message)
                }

                Constants.CMD_NTA ->{
                    Log.i("showCommand","CMD_NTA")
                    if(isAdded){
                        (requireActivity() as MainActivity).alertDialog(getString(R.string.atencion),getString(R.string.no_disponible))
                    }
                }

                else->{
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            (requireActivity() as MainActivity).beaconReceibed()
        }

        override fun onMessageSent(device: BluetoothDevice?, message: String?) {
        }

        override fun onCommandSent(device: BluetoothDevice?, command: ByteArray?) {
        }

        override fun onError(message: String?) {
            Log.i(TAG,"onError")
            if(isAdded)
                (requireActivity() as MainActivity).changeStatusDisconnected()
        }

        override fun onDeviceDisconnected() {
            Log.i(TAG,"onDeviceDisconnected")
            if(isAdded)
                (requireActivity() as MainActivity).changeStatusDisconnected()
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[RoundListFragment]onBondedDevices ${device?.size} \n$device")
            (requireActivity() as MainActivity).knowDevices = device
            selectedTabletMixerInFragment?.let {
                selectTablet(it)
            }
        }
    }

    private fun sincroRound(b: Boolean) {
        val adapter = mBinding.rvRoundsList.adapter
        if (adapter is RoundRunAdapter) {
            adapter.sincroRound(b)
        }
    }

    private fun refreshWeight(message:ByteArray) {
        val weight = String(message, 4, 8).toLong()
        val sign = String(message, 3, 1)
//        mBinding.tvPeso.setText("${sign}${weight}")
        var bConnected = true
        if(sign.contains("N")){
            Log.i("message","RMF bConnected false sign +")
            bConnected = false
        }
        if(sign.contains("n")){
            Log.i("message","RMF bConnected false sign -")
            bConnected = false
        }
        if(bConnected && isAdded)
            (requireActivity() as MainActivity).weightReceibed()
    }

    private fun refreshRound() {
        countMsg = 0
        Log.i(TAG,"refreshRound")
        (requireActivity() as MainActivity).sendRequestRoundRunDetail()
    }

    override fun onDestroyView() {
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onDestroyView()
    }

    fun sendGoToRound(id: Long) {
        if(bGoToRound){
            (requireActivity() as MainActivity).showCustomProgressDialog("Alerta","No se pudo iniciar ronda",R.layout.dialog_custom_progress_iniciar)
        }else{
            (requireActivity() as MainActivity).showCustomProgressDialog("Alerta","No se puede ver ronda",R.layout.dialog_custom_progress_ver)

        }
        Log.i("seguimiento","sendGoToRound ${id}")
        bGoToRound = true
        (requireActivity() as MainActivity).sendGoToRound(id)
    }

    override fun onResume() {
        fragmentRunning = true
        super.onResume()
    }

    fun connectTable(tabletMixer: TabletMixer){
        Log.i(TAG, "Cantidad: ${(requireActivity() as MainActivity).knowDevices?.size}")
        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
            bd.address == tabletMixer.mac
        }

        if (deviceBluetooth != null){
            Log.i(TAG,"Try to connect with ${deviceBluetooth}")
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).connectDevice(deviceBluetooth)
        } else {
            Toast.makeText(requireActivity(), "No se pudo conectar", Toast.LENGTH_SHORT).show()
        }
    }

    fun setTabletMixer(tabletMixerIn: TabletMixer) {
        tabletMixerIn.let { tabletMixer ->
            menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + tabletMixer.name
            selectedTabletMixerInFragment = tabletMixer
            Log.i(
                TAG,
                "setTabletMixer $tabletMixer  \nmService ${(requireActivity() as MainActivity).mService}"
            )
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
        }
    }

    fun selectTablet(tabletMixer :TabletMixer){
        val activity = requireActivity() as MainActivity
        Log.i(TAG,"tabletMixerInFragment ${tabletMixer.name} ${tabletMixer.mac}")
        activity.knowDevices?.forEach{
            Log.i(TAG,"bluetoothKnowed ${it.name} ${it.address}")
            if(tabletMixer.mac.equals(it.address)){
                var name = ""
                var address = ""
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED) {
                        name = it.name
                        address = it.address
                    }
                }else{
                    name = it.name
                    address = it.address
                }
                Log.i(TAG,"Se seleccionó $name : $address")
                if(tabletMixerBluetoothDevice != it){
                    connectTable(tabletMixer)
                }
                tabletMixerBluetoothDevice = it
            }
        }
    }

    override fun onPause() {
        fragmentRunning = false
        super.onPause()
    }
}