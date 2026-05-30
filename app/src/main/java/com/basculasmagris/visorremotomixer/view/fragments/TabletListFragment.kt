package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
import com.basculasmagris.visorremotomixer.databinding.FragmentTabletListBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.BluetoothUtils
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.RemoteTabletSession
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.TabletConfigActivity
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerData
import com.basculasmagris.visorremotomixer.view.adapter.CustomDynamicListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.TabletMixerAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TabletListFragment : BottomSheetDialogFragment() {

    private val TAG : String = "DEBTML"
    var menu: Menu? = null
    private lateinit var mBinding: FragmentTabletListBinding
    private var bluetoothDevices: MutableList<BluetoothDevice> = ArrayList()
    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    var selectedTabletInFragment: TabletMixer? = null
    private lateinit var mCustomListDialog : Dialog


    private val mTabletMixerViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerVRApplication).tabletMixerRepository)
    }
    private var mLocalTablets: List<TabletMixer>? = null
    private var likingTabletMixer: TabletMixer? = null

    private var dialog: AlertDialog? = null
    var dialogCustomListBinding: DialogCustomListBinding? = null


    private val handlerBaliza = Handler(Looper.getMainLooper())
    private val runnable: Runnable = object : Runnable {
        override fun run() {

            (requireActivity() as MainActivity).sendBeacon()
            handlerBaliza.postDelayed(this, 2000)
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
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(viewLifecycleOwner, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is TabletMixerData -> mLocalTablets = it.tabletMixers
                    else -> {}
                }

                if (mLocalTablets != null) {
                    (requireActivity() as MainActivity).mBinder?.getBondedDevices()
                    liveData.removeObserver(this)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        permission()
        mBinding = FragmentTabletListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start Sync
        getLocalData()
        getLocalTabletMixer()
        // Navigation Menu
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                this@TabletListFragment.menu = menu
                menuInflater.inflate(R.menu.menu_tablet_list, menu)
                if(isAdded){
                    (requireActivity() as MainActivity).clearbShowDevice()
                }

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_tablet)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvTabletList.adapter as TabletMixerAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvTabletList.adapter as TabletMixerAdapter).filter.filter(newText)
                        return true
                    }
                })

                val tabletName = menu.findItem(R.id.menu_selected_remote_tablet)
                tabletName.title = RemoteTabletSession.tabletName
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_tablet -> {
                        goToConfigMixer(null,false)
                        return true
                    }

                    R.id.menu_selected_remote_tablet ->{
                        selectedTabletInFragment?.let{ tabletMixer->
                            Log.v(TAG,"Force connection")
                            val device = Helper.getBluetoothDeviceFromMac(tabletMixer.mac)
                            device?.let {
                                val name = BluetoothUtils.getBluetoothName(requireContext(), it)
                                Log.v(TAG,"Force connection $name ${tabletMixer.mac}")
                                (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()
                                (requireActivity() as MainActivity).mBinder?.connectKnowDeviceWithTransfer(it)
                                (requireActivity() as MainActivity).showCustomProgressDialog()
                            }
                            return true
                        }
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        // Bluethoot
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireActivity(), mBluetoothListener)

        mBinding.btnAddTablet.setOnClickListener {
            goToConfigMixer(null,false)
        }

        mBinding.btnBack.setOnClickListener {
            if (isAdded) findNavController().popBackStack(R.id.nav_home, false)
        }
    }

    private fun getLocalTabletMixer(){
        mBinding.rvTabletList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val tabletAdapter =  TabletMixerAdapter(this@TabletListFragment)
        mBinding.rvTabletList.adapter = tabletAdapter
        mTabletMixerViewModel.allTabletMixerList.observe(viewLifecycleOwner) { remoteViewers ->
            remoteViewers.let{ _tablets ->
                if (_tablets.isNotEmpty()){
                    mBinding.rvTabletList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE

                    tabletAdapter.tabletMixerList(_tablets)

                    _tablets?.forEach { remoteViewer ->
                        val isLinked = bluetoothDevices.firstOrNull { bluetoothDevice ->
                            bluetoothDevice.address == remoteViewer.mac
                        }
                        (mBinding.rvTabletList.adapter as TabletMixerAdapter).statusConnexion(remoteViewer, isLinked!=null)
                    }
                } else {
                    mBinding.rvTabletList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToRemoteMixerFragment(tabletMixer: TabletMixer){
        (activity as MainActivity).changeTabletMixer(tabletMixer)
        cleanObservers()
        findNavController().navigate(TabletListFragmentDirections.actionTabletMixerListFragmentToRemoteMixerFragment())
    }

    fun deleteTabletMixer(tabletMixer: TabletMixer){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.eliminar))
        builder.setMessage(resources.getString(R.string.msg_delete_tablet_dialog, tabletMixer.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mTabletMixerViewModel.delete(tabletMixer)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun connect(tabletMixer: TabletMixer){
        likingTabletMixer = tabletMixer
        Log.i(TAG, "Cantidad: ${bluetoothDevices.size}")
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
            bd.address == tabletMixer.mac
        }

        if (deviceBluetooth != null){
            Log.i(TAG,"Try to connect with ${deviceBluetooth}")
            // Sincronizar selectedTabletInActivity antes de conectar,
            // para que el reconnect loop use el device correcto.
            (requireActivity() as MainActivity).selectTabletMixer(tabletMixer)
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).connectDevice(deviceBluetooth)

        } else {
//            Toast.makeText(requireActivity(), "Hubo un problema al iniciar la calibración. Intente nuevamente", Toast.LENGTH_SHORT).show()
        }
    }


    fun selectTabletMixer(tabletMixer: TabletMixer) {
        (requireActivity() as MainActivity).selectTabletMixer(tabletMixer)
        (requireActivity() as MainActivity).saveTabletMixer(tabletMixer)
        selectedTabletInFragment = tabletMixer
        (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()
    }

    var countMessage = 0
    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i("BLUE", "[MixerListFragment] onDiscoveryStarted")
            customItemsDialog(getString(R.string.dispositivos_disponibles), ArrayList(), Constants.DEVICE_REF)
        }

        override fun onDiscoveryStopped() {
            Log.i("BLUE", "[MixerListFragment] onDiscoveryStopped")
            dialog?.cancel()
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {

            device?.let { currentDevice ->
                allBluetoothDevice.add(device)
                val name = if (BluetoothUtils.getBluetoothName(requireContext(),device).isEmpty()) "No identificado" else BluetoothUtils.getBluetoothName(requireContext(),device)
                val mac = if (BluetoothUtils.getAddress(requireContext(),device).isEmpty()) "00:00:00:00" else BluetoothUtils.getAddress(requireContext(),device)

                val item = CustomListItem(0L,0, name, mac, R.drawable.icon_balance)
                //allBluetoothDeviceCustomList.add(item)
                (dialogCustomListBinding?.rvList?.adapter as CustomDynamicListItemAdapterFragment).updateList(item)

            }
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            Log.i("BLUE", "[MixerListFragment] onDeviceConnected")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("command", "TabletListFragment onCommandReceived ${message?.let { String(it) }}")

            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val messageStr = String(message,0, message.size)
            if(countMessage++>20){
                Log.d("message","TMLF message $messageStr")
                countMessage = 0
            }
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","TMLF command $command")
            when (command) {
                Constants.CMD_ACK->{
                    Log.i(TAG,"CMD_ACK")
                    handlerBaliza.removeCallbacks(runnable)
                }

                Constants.CMD_NTA ->{
                    Log.i("showCommand","CMD_NTA")
                    if(isAdded){
                        (requireActivity() as MainActivity).alertDialog(getString(R.string.warning),getString(R.string.no_disponible))
                    }
                }
                Constants.CMD_WEIGHT->{
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_CONFIG->{
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_LOAD->{
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_DWNL->{
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_RESUME->{
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_LOAD_FREE->{
                    refreshWeight(message)
                }
                Constants.CMD_WEIGHT_DWNL_FREE->{
                    refreshWeight(message)
                }

                Constants.CMD_TABLET->{
                    Log.i("showCommand","CMD_TABLET")
                    (activity as MainActivity).processTabletInfo(message)

                }

                else->{}
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            if(isAdded)
                (requireActivity() as MainActivity).beaconReceived()
            Log.i("message", "TMLF onMessageReceived $message")
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("send", "${this.javaClass.name} onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i("send", "${this.javaClass.name} onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[TabletListFragment] onError")
            if(isAdded)
                (requireActivity() as MainActivity).changeStatusDisconnected()
        }

        override fun onDeviceDisconnected() {
            Log.i(TAG, "[TabletListFragment] onDeviceDisconnected")
            if(isAdded)
                (requireActivity() as MainActivity).changeStatusDisconnected()
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i("BLUE", "[MixerListFragment] onBondedDevices: ${device?.size} | Local Mixers: ${mLocalTablets?.size}")
            device?.let { bdList ->
                bluetoothDevices = bdList.toMutableList()
            }

            mLocalTablets?.forEach { tablet ->
                val isLinked = device?.firstOrNull { bluetoothDevice ->
                    bluetoothDevice.address == tablet.mac
                }
                Log.i("BLUE", "[MixerListFragment] Mixer ${tablet.name}: ${isLinked!=null}")
                (mBinding.rvTabletList.adapter as TabletMixerAdapter).statusConnexion(tablet, isLinked!=null)
            }

            selectedTabletInFragment?.let {
                val deviceBluetooth = bluetoothDevices.firstOrNull { bd ->
                    bd.address == it.mac
                }
                Log.i(TAG,"onBondedDevices connectDevice")
                (requireActivity() as MainActivity).connectDevice(deviceBluetooth)
            }
        }

    }

    private fun customItemsDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(requireActivity())
        dialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(dialogCustomListBinding!!.root)
        dialogCustomListBinding?.tvTitle?.text = title
        dialogCustomListBinding?.rvList?.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomDynamicListItemAdapterFragment(this, itemsList.toMutableList(), selection)
        dialogCustomListBinding?.rvList?.adapter = adapter
        mCustomListDialog.show()
    }

    override fun onDestroyView() {
        Log.i(TAG,"onDestroy view")
        super.onDestroyView()
        cleanObservers()
    }


    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
        handlerBaliza.post(runnable)
        getLocalData()
        (requireActivity() as MainActivity).getSavedTabletMixer()
    }

    override fun onStop() {
        Log.i(TAG,"onStop")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        handlerBaliza.removeCallbacks(runnable)
    }

    private fun cleanObservers(){
        mLocalTablets = null
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireActivity(), mBluetoothListener)

    }


    fun goToConfigMixer(tabletMixer:TabletMixer?, isDetail :Boolean = false){
        Log.d(TAG, "Go to config Activity. BTDevice: $tabletMixer")
        if(tabletMixer == null || tabletMixer.id != selectedTabletInFragment?.id){
            (requireActivity() as MainActivity).reconnectDisable()
            (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()
        }
        val intent = Intent(requireActivity(), TabletConfigActivity::class.java)
        intent.putExtra(Constants.EXTRA_MIXER_MODE, isDetail)
        if(tabletMixer != null){
            val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
                bd.address == tabletMixer.mac
            }
            intent.putExtra(Constants.EXTRA_MIXER_DETAILS, tabletMixer)
            intent.putExtra("BTDEVICE",deviceBluetooth)
        }
        someActivityResultLauncher.launch(intent)
        return
    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Aquí manejas el resultado de la actividad
        Log.v(TAG,"Return from MixerConfigActivity")
        (requireActivity() as MainActivity).reconnectEnable()
        if (result.resultCode == Activity.RESULT_OK) {
            Log.v(TAG,"result code OK")
            val intent: Intent? = result.data
            var tabletReturned: TabletMixer? = null
            if (intent?.hasExtra(Constants.EXTRA_MIXER_DETAILS) == true){
                tabletReturned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent?.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS,  TabletMixer::class.java)
                } else {
                    intent?.getParcelableExtra< TabletMixer>(Constants.EXTRA_MIXER_DETAILS)
                }
            }
            Log.v(TAG,"tabletReturned $tabletReturned")
            tabletReturned?.let {
                Log.v(TAG,"TabletReturned ${it.name}  ${it.mac}  selectedTabletInFragment $selectedTabletInFragment")
                if(it.mac != selectedTabletInFragment?.mac && selectedTabletInFragment != null){
                    Log.v(TAG,"Disconnect TabletReturned")
                    (requireActivity() as MainActivity).mBinder?.disconnectKnowDeviceWithTransfer()
                    (requireActivity() as MainActivity).changeStatusDisconnected()
                }
            }
        }
    }

    private fun permission() {
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
            Log.d(TAG, "Permissions Granted")
        }

    }

    fun setTabletMixer(tabletMixerIn: TabletMixer) {
        tabletMixerIn.let { tabletMixer ->
            menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + tabletMixer.name
            selectedTabletInFragment = tabletMixer
            (requireActivity() as MainActivity).mBinder?.getBondedDevices()
            (mBinding.rvTabletList.adapter as TabletMixerAdapter).selectedTablet = tabletMixer
            BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        }
    }

    private fun refreshWeight(message:ByteArray) {
        val weight = String(message, 4, 8).toLong()
        val sign = String(message, 3, 1)
//        mBinding.tvPeso.setText("${sign}${weight}")
        if(!sign.contains("N") && !sign.contains("n")){
            (requireActivity() as MainActivity).weightReceived()
        }
    }
}