package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentResumeBinding
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.MarginItemDecoration
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralResumeAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductResumeAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.gson.Gson
import com.kofigyan.stateprogressbar.StateProgressBar

class ResumeFragment : Fragment() {
    private lateinit var mBinding: FragmentResumeBinding
    private var stepsDescription = arrayOf("Configuración","Carga", "Descarga", "Fin")
    private val TAG = "DEBRF"
    private val REFRESH_TIME = 10
    private var countMsg: Int = REFRESH_TIME
    private var bSyncroRoundDetail = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showResume()
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    private fun showResume() {
        val activity = (requireActivity() as MainActivity)
        val currentUser = Helper.getCurrentUser(activity)
        (requireActivity() as MainActivity).minRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvRoundProductsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
            val roundRunProductAdapter =  RoundRunProductResumeAdapter(
                this@ResumeFragment)
            dietDetail.products.let { products ->
                roundRunProductAdapter.productList(products)
            }
            mBinding.rvRoundProductsToLoad.adapter = roundRunProductAdapter
        }
        mBinding.spRoundRunStep.setStateDescriptionData(stepsDescription)
        mBinding.spRoundRunStep.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
        mBinding.rvRoundCorralsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
        mBinding.rvRoundProductsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))
        mBinding.rvRoundCorralsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))

        val roundRunCorralAdapter =  RoundRunCorralResumeAdapter(
            this@ResumeFragment)
        (requireActivity() as MainActivity).minRoundRunDetail?.round?.corrals?.let { corrals ->
            roundRunCorralAdapter.corralList(corrals)
        }
        mBinding.rvRoundCorralsToLoad.adapter = roundRunCorralAdapter


        val loadDiff = activity.getLoadDifference()
        if (loadDiff > -1 && loadDiff < 1){
            mBinding.tvTotalLoadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalLoad(), 0)}Kg     +${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
        } else if (loadDiff >= 1){
            mBinding.tvTotalLoadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalLoad(), 0)}Kg     +${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
        } else {
            mBinding.tvTotalLoadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalLoad(), 0)}Kg     ${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
        }

        val downloadDiff = activity.getDownloadDifference()
        if (downloadDiff >-1 && downloadDiff < 1){
            mBinding.tvTotalDownloadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalDownload(),0)}Kg     +${Helper.getNumberWithDecimals(downloadDiff,0)}Kg"
        } else if (downloadDiff >= 1){
            mBinding.tvTotalDownloadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalDownload(),0)}Kg     +${Helper.getNumberWithDecimals(downloadDiff,0)}Kg"
        } else {
            mBinding.tvTotalDownloadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalDownload(),0)}Kg     ${Helper.getNumberWithDecimals(downloadDiff,0)}Kg"
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentResumeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    var oneShot = true
    fun exitFragment(){
       if(oneShot){
           Log.i(TAG,"exitFragment")
           (requireActivity() as MainActivity).sendEndToMixer()
           BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
        try{
            findNavController().popBackStack(R.id.nav_home, false)
            findNavController().navigate(ResumeFragmentDirections.actionResumeFragmentToHomeFragment())
        }catch (e : Exception){
            Log.i(TAG,"Nav Exception $e")
        }

       }
        oneShot = false
    }


    private val mBluetoothListener: IBluetoothSDKListener = object : IBluetoothSDKListener {
        init {
            Log.i(TAG,"create mBluetoothListener")
        }

        override fun onDiscoveryStarted() {
            Log.i(TAG, "[MixRem] ACT onDiscoveryStarted")
        }

        override fun onDiscoveryStopped() {
            Log.i(TAG, "[MixRem] ACT onDiscoveryStopped")
        }

        override fun onDeviceDiscovered(device: BluetoothDevice?) {
            Log.i(TAG, "[MixRem] ACT onDeviceDiscovered")
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
            val command = messageStr.substring(0,3)
            Log.i("SHOWCOMAND","command $command")
            Log.i("message", String(message))
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
                                (requireActivity() as MainActivity).minRoundRunDetail = roundRunDetail
                                (requireActivity() as MainActivity).minRoundRunDetail?.let { minRoundRunDetail->
                                    val title = "Mixer: ${mixer?.name} - ${minRoundRunDetail.round.name} : ${minRoundRunDetail.round.diet.name}"
                                    (requireActivity() as MainActivity).changeActionBarTitle(title)
                                }
                                bSyncroRoundDetail = true
                                showResume()
                            }

                        }
                    }catch (e: NumberFormatException){
                        Log.i("showCommand","CMD_ROUNDDETAIL NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i("showCommand","CMD_ROUNDDETAIL Exception $e")
                    }

                }

                Constants.CMD_WEIGHT->{
                    Log.v("commandsWeight","CMD_WEIGHT")
                    exitFragment()
                }

                Constants.CMD_WEIGHT_LOAD->{
                    Log.v("commandsWeight","CMD_WEIGHT_LOAD")
                    exitFragment()
                }

                Constants.CMD_WEIGHT_DWNL->{
                    Log.v("commandsWeight","CMD_WEIGHT_DWNL")
                    exitFragment()
                }

                Constants.CMD_WEIGHT_LOAD_FREE->{
                    Log.v("commandsWeight","CMD_WEIGHT_LOAD_FREE")
                    exitFragment()
                }

                Constants.CMD_WEIGHT_DWNL_FREE->{
                    Log.v("commandsWeight","CMD_WEIGHT_DWNL_FREE")
                    exitFragment()
                }

                Constants.CMD_WEIGHT_RESUME->{
                    if(!bSyncroRoundDetail && countMsg++ > REFRESH_TIME){
                        (requireActivity() as MainActivity).requestRoundRunDetail()
                        countMsg = 0
                    }

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
            Log.i(TAG, "[MixRem] ACT onError")
            (requireActivity() as MainActivity).changeStatusDisconnected()        }

        override fun onDeviceDisconnected() {
            (requireActivity() as MainActivity).changeStatusDisconnected()
            Log.i(TAG, "[MixRem]ACT onDeviceDisconnected")
        }

        override fun onBondedDevices(device: List<BluetoothDevice>?) {
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                exitFragment()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)}
        }
    }
}