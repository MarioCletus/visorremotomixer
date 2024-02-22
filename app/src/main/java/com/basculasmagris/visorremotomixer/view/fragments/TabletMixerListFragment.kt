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
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentTabletMixerListBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerConfigActivity
import com.basculasmagris.visorremotomixer.view.activities.TabletMixerData
import com.basculasmagris.visorremotomixer.view.adapter.CustomDynamicListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.TabletMixerAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TabletMixerListFragment : BottomSheetDialogFragment() {

    private val TAG : String = "DEBTML"
    var menu: Menu? = null
    private lateinit var mBinding: FragmentTabletMixerListBinding
    private var bluetoothDevices: MutableList<BluetoothDevice> = ArrayList()
    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    private var selectedBluetoothDevice : BluetoothDevice? = null
    var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null
    private lateinit var mCustomListDialog : Dialog
    private val allBluetoothDeviceCustomList: java.util.ArrayList<CustomListItem> =
        java.util.ArrayList<CustomListItem>()
    private var knowDevices: List<BluetoothDevice>? = null

    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerApplication).tabletMixerRepository)
    }
    private var mLocalTabletMixers: List<TabletMixer>? = null
    private var likingTabletMixer: TabletMixer? = null


    //    private var mProgressDialog: Dialog? = null
//    private var isFound = false
    private var dialog: AlertDialog? = null
    var dialogCustomListBinding: DialogCustomListBinding? = null

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
                    is TabletMixerData -> mLocalTabletMixers = it.tabletMixers
                    else -> {}
                }

                if (mLocalTabletMixers != null) {
                    (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
                    liveData.removeObserver(this)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        permission()
        mBinding = FragmentTabletMixerListBinding.inflate(inflater, container, false)
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
                this@TabletMixerListFragment.menu = menu
                menuInflater.inflate(R.menu.menu_tablet_mixer_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_tablet_mixer)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_tablet_mixer -> {
                        goToConfigMixer(null,false)
                        return true
                    }
                    R.id.menu_selected_tabler_mixer ->{
                        selectedTabletMixerInFragment?.let{
                            goToRemoteMixerFragment(it)
                        }
                        return true
                    }
                    R.id.bluetooth_mixer_status ->{
                        selectedTabletMixerInFragment?.let{tabletMixer->
                            Log.v(TAG,"Force connection")
                            val deviceBluetooth = knowDevices?.firstOrNull { bd->
                                bd.address == tabletMixer.mac
                            }
                            deviceBluetooth?.let {
                                val name = (requireActivity() as MainActivity).getName(it)
                                val macAddress = (requireActivity() as MainActivity).getAddress(it)
                                Log.v(TAG,"Force connection $name $macAddress")
                                (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                                (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
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
    }

    private fun getLocalTabletMixer(){
        mBinding.rvTabletMixersList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val tabletMixerAdapter =  TabletMixerAdapter(this@TabletMixerListFragment)
        mBinding.rvTabletMixersList.adapter = tabletMixerAdapter
        mTabletMixerViewModel.allTabletMixerList.observe(viewLifecycleOwner) { remoteViewers ->
            remoteViewers.let{ _tablet_mixers ->
                if (_tablet_mixers.isNotEmpty()){
                    mBinding.rvTabletMixersList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE

                    tabletMixerAdapter.tabletMixerList(_tablet_mixers.filter { remoteViewer ->
                        remoteViewer.archiveDate.isNullOrEmpty()
                    }.toMutableList())

                    _tablet_mixers?.forEach { remoteViewer ->
                        val isLinked = bluetoothDevices.firstOrNull { bluetoothDevice ->
                            bluetoothDevice.address == remoteViewer.mac
                        }
                        (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).statusConnexion(remoteViewer, isLinked!=null)
                    }
                } else {
                    mBinding.rvTabletMixersList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToAddUpdateTabletMixer(){
        findNavController().navigate(TabletMixerListFragmentDirections.actionTabletMixerListFragmentToAddUpdateTabletMixerActivity())
    }

    fun goToRemoteMixerFragment(tabletMixer: TabletMixer){
        (activity as MainActivity).saveTabletMixer(tabletMixer)
        cleanObservers()
        findNavController().navigate(TabletMixerListFragmentDirections.actionTabletMixerListFragmentToRemoteMixerFragment())
    }





    fun deleteTabletMixer(tabletMixer: TabletMixer){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_mixer))
        builder.setMessage(resources.getString(R.string.msg_delete_mixer_dialog, tabletMixer.name))
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
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).connectDevice(deviceBluetooth)

        } else {
//            Toast.makeText(requireActivity(), "Hubo un problema al iniciar la calibración. Intente nuevamente", Toast.LENGTH_SHORT).show()
        }
    }


    fun selectTabletMixer(tabletMixer: TabletMixer) {
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
            bd.address == tabletMixer.mac
        }
        (requireActivity() as MainActivity).selectTabletMixer(tabletMixer)
        (requireActivity() as MainActivity).selectBluetooth(deviceBluetooth)
        (requireActivity() as MainActivity).saveTabletMixer(tabletMixer)
        selectedTabletMixerInFragment = tabletMixer
        selectedBluetoothDevice = deviceBluetooth
        selectedBluetoothDevice?.let {
            (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        }
    }

    var countMessage = 0
    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i("BLUE", "[MixerListFragment] onDiscoveryStarted")
            customItemsDialog("Dispositivos disponibles", ArrayList(), Constants.DEVICE_REF)
        }

        override fun onDiscoveryStopped() {
            Log.i("BLUE", "[MixerListFragment] onDiscoveryStopped")
            dialog?.cancel()
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {

            device?.let { currentDevice ->
                allBluetoothDevice.add(device)
                val name = if ((requireActivity() as MainActivity).getBluetoothName(device).isEmpty()) "No identificado" else (requireActivity() as MainActivity).getBluetoothName(device)
                val mac = if ((requireActivity() as MainActivity).getBluetoothAddress(device).isEmpty()) "00:00:00:00" else (requireActivity() as MainActivity).getBluetoothAddress(device)

                val item = CustomListItem(0L, 0, name, mac, R.drawable.icon_balance)
                //allBluetoothDeviceCustomList.add(item)
                (dialogCustomListBinding?.rvList?.adapter as CustomDynamicListItemAdapterFragment).updateList(item)

            }
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            Log.i("BLUE", "[MixerListFragment] onDeviceConnected")
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("command", "TabletMixerListFragment onCommandReceived ${message?.let { String(it) }}")
            (requireActivity() as MainActivity).changeStatusConnected()



            if(message == null || message.size<9){
                Log.i(TAG,"command not enough large (${message?.size})")
                return
            }
            val messageStr = String(message,0, message.size)
            if(countMessage++>20){
                Log.d("message","message $messageStr")
                countMessage = 0
            }
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","tabletMixerList command $command")
            when (command) {

            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            (requireActivity() as MainActivity).changeStatusConnected()
            Log.i("message", "TabletMixerListFragment onMessageReceived $message")
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("send", "${this.javaClass.name} onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i("send", "${this.javaClass.name} onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[TabletMixerListFragment] onError")
            (requireActivity() as MainActivity).changeStatusDisconnected()
        }

        override fun onDeviceDisconnected() {
            Log.i(TAG, "[TabletMixerListFragment] onDeviceDisconnected")
            (requireActivity() as MainActivity).changeStatusDisconnected()
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i("BLUE", "[MixerListFragment] onBondedDevices: ${device?.size} | Local Mixers: ${mLocalTabletMixers?.size}")
            device?.let { bdList ->
                bluetoothDevices = bdList.toMutableList()
            }

            mLocalTabletMixers?.forEach { mixer ->
                val isLinked = device?.firstOrNull { bluetoothDevice ->
                    bluetoothDevice.address == mixer.mac
                }
                Log.i("BLUE", "[MixerListFragment] Mixer ${mixer.name}: ${isLinked!=null}")
                (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).statusConnexion(mixer, isLinked!=null)
            }

            selectedTabletMixerInFragment?.let {
                val deviceBluetooth = bluetoothDevices.firstOrNull { bd ->
                    bd.address == it.mac
                }
                (requireActivity() as MainActivity).connectDevice(deviceBluetooth)
            }
        }

    }

    fun selectedListItem(item: CustomListItem, selection: String){

        selectedBluetoothDevice = allBluetoothDevice.firstOrNull { device ->
            device.address == item.description
        }

        selectedBluetoothDevice?.let { bluetoothDevice ->
            when (selection){
                Constants.DEVICE_REF -> {
                    (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDevice(requireActivity(), bluetoothDevice)

                }
                else -> {}
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
        getLocalData()
        (requireActivity() as MainActivity).getSavedTabletMixer()
    }

    override fun onStop() {
        Log.i(TAG,"onStop")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onStop()
    }

    private fun cleanObservers(){
        mLocalTabletMixers = null
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireActivity(), mBluetoothListener)

    }


    fun goToConfigMixer(tabletMixer:TabletMixer?, isDetail :Boolean = false){
        Log.d(TAG, "Go to config Activity. BTDevice: $tabletMixer")
        if(tabletMixer == null || tabletMixer.id != selectedTabletMixerInFragment?.id){
            (requireActivity() as MainActivity).reconnectDisable()
            (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        }
        val intent = Intent(requireActivity(), TabletMixerConfigActivity::class.java)
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
            var mixerReturned: TabletMixer? = null
            if (intent?.hasExtra(Constants.EXTRA_MIXER_DETAILS) == true){
                mixerReturned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent?.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS,  TabletMixer::class.java)
                } else {
                    intent?.getParcelableExtra< TabletMixer>(Constants.EXTRA_MIXER_DETAILS)
                }
            }
            Log.v(TAG,"mixerReturned $mixerReturned")
            mixerReturned?.let {
                Log.v(TAG,"MixerReturned ${it.name}  ${it.mac}  selectedTabletMixerInFragment $selectedTabletMixerInFragment")
                if(it.mac != selectedTabletMixerInFragment?.mac && selectedTabletMixerInFragment != null){
                    Log.v(TAG,"Disconnect MixerReturned")
                    (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
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
            menu?.findItem(R.id.menu_selected_tabler_mixer)?.title = "  " + tabletMixer.name
            selectedTabletMixerInFragment = tabletMixer
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
            (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).selectedTabletMixer = tabletMixer
            BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        }
    }

}