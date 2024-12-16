package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateMixerBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel
import com.basculasmagris.visorremotomixer.viewmodel.MixerViewModelFactory
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateMixerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateMixerBinding
    private var mMixerDetails: Mixer? = null
    private var mNewMixerDetails: Mixer? = null

    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((application as SpiMixerVRApplication).mixerRepository)
    }

//    private var mProgressDialog: Dialog? = null

//    private fun showCustomProgressDialog(){
//        if(mProgressDialog != null && mProgressDialog!!.isShowing){
//            return
//        }
//        mProgressDialog = Dialog(this)
//        mProgressDialog?.let {
//            it.setContentView(R.layout.dialog_custom_progress)
//            it.show()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateMixerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddMixer.visibility = GONE
        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            mMixerDetails = intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS)
        }

        setupActionBar()

        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_add_update_round, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_round -> {
                        if(!isKeyBoardShowing())
                            saveMmixer()
                        else
                            hideSoftKeyboard()
                        return true
                    }
                    R.id.action_hide_keyboard -> {
                        toggleKeyboard()
                        return true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)

        mMixerDetails?.let {
            if (it.id != 0L){
                binding.tiMixerName.setText(it.name)
                binding.tiMixerDescription.setText(it.description)
                binding.tiRfid.setText(it.rfid.toString())
                binding.btnAddMixer.text = resources.getString(R.string.lbl_update_mixer)
                binding.etBtBox.setText(it.btBox)
                binding.etCalibration.setText(it.calibration.toString())
                binding.etMac.setText(it.mac)
                binding.etTara.setText(it.tara.toString())
            }
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateMixer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mMixerDetails != null && mMixerDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_mixer)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_mixer)
            }
        }

        binding.toolbarAddUpdateMixer.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveMmixer(){

        val mixerName = binding.tiMixerName.text.toString().trim { it <= ' ' }
        val mixerDescription = binding.tiMixerDescription.text.toString().trim { it <= ' ' }
        val mixerRfId = binding.tiRfid.text.toString().trim { it <= ' ' }
        var remoteId = 0L
        val mixerMac = binding.etMac.text.toString().trim { it <= ' ' }
        val mixerBtBox = binding.etBtBox.text.toString().trim { it <= ' ' }
        val mixerTara = binding.etTara.text.toString().trim { it <= ' ' }
        val mixerCalibration = binding.etCalibration.text.toString().trim { it <= ' ' }

        mMixerDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(mixerName) -> {
                Toast.makeText(
                    this@AddUpdateMixerActivity,
                    resources.getString(R.string.err_msg_mixer_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var mixerId = 0L
                var updatedDate = ""
                var linked = false

                mMixerDetails?.let {
                    if (it.id != 0L){
                        mixerId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        linked = it.linked == true
                    }
                }

                val mixer = Mixer(
                    mixerName,
                    mixerDescription,
                    mixerMac,
                    mixerBtBox,
                    if (mixerTara.isEmpty()) 0.0 else mixerTara.toDouble(),
                    if (mixerCalibration.isEmpty()) 1.toFloat() else mixerCalibration.toFloat(),
                    if (mixerRfId.isEmpty()) 0 else mixerRfId.toLong(),
                    remoteId,
                    updatedDate,
                    "",
                    linked,
                    mixerId
                )

                mNewMixerDetails = mixer

                runBlocking {
                    if (mixerId == 0L){
                        mMixerViewModel.insertSync(mixer)
                    } else {
                        mMixerViewModel.updateSync(mixer)
                    }
                    Log.i("SYNC", "Se actualiza mixer con fecha ${mixer.updatedDate}")
                    finish()
                }

            }
        }
    }


    fun toggleKeyboard() {
        if(isKeyBoardShowing()){
            hideSoftKeyboard()
        }else{
            showKeyboard()
        }
    }
    fun Activity.hideSoftKeyboard() {
        val view: View = binding.root
        view.let { _view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(_view.windowToken, 0)
        }
    }

    fun Activity.showKeyboard() { // Or View.showKeyboard()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethod.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun isKeyBoardShowing(): Boolean {
        val view: View = binding.root
        return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
    }

}