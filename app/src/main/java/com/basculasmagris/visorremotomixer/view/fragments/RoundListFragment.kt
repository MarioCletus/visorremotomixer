package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.Dialog
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentRoundListBinding
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.BluetoothUtils
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.CustomAlertDialogBuilder
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.RemoteTabletSession
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerData
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import com.google.gson.Gson


class RoundListFragment : Fragment() {

    private lateinit var mBinding: FragmentRoundListBinding
    private val TAG = "DEBRLF"
    private val TAG1 = "SOS"

    private var bBlockButton = false

    private var bGoToRound = true
    private var fragmentRunning = false

    private val REFRESH_VIEW_TIME = 20
    private val TIME_REFRESH_ROUNDS = 2000L
    private var countMsg: Int = REFRESH_VIEW_TIME

    private var menu:Menu? = null
    private var selectedTabletInFragment: TabletMixer? = null
    private var bMixerInit = false
    private var roundAdapter: RoundRunAdapter? = null

    private val mTabletMixerViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerVRApplication).tabletMixerRepository)
    }
    private var liveData: MediatorLiveData<MergedLocalData>? = null
    private var mLocalTablet: MutableList<TabletMixer>? = null
    private val mLocalTabletCustomList: java.util.ArrayList<CustomListItem> =
        java.util.ArrayList<CustomListItem>()
    private lateinit var mCustomListDialog: Dialog

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
        (requireActivity() as MainActivity).getSavedTabletMixer()
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
                menu.findItem(R.id.menu_selected_remote_tablet)?.title = RemoteTabletSession.tabletName

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                Log.i(TAG,"Menu pressed ${menuItem.itemId}\n$menuItem")
                return when (menuItem.itemId) {
                    R.id.menu_refresh_data ->{
                        refreshData()
                        return true
                    }

                    R.id.bluetooth_remote_status -> {
                        if(!isAdded){
                            return false
                        }
                        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
                            bd.address == selectedTabletInFragment?.mac
                        }
                        Log.v(TAG,"Force connection $deviceBluetooth")
                        deviceBluetooth?.let {
                            val name = BluetoothUtils.getBluetoothName(requireActivity(), it)
                            Log.v(TAG,"Force connection $name")
                            (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()
                            (requireActivity() as MainActivity).mBinder?.connectKnowDeviceWithTransfer(it)
                            (requireActivity() as MainActivity).showCustomProgressDialog()
                        }
                        return true
                    }

                    R.id.menu_selected_remote_tablet->{
                        if(Helper.getCurrentUser(requireActivity()).codeRole == Constants.USER_NUTRICIONIST){
                            return false
                        }
                        if (mLocalTabletCustomList.size > 0){
                            customItemsLDialog(getString(R.string.tablet), mLocalTabletCustomList,Constants.MIXER_REF)
                        } else {
                            alertMixer(getString(R.string.no_hay_tablet_vinculada))
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


        mBinding.btnBack.setOnClickListener {
            if (isAdded) findNavController().popBackStack(R.id.nav_home, false)
        }

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


    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()

        liveDataMerger.addSource(mTabletMixerViewModel.allTabletMixerList) {
            if (it != null) {
                liveDataMerger.value = TabletMixerData(it)
            }
        }
        return liveDataMerger
    }


    private fun getLocalData(){
        Log.i(TAG,"getLocalData")
        getLocalRound()
        refreshData()
        Log.i(TAG, "getLocalData ${liveData?.value}")
        liveData = fetchLocalData()
        liveData?.observe(requireActivity(),  object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                Log.v(TAG, "it: ${it.toString()}")
                when (it) {
                    is TabletMixerData -> {
                        mLocalTablet = it.tabletMixers
                        mLocalTablet?.let {
                            Log.i(TAG,"mLocalTabletMixers: "+ it.size + "  "+ it)
                        }
                        mLocalTablet?.forEach {
                            val alreadyExist = mLocalTabletCustomList.firstOrNull { customItem ->
                                customItem.id == it.id
                            }

                            if (alreadyExist == null){
                                val customList = CustomListItem(it.id,0, it.name)
                                mLocalTabletCustomList.add(customList)
                            }
                        }
                    }
                    else -> {}
                }

                if (mLocalTablet != null) {
                    Log.i(TAG,"liveData.removeObserver")
//                    (requireActivity() as MainActivity).mBinder?.getBondedDevices()
//                    dialog?.dismiss()
                    liveData?.removeObserver(this)
                    liveData = null
                }
            }
        })
    }



    private fun getLocalRound(){
        // Crear el adapter una sola vez. Las llamadas siguientes solo actualizan datos
        // sin reasignar el adapter, lo que preserva la posición de scroll.
        if (roundAdapter == null) {
            mBinding.rvRoundsList.layoutManager = GridLayoutManager(requireActivity(), 3)
            roundAdapter = RoundRunAdapter(this@RoundListFragment)
            mBinding.rvRoundsList.adapter = roundAdapter
        }
        updateRoundListData()
    }

    private fun updateRoundListData() {
        val adapter = roundAdapter ?: return
        val rounds = (activity as? MainActivity)?.listOfMedRoundsRun ?: return
        if (rounds.isEmpty()) {
            mBinding.rvRoundsList.visibility = View.GONE
            mBinding.tvNoData.visibility = View.VISIBLE
        } else {
            mBinding.rvRoundsList.visibility = View.VISIBLE
            mBinding.tvNoData.visibility = View.GONE
            adapter.roundList(rounds)
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
            if(isAdded) {
                (requireActivity() as MainActivity).sendRequestListOfRounds()
                val name = BluetoothUtils.getBluetoothName(requireActivity(), device)
                val address = BluetoothUtils.getAddress(requireActivity(), device)
                Log.i(TAG, "onDeviceConnected $name $address")
                Log.i(TAG1, "onDeviceConnected $name $address")
            }
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val mainActivity = activity as? MainActivity
            if (mainActivity == null || !isAdded) {
                Log.w(TAG, "Activity no disponible, se ignora mensaje Bluetooth")
                return
            }
            if(!fragmentRunning){
                Log.i(TAG,"fragmentRunning == false")
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
                            updateRoundListData()
                            roundRunDetail.mixer?.let {mixerDetail->
                                val mixerName = if(mixerDetail.name.isNotEmpty()) "Mixer: ${mixerDetail.name}" else ""
                                menu?.findItem(R.id.bluetooth_balance)?.title = mixerName
                            }
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

                Constants.CMD_WEIGHT_CONFIG_FREE->{
                    Log.i("CMD_WEIGHT","CMD_WEIGHT_CONFIG_FREE $message")
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
                    if (!mainActivity.isVrDownloadEnabled()) {
                        return
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
                        (requireActivity() as MainActivity).alertDialog(getString(R.string.warning),getString(R.string.no_disponible))
                    }
                }

                Constants.CMD_VTL ->{
                    Log.i("showCommand","CMD_VTL: recibida lista de tablets VR")
                    if (isAdded) mainActivity.processVrTabletList(message)
                }

                Constants.CMD_MIXER ->{
                    Log.i("showCommand","CMD_MIXER")
                    try{
                        val convertZip = ConvertZip()
                        val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
                        val gson = Gson()
                        val mixerDetail = gson.fromJson(json,  MixerDetail::class.java)
                        Log.i(TAG,"mixer receibe $mixerDetail")
                        val mixerName = if(mixerDetail?.name?.isNotEmpty() == true) "  ${mixerDetail.name}" else ""
                        menu?.findItem(R.id.bluetooth_balance)?.title = mixerName
                        bMixerInit = true
                    }catch (e: NumberFormatException){
                        Log.i(TAG,"bSyncroMixer NumberFormatException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"bSyncroMixer Exception $e")
                        return
                    }
                }

                else->{
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            if(!isAdded)
                return
            (requireActivity() as MainActivity).beaconReceived()
            if(!bMixerInit){
                (requireActivity() as MainActivity).requestMixer()
            }
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
            if(isAdded){
                bMixerInit = false
                (requireActivity() as MainActivity).changeStatusDisconnected()
                Log.i(TAG1,"onDeviceDisconnected")
            }
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[RoundListFragment]onBondedDevices ${device?.size} \n$device")
            (requireActivity() as MainActivity).knowDevices = device
            selectedTabletInFragment?.let {
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
            (requireActivity() as MainActivity).weightReceived()
    }

    private fun refreshRound() {
        countMsg = 0
        Log.i(TAG,"refreshRound")
        (requireActivity() as MainActivity).sendRequestRoundRunDetail()
    }

    override fun onDestroyView() {
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        roundAdapter = null
        super.onDestroyView()
    }

    fun sendGoToRound(id: Long) {
        if(bGoToRound){
            (requireActivity() as MainActivity).showCustomProgressDialog(getString(R.string.warning),getString(R.string.no_se_pudo_iniciar_ronda),R.layout.dialog_custom_progress_iniciar)
        }else{
            (requireActivity() as MainActivity).showCustomProgressDialog(getString(R.string.warning),getString(R.string.no_se_puede_ver_ronda),R.layout.dialog_custom_progress_ver)
        }
        Log.i("seguimiento","sendGoToRound ${id}")
        bGoToRound = true
        (requireActivity() as MainActivity).sendGoToRound(id)
    }

    override fun onResume() {
        fragmentRunning = true
        bMixerInit = false
        super.onResume()
    }

    fun connectTable(tabletMixer: TabletMixer){
        Log.i(TAG, "Cantidad: ${(requireActivity() as MainActivity).knowDevices?.size}")
        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
            bd.address == tabletMixer.mac
        }

        if (deviceBluetooth != null){
            Log.i(TAG,"Try to connect with ${BluetoothUtils.getBluetoothName(requireContext(),deviceBluetooth)}  ${deviceBluetooth.address}")
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).connectDevice(deviceBluetooth)
        } else {
            Toast.makeText(requireActivity(), getString(R.string.no_se_pudo_conectar), Toast.LENGTH_SHORT).show()
        }
    }

    fun setTabletMixer(tabletMixerIn: TabletMixer) {
        tabletMixerIn.let { tabletMixer ->
            menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + tabletMixer.name
            selectedTabletInFragment = tabletMixer
            Log.i(
                TAG,
                "setTabletMixer $tabletMixer  \nmService ${(requireActivity() as MainActivity).mService}"
            )
            // Solo pedir bonded devices si no estamos ya conectados al dispositivo correcto.
            // Evita ciclos innecesarios getBondedDevices → onBondedDevices → selectTablet → connectDevice.
            val activity = requireActivity() as MainActivity
            if (activity.mBinder?.isConnected() != true) {
                activity.mBinder?.getBondedDevices()
            }
            menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + selectedTabletInFragment?.name
        }
    }

    fun selectTablet(tabletMixer: TabletMixer){
        val activity = requireActivity() as MainActivity
        Log.i(TAG,"tabletMixerInFragment ${tabletMixer.name} mac='${tabletMixer.mac}' btName='${tabletMixer.btName}'")

        if (tabletMixer.mac.isEmpty()) {
            val resolvedDevice = activity.resolveBluetoothDevice(tabletMixer)
            if (resolvedDevice != null) {
                Log.i(TAG, "selectTablet RLF: MAC resuelta por btName='${tabletMixer.btName}' → ${resolvedDevice.address}")
                if (activity.mBinder?.isConnected() != true) {
                    connectTable(tabletMixer.copy(mac = resolvedDevice.address))
                }
            }
            return
        }

        activity.knowDevices?.forEach{
            Log.i(TAG,"bluetoothKnowed ${it.name} ${it.address}")
            if(tabletMixer.mac.equals(it.address)){
                var name = ""
                var address = ""
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    if (ActivityCompat.checkSelfPermission(
                            activity, Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED) {
                        name = it.name
                        address = it.address
                    }
                }else{
                    name = it.name
                    address = it.address
                }
                Log.i(TAG,"Se seleccionó $name : $address")
                if (activity.mBinder?.isConnected() != true) {
                    connectTable(tabletMixer)
                }
            }
        }
    }

    override fun onPause() {
        fragmentRunning = false
        super.onPause()
    }





    private fun alertMixer(msg: String) {
        if(!isAdded){
            return
        }
        val builder = CustomAlertDialogBuilder(requireActivity())
        builder.setTitle(getString(R.string.warning))
        builder.setMessage(msg)
        builder.setPositiveButton(getString(R.string.vincular)) { dialog, _ ->
            val navController = findNavController()
            val currentDest = navController.currentDestination?.id
            /*
            // Opcional: solo navegar si no estamos ya en nav_mixer
                        if (currentDest != R.id.nav_mixer) {
                            val options = NavOptions.Builder()
                                .setPopUpTo(R.id.nav_round, inclusive = true)
                                .build()
                            navController.navigate(R.id.nav_mixer, null, options)
                        }
            */

            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cerrar)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }


    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        if(!isAdded){
            return
        }
        mCustomListDialog = Dialog(requireActivity(),R.style.CustomDialogTheme)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapterFragment(this, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        if(!isAdded){
            return
        }
        when(selection){
            Constants.MIXER_REF->{
                (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()//Se seleccionó una nueva tablet.
                mCustomListDialog.dismiss()
                menu?.findItem(R.id.menu_selected_remote_tablet)?.title = ""
                menu?.findItem(R.id.bluetooth_balance)?.title = ""
                bMixerInit = false

                val localTabletMixer = mLocalTablet?.firstOrNull { tabletMixer ->
                    tabletMixer.id == item.id
                } ?: return

                val changeTabletMixer = menu?.findItem(R.id.menu_selected_remote_tablet)
                changeTabletMixer?.title = "   "+ localTabletMixer.name
                localTabletMixer.let {
                    Log.i(TAG, "Se seleccionó tablet $localTabletMixer")
                    (requireActivity() as MainActivity).saveTabletMixer(localTabletMixer)
                }

                Log.i(TAG, "Local tablet selected: ${localTabletMixer.name} | ${localTabletMixer.mac}")

                selectedTabletInFragment = localTabletMixer
                (requireActivity() as MainActivity).changeTabletMixer(localTabletMixer)
                menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + selectedTabletInFragment?.name
            }
        }
    }

}