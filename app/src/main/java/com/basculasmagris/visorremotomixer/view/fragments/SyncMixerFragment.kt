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
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentSyncBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SyncMixerFragment : Fragment() {

    private lateinit var mBinding: FragmentSyncBinding
    private val TAG = "DEBSync"
    private var bSyncroRounds: Boolean = false
    private var bSyncroTablets: Boolean = false
    private var bSyncroUsers: Boolean = false

    private var menu: Menu? = null
    private var selectedTabletInFragment: TabletMixer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_remote_mixer_fragment, menu)
                this@SyncMixerFragment.menu = menu
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
                menu.findItem(R.id.bluetooth_balance).isVisible = false
                menu.findItem(R.id.bluetooth_remote_status).isVisible = false
                menu.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + selectedTabletInFragment?.name

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


                    R.id.bluetooth_balance -> {
                        (requireActivity() as MainActivity).sendReconnectBalance()
                        (requireActivity() as MainActivity).showCustomProgressDialog()
                        return true
                    }


                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        mBinding = FragmentSyncBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding.llSyncNoInternet.visibility = View.INVISIBLE
        mBinding.btnSync.setOnClickListener{
            GlobalScope.launch (Dispatchers.Main) {
                syncData()
            }
        }

        mBinding.btnBack.setOnClickListener {
            if (isAdded) findNavController().popBackStack(R.id.nav_home, false)
        }
        if(isAdded){
            (requireActivity() as MainActivity).clearbShowDevice()
        }
        selectedTabletInFragment = (requireActivity() as MainActivity).selectedTabletInActivity
        if(selectedTabletInFragment != null){
            menu?.findItem(R.id.menu_selected_remote_tablet)?.title = "  " + selectedTabletInFragment?.name
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
                    bSyncroUsers = (requireActivity() as MainActivity).processUsers(message)
                    if(bSyncroUsers){
                        mBinding.pbUsers.progress = 100
                        mBinding.tvUsersPercentage.text = "100%"
                    }
                    bSyncroUsers = false
                }

                Constants.CMD_TABLET->{
                    Log.i("showCommand","CMD_TABLET")
                    if(isAdded){
                        bSyncroTablets = (activity as MainActivity).processTabletInfo(message)
                        if(bSyncroTablets){
                            mBinding.pbTablet.progress = 100
                            mBinding.tvTabletPercentage.text = "100%"
                        }
                        bSyncroTablets = false
                    }
                }

                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS SF")
                    bSyncroRounds = (requireActivity() as MainActivity).refreshRounds(message)
                    if(bSyncroRounds){
                        mBinding.pbRounds.progress = 100
                        mBinding.tvRoundsPercentage.text = "100%"
                    }
                    bSyncroRounds = false
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

                Constants.CMD_NTA ->{
                    Log.i("showCommand","CMD_NTA")
                    if(isAdded){
                        (requireActivity() as MainActivity).alertDialog(getString(R.string.warning),getString(
                            R.string.no_disponible))
                    }
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
            if(isAdded)
                (requireActivity() as MainActivity).changeStatusDisconnected()        }

        override fun onDeviceDisconnected() {
            if(isAdded)
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
        Log.i(TAG,"syncData")

        if(isAdded){
            (requireActivity() as MainActivity).sendRequestListOfUsers()
            (requireActivity() as MainActivity).deleteRoundsFromDB()
        }
        delay(200)
        if(isAdded)
            (requireActivity() as MainActivity).sendRequestListOfRounds()
        delay(200)
        if(isAdded)
            (requireActivity() as MainActivity).sendRequestTablet()

    }




}