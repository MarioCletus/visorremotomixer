package com.basculasmagris.visorremotomixer.view.activities


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityLoginBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.UserApiService
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Constants.BASE_URL
import com.basculasmagris.visorremotomixer.utils.Constants.BASE_URL_POR_DEFECTO
import com.basculasmagris.visorremotomixer.utils.Constants.LOGIN_OFFLINE_INVALID_CREDENTIALS
import com.basculasmagris.visorremotomixer.utils.Constants.LOGIN_OFFLINE_NO_DATA
import com.basculasmagris.visorremotomixer.utils.Constants.LOGIN_OFFLINE_OK
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_IS_LOGGED
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ACCESS_TOKEN
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_CODE_CLIENT
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ENCRYPTED_PASSWORD
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ID
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_LASTNAME
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_MAIL
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_NAME
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ROLE
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ROLE_DESC
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_USERNAME
import com.basculasmagris.visorremotomixer.utils.CustomAlertDialogBuilder
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.checkForInternet
import com.basculasmagris.visorremotomixer.utils.Session
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var alertDialog: android.app.AlertDialog? = null
    private val userApiService = UserApiService()
    private val TAG = "DEBLog"
    private var sharedpreferences: SharedPreferences? = null
    private lateinit var mCustomListDialog : Dialog
    private var selectedUser: CustomListItem? = null
    private var usersList: MutableList<User>? = null
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as SpiMixerVRApplication).userRepository)
    }
    private val mUserList: ArrayList<CustomListItem> = ArrayList<CustomListItem>()
    private val originUserList: ArrayList<User> = ArrayList()

    private val roles: ArrayList<Pair<Int, String>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedpreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        initRoles()

        //TEST
        binding.tiUsersRef.visibility = GONE
        binding.username.visibility = VISIBLE
        binding.llLoginNoData.visibility = GONE
        binding.llLoginData.visibility = VISIBLE


        val urlSuggestions = arrayListOf(
            Constants.BASE_URL0,
            Constants.BASE_URL1,
            Constants.BASE_URL2
        )
        urlSuggestions.add(getString(R.string.por_defecto))


        binding.ivServer.setOnClickListener{
            if(binding.llUrlServer.isVisible){
                binding.llUrlServer.visibility = GONE
            }else{
                binding.llUrlServer.visibility = VISIBLE
            }
        }

        val userSharedPreferences = getSharedPreferences("UserConfig", Context.MODE_PRIVATE)
        val serverUrl = userSharedPreferences.getString("serverUrl", BASE_URL)?:BASE_URL//
        BASE_URL = serverUrl
        Log.i(TAG," serverUrl: $serverUrl - BASE_URL: $BASE_URL")

// Crear el ArrayAdapter para el autocompletado
        val urlAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, urlSuggestions)
        binding.spServer.adapter = urlAdapter

