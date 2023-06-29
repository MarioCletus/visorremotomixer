package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.DialogInterface.OnShowListener
import android.os.*
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.size
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.navigation.navArgs
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityRoundRunBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.model.entities.MixerDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.fragments.StepConfigurationFragment
import com.basculasmagris.visorremotomixer.view.fragments.StepDownloadFragment
import com.basculasmagris.visorremotomixer.view.fragments.StepLoadFragment
import com.basculasmagris.visorremotomixer.view.fragments.StepResumeFragment
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

class RoundRunActivity : AppCompatActivity() {

    private var snackbar: Snackbar? = null
    private var isConnected: Boolean = false
    private val TAG : String = "DEBRound"
    private lateinit var mBinding: ActivityRoundRunBinding
    private var mixerDetail: MixerDetail? = null
    var currentRoundRunDetail: RoundRunDetail? = null
    private var currentStep = 0
    // Bluetooth
    var mService: BluetoothSDKService? = null
    private var mProgressDialog: Dialog? = null
    private var dialogCountDown: AlertDialog? = null
    private var mBound: Boolean = false
    var initialMixer = 0.0
    private var repeatJob: Job? = null
    private var showSnackbar = true

    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((application as SpiMixerApplication).roundRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRoundRunBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val args: RoundRunActivityArgs by navArgs()
        Log.i(TAG, "DATA: ${args.rounRunDetail}")
        Log.i(TAG, "ID ROUND RUN: ${args.rounRunDetail.id}")
        mixerDetail = args.rounRunDetail.mixer
        currentRoundRunDetail = args.rounRunDetail

        setupActionBar()
        val menuHost: MenuHost = this
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_step_load, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.bluetooth_mixer_status->{
                        connectDevice()
                        return true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
        refreshLogo()

        if (Build.VERSION.SDK_INT in 12..18) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions =
                SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }

        val toStep = args.toStep
        Log.i(TAG, "startDate: ${currentRoundRunDetail?.startDate}")

        Log.i(TAG, "ToStep: $toStep")
        when (toStep){
            1 -> changeStep(1)
            2 -> changeStep(2)
            3 -> changeStep(3)
            4 -> changeStep(4)
            else -> changeStep(1)
        }

        supportActionBar?.let {
            it.title = "${currentRoundRunDetail?.round?.name}: ${currentRoundRunDetail?.round?.diet?.name}"
        }

        bindBluetoothService()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    fun startAutomaticSave(){
        if (repeatJob == null || repeatJob?.isActive == false){
            Log.i("AUTOMATIC SAVE", "START")
            repeatJob = startRepeatingJob(10000L)
            repeatJob?.start()
        }
    }

    fun stopAutomaticSave(){
        if (repeatJob != null && repeatJob?.isActive == true){
            Log.i("AUTOMATIC SAVE", "STOP")
            repeatJob?.cancel()
        }
    }

    var antStatus : Boolean = false
    fun changeStatusConnection(bConnected: Boolean){
        runOnUiThread {
            if(antStatus != bConnected){
                Log.i(TAG, "changeStatusConnection $bConnected")
                antStatus = bConnected
            }
            if (bConnected) {
                showSnackbar = true
                deviceConnected()
                snackbar?.dismiss()
            } else {
                deviceDisconnected()
                if (showSnackbar){
                    showSnackbar = false
                    snackbar =
                        Snackbar.make(mBinding.viewActivityRoundRun, "Se perdió la conexión", LENGTH_INDEFINITE)
                    snackbar?.setAction(
                        "Reconectar"
                    ) {
                        currentRoundRunDetail?.mixerBluetoothDevice?.let {
                            mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                        }
                        showCustomProgressDialog()
                        Timer().schedule(5000) {
                            hideCustomProgressDialog()
                            showSnackbar = true
                            changeStatusConnection(false)
                            Log.i(TAG,"changeStatusConnection(false) RRA 179")
                        }
                    }
                    snackbar?.show()
                }

            }
        }
    }

    fun showCustomProgressDialog(){
        if(mProgressDialog != null && mProgressDialog!!.isShowing){
            return
        }
        mProgressDialog = Dialog(this)
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.let {_dialog->
            _dialog.setContentView(R.layout.dialog_custom_progress)
            _dialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                if (_dialog.isShowing){
                    _dialog.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Alerta")
                    builder.setMessage("No se pudo establecer la comunicación con el Mixer " + mixerDetail?.name )

                    builder.setPositiveButton(android.R.string.yes) { _, _ ->
                        _dialog.dismiss()
//                        finish()
                        changeStatusConnection(false)
                        Log.i(TAG,"changeStatusConnection(false) RRA 209")
                    }
                    builder.show()
                }
            }, 10000)
        }
    }

    fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    fun customDialog(title: String, message: String,fontSize : Float = 0F,gravity: Int = 0) : AlertDialog? {
        val fragmentInstance = supportFragmentManager.findFragmentById(R.id.fragment_round_run)
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { _, _ ->
                if (fragmentInstance is StepLoadFragment) {
                    fragmentInstance.tare()
                }
                if (fragmentInstance is StepDownloadFragment) {
                    fragmentInstance.tare()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
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

    fun dialogAlertTargetWeight(nextItem : String ="",strKg :String="") : AlertDialog? {
        if(dialogCountDown != null && dialogCountDown!!.isShowing ){
            return dialogCountDown
        }
        var title: String? = null
        var message: String? = null
        val strkg : String =  strKg
        val fragmentInstance = supportFragmentManager.findFragmentById(R.id.fragment_round_run)
        if(fragmentInstance is StepLoadFragment && fragmentInstance.getCurrentProduct()!=null){
            title = fragmentInstance.getCurrentProduct()!!.name
            message = if(nextItem.isNotEmpty()){
                "Se pasa a $nextItem"
            }else{
                "Se va a pasar al proximo producto! "
            }
        }

        val builder = AlertDialog.Builder(this)
            .create()
        val  view = layoutInflater.inflate(R.layout.dialog_custom_target_weight_reached,null)
        val  btnAcept = view.findViewById<Button>(R.id.btn_dialog_acept)
        val  btnCancel = view.findViewById<Button>(R.id.btn_dialog_cancel)
        val  tvTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val  tvMessage = view.findViewById<TextView>(R.id.tv_dialog_message)
        val  tvCount = view.findViewById<TextView>(R.id.tv_dialog_count)
        if(fragmentInstance is StepLoadFragment && fragmentInstance.getCurrentProduct()!=null){
            title = fragmentInstance.getCurrentProduct()!!.name
            message = if(nextItem.isNotEmpty()){
                "Se pasa a $nextItem"
            }else{
                "Se va a pasar al proximo producto! "
            }
        }
        if(fragmentInstance is StepDownloadFragment && fragmentInstance.getCurrentCorral()!=null){
            title = fragmentInstance.getCurrentCorral()!!.name
            message = if(nextItem.isNotEmpty()){
                "Se pasa a $nextItem"
            }else{
                "Se va a pasar al proximo corral! "
            }
        }

        if(title==null || message == null){
            return null
        }
        builder.setView(view)
        tvMessage.text = message
        tvTitle.text = title
        btnAcept.setOnClickListener {
            if(fragmentInstance is StepLoadFragment && fragmentInstance.getCurrentProduct()!=null){
                fragmentInstance.nextProduct()
            }
            if(fragmentInstance is StepDownloadFragment && fragmentInstance.getCurrentCorral()!=null){
                fragmentInstance.nextCorral()
            }
            builder.dismiss()
        }
        btnCancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)

        builder.setOnShowListener(object : OnShowListener {
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
                            if(fragmentInstance is StepLoadFragment)fragmentInstance.nextProduct()
                            if(fragmentInstance is StepDownloadFragment)fragmentInstance.nextCorral()
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

    fun saveRoundLoadStatus(){
        Log.i("AUTOMATIC SAVE", "Se guarda $currentRoundRunDetail")

        currentRoundRunDetail?.let { roundRunDetail ->
            if (roundRunDetail.id != 0L){

                runBlocking {
//                    Log.i(TAG, "[ConfigFragment] Save... Mixer: ${roundRunDetail.mixer!!.id} | customPercentage: ${roundRunDetail.customPercentage}")
                    mRoundViewModel.updateRoundRunSync(
                        roundRunDetail.id,
                        roundRunDetail.mixer!!.id,
                        roundRunDetail.customPercentage,
                        roundRunDetail.customTara,
                        roundRunDetail.addedBlend,
                        roundRunDetail.state
                    )

                    // Productos de dieta (carga)
                    roundRunDetail.round.diet.products.forEach { productDetail ->
                        productDetail.targetWeight = (productDetail.percentage*roundRunDetail.round.corrals.sumOf { corral -> corral.customTargetWeight }/100)
//                        Log.i(TAG ,"Se actualiza [RunId: ${roundRunDetail.id} | DietId:${roundRunDetail.round.diet.id} | ProductId:${productDetail.id} | Initial Weight:${productDetail.initialWeight} | Actual Weight:${productDetail.currentWeight}")
                        mRoundViewModel.updateProgressLoad(
                            roundRunDetail.id,
                            roundRunDetail.round.diet.id,
                            productDetail.id,
                            productDetail.initialWeight,
                            productDetail.currentWeight,
                            productDetail.finalWeight,
                            productDetail.targetWeight
                        )
                    }

                    // Corrales de ronda (descarga)
                    Log.i(TAG ,"Corrales: ${roundRunDetail.round.corrals.size}")
                    roundRunDetail.round.corrals.forEach { corralDetail ->
                        Log.i(TAG ,"Se actualiza ronda [RunId: ${roundRunDetail.id} | RoundId:${roundRunDetail.round.id} actualTargetWeight:${corralDetail.actualTargetWeight} || customTargetWeight:${corralDetail.customTargetWeight} | Corral currentWeight:${corralDetail.currentWeight}")
                        mRoundViewModel.updateProgressDownload(
                            roundRunDetail.id,
                            corralDetail.id,
                            corralDetail.initialWeight,
                            corralDetail.currentWeight,
                            corralDetail.finalWeight,
                            corralDetail.customTargetWeight,
                            corralDetail.actualTargetWeight
                        )
                    }
                }
            } else {
                runBlocking {
                    Log.i(TAG, "Se agrega ejecución con Ronda ${roundRunDetail.round.id} y Mixer: ${roundRunDetail.mixer?.id}")
                    val roundRunId = mRoundViewModel.addRoundRunSync(
                        roundRunDetail.round.id,
                        roundRunDetail.mixer!!.id,
                        roundRunDetail.customPercentage,
                        roundRunDetail.customTara,
                        roundRunDetail.addedBlend,
                        roundRunDetail.state,
                        roundRunDetail.userId,
                        roundRunDetail.userDisplayName
                    )

                    roundRunDetail.id = roundRunId

                    // Productos de dieta (carga)
                    roundRunDetail.round.diet.products.forEach { productDetail ->
                        productDetail.targetWeight = (productDetail.percentage*roundRunDetail.round.corrals.sumOf { corral -> corral.customTargetWeight }/100)
                        Log.i(TAG ,"Se agrega [RunId: $roundRunId | DietId:${roundRunDetail.round.diet.id} | ProductId:${productDetail.id} | Initial Weight:${productDetail.initialWeight} | Actual Weight:${productDetail.currentWeight}")
                        mRoundViewModel.addProgressLoad(
                            roundRunId,
                            roundRunDetail.round.diet.id,
                            productDetail.id,
                            productDetail.initialWeight,
                            productDetail.currentWeight,
                            productDetail.finalWeight,
                            productDetail.targetWeight)
                    }

                    // Corrales de ronda (descarga)
                    Log.i(TAG ,"Corrales: ${roundRunDetail.round.corrals.size}")
                    roundRunDetail.round.corrals.forEach { corralDetail ->
                        Log.i(TAG ,"Se agrega [RunId: $roundRunId | CorralId:${corralDetail.id} | customTargetWeight:${corralDetail.customTargetWeight} | currentWeight:${corralDetail.currentWeight}")
                        mRoundViewModel.addProgressDownload(
                            roundRunId,
                            corralDetail.id,
                            corralDetail.initialWeight,
                            corralDetail.currentWeight,
                            corralDetail.finalWeight,
                            corralDetail.customTargetWeight,
                            corralDetail.actualTargetWeight)
                    }
                }
            }
        }
    }

    fun finishRoundStatus(state: Int){
        currentRoundRunDetail?.let { roundRunDetail ->
            if (roundRunDetail.id != 0L){
                runBlocking {
                    Log.i(TAG, "[ConfigFragment] Save... Mixer: ${roundRunDetail.mixer!!.id} | customPercentage: ${roundRunDetail.customPercentage}")
                    mRoundViewModel.finishRoundRunSyncWState(roundRunDetail.id,state)
                }
                // Save report
            }
        }
    }

    fun finishRoundStatusWStatus(status : Int){
        currentRoundRunDetail?.let { roundRunDetail ->
            if (roundRunDetail.id != 0L){
                runBlocking {
                    Log.i(TAG, "[ConfigFragment] Save... Mixer: ${roundRunDetail.mixer!!.id} | customPercentage: ${roundRunDetail.customPercentage}")
                    mRoundViewModel.finishRoundRunSyncWState(roundRunDetail.id,status)
                }
                // Save report
            }
        }
    }

    fun changeStep(step: Int) {
        val fm: FragmentManager = supportFragmentManager

        // replace
        val ft: FragmentTransaction = fm.beginTransaction()
        val lastStep = this.currentStep
        this.currentStep = this.currentStep+step

        when (this.currentStep) {
            1 -> {
                ft.replace(R.id.fragment_round_run, StepConfigurationFragment(mixerDetail))
                ft.commit()
            }
            2 -> {
                var showAlert = false
                val fragmentInstance = supportFragmentManager.findFragmentById(R.id.fragment_round_run)
                if (fragmentInstance is StepConfigurationFragment){
                    currentRoundRunDetail?.let {
                        val tara = it.customTara
                        if ((fragmentInstance.lastWeight - tara).absoluteValue > 10 && lastStep == 1){
                            showAlert = true
                        }
                    }
                }
                if (showAlert){
                    (fragmentInstance as StepConfigurationFragment)//ShowAlert true si solo es StepConfigurationFragment
                    val alertTara = AlertDialog.Builder(this)
                    alertTara.setTitle("Alerta")
                    alertTara.setMessage("Se registra un peso de ${fragmentInstance.lastWeight} en el mixer.\nDesea añadirlos a la mezcla y continuar?" )
                    alertTara.setNeutralButton(getString(R.string.cancelar)){dialog,_->
                        this.currentStep = lastStep
                        dialog.dismiss()
                    }
                    alertTara.setPositiveButton(getText(R.string.aceptar)) { _, _ ->
                        Log.i("AUTOMATIC SAVE", "01")
                        fragmentInstance.addInitialWeight()
                        currentRoundRunDetail?.state = Constants.STATE_LOAD
                        saveRoundLoadStatus()
                        ft.replace(R.id.fragment_round_run, StepLoadFragment(mixerDetail!!))
                        ft.commit()
                    }
                    alertTara.setNegativeButton(getString(R.string.no_add)) { _, _ ->
                        Log.i("AUTOMATIC SAVE", "01")
                        fragmentInstance.tare()
                        currentRoundRunDetail?.state = Constants.STATE_LOAD
                        saveRoundLoadStatus()
                        ft.replace(R.id.fragment_round_run, StepLoadFragment(mixerDetail!!))
                        ft.commit()
                    }
                    alertTara.show()

                } else {
                    Log.i("AUTOMATIC SAVE", "02")
                    currentRoundRunDetail?.state  = Constants.STATE_LOAD
                    saveRoundLoadStatus()
                    ft.replace(R.id.fragment_round_run, StepLoadFragment(mixerDetail!!))
                    ft.commit()
                }

            }
            3 -> {
                Log.i("AUTOMATIC SAVE", "03")
                currentRoundRunDetail?.state = Constants.STATE_DOWNLOAD
                saveRoundLoadStatus()
                ft.replace(R.id.fragment_round_run, StepDownloadFragment(mixerDetail!!))
                ft.commit()

            }
            4 -> {
                Log.i("AUTOMATIC SAVE", "04")
                currentRoundRunDetail?.state = Constants.STATE_RESUME
                saveRoundLoadStatus()
                ft.replace(R.id.fragment_round_run, StepResumeFragment())
                snackbar?.dismiss()
                ft.commit()
            }
            5 -> {
                finish()
            }
            else -> {
                // Se termina la ronda
                Log.i("AUTOMATIC SAVE", "05")
                currentRoundRunDetail?.state = Constants.STATE_FINISH
                saveRoundLoadStatus()
                finish()
            }
        }

        //ft.commit()
    }

    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarRoundRun)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarRoundRun.title = currentRoundRunDetail?.round?.name
        mBinding.toolbarRoundRun.setNavigationOnClickListener {

            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.fragment_round_run)
            if(fragmentInstance is StepConfigurationFragment){
                Log.i(TAG,"StepConfigurationFragment topbar back button")
                fragmentInstance.exitFragment()
            }
            if(fragmentInstance is StepLoadFragment){
                fragmentInstance.exitFragment()
            }
            if(fragmentInstance is StepDownloadFragment){
                fragmentInstance.exitFragment()
            }
            if(fragmentInstance is StepResumeFragment){
                fragmentInstance.exitFragment()
            }
            onBackPressed()
        }
    }


    fun getCurrentWeight() : Double {
        var currentWeight = 0.0
        currentRoundRunDetail?.round?.diet?.products?.forEach {
            currentWeight += it.currentWeight-it.initialWeight
        }
        return currentWeight.roundToLong().toDouble()
    }

    fun getFinalLoad() : Double {
        var currentWeight = 0.0
        currentRoundRunDetail?.round?.diet?.products?.forEach {
            currentWeight += it.finalWeight - it.initialWeight
        }
        return currentWeight.roundToLong().toDouble()
    }

    fun getFinalDownload() : Double {
        var currentWeight = 0.0
        currentRoundRunDetail?.round?.corrals?.forEach {
            currentWeight += it.initialWeight - it.finalWeight
        }
        return currentWeight.roundToLong().toDouble()
    }

    fun getCurrentDownload() : Double {
        var currentWeight = 0.0
        currentRoundRunDetail?.round?.corrals?.forEach {
            currentWeight += it.initialWeight - it.currentWeight
        }
        return currentWeight.roundToLong().toDouble()
    }

    fun getTargetDownload() : Double {
        var customTargetWeight = 0.0
        currentRoundRunDetail?.round?.corrals?.forEach {
            customTargetWeight += it.actualTargetWeight
        }
        return customTargetWeight.roundToLong().toDouble()
    }

    fun getTargetWeight() : Double {
        var targetWeight = 0.0

        currentRoundRunDetail?.round?.corrals?.forEach {
            targetWeight += it.actualTargetWeight
        }
        return targetWeight.roundToLong().toDouble()
    }

    fun getLoadDifference() : Double {
        var totalLoadDiff = 0.0
        currentRoundRunDetail?.round?.diet?.products?.forEach { productDetail->
            totalLoadDiff += ((productDetail.finalWeight-productDetail.initialWeight)-productDetail.targetWeight )
        }
        return totalLoadDiff.roundToLong().toDouble()
    }

    fun getDownloadDifference() : Double {
        var totalDownloadDiff = 0.0
        currentRoundRunDetail?.round?.corrals?.forEach { corralDetail->
            totalDownloadDiff += ((corralDetail.initialWeight - corralDetail.finalWeight) - corralDetail.actualTargetWeight )
        }
        return totalDownloadDiff.roundToLong().toDouble()
    }

    fun getCustomRoundRunTargetWeight() : Double {
        var targetCustomRoundRunWeight = 0.0
        currentRoundRunDetail?.round?.let {
            targetCustomRoundRunWeight = it.customRoundRunWeight
        }
        return targetCustomRoundRunWeight.roundToLong().toDouble()
    }

    fun getRealRoundRunCorralTargetWeight(corral: CorralDetail) : Double {
        return  (corral.percentage * initialMixer / 100).roundToLong().toDouble()
    }

    private fun startRepeatingJob(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                delay(timeInterval)
                Log.i("RUN", "Guardando estado actual...")
                Log.i("AUTOMATIC SAVE", "07")
                saveRoundLoadStatus()
            }
        }
    }

    //Bluetooth
    private fun bindBluetoothService() {
        // Bind to LocalService
        Log.i(TAG, "Se inicia servicio")
        Intent(this, BluetoothSDKService::class.java).also { intent ->
            Log.i(TAG, "intent Se inicia servicio")
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as BluetoothSDKService.LocalBinder
            mService = binder.getService()
            mBound = true
            Log.i(TAG, "*********** onServiceConnected [RoundRunActivity] CONECTADO")

            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.fragment_round_run)

            Log.i(TAG, "onServiceConnected: ${fragmentInstance.toString()}")
            if(fragmentInstance is StepConfigurationFragment){
                Log.i(TAG, "onServiceConnected: ${currentRoundRunDetail?.mixerBluetoothDevice}")
                currentRoundRunDetail?.mixerBluetoothDevice?.let {
                    mService?.LocalBinder()?.connectKnowDeviceWithTransfer(it)
                }
            }

        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i(TAG, "*********** [RoundRunActivity]  DESCONECTADO")
            mBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mProgressDialog?.dismiss()
        Log.i("AUTOMATIC SAVE", "CANCELLED")
        repeatJob?.cancel()
    }

    override fun onBackPressed() {
        //moveTaskToBack(true)

        val fragmentInstance = supportFragmentManager.findFragmentById(R.id.fragment_round_run)
        val data = Intent()
        data.putExtra("currentRound", currentRoundRunDetail)
        setResult(Activity.RESULT_OK, data)

        if (fragmentInstance is StepConfigurationFragment) {
            Log.i("AUTOMATIC SAVE", "08")
            currentRoundRunDetail?.state = Constants.STATE_CONFIG
            saveRoundLoadStatus()
            finish()
        }

        if (fragmentInstance is StepLoadFragment) {
            Log.i("AUTOMATIC SAVE", "09")
            currentRoundRunDetail?.state = Constants.STATE_LOAD
            saveRoundLoadStatus()
            fragmentInstance.cleanAll()
            finish()
        }

        if (fragmentInstance is StepDownloadFragment) {
            Log.i("AUTOMATIC SAVE", "10")
            currentRoundRunDetail?.state = Constants.STATE_DOWNLOAD
            saveRoundLoadStatus()
            fragmentInstance.cleanAll()
            finish()
        }

        if (fragmentInstance is StepResumeFragment) {
            finish()
        }
    }

    fun deviceDisconnected() {
        if(mBinding.toolbarRoundRun.menu.size>0){
            mBinding.toolbarRoundRun.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_disconnected_24px)
            mBinding.toolbarRoundRun.menu?.getItem(0)?.icon?.setTint(getColor(R.color.white))
        }

        isConnected = false
    }

    fun deviceConnected() {
        if(mBinding.toolbarRoundRun.menu.size>0){
            mBinding.toolbarRoundRun.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_connected_24px)
            mBinding.toolbarRoundRun.menu?.getItem(0)?.icon?.setTint(getColor(R.color.white))
        }
        isConnected = true
    }

    fun connectDevice(){
        if(!isConnected){
            snackbar?.dismiss()
            currentRoundRunDetail?.mixerBluetoothDevice?.let { _blueToothDevice->
                mService?.LocalBinder()?.connectKnowDeviceWithTransfer(_blueToothDevice)
            }
            showCustomProgressDialog()
            Timer().schedule(5000) {
                hideCustomProgressDialog()
                showSnackbar = true
                Log.i(TAG,"changeStatusConnection(false) RRA 773")
                changeStatusConnection(false)
            }
        }
    }

    fun refreshLogo() {
        mBinding.ibLogo.setImageDrawable(getDrawable(R.drawable.magris_logo_topbar))
    }

}