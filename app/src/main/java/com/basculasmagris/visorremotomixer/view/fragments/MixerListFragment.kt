package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.databinding.FragmentMixerListBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.adapter.CustomDynamicListItemAdapterFragment
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.MixerAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.MixerRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MixerListFragment : Fragment() {

    private lateinit var mBinding: FragmentMixerListBinding
    private var bluetoothDevices: MutableList<BluetoothDevice> = ArrayList()
    private var allBluetoothDevice: MutableList<BluetoothDevice> = ArrayList()
    private var selectedBluetoothDevice : BluetoothDevice? = null
    private lateinit var mCustomListDialog : Dialog
    var myMenu: Menu? = null
    private var mMixerViewModelRemote: MixerRemoteViewModel? = null
    private var mLocalMixers: List<Mixer>? = null
    var selectedMixer: Mixer? = null
    private var readCount = 0
    private var dialog: AlertDialog? = null
    var dialogCustomListBinding: DialogCustomListBinding? = null

    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((requireActivity().application as SpiMixerApplication).mixerRepository)
    }
    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mMixerViewModel.allMixerList) {
            if (it != null) {
                liveDataMerger.value = MixerData(it)
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
                    is MixerData -> mLocalMixers = it.mixers
                    else -> {}
                }

                if (mLocalMixers != null) {
                    (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
                    liveData.removeObserver(this)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentMixerListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("DEBUG","MixerListFragment onViewCreated")
        // Start Sync
        getLocalData()
        getLocalMixer()
        // Navigation Menu
//        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                // Add menu items here
//                myMenu = menu
//                menuInflater.inflate(R.menu.menu_mixer_list, menu)
//
//                // Associate searchable configuration with the SearchView
//                val search = menu.findItem(R.id.search_mixer)
//                val searchView = search.actionView as SearchView
//                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        (mBinding.rvMixersList.adapter as MixerAdapter).filter.filter(query)
//                        return false
//                    }
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        (mBinding.rvMixersList.adapter as MixerAdapter).filter.filter(newText)
//                        return true
//                    }
//                })
//            }
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                // Handle the menu selection
//                return when (menuItem.itemId) {
//                    R.id.action_add_mixer -> {
//                        (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
//                        val intent = Intent(requireActivity(), MixerConfigActivity::class.java)
//                        startActivity(intent)
//                        return true
//                    }
//                    else -> false
//                }
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
//
//        // Bluethoot
//        Log.i("BLUE", "Se inicia la búsqueda de dispositivos asociados")
//        getSavedMixer()
//        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireActivity(), mBluetoothListener)
//        selectedMixer?.let { connectDevice(it) }
    }

    private fun getLocalMixer(){
        mBinding.rvMixersList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val mixerAdapter =  MixerAdapter(this@MixerListFragment)
        mBinding.rvMixersList.adapter = mixerAdapter
        mMixerViewModel.allMixerList.observe(viewLifecycleOwner) { mixers ->
            mixers.let{ _mixers ->
                if (_mixers.isNotEmpty()){
                    mBinding.rvMixersList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE

                    mixerAdapter.mixerList(_mixers.filter { mixer ->
                        mixer.archiveDate.isNullOrEmpty()
                    }.toMutableList())

                    _mixers?.forEach { mixer ->
                        val isLinked = bluetoothDevices.firstOrNull { bluetoothDevice ->
                            bluetoothDevice.address == mixer.mac
                        }
                        Log.i("BLUE", "[MixerListFragment] Mixer ${mixer.name}: ${isLinked!=null}")
                        (mBinding.rvMixersList.adapter as MixerAdapter).statusConnexion(mixer, isLinked!=null)
                    }
                } else {
                    mBinding.rvMixersList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getLocalData()
        Log.i("DEBUG","onResume MixerListFragment")
        myMenu?.findItem(R.id.menu_selected_mixer)?.title = "   " + selectedMixer?.name

    }



    fun deleteMixer(mixer: Mixer){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_mixer))
        builder.setMessage(resources.getString(R.string.msg_delete_mixer_dialog, mixer.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mMixerViewModel.delete(mixer)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun bluetoothConnectionError(){
        Log.i("BLUEFAT", "Mixerlist")
        Snackbar.make(mBinding.mixerListView, "No se pudo conectar con el dispositivo",
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()

        mCustomListDialog.dismiss()
    }

    fun bluetoothConnectionSuccess(){

        selectedBluetoothDevice?.let { selectedDevice ->
            selectedMixer?.let {
                bluetoothDevices.add(selectedDevice)
                mMixerViewModel.setUpdatedMacAddress(it.id, selectedDevice.address)
                (mBinding.rvMixersList.adapter as MixerAdapter).statusConnexion(it, true)
            }
        }

        Snackbar.make(mBinding.mixerListView, "Dispositivo vinculado",
            BaseTransientBottomBar.LENGTH_SHORT
        ).show()
        mCustomListDialog.dismiss()
    }

    private fun connectDevice(mixer: Mixer){
        Log.i("DEBUG", "Cantidad: ${bluetoothDevices.size} | mixer ${mixer.name}  mixerMac ${mixer.mac}\nMixer: $mixer")
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
             bd.address == mixer.mac
        }

        if (deviceBluetooth != null){
            (requireActivity() as MainActivity).showCustomProgressDialog()
            (requireActivity() as MainActivity).mService?.LocalBinder()?.connectKnowDeviceWithTransfer(deviceBluetooth)

        } else {
            Log.i("DEBUG","MixerListFragment deviceBluetooth null")
        }
    }

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

                val name = if (currentDevice.name == null) "No identificado" else currentDevice.name
                val mac = if (currentDevice.address == null) "00:00:00:00" else currentDevice.address

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
            Log.i("command", "MixerListFragment onCommandReceived ${message?.let { String(it) }}")
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("message", "MixerListFragment onMessageReceived $message")
            message?.let{
                readCount++
                if(readCount>50){
                    Log.i("DEBUG","MixerListFragment message $message")
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
            Log.i("BLUE", "[MixerListFragment] onError")
        }

        override fun onDeviceDisconnected() {
            Log.i("BLUE", "[MixerListFragment] onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i("BLUE", "[MixerListFragment] onBondedDevices: ${device?.size} | Local Mixers: ${mLocalMixers?.size}")
            device?.let { bdList ->
                bluetoothDevices = bdList.toMutableList()
            }

            mLocalMixers?.forEach { mixer ->
                val isLinked = device?.firstOrNull { bluetoothDevice ->
                    bluetoothDevice.address == mixer.mac
                }
                Log.i("BLUE", "[MixerListFragment] Mixer ${mixer.name}: ${isLinked!=null}")
                (mBinding.rvMixersList.adapter as MixerAdapter).statusConnexion(mixer, isLinked!=null)
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
        Log.i("DEBUG","MixerListFragment onDestroy")
        (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireActivity(), mBluetoothListener)
        cleanObservers()
    }

    private fun cleanObservers(){
        mMixerViewModelRemote?.mixersResponse?.value = null
        mMixerViewModelRemote?.mixersLoadingError?.value = null
        mMixerViewModelRemote?.loadMixer?.value = null
        mMixerViewModelRemote?.addMixersResponse?.value = null
        mMixerViewModelRemote?.addMixerErrorResponse?.value = null
        mMixerViewModelRemote?.addMixersLoad?.value = null
        mMixerViewModelRemote?.updateMixersResponse?.value = null
        mMixerViewModelRemote?.updateMixersErrorResponse?.value = null
        mMixerViewModelRemote?.updateMixersLoad?.value = null
        mMixerViewModelRemote = null
        mLocalMixers = null
    }

    fun configMixer(mixer:Mixer, isDetail :Boolean = false):Boolean {
        Log.i("BLUE", "Cantidad: ${bluetoothDevices.size} | mixer.name ${mixer.name}  | mixer.mac: ${mixer.mac}")
        val deviceBluetooth = bluetoothDevices.firstOrNull { bd->
            bd.address == mixer.mac
        }

        Log.d("BLUEMixListFrag", "Go to config Activity. BTDevice: $deviceBluetooth")
        (requireActivity() as MainActivity).mService?.LocalBinder()?.disconnectKnowDeviceWithTransfer()
        val intent = Intent(requireActivity(), MixerConfigActivity::class.java)
        intent.putExtra(Constants.EXTRA_MIXER_DETAILS, mixer)
        intent.putExtra(Constants.EXTRA_MIXER_MODE, isDetail)
        intent.putExtra("BTDEVICE",deviceBluetooth)
        startActivity(intent)
        return true
    }

    private fun getSavedMixer(){
        lifecycleScope.launch(Dispatchers.IO){
            val flowLong = getSavedMixerId()
            flowLong.collect { id ->
                if(id==null){
                    return@collect
                }
                val localKnowDevice = mMixerViewModel.getMixerById(id)
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        localKnowDevice.observe(viewLifecycleOwner ){mixer->
                            if (mixer != null){
                                selectedMixer = mixer
                                myMenu?.findItem(R.id.menu_selected_mixer)?.title = "   " + mixer.name
                                (mBinding.rvMixersList.adapter as MixerAdapter).selectedMixer = mixer
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getSavedMixerId() = (requireActivity() as MainActivity).datastore.data.map { preferences->
        preferences[longPreferencesKey("IDMIXER")]
    }

}