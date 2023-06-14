package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
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
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentRemoteViewerListBinding
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.setProgressDialog
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.adapter.CustomDynamicListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.RemoteViewerAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.RemoteViewerRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RemoteViewerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RemoteViewerViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt


class RemoteViewerListFragment : Fragment() {

    var menu: Menu? = null
    private lateinit var mBinding: FragmentRemoteViewerListBinding
    private var bluetoothDevices: MutableList<BluetoothDevice> = ArrayList()
    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    private var selectedBluetoothDevice : BluetoothDevice? = null
    private lateinit var mCustomListDialog : Dialog
    private val allBluetoothDeviceCustomList: java.util.ArrayList<CustomListItem> =
        java.util.ArrayList<CustomListItem>()

    private val mRemoteViewerViewModel: RemoteViewerViewModel by viewModels {
        RemoteViewerViewModelFactory((requireActivity().application as SpiMixerApplication).remoteViewerRepository)
    }
    private var mRemoteViewerViewModelRemote: RemoteViewerRemoteViewModel? = null
    private var mLocalRemoteViewers: List<RemoteViewer>? = null
    private var likingRemoteViewer: RemoteViewer? = null
    private var accumulatedWight = 0.0
    private var readCount = 0

    //    private var mProgressDialog: Dialog? = null
//    private var isFound = false
    private var dialog: AlertDialog? = null
    var dialogCustomListBinding: DialogCustomListBinding? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mRemoteViewerViewModel.allRemoteViewerList) {
            if (it != null) {
                liveDataMerger.value = RemoteViewerData(it)
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
                    is RemoteViewerData -> mLocalRemoteViewers = it.remoteViewers
                    else -> {}
                }

                if (mLocalRemoteViewers != null) {
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
        mBinding = FragmentRemoteViewerListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start Sync
        getLocalData()
        getLocalRemoteViewer()
        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                this@RemoteViewerListFragment.menu = menu
                menuInflater.inflate(R.menu.menu_remote_viewer_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_remote_viewer)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvRemoteViewersList.adapter as RemoteViewerAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvRemoteViewersList.adapter as RemoteViewerAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_remote_viewer -> {
                        // clearCompletedTasks()
                        startActivity(Intent(requireActivity(), AddUpdateRemoteViewerActivity::class.java))
                        goToAddUpdateRemoteViewer()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Bluethoot
        Log.i("BLUE", "Se inicia la búsqueda de dispositivos asociados")
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireActivity(), mBluetoothListener)
    }

    private fun getLocalRemoteViewer(){
        mBinding.rvRemoteViewersList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val remoteViewerAdapter =  RemoteViewerAdapter(this@RemoteViewerListFragment)
        mBinding.rvRemoteViewersList.adapter = remoteViewerAdapter
        mRemoteViewerViewModel.allRemoteViewerList.observe(viewLifecycleOwner) { remoteViewers ->
            remoteViewers.let{ _remote_viewers ->
                if (_remote_viewers.isNotEmpty()){
                    mBinding.rvRemoteViewersList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE

                    remoteViewerAdapter.remoteViewerList(_remote_viewers.filter { remoteViewer ->
                        remoteViewer.archiveDate.isNullOrEmpty()
                    }.toMutableList())

                    _remote_viewers?.forEach { remoteViewer ->
                        val isLinked = bluetoothDevices.firstOrNull { bluetoothDevice ->
                            bluetoothDevice.address == remoteViewer.mac
                        }
                        Log.i("BLUE", "[RemoteViewerListFragment] RemoteViewer ${remoteViewer.name}: ${isLinked!=null}")
                        (mBinding.rvRemoteViewersList.adapter as RemoteViewerAdapter).statusConnexion(remoteViewer, isLinked!=null)
                    }
                } else {
                    mBinding.rvRemoteViewersList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToAddUpdateRemoteViewer(){
        findNavController().navigate(RemoteViewerListFragmentDirections.actionRemoteViewerListFragmentToAddUpdateRemoteViewerActivity())
    }

    fun deleteRemoteViewer(remoteViewer: RemoteViewer){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_mixer))
        builder.setMessage(resources.getString(R.string.msg_delete_remote_viewer_dialog, remoteViewer.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mRemoteViewerViewModel.delete(remoteViewer)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun linkDevice(remoteViewer: RemoteViewer){
        likingRemoteViewer = remoteViewer
        dialog = setProgressDialog(requireActivity(), "Buscando un remoteViewer...")
        dialog?.show()
        (requireActivity() as MainActivity).mService?.LocalBinder()?.startDiscovery(requireActivity())
    }

    fun bluetoothConnectionError(){
        Log.i("BLUEFAT", "RemoteViewerlist")
        Snackbar.make(mBinding.remoteViewerListView, "No se pudo conectar con el dispositivo",
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()

        mCustomListDialog.dismiss()
    }

    fun bluetoothConnectionSuccess(){

        selectedBluetoothDevice?.let { selectedDevice ->
            likingRemoteViewer?.let {
                bluetoothDevices.add(selectedDevice)
                mRemoteViewerViewModel.setUpdatedMacAddress(it.id, selectedDevice.address)
                (mBinding.rvRemoteViewersList.adapter as RemoteViewerAdapter).statusConnexion(it, true)
            }
        }

        Snackbar.make(mBinding.remoteViewerListView, "Dispositivo vinculado",
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()
        mCustomListDialog.dismiss()
    }

    fun calibrateDevice(remoteViewer: RemoteViewer){
        likingRemoteViewer = remoteViewer
        //dialog = setProgressDialog(requireActivity(), "Calibrando remoteViewer...")
        //dialog?.show()
        Log.i("BLUE", "Cantidad: ${bluetoothDevices.size}")
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
            bd.address == remoteViewer.mac
        }

        if (deviceBluetooth != null){
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(deviceBluetooth)

        } else {
            Toast.makeText(requireActivity(), "Hubo un problema al iniciar la calibración. Intente nuevamente", Toast.LENGTH_SHORT).show()
        }
    }

    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        override fun onDiscoveryStarted() {
            Log.i("BLUE", "[RemoteViewerListFragment] onDiscoveryStarted")
            customItemsDialog("Dispositivos disponibles", ArrayList(), Constants.DEVICE_REF)
        }

        override fun onDiscoveryStopped() {
            Log.i("BLUE", "[RemoteViewerListFragment] onDiscoveryStopped")
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
                Toast.makeText(requireActivity(), "No se encontraron remoteViewers cercanos", Toast.LENGTH_SHORT).show()
            }
            */


        }




        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            val alreadyLinked = mLocalRemoteViewers?.firstOrNull {
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
                likingRemoteViewer?.let {
                    bluetoothDevice.add(device)
                    mRemoteViewerViewModel.setUpdatedMacAddress(it.id, device?.address)
                    (mBinding.rvRemoteViewersList.adapter as RemoteViewerAdapter).statusConnexion(it, true)
                }
            }
            */

        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            // Do stuff when is connected
            Log.i("BLUE", "[RemoteViewerListFragment] onDeviceConnected")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("BLUE", "[RemoteViewerListFragment] onMessageReceived")
            message?.let{msg->
                if (msg.length > 3){
                    readCount += 1
                    val currentRemoteViewerWeight = msg.substring(1, 7).toDoubleOrNull()
                    currentRemoteViewerWeight?.let {
                        accumulatedWight += it
                    }

                    if (readCount >= 10){
                        (requireActivity() as MainActivity).hideCustomProgressDialog()
                        val average = (accumulatedWight/readCount).roundToInt()

                        likingRemoteViewer?.let {

                            Toast.makeText(requireActivity(), "Se calibró el remoteViewer con un valor de ${average.toDouble()}Kg", Toast.LENGTH_SHORT).show()
                        }
//                        (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
                    }
                }
            }
        }

        override fun onMessageSent(device: BluetoothDevice?) {
            Log.i("BLUE", "[RemoteViewerListFragment] onMessageSent")
        }

        override fun onError(message: String?) {
            Log.i("BLUE", "[RemoteViewerListFragment] onError")
        }

        override fun onDeviceDisconnected() {
            Log.i("BLUE", "[RemoteViewerListFragment] onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i("BLUE", "[RemoteViewerListFragment] onBondedDevices: ${device?.size} | Local RemoteViewers: ${mLocalRemoteViewers?.size}")
            device?.let { bdList ->
                bluetoothDevices = bdList.toMutableList()
            }

            mLocalRemoteViewers?.forEach { remoteViewer ->
                val isLinked = device?.firstOrNull { bluetoothDevice ->
                    bluetoothDevice.address == remoteViewer.mac
                }
                Log.i("BLUE", "[RemoteViewerListFragment] RemoteViewer ${remoteViewer.name}: ${isLinked!=null}")
                (mBinding.rvRemoteViewersList.adapter as RemoteViewerAdapter).statusConnexion(remoteViewer, isLinked!=null)
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
        super.onDestroyView()
        (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireActivity(), mBluetoothListener)
        cleanObservers()
    }

    private fun cleanObservers(){
        mRemoteViewerViewModelRemote?.remoteViewersResponse?.value = null
        mRemoteViewerViewModelRemote?.remoteViewersLoadingError?.value = null
        mRemoteViewerViewModelRemote?.loadRemoteViewer?.value = null
        mRemoteViewerViewModelRemote?.addRemoteViewersResponse?.value = null
        mRemoteViewerViewModelRemote?.addRemoteViewerErrorResponse?.value = null
        mRemoteViewerViewModelRemote?.addRemoteViewersLoad?.value = null
        mRemoteViewerViewModelRemote?.updateRemoteViewersResponse?.value = null
        mRemoteViewerViewModelRemote?.updateRemoteViewersErrorResponse?.value = null
        mRemoteViewerViewModelRemote?.updateRemoteViewersLoad?.value = null

        mRemoteViewerViewModelRemote = null
        mLocalRemoteViewers = null
    }

    fun configRemoteViewer(remoteViewer: RemoteViewer, mode :Boolean = false):Boolean {
        Log.i("BLUE", "Cantidad: ${bluetoothDevices.size} remoteViewer.mac: ${remoteViewer.mac}")
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
            bd.address == remoteViewer.mac
        }
        val intent = Intent(requireActivity(), RemoteViewerConfigActivity::class.java)
        intent.putExtra(Constants.EXTRA_REMOTE_VIEWER_DETAILS, remoteViewer)
        intent.putExtra(Constants.EXTRA_REMOTE_VIEWER_MODE, mode)
        intent.putExtra("BTDEVICE",deviceBluetooth)
        startActivity(intent)


//        startActivity(Intent(requireActivity(), AddUpdateRemoteViewerActivity::class.java))
//        goToAddUpdateRemoteViewer()
        return true
    }

}