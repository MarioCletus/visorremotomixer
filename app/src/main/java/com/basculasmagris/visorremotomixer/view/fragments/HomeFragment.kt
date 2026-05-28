package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding
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
import com.basculasmagris.visorremotomixer.view.adapter.TabletMixerHomeAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val TAG1: String = "SOS"
    private val TAG = "DEBHome"
    private var selectedTabletInFragment: TabletMixer? = null
    private var menu: Menu? = null
    private var bInFree: Boolean = true
    private var bInCfg: Boolean = false
    private var bInRes: Boolean = false
    private var bInLoad: Boolean = false
    private var bInDownload : Boolean = false

    private var liveData: MediatorLiveData<MergedLocalData>? = null

    private val mTabletMixerViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerVRApplication).tabletMixerRepository)
    }
    private val mLocalTabletMixersCustomList: java.util.ArrayList<CustomListItem> =
        java.util.ArrayList<CustomListItem>()
    private lateinit var mCustomListDialog: Dialog

    private var tabletMixerHomeAdapter: TabletMixerHomeAdapter? = null
    private var mLocalTabletMixers: MutableList<TabletMixer>? = null

    private var bMixerInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_remote_mixer_fragment, menu)
                this@HomeFragment.menu = menu
                val activity = requireActivity() as MainActivity
                if(isAdded){
                    activity.clearbShowDevice()
                }
                activity.supportActionBar?.let {
                    val user = Helper.getCurrentUser(activity)
                    val title = "${getString(R.string.inicio)} - ${user.name} ${user.lastname}"
                    it.title = title

                }
                menu.findItem(R.id.cancel_round).isVisible = false
                menu.findItem(R.id.menu_selected_remote_tablet)?.title = RemoteTabletSession.tabletName
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.bluetooth_remote_status -> {
                        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
                            bd.address == selectedTabletInFragment?.mac
                        }
                        Log.v(TAG,"Force connection ${deviceBluetooth?.name} $selectedTabletInFragment")
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
                        if (mLocalTabletMixersCustomList.size > 0){
                            customItemsLDialog(getString(R.string.tablet), mLocalTabletMixersCustomList,Constants.MIXER_REF)
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

        getLocalData()

        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding.btnAdmin.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToAdmin())
        }

        mBinding.btnToTablet.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToMixer())
        }

        mBinding.btnSync.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToSync())
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeTabletMixers()
        (requireActivity() as MainActivity).getSavedTabletMixer()
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.rvTabletMixerHomeAdapter.adapter = null
    }

    private fun observeTabletMixers() {
        mTabletMixerViewModel.allTabletMixerList.observe(viewLifecycleOwner) { tabletMixers ->
            mLocalTabletMixers = tabletMixers
            tabletMixerHomeAdapter?.updateList(tabletMixers)
        }
    }

    private fun setupRecyclerView() {
        tabletMixerHomeAdapter = TabletMixerHomeAdapter(emptyList()) { tabletMixer ->
            onTabletMixerPressed(tabletMixer)
        }

        mBinding.rvTabletMixerHomeAdapter.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tabletMixerHomeAdapter
        }
    }

    private fun onTabletMixerPressed(tabletMixer: TabletMixer) {
        Log.i(TAG, "TabletMixer presionado: ${tabletMixer.id} - ${tabletMixer.name}")
        Log.i(TAG1, "TabletMixer presionado: ${tabletMixer.id} - ${tabletMixer.name}")


        viewLifecycleOwner.lifecycleScope.launch {
            (requireActivity() as MainActivity).changeTabletMixer(tabletMixer,Helper.getBluetoothDeviceFromMac(tabletMixer.mac))
            (requireActivity() as MainActivity).saveTabletMixerAndWait(tabletMixer)

            if (tabletMixer.btName.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.seleccione_el_bluetooth_de_latablet, tabletMixer.name),
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToTabletConfig(tabletMixer)
                )
            } else {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToRound()
                )
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


    private fun getLocalData(){
        Log.i(TAG,"getLocalData")
        Log.i(TAG, "getLocalData ${liveData?.value}")
        liveData = fetchLocalData()
        liveData?.observe(requireActivity(),  object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                Log.v(TAG, "it: ${it.toString()}")
                when (it) {
                    is TabletMixerData -> {
                        mLocalTabletMixers = it.tabletMixers
                        mLocalTabletMixers?.let {
                            Log.i(TAG,"mLocalTabletMixers: "+ it.size + "  "+ it)
                        }
                        mLocalTabletMixers?.forEach {
                            val alreadyExist = mLocalTabletMixersCustomList.firstOrNull { customItem ->
                                customItem.id == it.id
                            }

                            if (alreadyExist == null){
                                val customList = CustomListItem(it.id,0, it.name)
                                mLocalTabletMixersCustomList.add(customList)
                            }
                        }
                    }
                    else -> {}
                }

                if (mLocalTabletMixers != null) {
                    Log.i(TAG,"liveData.removeObserver")
                    liveData?.removeObserver(this)
                    liveData = null
                }
            }
        })
    }

    var countMessage = 0
    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        init {
            Log.i(TAG,"create mBluetoothListener")
        }

        override fun onDiscoveryStarted() {
            Log.i(TAG, "[Home] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[Home] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[Home] ACT onDeviceDiscovered")
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            if(isAdded){
                (requireActivity() as MainActivity).sendRequestListOfRounds()
                Log.i(TAG1, "[Home] ACT onDeviceConnected ${BluetoothUtils.getBluetoothName(requireContext(),device)}  ${BluetoothUtils.getAddress(requireActivity(),device)}")
            }
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {

            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }

            val messageStr = String(message,0, message.size)
            if(countMessage++>20){
                Log.d("message","HF message $messageStr")
                countMessage = 0
            }
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","HF command $command")

            when (command){

                Constants.CMD_ROUNDDETAIL->{
                    val convertZip = ConvertZip()
                    Log.i(TAG,"message.lenght ${message.size}")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i("showCommand","CMD_ROUNDDETAIL ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                    try{
                        val jsonString : String = convertZip.decompressText(byteArrayUtil)
                        Log.i("Json","jsonString * $jsonString")
                        if(jsonString.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : MinRoundRunDetail = gson.fromJson(jsonString,  MinRoundRunDetail::class.java)
                            (requireActivity() as MainActivity).updateRoundDetail(roundRunDetail)
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

                Constants.CMD_ROUNDDATA->{
                    Log.i(TAG,"message.lenght ${message.size}")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i("showCommand","CMD_ROUNDDATA ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                }

                Constants.CMD_USER_LIST->{
                    Log.i("showCommand","CMD_USER_LIST")

                }

                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS Home")
                    if(isAdded)
                        (requireActivity() as MainActivity).refreshRounds(message)

                }

                Constants.CMD_DLG_PRODUCT->{
                    Log.i("showCommand","CMD_DLG_PRODUCT")
                }


                Constants.CMD_DLG_EST->{
                    Log.i("showCommand","CMD_DLG_EST")
                }


                Constants.CMD_DLG_CORRAL->{
                    Log.i("showCommand","CMD_DLG_CORRAL")
                }


                Constants.CMD_WEIGHT->{
                    bInCfg = false
                    bInLoad = false
                    bInDownload = false
                    bInRes = false
                    bInFree = false
                    Log.v("cmd_weight","CMD_WEIGHT Home $message")
                    refreshWeight(message)
                }


                Constants.CMD_WEIGHT_CONFIG->{
                    bInCfg = true
                    bInLoad = false
                    bInDownload = false
                    bInRes = false
                    bInFree = false
                    Log.v("cmd_weight","CMD_WEIGHT_CONFIG")
                    refreshWeight(message)
                }

                Constants.CMD_WEIGHT_RESUME->{
                    bInCfg = false
                    bInLoad = false
                    bInDownload = false
                    bInRes = true
                    bInFree = false
                    Log.v("cmd_weight","CMD_WEIGHT_RESUME")
                    refreshWeight(message)
                }

                Constants.CMD_WEIGHT_LOAD->{
                    bInCfg = false
                    bInLoad = true
                    bInDownload = false
                    bInRes = false
                    bInFree = false
                    refreshWeight(message)
                }

                Constants.CMD_WEIGHT_DWNL->{
                    bInCfg = false
                    bInLoad = false
                    bInDownload = true
                    bInRes = false
                    bInFree = false
                    refreshWeight(message)

                }

                Constants.CMD_WEIGHT_LOAD_FREE->{
                    bInCfg = false
                    bInLoad = true
                    bInDownload = false
                    bInRes = false
                    bInFree = true
                    refreshWeight(message)
                }

                Constants.CMD_WEIGHT_DWNL_FREE->{
                    bInCfg = false
                    bInLoad = false
                    bInDownload = true
                    bInRes = false
                    bInFree = true
                    refreshWeight(message)
                }

                Constants.CMD_WEIGHT_TIMER->{
                    Log.v("cmd_weight","Home CMD_WEIGHT_TIMER")
                    Log.i("showCommand","Home CMD_WEIGHT_TIMER ${String(message)}")
                    if(isAdded)
                        (requireActivity() as MainActivity).weightReceived()
                }

                Constants.CMD_START_TIMER->{
                    Log.v("cmd_weight","Home CMD_START_TIMER ${String(message)}")
                    Log.i("showCommand","Home CMD_START_TIMER ${String(message)}")
                    if(isAdded)
                        (requireActivity() as MainActivity).weightReceived()
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


                Constants.CMD_NTA ->{
                    Log.i("showCommand","CMD_NTA")
                    if(isAdded){
                        (requireActivity() as MainActivity).alertDialog(getString(R.string.warning),getString(
                            R.string.no_disponible))
                    }
                }

                else->{
                    Log.i(TAG,"else $command")
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("message","HF message $message")
            (requireActivity() as MainActivity).beaconReceived()
            if(!bMixerInit){
                (requireActivity() as MainActivity).requestMixer()
            }
        }

        override fun onMessageSent(device: BluetoothDevice?, message: String?) {
            Log.i("sent", "onMessageSent ${device?.address} $message")
        }

        override fun onCommandSent(device: BluetoothDevice?, message: ByteArray?) {
            Log.i("sent", "onCommandSent ${device?.address} ${message?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[Home] ACT onError")
            (requireActivity() as MainActivity).changeStatusDisconnected()        }

        override fun onDeviceDisconnected() {
            if(isAdded){
                bMixerInit = false
                (requireActivity() as MainActivity).changeStatusDisconnected()
            }
            Log.i(TAG, "[Home]ACT onDeviceDisconnected")
            Log.i(TAG1, "[Home]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[Home]onBondedDevices ${device?.size} \n$device")
            (requireActivity() as MainActivity).knowDevices = device
            selectedTabletInFragment?.let {
                selectTablet(it)
            }
        }
    }

    private fun refreshWeight(message:ByteArray) {
        val weight = String(message, 4, 8).toLong()
        val sign = String(message, 3, 1)
        mBinding.tvPeso.setText("${sign}${weight}")
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
                // Conectar solo si no está ya conectado (evita reconexiones innecesarias)
                if ((requireActivity() as MainActivity).mBinder?.isConnected() != true) {
                    connectTable(tabletMixer)
                }
            }
        }
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
            Toast.makeText(requireActivity(), getString(R.string.no_se_pudo_conectar), Toast.LENGTH_SHORT).show()
        }
    }


    fun setTabletMixer(tabletMixerIn: TabletMixer) {
        selectedTabletInFragment = tabletMixerIn
        menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  ${tabletMixerIn.name}"
        Log.i(TAG, "setTabletMixer $tabletMixerIn mService ${(requireActivity() as MainActivity).mService}")
    }


    override fun onResume() {
        Log.i(TAG,"onResume")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        bMixerInit = false
        super.onResume()
    }

    override fun onStop() {
        Log.i(TAG,"onStop")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onStop()
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
                (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()//Se seleccionó un nuevo mixer.
                mCustomListDialog.dismiss()
                menu?.findItem(R.id.menu_selected_remote_tablet)?.title = ""
                menu?.findItem(R.id.bluetooth_balance)?.title = ""
                bMixerInit = false

                val localTabletMixer = mLocalTabletMixers?.firstOrNull { tabletMixer ->
                    tabletMixer.id == item.id
                } ?: return

                val changeTabletMixer = menu?.findItem(R.id.menu_selected_remote_tablet)
                changeTabletMixer?.title = "   "+ localTabletMixer.name
                localTabletMixer.let {
                    Log.i(TAG, "1Se seleccionó mixer $localTabletMixer")
                    (requireActivity() as MainActivity).saveTabletMixer(localTabletMixer)
                }

                Log.i(TAG, "Local mixer selected: ${localTabletMixer.name} | ${localTabletMixer.mac}")
                val localKnowDevice = Helper.getBluetoothDeviceFromMac(localTabletMixer.mac)
                Log.i(TAG, "localKnowDevice: ${BluetoothUtils.getBluetoothName(requireContext(),localKnowDevice)} | ${localKnowDevice?.address}")

                if (localKnowDevice != null ){
                    selectedTabletInFragment = localTabletMixer
                    (requireActivity() as MainActivity).changeTabletMixer(localTabletMixer,localKnowDevice)
                    menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + selectedTabletInFragment?.name
                }
            }
        }
    }

}

