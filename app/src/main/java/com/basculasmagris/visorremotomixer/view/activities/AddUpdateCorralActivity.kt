package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateCorralBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.Corral
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.viewmodel.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateCorralActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateCorralBinding


    private lateinit var mCustomListDialog : Dialog
    private var mCorralDetails: Corral? = null
    private var mNewCorralDetails: Corral? = null
    private var selectedEstablishment: CustomListItem? = null

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((application as SpiMixerApplication).corralRepository)
    }

    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((application as SpiMixerApplication).establishmentRepository)
    }
    private val mEstablishmentList: ArrayList<CustomListItem> = ArrayList<CustomListItem>()

    private fun getLocalEstablishment(){
        mEstablishmentViewModel.allEstablishmentList.observe(this) {
                establishments ->
            Log.i("OBSERVER", "[add corral] mEstablishmentViewModel.allEstablishmentList")
            establishments.let{
                    establishments.forEach {

                        if (it.archiveDate.isNullOrEmpty()){
                            val item = CustomListItem(it.id, it.remoteId, it.name, it.description, R.drawable.ic_local_dining)
                            mEstablishmentList.add(item)
                        }

                    }

                    mCorralDetails?.let {
                        if (it.id != 0L){
                            val establishment =  mEstablishmentList.filter { establishment ->
                                establishment.id == it.establishmentId
                            }
                            establishment.let {  _establishment ->
                                if (_establishment.isNotEmpty()){
                                    selectedEstablishment = _establishment[0]
                                    binding.tiEstablishmentRef.setText(_establishment[0].name)
                                } else {
                                    selectedEstablishment = mEstablishmentList[0]
                                    binding.tiEstablishmentRef.setText(mEstablishmentList[0].name)
                                }

                            }
                        }
                    }

                    if (mCorralDetails == null && mEstablishmentList.isNotEmpty()) {
                        selectedEstablishment = mEstablishmentList[0]
                        binding.tiEstablishmentRef.setText(mEstablishmentList[0].name)
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateCorralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_CORRAL_DETAILS)){
            mCorralDetails = intent.getParcelableExtra(Constants.EXTRA_CORRAL_DETAILS)
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
                            saveCorral()
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

        // Obtenemos los establecimientos disponibles
        getLocalEstablishment()

        mCorralDetails?.let {
            if (it.id != 0L){
                binding.tiCorralName.setText(it.name)
                binding.tiCorralDescription.setText(it.description)
                binding.btnAddCorral.text = resources.getString(R.string.lbl_update_corral)
                binding.tiRfid.setText(it.rfid.toString())
                binding.tiAnimalQuantity.setText(it.animalQuantity.toString())
            }
        }

        binding.btnAddCorral.visibility = GONE
        //binding.btnAddCorral.setOnClickListener(this)
        binding.tiEstablishmentRef.setOnClickListener(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateCorral)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mCorralDetails != null && mCorralDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_corral)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_corral)
            }
        }

        binding.toolbarAddUpdateCorral.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveCorral(){
        var corralEstablishmentRef = 0L
        var corralEstablishmentRemoteId = 0L
        selectedEstablishment?.let {
            corralEstablishmentRef = it.id
            corralEstablishmentRemoteId = it.remoteId
        }
        val corralName = binding.tiCorralName.text.toString().trim { it <= ' ' }
        val corralDescription = binding.tiCorralDescription.text.toString().trim { it <= ' ' }
        val corralRfId = binding.tiRfid.text.toString().trim { it <= ' ' }
        val corralAnimalQuantity = binding.tiAnimalQuantity.text.toString().trim { it <= ' ' }
        var remoteId = 0L

        mCorralDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(corralName) -> {
                Toast.makeText(
                    this@AddUpdateCorralActivity,
                    resources.getString(R.string.err_msg_corral_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var corralId = 0L
                var updatedDate = ""

                mCorralDetails?.let {
                    if (it.id != 0L){
                        corralId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    }
                }

                val corral = Corral(
                    corralEstablishmentRef,
                    corralEstablishmentRemoteId,
                    corralName,
                    corralDescription,
                    remoteId,
                    updatedDate,
                    "",
                    if (corralAnimalQuantity.isEmpty()) 0 else corralAnimalQuantity.toInt(),
                    if (corralRfId.isEmpty()) 0 else corralRfId.toLong(),
                    corralId
                )

                mNewCorralDetails = corral

                runBlocking {
                    if (corralId == 0L){
                        mCorralViewModel.insertSync(corral)
                    } else {
                        mCorralViewModel.update(corral)
                    }

                    finish()
                }

            }
        }
    }
    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.ti_establishment_ref -> {
                    Log.i("corral TOQUÉ LISA", "corral de lista")
                    customItemsLDialog("Establecimientos disponibles", mEstablishmentList, Constants.ESTABLISHMENT_REF)
                }
            }
        }
    }

    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        when (selection){
            Constants.ESTABLISHMENT_REF -> {
                selectedEstablishment = item
                binding.tiEstablishmentRef.setText(item.name)
                mCustomListDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mEstablishmentViewModel.allEstablishmentList.removeObservers(this)
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
