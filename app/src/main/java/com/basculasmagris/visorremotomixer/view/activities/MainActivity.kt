package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityMainBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.services.BluetoothSDKService
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.fragments.HomeFragment
import com.basculasmagris.visorremotomixer.view.fragments.MixerListFragment
import com.basculasmagris.visorremotomixer.view.fragments.RemoteMixerFragment
import com.basculasmagris.visorremotomixer.view.fragments.TabletMixerListFragment
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.datastore by preferencesDataStore(name = "PREFERENCIAS")
class MainActivity : AppCompatActivity() {

    private val TAG : String =  "DEBMain"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var mProgressDialog: Dialog? = null
    private var isConnected: Boolean = false
    var mLocalDetailRound: MutableList<RoundRunDetail> = ArrayList()
    private var roles: ArrayList<Pair<Int, String>> = arrayListOf(
        Pair(1, "Administrador"),
        Pair(2, "Operario"),
        Pair(3, "Supervisor"),
        Pair(4, "Super Admin")
    )
    // Bluetooth
    var mService: BluetoothSDKService? = null

    private var selectedMixerInActivity: Mixer? = null
    private var selectedTabletMixerInActivity: TabletMixer? = null

    // -------------------
    // Mixer
    // -------------------
    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((application as SpiMixerApplication).mixerRepository)
    }

    private val mTabletMixerViewModel: TabletMixerViewModel by viewModels {
        TabletMixerViewModelFactory((application as SpiMixerApplication).tabletMixerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbarMain)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: CancellableNavigationView = binding.navView
        val currentUser = Helper.getCurrentUser(this)
        val navHeader = navView.getHeaderView(0)
        val tvFullName = navHeader.findViewById<TextView>(R.id.user_display_name)
        val tvRoleDescription = navHeader.findViewById<TextView>(R.id.role_description)

        Log.i("tvFullName", "DATA USER tvFullName: ${currentUser.displayName}")
        Log.i("tvRoleDescription", "DATA USER tvRoleDescription: ${currentUser.codeRole}")
        tvFullName.text = currentUser.displayName.trim()
        tvRoleDescription.text = roles.first { role -> role.first == currentUser.codeRole }.second  //currentUser.role.description.trim()

        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_tablet_mixer,
                R.id.nav_mixer_remoto,
                R.id.nav_home,
                R.id.nav_product,
                R.id.nav_establishment,
                R.id.nav_corral,
                R.id.nav_mixer,
                R.id.nav_diet,
                R.id.nav_round,
                R.id.nav_report,
                R.id.nav_user
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedPreviewListener { item ->
            Log.d(TAG, "got item: ${item.itemId}")
            var outPut = true
            when (item.itemId) {
                R.id.nav_logout -> {
                    outPut = false
                    android.app.AlertDialog.Builder(this)
                        .setMessage("¿Desea cerrar la sesión?")
                        .setPositiveButton("Sí")
                        { _, _ ->
                            Helper.logOut(this)
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        .setNegativeButton("No") { _, _ ->
                        }
                        .show()
                    drawerLayout.close()
                }
                R.id.nav_exit ->{
                    android.app.AlertDialog.Builder(this)
                        .setMessage("¿Desea cerrar la aplicación?")
                        .setPositiveButton("Sí")
                        { _, _ ->
                            Helper.logOut(this)
                            finish()
                        }
                        .setNegativeButton("No") { _, _ ->
                        }
                        .show()
                    drawerLayout.close()
                }
            }
            outPut
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        mMixerViewModel.allMixerList.observe(this){
            mService?.LocalBinder()?.getBondedDevices()
        }

        refreshLogo()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        bindBluetoothService()
    }

    fun refreshLogo() {
        binding.appBarMain.ibLogoMain.setImageDrawable(getDrawable(R.drawable.magris_logo_topbar))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            Activity.RESULT_OK -> {
                Log.i(TAG, "Se recibe: ${data?.getParcelableExtra<RoundRunDetail>("currentRound")}")
            }
            // Other result codes
            else -> {}
        }
    }

    //Bluetooth
    private fun bindBluetoothService() {
        // Bind to LocalService
        Log.i(TAG, "Se inicia servicio")
        Intent(
            this.applicationContext,
            BluetoothSDKService::class.java
        ).also { intent ->
            Log.i(TAG, "intent Se inicia servicio")
            this.applicationContext.bindService(
                intent,
                connection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as BluetoothSDKService.LocalBinder
            mService = binder.getService()
            Log.i(TAG, "[MAIN] ****** CONECTADO")

            //val fragmentInstance = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            //Log.i(TAG, "[MAIN] ****** CONECTADO ${navController.currentBackStackEntry.toString()}")
            //if(fragmentInstance is HomeFragment){
            //  Log.i(TAG, "[MAIN] ****** CONECTADO getBondedDevices")

            //}
            mService?.LocalBinder()?.getBondedDevices()
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i(TAG, "[MAIN] ****** DESCONECTADO")
        }
    }

    fun showCustomProgressDialog(){
        if(mProgressDialog != null && mProgressDialog!!.isShowing){
            return
        }
        mProgressDialog = Dialog(this)
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
            Handler(Looper.getMainLooper()).postDelayed({
                if (it.isShowing){
                    it.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Alerta")
                    builder.setMessage("No se pudo establecer la comunicación con el Mixer")

                    builder.setPositiveButton(android.R.string.yes) { _, _ ->
                        it.dismiss()
                    }
                    builder.show()
                }
            }, 10000)
        }
    }


    fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    fun bluetoothConnectionError(){
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let {fragment->
                if (fragment is MixerListFragment){
                    fragment.bluetoothConnectionError()
                }
            }
        }
    }

    fun bluetoothConnectionSuccess(){
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let {fragment->
                if (fragment is MixerListFragment){
                    runOnUiThread {
                        fragment.bluetoothConnectionSuccess()
                    }


                }
            }
        }
    }

    fun saveMixer(mixer: Mixer){
        lifecycleScope.launch(Dispatchers.IO){
            saveMixer(mixer.id)
        }
    }

    private suspend fun saveMixer(idMixer: Long){
        datastore.edit { preferences->
            preferences[longPreferencesKey("IDMIXER")] = idMixer
        }
    }


    fun saveTabletMixer(tabletMixer: TabletMixer){
        lifecycleScope.launch(Dispatchers.IO){
            saveTabletMixer(tabletMixer.id)
        }
    }

    private suspend fun saveTabletMixer(idTablet: Long){
        datastore.edit { preferences->
            preferences[longPreferencesKey("IDTABLET")] = idTablet
        }
    }


    fun getSavedTabletMixer(){
        lifecycleScope.launch(Dispatchers.IO){
            Log.i(TAG,"getSavedTabletMixer")
            val flowLong = getSavedMixerTabletId()
            flowLong.collect {id->
                if(id==null){
                    return@collect
                }
                val localKnowDevice = mTabletMixerViewModel.getTabletMixerById(id)
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        localKnowDevice.observe(this@MainActivity){tabletMixer->
                            if (tabletMixer != null){
                                selectedTabletMixerInActivity = tabletMixer
                                val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                                navHost?.let { navFragment ->
                                    navFragment.childFragmentManager.primaryNavigationFragment?.let {fragment->
                                        Log.i(TAG,"observe selectedMixerInFragment $selectedTabletMixerInActivity")
                                        if(selectedTabletMixerInActivity != null){
                                            when (fragment){
                                                is RemoteMixerFragment->{
                                                    fragment.setTabletMixer(selectedTabletMixerInActivity!!)
                                                }
                                                is TabletMixerListFragment->{
                                                    fragment.setTabletMixer(selectedTabletMixerInActivity!!)
                                                    fragment.connect(selectedTabletMixerInActivity!!)
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }




    fun deviceDisconnected() {
        if(isConnected && binding.appBarMain.toolbarMain.menu.size>0){
            Log.i(TAG,"main disconnected icon off")
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_disconnected_24px)
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon?.setTint(getColor(R.color.white))
            isConnected = false
        }

    }

    fun deviceConnected() {
        hideCustomProgressDialog()
        if(!isConnected && binding.appBarMain.toolbarMain.menu.size>0){
            Log.i(TAG,"main connected icon on")
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_bluetooth_connected_24px)
            binding.appBarMain.toolbarMain.menu?.getItem(0)?.icon?.setTint(getColor(R.color.white))
            isConnected = true
        }
    }

    fun getSavedMixer(){
        lifecycleScope.launch(Dispatchers.IO){
            Log.i(TAG,"getSavedMixer")
            val flowLong = getSavedMixerId()
            flowLong.collect {id->
                if(id==null){
                    return@collect
                }
                val localKnowDevice = mMixerViewModel.getMixerById(id)
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        localKnowDevice.observe(this@MainActivity){mixer->
                            if (mixer != null){
                                selectedMixerInActivity = mixer
                                val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                                navHost?.let { navFragment ->
                                    navFragment.childFragmentManager.primaryNavigationFragment?.let {fragment->
                                        Log.i(TAG,"observe selectedMixerInFragment $selectedMixerInActivity")
                                        if(fragment is HomeFragment){
                                            fragment.setMixer(selectedMixerInActivity)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getSavedMixerTabletId() = datastore.data.map { preferences->
        preferences[longPreferencesKey("IDTABLET")]
    }

    private fun getSavedMixerId() = datastore.data.map { preferences->
        preferences[longPreferencesKey("IDMIXER")]
    }

    fun changeActionBarTitle(title: String) {
        supportActionBar?.let {
            it.title = title
        }
    }

    fun isCustomProgresDialogShowing(): Boolean {
        return mProgressDialog?.isShowing == true
    }

}

class CancellableNavigationView(context: Context, attrs: AttributeSet) :
    NavigationView(context, attrs) {
    private var cancellableListener =
        object : OnNavigationItemSelectedListener {
            var listener: OnNavigationItemSelectedListener? = null
            var prevListener: OnNavigationItemSelectedListener? = null

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (listener?.onNavigationItemSelected(item) == false) {
                    return false
                }
                return prevListener?.onNavigationItemSelected(item) ?: true
            }
        }

    override fun setNavigationItemSelectedListener(
        listener: OnNavigationItemSelectedListener?
    ) {
        cancellableListener.prevListener = listener
        super.setNavigationItemSelectedListener(cancellableListener)
    }

    fun setNavigationItemSelectedPreviewListener(
        listener: OnNavigationItemSelectedListener?
    ) {
        cancellableListener.listener = listener
    }

}