package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.gson.Gson


class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val TAG = "DEBHome"
    private var selectedTabletMixerInFragment: TabletMixer? = null
    private var tabletMixerBluetoothDevice : BluetoothDevice? = null

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

        mBinding.btnRondaLibre.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeToFreeRound())
        }


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
        }

        override fun onCommandReceived(device: BluetoothDevice?, message: ByteArray?) {
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
            Log.i("SHOWCOMAND","command $command")

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
                            (requireActivity() as MainActivity).minRoundRunDetail = roundRunDetail
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
                    Log.i("showCommand","CMD_ROUNDS")
                }

                Constants.CMD_DLG_PRODUCT->{
                    Log.i("showCommand","CMD_DLG_PRODUCT")
                    (requireActivity() as MainActivity).dlgProduct(message)
                }


                Constants.CMD_DLG_EST->{
                    Log.i("showCommand","CMD_DLG_EST")
                    (requireActivity() as MainActivity).dlgEstablishment(message)
                }


                Constants.CMD_DLG_CORRAL->{
                    Log.i("showCommand","CMD_DLG_CORRAL")
                    (requireActivity() as MainActivity).dlgCorral(message)
                }


                Constants.CMD_WEIGHT->{
                    Log.v("cmd_weight","CMD_WEIGHT")
                }


                Constants.CMD_WEIGHT_CONFIG->{
                    Log.v("cmd_weight","CMD_WEIGHT_CONFIG")
                }

                Constants.CMD_WEIGHT_RESUME->{
                    Log.v("cmd_weight","CMD_WEIGHT_RESUME")
                }

                Constants.CMD_WEIGHT_LOAD->{

                }

                Constants.CMD_WEIGHT_DWNL->{
                }

                Constants.CMD_WEIGHT_LOAD_FREE->{
                }

                Constants.CMD_WEIGHT_DWNL_FREE->{
                }

                Constants.CMD_MIXER ->{
                }
                else->{
                    Log.i(TAG,"else $command")
                }
            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("message","message $message")
            (requireActivity() as MainActivity).changeStatusConnected()
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
            Toast.makeText(requireActivity(), "No se pudo conectar", Toast.LENGTH_SHORT).show()
        }
    }


    fun setTabletMixer(tabletMixerIn: TabletMixer) {
        tabletMixerIn.let { tabletMixer ->
//            menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + tabletMixer.name
            selectedTabletMixerInFragment = tabletMixer
            Log.i(
                TAG,
                "setTabletMixer $tabletMixer  \nmService ${(requireActivity() as MainActivity).mService}"
            )
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
        }
    }

}