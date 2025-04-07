package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
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
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.gson.Gson


class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val TAG = "DEBHome"
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null
    private var menu:Menu? = null
    private var bInFree: Boolean = true
    private var bInCfg: Boolean = false
    private var bInRes: Boolean = false
    private var bInLoad: Boolean = false
    private var bInDownload : Boolean = false

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
                    val title = "Inicio - ${user.name} ${user.lastname}"
                    it.title = title

                }
                menu.findItem(R.id.cancel_round).isVisible = false

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_selected_remote_tablet -> {
                        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
                            bd.address == selectedTabletMixerInFragment?.mac
                        }
                        Log.v(TAG,"Force connection ${deviceBluetooth?.name} $selectedTabletMixerInFragment")
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



        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding.btnMixer.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeToMixer())
        }

        mBinding.btnSincro.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeToSync())
        }

        mBinding.btnRondas.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeToRound())
        }

//        mBinding.btnRondaLibre.setOnClickListener{
//            findNavController().navigate(HomeFragmentDirections.actionHomeToFreeRound(selectedTabletMixerInFragment,1))
//        }


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).getSavedTabletMixer()
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
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
            if(isAdded)
                (requireActivity() as MainActivity).sendRequestListOfRounds()
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
                        Log.i("Json","jsonString * ${jsonString}")
                        if(jsonString.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : MinRoundRunDetail = gson.fromJson(jsonString,  MinRoundRunDetail::class.java)
                            (requireActivity() as MainActivity).updateRoundDetail(roundRunDetail)
                            roundRunDetail.mixer?.let {mixerDetail->
                                val mixer = Mixer(
                                    mixerDetail.name,
                                    mixerDetail.description,
                                    mixerDetail.mac,
                                    "",
                                    0.0,
                                    1F,
                                    0L,
                                    0,
                                    "",
                                    "",
                                    true,
                                    mixerDetail.id
                                )
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
                        (requireActivity() as MainActivity).weightReceibed()
                }

                Constants.CMD_START_TIMER->{
                    Log.v("cmd_weight","Home CMD_START_TIMER ${String(message)}")
                    Log.i("showCommand","Home CMD_START_TIMER ${String(message)}")
                    if(isAdded)
                        (requireActivity() as MainActivity).weightReceibed()
                }

                Constants.CMD_MIXER ->{
                }


                Constants.CMD_NTA ->{
                    Log.i("showCommand","CMD_NTA")
                    if(isAdded){
                        (requireActivity() as MainActivity).alertDialog(getString(R.string.warning),getString(R.string.no_disponible))
                    }
                }

                else->{
                    Log.i(TAG,"else $command")
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("message","HF message $message")
            (requireActivity() as MainActivity).beaconReceibed()
        }

        override fun onMessageSent(device: BluetoothDevice?, message: String?) {
            Log.i("sent", "onMessageSent ${device?.address} $message")
        }

        override fun onCommandSent(device: BluetoothDevice?, command: ByteArray?) {
            Log.i("sent", "onCommandSent ${device?.address} ${command?.let { String(it) }}")
        }

        override fun onError(message: String?) {
            Log.i(TAG, "[Home] ACT onError")
            (requireActivity() as MainActivity).changeStatusDisconnected()        }

        override fun onDeviceDisconnected() {
            (requireActivity() as MainActivity).changeStatusDisconnected()
            Log.i(TAG, "[Home]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[Home]onBondedDevices ${device?.size} \n$device")
            (requireActivity() as MainActivity).knowDevices = device
            selectedTabletMixerInFragment?.let {
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
            (requireActivity() as MainActivity).weightReceibed()
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

    override fun onResume() {
        Log.i(TAG,"onResume")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onResume()
    }

    override fun onStop() {
        Log.i(TAG,"onStop")
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onStop()
    }
}