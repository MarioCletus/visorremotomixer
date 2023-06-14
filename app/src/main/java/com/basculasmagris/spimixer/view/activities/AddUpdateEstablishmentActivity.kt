package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateEstablishmentBinding
import com.basculasmagris.visorremotomixer.model.entities.Establishment
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.viewmodel.EstablishmentViewModel
import com.basculasmagris.visorremotomixer.viewmodel.EstablishmentViewModelFactory
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateEstablishmentActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateEstablishmentBinding

    private var mEstablishmentDetails: Establishment? = null
    private var mNewEstablishmentDetails: Establishment? = null

    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((application as SpiMixerApplication).establishmentRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateEstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS)){
            mEstablishmentDetails = intent.getParcelableExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS)
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
                            saveEstablishment()
                        else
                            hideSoftKeyboard()
                        return true
                    }
                    R.id.action_hide_keyboard->{
                        toggleKeyboard()
                        return true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)

        mEstablishmentDetails?.let {
            if (it.id != 0L){
                binding.tiEstablishmentName.setText(it.name)
                binding.tiEstablishmentDescription.setText(it.description)
                binding.btnAddEstablishment.text = resources.getString(R.string.lbl_update_establishment)
            }
        }
        binding.btnAddEstablishment.visibility = GONE
        //binding.btnAddEstablishment.setOnClickListener(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }
    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateEstablishment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mEstablishmentDetails != null && mEstablishmentDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_establishment)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_establishment)
            }
        }

        binding.toolbarAddUpdateEstablishment.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveEstablishment(){
        val establishmentName = binding.tiEstablishmentName.text.toString().trim { it <= ' ' }
        val establishmentDescription = binding.tiEstablishmentDescription.text.toString().trim { it <= ' ' }
        var remoteId = 0L

        mEstablishmentDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(establishmentName) -> {
                Toast.makeText(
                    this@AddUpdateEstablishmentActivity,
                    resources.getString(R.string.err_msg_establishment_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var establishmentId = 0L
                var updatedDate = ""

                mEstablishmentDetails?.let {
                    if (it.id != 0L){
                        establishmentId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    }
                }

                val establishment = Establishment(
                    establishmentName,
                    establishmentDescription,
                    remoteId,
                    updatedDate,
                    "",
                    establishmentId
                )

                mNewEstablishmentDetails = establishment

                runBlocking {
                    if (establishmentId == 0L){
                        mEstablishmentViewModel.insertSync(establishment)
                    } else {
                        mEstablishmentViewModel.updateSync(establishment)
                    }

                    Log.i("SYNC", "Se modifica establecimiento con fecha ${establishment.updatedDate}")
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