// Seleccionar automáticamente el item correspondiente a BASE_URL
        val selectedPosition = urlSuggestions.indexOfFirst {
            it == BASE_URL || (it == getString(R.string.por_defecto) && BASE_URL == BASE_URL_POR_DEFECTO)
        }
        if (selectedPosition >= 0) {
            binding.spServer.setSelection(selectedPosition)
        }

        binding.etUserRef.setOnClickListener {
            Log.i("LOGIN", "[mUsersList]: ${mUserList.count()}")
            customItemsLDialog(getString(R.string.usuarios), mUserList, Constants.USER_REF)
        }

        binding.ivGraintCart.setOnClickListener {
//            startActivity(Intent(this@LoginActivity, DisplayWeightActivity::class.java))
        }

        binding.spServer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val serverUrlSelected = parent?.getItemAtPosition(position).toString()
                val sharedPreferences: SharedPreferences = getSharedPreferences("UserConfig", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                when (serverUrlSelected){
                    getString(R.string.por_defecto) -> {
                        BASE_URL = BASE_URL_POR_DEFECTO
                        editor.putString("serverUrl", BASE_URL)
                        Log.i(TAG,"por defecto BASE_URL: $BASE_URL")
                        editor.apply()
                    }
                    ""->{}
                    else->{
                        BASE_URL = serverUrlSelected
                        editor.putString("serverUrl", serverUrlSelected)
                        Log.i(TAG,"else BASE_URL: $BASE_URL")
                        editor.apply()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        mUserViewModel.allUserList.observe(this) {

                users ->
            mUserList.clear()
            originUserList.clear()
            Log.i("LOGIN", "[allUserList]: ${users.count()}")
            usersList = users
            users.let{

                users.forEach {

                    if (it.archiveDate.isNullOrEmpty() && it.codeRole != Constants.USER_API){
                        val item = CustomListItem(it.id, it.remoteId, "${it.name} ${it.lastname} - ${it.username} - ${roles.firstOrNull{role->it.codeRole == role.first}?.second?:""}", it.username, R.drawable.ic_face)
                        mUserList.add(item)
                        originUserList.add(it)
                    }
                }
                Log.i("LOGIN", "[mUserList]: ${mUserList.count()}")
                val savedUserId = sharedpreferences?.getLong(PREF_LOGIN_KEY_ID, -1) ?: -1L
                selectedUser = mUserList.firstOrNull { it.id == savedUserId }
                if(selectedUser == null){
                    selectedUser = if (mUserList.isNotEmpty()) mUserList.firstOrNull { it.remoteId == savedUserId }?:mUserList[0]
                    else
                        null
                }
                binding.etUserRef.setText(selectedUser?.name)

                if (originUserList.size > 0) {
                    binding.switchRole.isChecked = true
                    showUserList()
                } else {
                    binding.switchRole.isChecked = false
                    hideUserList()
                }

            }
        }

        binding.switchRole.setOnCheckedChangeListener { compoundButton, isChecked ->
            Log.i("LOGIN", "isChecked: $isChecked")
            if (isChecked) {
                showUserList()
            } else {
                hideUserList()
            }
        }

        alertDialog = Helper.setProgressDialog(this, getString(R.string.validando_credenciales))
        binding.login.setOnClickListener {

            val isAvailableInternet = checkForInternet(this)
            Log.i("INTERNET", "INTERNET Conexion: $isAvailableInternet")

            val username = if (binding.switchRole.isChecked) selectedUser?.description else binding.username.text.toString()
            val name = selectedUser?.name
            val id = selectedUser?.id
            Log.i(TAG,"username: $username  name: $name  id: $id")
            when (Helper.isAllowedLoginOffline(this, originUserList, username.toString(), binding.password.text.toString())) {
                LOGIN_OFFLINE_INVALID_CREDENTIALS -> {
                    Log.i(TAG,"LOGIN_OFFLINE_INVALID_CREDENTIALS")
                    if (!isAvailableInternet) {
                        Toast.makeText(this@LoginActivity, getString(R.string.usuario_invalido), Toast.LENGTH_SHORT).show()
                    } else {
                        connectionOnline()
                    }
                }
                LOGIN_OFFLINE_OK -> {
                    val editor = sharedpreferences?.edit()
                    val userLogged = originUserList.first { user ->
                        user.username == username
                    }
                    Log.i(TAG,"LOGIN_OFFLINE_OK $userLogged")
                    editor?.putString(PREF_LOGIN_KEY_NAME, userLogged.name)
                    editor?.putString(PREF_LOGIN_KEY_MAIL, userLogged.mail)
                    editor?.putString(PREF_LOGIN_KEY_LASTNAME, userLogged.lastname)
                    editor?.putInt(PREF_LOGIN_KEY_ROLE, userLogged.codeRole)
                    editor?.putString(PREF_LOGIN_KEY_USERNAME, userLogged.username)
                    editor?.putString(PREF_LOGIN_KEY_CODE_CLIENT, userLogged.codeClient)
                    editor?.putLong(PREF_LOGIN_KEY_ID, userLogged.id)
                    editor?.putBoolean(PREF_IS_LOGGED, true)
                    editor?.commit()
                    Log.i(TAG,"PREF_LOGIN_KEY_ID offline ${userLogged.id}")
                    Toast.makeText(this, getString(R.string.conexion_ofline,"${username} | ${userLogged.name}"), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    // connectionOnline()
                }
                LOGIN_OFFLINE_NO_DATA -> {
                    Log.i(TAG,"LOGIN_OFFLINE_NO_DATA")
                    alertDialog?.show()
                    if (isAvailableInternet) {
                        connectionOnline()
                    } else {
                        alertDialog?.dismiss()
                        Toast.makeText(this@LoginActivity, getString(R.string.se_requiere_internet), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun initRoles() {
        roles.add(Pair(1,getString(R.string.role_admin)))
        roles.add(Pair(2,getString(R.string.role_operator)))
        roles.add(Pair(3,getString(R.string.role_supervisor)))
        roles.add(Pair(4,getString(R.string.role_superadmin)))
        roles.add(Pair(5,getString(R.string.role_nutritionist)))
        roles.add(Pair(6,getString(R.string.role_api)))
    }

    private fun hideUserList(){
        binding.tiUsersRef.visibility = GONE
        binding.username.visibility = VISIBLE
        binding.llLoginNoData.visibility = GONE
        binding.llLoginData.visibility = VISIBLE
    }

    private fun showUserList(){
        if (originUserList.isNotEmpty()) {
            binding.tiUsersRef.visibility = VISIBLE
            binding.username.visibility = GONE
            binding.llLoginNoData.visibility = GONE
            binding.llLoginData.visibility = VISIBLE
        } else {
            binding.llLoginNoData.visibility = VISIBLE
            binding.llLoginData.visibility = GONE
        }
    }

    private fun connectionOnline(){
        val username = if (binding.switchRole.isChecked) selectedUser?.description else binding.username.text.toString()
        Log.i(TAG,"is connection online?? $username")
        Log.i(TAG,"connectionOnlin BASE_URL: $BASE_URL")
        userApiService.login(username.toString(), binding.password.text.toString()).subscribeOn(
            Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<UserRemote>() {
                override fun onSuccess(value: UserRemote) {
                    Toast.makeText(this@LoginActivity, getString(R.string.conexion_online,"${value.role.description}"), Toast.LENGTH_SHORT).show()
                    usersList?.let{mutableListUsers->
                        if(mutableListUsers.isNotEmpty()){
                            val codeClient = mutableListUsers.firstOrNull{
                                it.codeClient.isNotEmpty()
                            }?.codeClient
                            if(codeClient != null && value.codeClient.isNotEmpty() && value.codeClient != codeClient){
                                val dialogBuilder = CustomAlertDialogBuilder(this@LoginActivity)

                                // set message of alert dialog
                                dialogBuilder.setMessage(getString(R.string.hay_datos_de_otro_cliente))
                                    .setCancelable(false)
                                    .setTitle(getString(R.string.warning))
                                    .setPositiveButton(getString(R.string.aceptar)) { dialog, _ ->
                                        dialog.dismiss()
                                        finish()
                                    }
                                val alert = dialogBuilder.create()
                                alert?.show()
                                return
                            }
                        }
                    }
                    val editor = sharedpreferences?.edit()
                    editor?.putString(PREF_LOGIN_KEY_NAME, value.name)
                    editor?.putString(PREF_LOGIN_KEY_MAIL, value.mail)
                    editor?.putString(PREF_LOGIN_KEY_LASTNAME, value.lastname)
                    editor?.putString(PREF_LOGIN_KEY_ROLE_DESC, value.role.description)
                    editor?.putString(PREF_LOGIN_KEY_ACCESS_TOKEN, value.accessToken)
                    Session.accessToken = value.accessToken
                    editor?.putInt(PREF_LOGIN_KEY_ROLE, value.codeRole)
                    editor?.putString(PREF_LOGIN_KEY_USERNAME, value.username)
                    editor?.putString(PREF_LOGIN_KEY_CODE_CLIENT, value.codeClient)
                    editor?.putLong(PREF_LOGIN_KEY_ID, value.id)
                    editor?.putString(PREF_LOGIN_KEY_ENCRYPTED_PASSWORD, Helper.md5(binding.password.text.toString()))
                    editor?.putBoolean(PREF_IS_LOGGED, true)

                    editor?.commit()
                    Log.i(TAG,"PREF_LOGIN_KEY_ID online${value.id}")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    alertDialog?.hide()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@LoginActivity, getString(R.string.usuario_invalido), Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Login onError: ${e?.message}")
                    alertDialog?.hide()
                    e.printStackTrace()
                }
            })
    }

    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(this,R.style.CustomDialogTheme)
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
            Constants.USER_REF -> {
                selectedUser = item
                Log.i(TAG,"selectedUser ${item.name} ${item.description} ${item.id}  ${item.remoteId}")
                binding.etUserRef.setText(item.name)
                mCustomListDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mUserList.clear()
        mUserViewModel.allUserList.removeObservers(this)
    }
}