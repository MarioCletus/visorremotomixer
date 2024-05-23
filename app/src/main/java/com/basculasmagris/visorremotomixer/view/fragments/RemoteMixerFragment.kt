package com.basculasmagris.visorremotomixer.view.fragments

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding
import com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail
import com.basculasmagris.visorremotomixer.model.entities.MinDietDetail
import com.basculasmagris.visorremotomixer.model.entities.MinProductDetail
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunData
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.ConvertZip
import com.basculasmagris.visorremotomixer.utils.MarginItemDecorationHorizontal
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class RemoteMixerFragment : BottomSheetDialogFragment() {


    lateinit var mBinding: FragmentRemoteMixerBinding
    private val TAG: String = "DEBMixerVR"
    var menu: Menu? = null
    private var dialogResto: AlertDialog? = null
    private var dialogTara: AlertDialog? = null
    private var rest: Int = 0
    private var timerTask: TimerTask? = null
    private var estableTimer: Timer? = null
    private var activity: MainActivity? = null
    private var noPrevAlert : Boolean = true
    private var countGreaterThanTarget: Int = 0
    private var currentProductDetail: MinProductDetail? = null
    private var mixerDetail: MixerDetail? = null
    var roundRunProductAdapter : RoundRunProductAdapter? = null
    private var currentCorralDetail : MinCorralDetail? = null
    var roundRunCorralAdapter : RoundRunCorralDownloadAdapter? = null

    private var bInFree: Boolean = true
    private var bInCfg: Boolean = false
    private var bInRes: Boolean = false
    private var bInLoad: Boolean = false
    private var bInDownload : Boolean = false

    private var bSyncroUsers = false
    private var bSyncroRounds = false

    private var selectedMixerInFragment: Mixer? = null
    private var selectedTabletMixerInFragment: TabletMixer? = null

    private var targetReachedDialog: AlertDialog? = null
    private var dialogCountDown: AlertDialog? = null

    private val REFRESH_VIEW_TIME = 20
    private var countMsg: Int = REFRESH_VIEW_TIME
    private val REFRESH_DATA_TIME = 4
    private var countDataMsg: Int = REFRESH_DATA_TIME
    private var count_resume = 0
    private var count_weight = 0
    private var contPressTara = 0
    private var weight:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG,"onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = (requireActivity() as MainActivity)
        activity?.showCustomProgressDialog()

        val args: RemoteMixerFragmentArgs by navArgs()

        selectedTabletMixerInFragment = args.tabletMixer
        val tipoRonda = args.tipoRonda
        Log.i(TAG,"tabletMixer $selectedTabletMixerInFragment")

        mBinding.btnTara.setOnClickListener{
            if(contPressTara++ > 3 ){
                dialogTara = tareDialog("Tara","Mantenga presionado el boton para hacer tara")
                contPressTara = 0
            }
        }

        mBinding.btnTara.setOnLongClickListener{
            Log.i(TAG,"Send CMD_TARA")
            (requireActivity() as MainActivity).sendTareToMixer()
            contPressTara = 0
            return@setOnLongClickListener false
        }


        mBinding.btnInitFreeRound.setOnClickListener{
            (requireActivity() as MainActivity).sendGoToFreeRound()
        }

        mBinding.btnJump.setOnClickListener{
            Log.i(TAG, "btnJump")

            if(bInCfg){
                (requireActivity() as MainActivity).sendIniToMixer()
                return@setOnClickListener
            }
            if(bInFree){
                if(bInLoad){
                    (requireActivity() as MainActivity).requestListOfProducts()
                    return@setOnClickListener
                }else{
                    (requireActivity() as MainActivity).requestListOfCorrals()
                    return@setOnClickListener
                }
            }else{
                if(bInLoad){
                    sendNextProduct()
                    return@setOnClickListener
                }
                if(bInDownload){
                    sendNextCorral()
                    return@setOnClickListener
                }
            }
        }

        mBinding.btnJump.setOnLongClickListener {
            Log.i(TAG, "btnJump")
            if(bInFree && bInLoad){
                alertFinalDialog()
                return@setOnLongClickListener false
            }
            (requireActivity() as MainActivity).sendEndToMixer()
            return@setOnLongClickListener false
        }

        mBinding.btnPause.setOnCheckedChangeListener { v, isChecked ->
            Log.i(TAG,"setOnCheckedChangeListener $isChecked")
            if(!bInCfg && !bInLoad && !bInDownload && !bInRes){
                return@setOnCheckedChangeListener
            }
            if(isChecked){
                v.setBackgroundResource(R.drawable.btn_round_to_run_red)
                val msg = "CMD${Constants.CMD_PAUSE_ON}"
                activity?.mService?.LocalBinder()?.write(msg.toByteArray())
            }else{
                val msg = "CMD${Constants.CMD_PAUSE_OFF}"
                activity?.mService?.LocalBinder()?.write(msg.toByteArray())
                v.setBackgroundResource(R.drawable.btn_round_rounded_green)
            }
        }

        mBinding.btnRest.setOnClickListener{
            dialogResto = if(dialogResto == null){
                (requireActivity() as MainActivity).sendRestToMixer()
                restDialog()
            }else{
                dialogResto?.dismiss()
                (requireActivity() as MainActivity).sendCloseDlgToMixer()
                null
            }
        }

        loadRoundDetail()
        mBinding.rvMixerProductsToLoad.addItemDecoration(MarginItemDecorationHorizontal(resources.getDimensionPixelSize(R.dimen.margin_recycler_horizontal)))

        //Conectar bluetooth
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(requireContext(), mBluetoothListener)
        (requireActivity() as MainActivity).minRoundRunDetail?.state = Constants.STATE_LOAD
        Log.i(TAG,"onViewCreated ready")
        if(tipoRonda == 1){
            (requireActivity() as MainActivity).sendGoToFreeRound()
            mBinding.btnInitFreeRound.visibility = View.VISIBLE
            mBinding.btnJump.visibility = View.GONE
            mBinding.btnTara.visibility = View.GONE
            mBinding.btnPause.visibility = View.GONE
            mBinding.btnRest.visibility = View.GONE
        }else{
            mBinding.btnInitFreeRound.visibility = View.INVISIBLE
        }

    }

    private fun loadRoundDetail() {
        Log.i("loadRoundDetail","loadRoundDetail ${(requireActivity() as MainActivity).minRoundRunDetail}")
        (requireActivity() as MainActivity).minRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvMixerProductsToLoad.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
            if(bInLoad){
                roundRunProductAdapter =  RoundRunProductAdapter(
                    this@RemoteMixerFragment)
                dietDetail.products.let { products ->
                    roundRunProductAdapter?.productList(products)
                }

                mBinding.rvMixerProductsToLoad.adapter = roundRunProductAdapter

                if(bInFree){
                    Log.i(TAG,"dietDetail ${dietDetail.products}")
                }
                dietDetail.products.let { products ->
                    if(products.isNotEmpty()){
                        products.forEach {
                            Log.i("list_products","${it.name} ${it.order}")
                        }

                        var currentProduct = products[0]
                        var position = 0
                        products.sortedBy { it.order }.forEachIndexed{index, productInRound ->
                            if(productInRound.finalWeight == 0L && productInRound.initialWeight != 0L){
                                currentProduct = productInRound
                                Log.i("select_product","Product selected: $index ${productInRound.name}  ")
                                roundRunProductAdapter?.selectProduct(index)
                                position = index
                                return@forEachIndexed
                            }
                        }
                        currentProductDetail = currentProduct

                        if(currentProduct != products[0] && position > 2){
                            mBinding.rvMixerProductsToLoad.scrollToPosition(position)
                        }
                    }else{
                        currentProductDetail = null
                    }
                }
            }else if(bInDownload){
                roundRunCorralAdapter =  RoundRunCorralDownloadAdapter(
                    this@RemoteMixerFragment)
                (requireActivity() as MainActivity).minRoundRunDetail?.round?.corrals?.let { corrals ->
                    roundRunCorralAdapter?.corralList(corrals)
                }
                mBinding.rvMixerProductsToLoad.adapter = roundRunCorralAdapter

                (requireActivity() as MainActivity).minRoundRunDetail?.round?.corrals?.let { corrals ->
                    if(corrals.isNotEmpty()){
                        var position = 0
                        var currentCorral = corrals[0]
                        corrals.sortedBy { it.order }.forEachIndexed{index, corralInRound ->
                            if(corralInRound.initialWeight != 0L && corralInRound.finalWeight == 0L){
                                currentCorral = corralInRound
                                roundRunCorralAdapter?.selectCorral(index)
                                position = index
                                return@forEachIndexed
                            }
                        }
                        currentCorralDetail = currentCorral
                        if(currentCorral != corrals[0] && position > 2){
                            mBinding.rvMixerProductsToLoad.scrollToPosition(position)
                        }
                    }else{
                        currentCorralDetail = null
                    }
                }
            }else{}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        permission()
        mBinding = FragmentRemoteMixerBinding.inflate(inflater, container, false)

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_remote_mixer_fragment, menu)
                this@RemoteMixerFragment.menu = menu

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_selected_remote_tablet -> {
                        Log.v(TAG,"Force connection")
                        val deviceBluetooth = (requireActivity() as MainActivity).knowDevices?.firstOrNull { bd->
                            bd.address == selectedTabletMixerInFragment?.mac
                        }
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

                    R.id.cancel_round -> {
                        if ((bInCfg || bInLoad || bInDownload || bInRes)) {
                            Log.i(TAG,"Cancel round")
                            (requireActivity() as MainActivity).sendCancelToMixer()
                            (requireActivity() as MainActivity).onBackPressed()
                        }
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return mBinding.root
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
            Log.d("BLUE", "Permissions Granted")
        }
    }

    var countInResume = 0
    override fun onResume() {
        super.onResume()
        activity = this.getActivity() as MainActivity?
        Log.i(TAG,"onResume fragment: $this")
        if(activity is MainActivity){
            Log.i(TAG,"activity is MainActivity $activity")
            (requireActivity() as MainActivity).getSavedTabletMixer()
        }

        if(estableTimer != null ){
            Log.i(TAG,"estableTimer != null $estableTimer")
            return
        }
        estableTimer = Timer(false)
        timerTask = object : TimerTask() {
            init {
                Log.i(TAG,"init timerTask ${countInResume++}: $timerTask estableTimer $estableTimer")
            }
            override fun run() {
                if(bInLoad && !bInFree)
                    currentProductDetail?.let {productDetail ->
                        activity?.let {mainActivity ->
                            if(productDetail.currentWeight - productDetail.initialWeight > productDetail.targetWeight*0.9){
                                if(mBinding.tvCurrentProductWeightPending.currentTextColor== ContextCompat.getColor(mainActivity, R.color.white)){
                                    if(productDetail.currentWeight - productDetail.initialWeight > productDetail.targetWeight){
                                        mBinding.tvCurrentProductWeightPending.setTextColor(
                                            ContextCompat.getColor(mainActivity, R.color.color_yellow))
                                        }else{
                                            mBinding.tvCurrentProductWeightPending.setTextColor(
                                                ContextCompat.getColor(mainActivity, R.color.color_orange))
                                        }
                                    }else{
                                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                                    }
                                }else{
                                    mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                                }
                                if(productDetail.currentWeight - productDetail.initialWeight > productDetail.targetWeight && noPrevAlert){
                                    countGreaterThanTarget++
                                }
                        }
                    }

                if(bInDownload && !bInFree)
                    currentCorralDetail?.let { corralDetail ->
                        activity?.let {mainActivity ->
                            Log.i(TAG,"corralDetail ${corralDetail.name}   ${corralDetail.initialWeight}    ${corralDetail.currentWeight}  ${corralDetail.actualTargetWeight}")
                            if(corralDetail.initialWeight - corralDetail.currentWeight  > corralDetail.actualTargetWeight*0.9){
                                if(mBinding.tvCurrentProductWeightPending.currentTextColor == ContextCompat.getColor(mainActivity, R.color.white)){
                                    if(corralDetail.initialWeight - corralDetail.currentWeight > corralDetail.actualTargetWeight){
                                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.color_yellow))
                                    }else{
                                        mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.color_orange))
                                    }
                                }else{
                                    mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                                }
                            }else{
                                mBinding.tvCurrentProductWeightPending.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
                            }
                            if(corralDetail.initialWeight - corralDetail.currentWeight > corralDetail.actualTargetWeight && noPrevAlert){
                                countGreaterThanTarget++
                            }
                        }
                    }

            }
        }
        estableTimer?.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    var countMessage = 0
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
            (requireActivity() as MainActivity).commandReceibed()

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
//                    Log.i(TAG,"message.lenght ${message.size}")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i("showCommand","CMD_ROUNDDETAIL ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                    try{
                        val jsonString : String = convertZip.decompressText(byteArrayUtil)
                        Log.i("Json","jsonString * ${jsonString}")
                        if(jsonString.isNotEmpty()){
                            val gson = Gson()
                            val roundRunDetail : MinRoundRunDetail = gson.fromJson(jsonString,  MinRoundRunDetail::class.java)
                            (requireActivity() as MainActivity).minRoundRunDetail = roundRunDetail
                            loadRoundDetail()
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
                                setMixer(mixer)
                            }
                            (requireActivity() as MainActivity).minRoundRunDetail?.let { minRoundRunDetail->
                                val title = if(bInFree)
                                    "${getString(R.string.mixer)}: ${mixerDetail?.name} - ${minRoundRunDetail.round.name}"
                                else
                                    "${getString(R.string.mixer)}: ${mixerDetail?.name} - ${minRoundRunDetail.round.name} : ${minRoundRunDetail.round.diet.name}"

                                activity?.changeActionBarTitle(title)
                            }
                            if(!bInFree){
                                if(bInLoad){
                                    (requireActivity() as MainActivity).minRoundRunDetail?.round?.diet?.products?.let{
                                        if(it.isNotEmpty() && it[it.size-1].initialWeight != 0L){
                                            mBinding.btnJump.text = getString(R.string.descarga)
                                        }
                                    }
                                }else{
                                    (requireActivity() as MainActivity).minRoundRunDetail?.round?.corrals?.let{
                                        if(it.isNotEmpty() && it[it.size-1].initialWeight != 0L){
                                            mBinding.btnJump.text = getString(R.string.fin)
                                        }
                                    }
                                }

                            }
                        }
                    }catch (e: NumberFormatException){
                        Log.i("showCommand","CMD_ROUNDDETAIL NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i("showCommand","CMD_ROUNDDETAIL Exception $e")
                    }
                }

                Constants.CMD_ROUNDDATA->{
                    val convertZip = ConvertZip()
                    Log.i(TAG,"message.lenght ${message.size}")
                    val byteArrayUtil = message.copyOfRange(9,message.size-1)
                    Log.i("showCommand","CMD_ROUNDDATA ${messageStr.length} byteArrayUtil ${byteArrayUtil.size} ")
                    try{
                        val jsonString : String = convertZip.decompressText(byteArrayUtil)
                        Log.i("Json","jsonString * $jsonString")
                        if(jsonString.isNotEmpty()){
                            val gson = Gson()
                            val roundRunData : MinRoundRunData = gson.fromJson(jsonString,  MinRoundRunData::class.java)
                            (requireActivity() as MainActivity).minRoundRunDetail?.let{roundRundDetail->
                                if(bInLoad){
                                    roundRundDetail.round.diet.products.forEach {product->
                                        val newProductData = roundRunData.productsData.firstOrNull {
                                            it.id == product.id && it.order == product.order
                                        }
                                        newProductData?.let {data->
                                            product.initialWeight = data.initialWeight
                                            product.currentWeight = data.currentWeight
                                            product.finalWeight = data.finalWeight
                                            product.targetWeight = data.targetWeight
                                        }
                                        if(newProductData == null) {
                                            refreshRound()
                                            return
                                        }
                                    }
                                    roundRunProductAdapter?.notifyDataSetChanged()
                                }
                                if(bInDownload){
                                    roundRundDetail.round.corrals.forEach {corral->
                                        val newCorralData = roundRunData.corralsData.firstOrNull {
                                            it.id == corral.id && it.order == corral.order
                                        }
                                        newCorralData?.let {data->
                                            corral.initialWeight = data.initialWeight
                                            corral.currentWeight = data.currentWeight
                                            corral.finalWeight = data.finalWeight
                                            corral.customTargetWeight = data.customTargetWeight
                                            corral.actualTargetWeight = data.actualTargetWeight
                                        }
                                        if(newCorralData == null) {
                                            refreshRound()
                                            return
                                        }
                                    }
                                    roundRunCorralAdapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }catch (e: NumberFormatException){
                        Log.i("showCommand","CMD_ROUNDDATA NumberFormatException $e")
                    }catch (e:Exception){
                        Log.i("showCommand","CMD_ROUNDDATA Exception $e")
                    }
                }

                Constants.CMD_NEXT_CORRAL_DLG->{
                    Log.i("showCommand", "CMD_NEXT_CORRAL_DLG")
                    roundRunCorralAdapter?.let {corralAdapter ->
                        val position = corralAdapter.selectedPosition.plus(1)
                        if(position < corralAdapter.itemCount){
                            val nextItem = (requireActivity() as MainActivity).minRoundRunDetail?.round?.corrals?.get(position)
                            nextItem?.let {corralDetail ->
                                targetReachedDialog = dialogAlertTargetWeight(corralDetail.name)
                            }
                        }else{
                            targetReachedDialog = dialogAlertTargetWeight("fin")
                        }
                    }

                }

                Constants.CMD_NEXT_PRODUCT_DLG->{
                    Log.i("showCommand", "CMD_NEXT_PRODUCT_DLG")
                    roundRunProductAdapter?.let { productAdapter->
                        val position = productAdapter.selectedPosition.plus(1)
                        if(position < productAdapter.itemCount){
                            val nextItem = (requireActivity() as MainActivity).minRoundRunDetail?.round?.diet?.products?.get(position)
                            nextItem?.let { productDetail->
                                targetReachedDialog = dialogAlertTargetWeight(if(bInFree) getString(R.string.proximo_producto) else productDetail.name)
                            }
                        }else{
                            targetReachedDialog = dialogAlertTargetWeight("descarga")
                        }
                    }
                }

                Constants.CMD_CLOSE_DLG->{
                    Log.i("showCommand","CMD_CLOSE_DLG")
                    dialogResto?.dismiss()
                    dialogResto = null
                    dialogTara?.dismiss()
                    dialogTara = null
                    targetReachedDialog?.dismiss()
                    targetReachedDialog = null
                    (requireActivity() as MainActivity).closeDialogs()
                }

                Constants.CMD_DLG_TARA->{
                    (requireActivity() as MainActivity).dlgTareToLoad(weight)
                }

                Constants.CMD_REFRESH->{
                    Log.i("showCommand","CMD_REFRESH")
                    countMsg = REFRESH_VIEW_TIME
                    countDataMsg = REFRESH_DATA_TIME
                }

                Constants.CMD_USER_LIST->{
                    Log.i("showCommand","CMD_USER_LIST")
                    bSyncroUsers = (requireActivity() as MainActivity).refreshUsers(message)

                }

                Constants.CMD_ROUNDS->{
                    Log.i("showCommand","CMD_ROUNDS")
                    bSyncroRounds = (requireActivity() as MainActivity).refreshRounds(message)
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

                Constants.CMD_DLG_REST->{
                    Log.i("showCommand","CMD_REST")
                    dialogResto = restDialog()
                }

                Constants.CMD_WEIGHT->{
                    if(count_weight++<5){
                        return
                    }
                    Log.v("cmd_weight","CMD_WEIGHT")
                    (requireActivity()).onBackPressed()
                    return
                }

                Constants.CMD_WEIGHT_CONFIG->{
                    count_resume = 0
                    count_weight = 0
                    try{
                        if(!bInCfg){
                            mBinding.btnJump.visibility = View.VISIBLE
                            mBinding.btnTara.visibility = View.VISIBLE
                            mBinding.btnInitFreeRound.visibility = View.GONE
                            mBinding.btnRest.visibility = View.GONE
                            mBinding.btnPause.visibility = View.GONE
                            mBinding.btnJump.text = getString(R.string.iniciar)
                            refreshRound()
                        }
                        if(countMsg++ > REFRESH_VIEW_TIME){
                            refreshRound()
                        }
                        mBinding.tvTitleProduct.text = getString(R.string.iniciar)
                        mBinding.tvCurrentProduct.text = (requireActivity() as MainActivity).minRoundRunDetail?.round?.name
                        countMsg = 0
                        val mutableList = ArrayList<MinProductDetail>()
                        val dietDetail = MinDietDetail (
                            name = "",
                            description = "",
                            products = mutableList,
                            id = 0L
                        )

                        val emptyAdapter =  RoundRunProductAdapter(
                            this@RemoteMixerFragment)

                        mBinding.rvMixerProductsToLoad.adapter = emptyAdapter
                        mBinding.btnJump.isVisible = true
                        mBinding.btnPause.isVisible = false
                        refreshWeight(message)
                        bInLoad = false
                        bInDownload = false
                        bInFree = false
                        bInCfg = true
                        bInRes = false
                    }catch (e : Exception){
                        Log.i("showCommand","CMD_WEIGHT_LOAD Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_RESUME->{
                    count_weight = 0
                    if(count_resume++ > 5){
                        Log.v("commandsWeight","CMD_WEIGHT_RESUME")
                        try{
                            findNavController().navigate(RemoteMixerFragmentDirections.actionRemoteMixerFragmentToResumeFragment())
                        }catch (e : Exception){
                            Log.i("showCommand","CMD_WEIGHT_RESUME Exception $e")
                        }
                    }
                }

                Constants.CMD_WEIGHT_LOAD->{
                    count_resume = 0
                    count_weight = 0
                    try{
                        if(!bInLoad){
                            mBinding.btnJump.visibility = View.VISIBLE
                            mBinding.btnTara.visibility = View.VISIBLE
                            mBinding.btnInitFreeRound.visibility = View.INVISIBLE
                            mBinding.btnRest.visibility = View.VISIBLE
                            mBinding.btnPause.visibility = View.VISIBLE
                            mBinding.btnJump.text = getString(R.string.salto)
                            refreshRound()
                        }
                        if(countMsg++ > REFRESH_VIEW_TIME){
                            refreshRound()
                        }
                        if(countDataMsg++ > REFRESH_DATA_TIME){
                            refreshDataRound()
                        }
                        mBinding.tvTitleProduct.text = getString(R.string.cargar)
                        mBinding.tvCurrentProduct.text = currentProductDetail?.name
                        refreshWeight(message)
                        bInLoad = true
                        bInDownload = false
                        bInFree = false
                        bInCfg = false
                        bInRes = false
                    }catch (e : Exception){
                        Log.i("showCommand","CMD_WEIGHT_LOAD Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_DWNL->{
                    count_weight = 0
                    count_resume = 0
                    try{
                        if(!bInDownload){
                            mBinding.btnJump.visibility = View.VISIBLE
                            mBinding.btnTara.visibility = View.VISIBLE
                            mBinding.btnInitFreeRound.visibility = View.INVISIBLE
                            mBinding.btnRest.visibility = View.VISIBLE
                            mBinding.btnPause.visibility = View.VISIBLE
                            mBinding.btnJump.text = getString(R.string.salto)
                            countMsg = REFRESH_VIEW_TIME
                        }
                        mBinding.tvTitleProduct.text = getString(R.string.descargar_en)
                        mBinding.tvCurrentProduct.text = currentCorralDetail?.name
                        if(countMsg++ > REFRESH_VIEW_TIME){
                            refreshRound()
                        }
                        refreshWeight(message)
                        bInDownload = true
                        bInLoad = false
                        bInFree = false
                    }catch (e : Exception){
                        Log.i("showCommand","CMD_WEIGHT_DWNL Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_LOAD_FREE->{
                    count_weight = 0
                    count_resume = 0
                    try{
                        if(!bInLoad){
                            mBinding.btnJump.visibility = View.VISIBLE
                            mBinding.btnTara.visibility = View.VISIBLE
                            mBinding.btnInitFreeRound.visibility = View.INVISIBLE
                            mBinding.btnRest.visibility = View.VISIBLE
                            mBinding.btnPause.visibility = View.VISIBLE
                            mBinding.btnJump.text = getString(R.string.salto)
                            countMsg = REFRESH_VIEW_TIME
                        }
                        if(countMsg++ > REFRESH_VIEW_TIME){
                            refreshRound()
                        }
                        mBinding.tvTitleProduct.text = getString(R.string.cargar)
                        mBinding.tvCurrentProduct.text = getString(R.string.product)
                        refreshWeight(message)
                        bInLoad = true
                        bInDownload = false
                        bInFree = true
                    }catch (e : Exception){
                        Log.i("showCommand","CMD_WEIGHT_LOAD Exception $e")
                    }

                }

                Constants.CMD_WEIGHT_DWNL_FREE->{
                    count_weight = 0
                    count_resume = 0
                    try{
                        if(!bInDownload){
                            mBinding.btnJump.visibility = View.VISIBLE
                            mBinding.btnTara.visibility = View.VISIBLE
                            mBinding.btnInitFreeRound.visibility = View.INVISIBLE
                            mBinding.btnRest.visibility = View.VISIBLE
                            mBinding.btnPause.visibility = View.VISIBLE
                            mBinding.btnJump.text = getString(R.string.salto_fin)
                            countMsg = REFRESH_VIEW_TIME
                        }
                        mBinding.tvTitleProduct.text = getString(R.string.descargar_en)
                        mBinding.tvCurrentProduct.text = getString(R.string.corral)
                        if(countMsg++ > REFRESH_VIEW_TIME){
                            refreshRound()
                        }
                        refreshWeight(message)
                        bInDownload = true
                        bInLoad = false
                        bInFree = true
                    }catch (e : Exception){
                        Log.i("showCommand","CMD_WEIGHT_DWNL Exception $e")
                    }
                }

                Constants.CMD_MIXER ->{
                    Log.i("showCommand","CMD_MIXER")
                    try{
                        val convertZip = ConvertZip()
                        val json = convertZip.decompressText(message.copyOfRange(7,message.size-1))
                        val gson = Gson()
                        mixerDetail = gson.fromJson(json,  MixerDetail::class.java)
                        Log.i(TAG,"mixer receibe $mixerDetail")
                        val title = "Mixer: ${mixerDetail?.name}"
                        activity?.changeActionBarTitle(title)

                    }catch (e: NumberFormatException){
                        Log.i(TAG,"bSyncroMixer NumberFormatException $e")
                        return
                    }catch (e:Exception){
                        Log.i(TAG,"bSyncroMixer Exception $e")
                        return
                    }
                }
                else->{
                    Log.i(TAG,"else $command")
                }

            }
        }

        override fun onMessageReceived(device: BluetoothDevice?, message: String?) {
            Log.i("message","message $message")
            (requireActivity() as MainActivity).beaconReceibed()
        }

        override fun onMessageSent(device: BluetoothDevice?,message: String?) {
            Log.i("sent", "onMessageSent ${device?.address} $message")
        }

        override fun onCommandSent(device: BluetoothDevice?,command: ByteArray?) {
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
            Log.i(TAG, "[MixRem]onBondedDevices ${device?.size} \n$device")
            (requireActivity() as MainActivity).knowDevices = device
        }
    }

    private fun refreshWeight(message:ByteArray) {
        weight = String(message,4,8).toLong()
        val sign = String(message,3,1)
        val progress = String(message,12,3).toInt()
        val signRest = String(message,15,1)
        rest = String(message,16,8).toIntOrNull()?:0
        val isChecked = (String(message, 24, 1).toIntOrNull() ?: 0) == 1
        if(isChecked){
            Log.i(TAG,"isChecket ${String(message)}")
        }
        mBinding.btnPause.isChecked = isChecked
        mBinding.tvCurrentProductWeightPending.text = "${sign}${weight}Kg"
        mBinding.pbCurrentProduct.progress = progress
        dialogResto?.setMessage("$signRest${rest}Kg")

    }

    private fun refreshRound() {
        countMsg = 0
        (requireActivity() as MainActivity).sendRequestRoundRunDetail()
        mBinding.btnJump.isVisible = true
        mBinding.btnPause.isVisible = true
    }

    private fun refreshDataRound() {
        countDataMsg = 0
        (requireActivity() as MainActivity).requestDataRoundRunDetail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanAll()
        Log.i(TAG, "on destroy")
    }

    private fun cleanAll(){
        Log.i(TAG,"clean all estableTimer: $estableTimer timerTask: $timerTask")
        estableTimer?.purge()
        estableTimer?.cancel()
        estableTimer = null
        timerTask = null
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(requireContext(), mBluetoothListener)
    }

    private fun alertFinalDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Advertencia")
        builder.setMessage("¿Seguro quiere pasar a descarga?")
        builder.setPositiveButton("Descarga"){_,_->
            (requireActivity() as MainActivity).sendGoToDownload()
        }
        builder.setNegativeButton("Cancelar"){dialog,_->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun restDialog(): AlertDialog? {
        // Verifica si el fragmento está adjunto a una actividad antes de proceder
        if (!isAdded) return null

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        // set message of alert dialog
        dialogBuilder.setMessage("${rest}Kg")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                if (isAdded) { // Verifica nuevamente antes de usar requireActivity()
                    (requireActivity() as MainActivity).sendCloseDlgToMixer()
                }
                dialog.dismiss()
                dialogResto = null
            }
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.rest))
        alert.show()
        val textView: TextView = alert.findViewById(android.R.id.message)
        textView.textSize = 40F
        textView.gravity = Gravity.CENTER
        return alert
    }


    fun setMixer(mixerIn: Mixer?) {
        if(selectedMixerInFragment?.equals(mixerIn) == true && mixerDetail != null){
            return
        }
        mixerIn.let {mixer ->
            selectedMixerInFragment = mixer
            mixer?.let { mixer1->
                mixerDetail = MixerDetail(
                    mixer1.name,
                    mixer1.description,
                    mixer1.mac,
                    mixer1.btBox,
                    mixer1.tara,
                    mixer1.calibration,
                    mixer1.rfid,
                    mixer1.remoteId,
                    mixer1.updatedDate,
                    mixer1.archiveDate,
                    mixer1.id
                )
            }
            Log.i(TAG,"setMixer $mixer  \nmService ${(requireActivity() as MainActivity).mService}")
            (requireActivity() as MainActivity).mService?.LocalBinder()?.getBondedDevices()
        }
    }

    private fun dialogAlertTargetWeight(nextItem : String ="", strKg :String="") : AlertDialog? {
        Log.i(TAG,"dialogAlertTargetWeight")
        dialogCountDown?.let {
            if(it.isShowing){
                Log.i(TAG,"already showing")
                return dialogCountDown
            }
        }
        var title: String? = null
        var message: String? = null
        val strkg : String =  strKg

        if(bInLoad)
            if(bInFree){
                if(currentProductDetail != null) {
                    title = currentProductDetail?.name
                    message = if(nextItem.isNotEmpty()){
                        "Se pasa a $nextItem"
                    }else{
                        "Se va a pasar al proximo producto! "
                    }
                }else {
                    title = "Elegir producto"
                    message = nextItem
                }
            }else{
                currentProductDetail?.let {currentProduct->
                    title = currentProduct.name
                    message = if(nextItem.isNotEmpty()){
                        "Se pasa a $nextItem"
                    }else{
                        "Se va a pasar al proximo producto! "
                    }
                }?: Log.i(TAG,"current product null")

            }
        if(bInDownload)
            if(bInFree){
                if(currentProductDetail != null) {
                    title = currentProductDetail?.name
                    message = nextItem.ifEmpty {
                        "Se va a pasar al proximo producto! "
                    }
                }else {
                    title = "Elegir producto"
                    message = nextItem
                }
            }else{
                currentCorralDetail?.let{ currentCorral->
                    title = currentCorral.name
                    message = if(nextItem.isNotEmpty()){
                        "Se pasa a $nextItem"
                    }else{
                        "Se va a pasar al proximo corral! "
                    }
                }?: Log.i(TAG,"current corral null")
            }

        val builder = AlertDialog.Builder(requireActivity())
            .create()
        val  view = layoutInflater.inflate(R.layout.dialog_custom_target_weight_reached,null)
        val  btnAcept = view.findViewById<Button>(R.id.btn_dialog_acept)
        val  btnCancel = view.findViewById<Button>(R.id.btn_dialog_cancel)
        val  tvTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val  tvMessage = view.findViewById<TextView>(R.id.tv_dialog_message)
        val  tvCount = view.findViewById<TextView>(R.id.tv_dialog_count)


        if(title == null){
            Log.i(TAG,"title = $title message = $message")
            return null
        }
        builder.setView(view)
        tvMessage.text = message
        tvTitle.text = title
        btnAcept.setOnClickListener {
            if(bInLoad){
                sendNextProduct()
            }
            if(bInDownload){
                sendNextCorral()
            }
            builder.dismiss()
        }
        btnCancel.setOnClickListener {
            (requireActivity() as MainActivity).sendCloseDlgToMixer()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)

        builder.setOnShowListener(object : DialogInterface.OnShowListener {
            private val AUTO_DISMISS_MILLIS = 10000
            override fun onShow(dialog: DialogInterface) {
                (dialog as AlertDialog)
                object : CountDownTimer(AUTO_DISMISS_MILLIS.toLong(), 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        tvCount?.text = String.format(
                            Locale.getDefault(), "%s (%d)",
                            strkg,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        )
                    }

                    override fun onFinish() {
                        if (dialog.isShowing) {
                            if(bInLoad)
                                sendNextProduct()
                            if(bInDownload)
                                sendNextCorral()
                            dialog.dismiss()
                            dialogCountDown = null
                        }
                    }
                }.start()
            }

        })

        builder.show()
        dialogCountDown = builder
        return dialogCountDown
    }

    fun sendNextProduct(){
        val msg = "CMD${Constants.CMD_NXTPRODUCT}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
    }

    fun sendNextCorral(){
        val msg = "CMD${Constants.CMD_NXTCORRAL}"
        activity?.mService?.LocalBinder()?.write(msg.toByteArray())
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

    private fun tareDialog(title: String, message: String, fontSize : Float = 0F, gravity: Int = 0) : AlertDialog? {
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
        if(fontSize>0){
            val textView : TextView = alert.findViewById(android.R.id.message)
            textView.textSize = fontSize
        }
        if(gravity != Gravity.NO_GRAVITY){
            val textView : TextView = alert.findViewById(android.R.id.message)
            textView.gravity = gravity
        }
        return alert
    }

    fun isInFree(): Boolean {
        return bInFree
    }


}