package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentSyncBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.basculasmagris.visorremotomixer.viewmodel.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class SyncFragment : Fragment() {

    private lateinit var mBinding: FragmentSyncBinding
    private val TAG = "DEBSync"
    private var bSyncroRounds: Boolean = false
    private var bSyncroUsers: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentSyncBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnSync.setOnClickListener{
            GlobalScope.launch (Dispatchers.Main) {
                syncData()
            }
            
        }

        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)

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
            val messageStr = String(message,0, message.size)
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","command $command")
            Log.i("message", String(message))
            when (command){

                Constants.CMD_ROUNDDETAIL->{
                 }

                Constants.CMD_ROUNDDATA->{
                }

                Constants.CMD_USER_LIST->{
                    Log.i("showCommand","CMD_USER_LIST")
                    bSyncroUsers = (requireActivity() as MainActivity).refreshUsers(message)
                    if(bSyncroUsers){
                        mBinding.pbUsers.progress = 100
                    }
                    bSyncroUsers = false
                }

                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS")
                    bSyncroRounds = (requireActivity() as MainActivity).refreshRounds(message)
                    if(bSyncroRounds){
                        mBinding.pbRounds.progress = 100
                    }
                    bSyncroRounds = false
                }

//                Constants.CMD_PRODUCT->{
//                    Log.i("showCommand","CMD_PRODUCT")
//                    bSyncroProducts = (requireActivity() as MainActivity).refreshProducts(message)
//                }
//
//                Constants.CMD_CORRAL->{
//                    Log.i("showCommand","CMD_CORRAL")
//                    bSyncroCorrals = (requireActivity() as MainActivity).refreshCorrals(message)
//                }
//
//                Constants.CMD_ESTAB_LIST->{
//                    Log.i("showCommand","CMD_ESTAB_LIST")
//                    bSyncroEstablishment = (requireActivity() as MainActivity).refreshEstablishments(message)
//                }

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

                else->{
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
            Log.i(TAG, "[Sync] ACT onError")
            (requireActivity() as MainActivity).changeStatusDisconnected()        }

        override fun onDeviceDisconnected() {
            (requireActivity() as MainActivity).changeStatusDisconnected()
            Log.i(TAG, "[Sync]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
            Log.i(TAG, "[Sync]onBondedDevices ${device?.size} \n$device")
        }
    }
    override fun onDestroyView() {
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        super.onDestroyView()
    }



    private suspend fun syncData(){
        (requireActivity() as MainActivity).requestListOfUsers()
        delay(1000)
        (requireActivity() as MainActivity).requestListOfRounds()

    }


}