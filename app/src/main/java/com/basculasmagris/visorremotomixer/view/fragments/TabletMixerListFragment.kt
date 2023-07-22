package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
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
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.setProgressDialog
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.adapter.CustomDynamicListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.TabletMixerAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt


class TabletMixerListFragment : BottomSheetDialogFragment() {

    private var bConexionClosed: Boolean = false
    private val TAG : String = "DEBTML"
    var menu: Menu? = null
    private lateinit var mBinding: FragmentTabletMixerListBinding
    private var bluetoothDevices: MutableList<BluetoothDevice> = ArrayList()
    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    private var selectedBluetoothDevice : BluetoothDevice? = null
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null
    private lateinit var mCustomListDialog : Dialog
    private val allBluetoothDeviceCustomList: java.util.ArrayList<CustomListItem> =
        java.util.ArrayList<CustomListItem>()
    private var knowDevices: List<BluetoothDevice>? = null

    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerApplication).tabletMixerRepository)
    }
    private var mTabletMixerViewModelRemote: TabletMixerRemoteViewModel? = null
    private var mLocalTabletMixers: List<TabletMixer>? = null
    private var likingTabletMixer: TabletMixer? = null
    private var accumulatedWight = 0.0
    private var readCount = 0

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
                        // clearCompletedTasks()
                        startActivity(Intent(requireActivity(), AddUpdateTabletMixerActivity::class.java))
                        goToAddUpdateTabletMixer()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (requireActivity() as MainActivity).getSavedTabletMixer()
        // Bluethoot
        Log.i(TAG, "Se inicia la búsqueda de dispositivos asociados")
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
                        Log.i(TAG, "[TabletMixerListFragment] TabletMixer ${remoteViewer.name}: ${isLinked!=null}")
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
        closeConnection()
        findNavController().navigate(TabletMixerListFragmentDirections.actionTabletMixerListFragmentToRemoteMixerFragment())
    }

    private fun closeConnection() {
        Log.i(TAG,"closeconnection $bConexionClosed")
        if(!bConexionClosed){
            (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
            BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireActivity(), mBluetoothListener)
        }
        bConexionClosed = true
    }

    fun deleteTabletMixer(tabletMixer: TabletMixer){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_mixer))
        builder.setMessage(resources.getString(R.string.msg_delete_tablet_mixer_dialog, tabletMixer.name))
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

    fun linkDevice(tabletMixer: TabletMixer){
        likingTabletMixer = tabletMixer
        dialog = setProgressDialog(requireActivity(), "Buscando un tabletMixer...")
        dialog?.show()
        (requireActivity() as MainActivity).mService?.LocalBinder()?.startDiscovery(requireActivity())
    }

    fun bluetoothConnectionError(){
        Log.i("BLUEFAT", "TabletMixerlist")
        Snackbar.make(mBinding.tabletMixerListView, "No se pudo conectar con el dispositivo",
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()

        mCustomListDialog.dismiss()
    }

    fun bluetoothConnectionSuccess(){

        selectedBluetoothDevice?.let { selectedDevice ->
            likingTabletMixer?.let {
                bluetoothDevices.add(selectedDevice)
                mTabletMixerViewModel.setUpdatedMacAddress(it.id, selectedDevice.address)
                (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).statusConnexion(it, true)
            }
        }

        Snackbar.make(mBinding.tabletMixerListView, "Dispositivo vinculado",
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()
        mCustomListDialog.dismiss()
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
            (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(deviceBluetooth)

        } else {
//            Toast.makeText(requireActivity(), "Hubo un problema al iniciar la calibración. Intente nuevamente", Toast.LENGTH_SHORT).show()
        }
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i(TAG, "[TabletMixerListFragment] onDiscoveryStarted")
            customItemsDialog("Dispositivos disponibles", ArrayList(), Constants.DEVICE_REF)
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[TabletMixerListFragment] onDiscoveryStopped")
            dialog?.cancel()

            /*
            allBluetoothDeviceCustomList.clear()
            allBluetoothDevice.forEachIndexed { index, bluetoothDevice ->
                val name = if (bluetoothDevice.name == null) "No identificado" else bluetoothDevice.name
                val mac = if (bluetoothDevice.address == null) "00:00:00:00" else bluetoothDevice.address

                val item = CustomListItem(index.toLong(), 0, name, mac, R.drawable.icon_balance)
                allBluetoothDeviceCustomList.add(item)
            }*/

            //customItemsDialog("Dispositivos disponibles", allBluetoothDeviceCustomList, Constants.DEVICE_REF)

            /*
            if (!isFound) {
                Toast.makeText(requireActivity(), "No se encontraron tabletMixers cercanos", Toast.LENGTH_SHORT).show()
            }
            */


        }




        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            val alreadyLinked = mLocalTabletMixers?.firstOrNull {
                it.mac == device?.address
            }

            device?.let { currentDevice ->
                allBluetoothDevice.add(device)

                val name = if (currentDevice.name == null) "No identificado" else currentDevice.name
                val mac = if (currentDevice.address == null) "00:00:00:00" else currentDevice.address

                val item = CustomListItem(0L, 0, name, mac, R.drawable.icon_balance)
                //allBluetoothDeviceCustomList.add(item)
                (dialogCustomListBinding?.rvList?.adapter as CustomDynamicListItemAdapterFragment).updateList(item)

            }

            /*
            if (device?.name?.startsWith(Constants.KEY_PREFIX_DEVICE) == true /*&& (alreadyLinked == null)*/) {
                isFound = true
                dialog?.cancel()
                (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDevice(device)
                likingTabletMixer?.let {
                    bluetoothDevice.add(device)
                    mTabletMixerViewModel.setUpdatedMacAddress(it.id, device?.address)
                    (mBinding.rvTabletMixersList.adapter as TabletMixerAdapter).statusConnexion(it, true)
                }
            }
            */

        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            Log.i(TAG, "[TabletMixerListFragment] onDeviceConnected")
            (requireActivity() as MainActivity).deviceConnected()
        }


        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?){
            Log.i("command", "TabletMixerListFragment onCommandReceived ${message?.let { String(it) }}")
            (requireActivity() as MainActivity).deviceConnected()
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            (requireActivity() as MainActivity).deviceConnected()
            Log.i("message", "TabletMixerListFragment onMessageReceived $message")
            message?.let{msg->
                if (msg.length > 8){
                    readCount += 1
                    val currentTabletMixerWeight = msg.substring(1, 7).toDoubleOrNull()
                    currentTabletMixerWeight?.let {
                        accumulatedWight += it
                    }

                    if (readCount >= 10){
                        (requireActivity() as MainActivity).hideCustomProgressDialog()
                        val average = (accumulatedWight/readCount).roundToInt()

                        likingTabletMixer?.let {

                            Toast.makeText(requireActivity(), "Se calibró el remoteViewer con un valor de ${average.toDouble()}Kg", Toast.LENGTH_SHORT).show()
                        }
//                        (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                    }
                }
            }
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("${this.javaClass.name}", "onMessageSent $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
            Log.i("${this.javaClass.name}", "onCommandSent ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[TabletMixerListFragment] onError")
            (requireActivity() as MainActivity).deviceDisconnected()
        }

        override fun onDeviceDisconnected() {
            Log.i(TAG, "[TabletMixerListFragment] onDeviceDisconnected")
            (requireActivity() as MainActivity).deviceDisconnected()
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[TabletMixerListFragment] onBondedDevices: ${device?.size} | Local TabletMixers: ${mLocalTabletMixers?.size}")
            device?.let { bdList ->
                bluetoothDevices = bdList.toMutableList()
            }

            knowDevices = device

            knowDevices?.forEach{
                Log.i(TAG,"selectedTabletMixerInFragment $selectedTabletMixerInFragment \nbluetoothKnowed $it")
                if(selectedTabletMixerInFragment != null && selectedTabletMixerInFragment!!.mac == it.address){
                    if(it.address != tabletMixerBluetoothDevice?.address){
                        tabletMixerBluetoothDevice = it
                        Log.i(TAG,"Se seleccionó ${it.name} : ${it.address}")
                        connect(selectedTabletMixerInFragment!!)
                    }
                    return@forEach
                }
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

    private fun cleanObservers(){
        mTabletMixerViewModelRemote?.remoteViewersResponse?.value = null
        mTabletMixerViewModelRemote?.remoteViewersLoadingError?.value = null
        mTabletMixerViewModelRemote?.loadTabletMixer?.value = null
        mTabletMixerViewModelRemote?.addTabletMixersResponse?.value = null
        mTabletMixerViewModelRemote?.addTabletMixerErrorResponse?.value = null
        mTabletMixerViewModelRemote?.addTabletMixersLoad?.value = null
        mTabletMixerViewModelRemote?.updateTabletMixersResponse?.value = null
        mTabletMixerViewModelRemote?.updateTabletMixersErrorResponse?.value = null
        mTabletMixerViewModelRemote?.updateTabletMixersLoad?.value = null
        mTabletMixerViewModelRemote = null
        mLocalTabletMixers = null
        closeConnection()
    }

    fun configTabletMixer(tabletMixer: TabletMixer, mode :Boolean = false):Boolean {
        Log.i(TAG, "Cantidad: ${bluetoothDevices.size} tabletMixer.mac: ${tabletMixer.mac}")
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
            bd.address == tabletMixer.mac
        }
        val intent = Intent(requireActivity(), TabletMixerConfigActivity::class.java)
        intent.putExtra(Constants.EXTRA_TABLET_MIXER_DETAILS, tabletMixer)
        intent.putExtra(Constants.EXTRA_TABLET_MIXER_MODE, mode)
        intent.putExtra("BTDEVICE",deviceBluetooth)
        startActivity(intent)


//        startActivity(Intent(requireActivity(), AddUpdateTabletMixerActivity::class.java))
//        goToAddUpdateTabletMixer()
        return true
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
            Log.i(TAG,"setTabletMixer ${tabletMixerIn}")
            menu?.findItem(R.id.menu_selected_tabler_mixer)?.title = "  " + tabletMixer.name
            selectedTabletMixerInFragment = tabletMixer
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
        }
    }
}