package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityLoginBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.UserApiService
import com.basculasmagris.visorremotomixer.utils.Constants
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
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.checkForInternet
import com.basculasmagris.visorremotomixer.utils.Session
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.viewmodel.EstablishmentViewModel
import com.basculasmagris.visorremotomixer.viewmodel.EstablishmentViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var alertDialog: android.app.AlertDialog? = null
    private val userApiService = UserApiService()
    private val TAG = "DEBLogin"
    private var sharedpreferences: SharedPreferences? = null
    private lateinit var mCustomListDialog : Dialog
    private val mUsersList: ArrayList<CustomListItem> = ArrayList<CustomListItem>()
    private var selectedUser: CustomListItem? = null
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as SpiMixerApplication).userRepository)
    }
    private val mUserList: ArrayList<CustomListItem> = ArrayList<CustomListItem>()
    private val originUserList: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedpreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);


        binding.tiUsersRef.visibility = GONE
        binding.username.visibility = VISIBLE
        binding.llLoginNoData.visibility = GONE
        binding.llLoginData.visibility = VISIBLE

        binding.etUserRef.setOnClickListener {
            Log.i(TAG, "[mUsersList]: ${mUserList.count()}")
            customItemsLDialog(getString(R.string.usuarios), mUserList, Constants.USER_REF)
        }

        mUserViewModel.allUserList.observe(this) {

                users ->
            mUserList.clear()
            originUserList.clear()
            Log.i(TAG, "[allUserList]: ${users.count()}")
            if(users.count() == 0){
                binding.switchRole.visibility = INVISIBLE
                binding.llLoginNoData.visibility = GONE
                binding.llLoginData.visibility = GONE
                binding.llLoginFirstIn.visibility = VISIBLE
            }else{
                users.let {
                    users.forEach {

                        if (it.archiveDate.isNullOrEmpty()) {
                            val item = CustomListItem(
                                it.id,
                                it.remoteId,
                                it.name,
                                it.username,
                                R.drawable.ic_face
                            )
                            mUserList.add(item)
                            originUserList.add(it)
                        }
                    }
                    Log.i(TAG, "[mUserList]: ${mUserList.count()}")
                    selectedUser = if (mUserList.isNotEmpty()) mUserList[0] else null
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
        }

        binding.switchRole.setOnCheckedChangeListener { compoundButton, isChecked ->
            Log.i(TAG, "isChecked: $isChecked")
            if (isChecked) {
                showUserList()
            } else {
                hideUserList()
            }
        }

        alertDialog = Helper.setProgressDialog(this, "Validando credenciales")
        binding.login.setOnClickListener {

            val isAvailableInternet = false //checkForInternet(this)
            Log.i("INTERNET", "INTERNET Conexion: $isAvailableInternet")

            val username = if (binding.switchRole.isChecked) selectedUser?.description else binding.username.text.toString()
            when (Helper.isAllowedLoginOffline(this, originUserList, username.toString(), binding.password.text.toString())) {
                LOGIN_OFFLINE_INVALID_CREDENTIALS -> {
                    if (!isAvailableInternet) {
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña inválida", Toast.LENGTH_SHORT).show()
                    } else {
                        connectionOnline()
                    }
                }
                LOGIN_OFFLINE_OK -> {
                    val editor = sharedpreferences?.edit()
                    val userLogged = originUserList.first { user ->
                        user.username == username
                    }
                    Log.v(TAG,"userLogged $userLogged")
                    editor?.putString(PREF_LOGIN_KEY_NAME, userLogged.name)
                    editor?.putString(PREF_LOGIN_KEY_MAIL, userLogged.mail)
                    editor?.putString(PREF_LOGIN_KEY_LASTNAME, userLogged.lastname)
                    editor?.putInt(PREF_LOGIN_KEY_ROLE, userLogged.codeRole)
                    editor?.putString(PREF_LOGIN_KEY_USERNAME, userLogged.username)
                    editor?.putString(PREF_LOGIN_KEY_CODE_CLIENT, userLogged.codeClient)
                    editor?.putLong(PREF_LOGIN_KEY_ID, userLogged.id)
                    editor?.putBoolean(PREF_IS_LOGGED, true)
                    editor?.commit()
                    Toast.makeText(this, "Conexión OFFLINE ${username} | ${userLogged.name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                   // connectionOnline()
                }
                LOGIN_OFFLINE_NO_DATA -> {
                    alertDialog?.show()
                    if (isAvailableInternet) {
                        connectionOnline()
                    } else {
                        alertDialog?.dismiss()
                        Toast.makeText(this@LoginActivity, "Se requiere conexión a internet para iniciar sesión por primera vez", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.btnSearchAndSincro.setOnClickListener {
            val intent = Intent(this, TabletMixerConfigActivity::class.java)
            intent.putExtra(Constants.EXTRA_MIXER_MODE, false)
            intent.putExtra(Constants.FIRST_IN, true)
            someActivityResultLauncher.launch(intent)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Aquí manejas el resultado de la actividad
        Log.v(TAG,"Return from MixerConfigActivity")
        if (result.resultCode == Activity.RESULT_OK) {
            Log.v(TAG,"result code OK")
            val intent: Intent? = result.data
            var mixerReturned: TabletMixer? = null
            if (intent?.hasExtra(Constants.EXTRA_MIXER_DETAILS) == true){
                mixerReturned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent?.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS,  TabletMixer::class.java)
                } else {
                    intent?.getParcelableExtra<TabletMixer>(Constants.EXTRA_MIXER_DETAILS)
                }
            }
            Log.v(TAG,"mixerReturned $mixerReturned")
            mixerReturned?.let {
                Log.v(TAG,"MixerReturned ${it.name}  ${it.mac}  selectedTabletMixerInFragment")
                binding.llLoginFirstIn.visibility = GONE
            }
        }
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
        userApiService.login(username.toString(), binding.password.text.toString()).subscribeOn(
            Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<UserRemote>() {
                override fun onSuccess(value: UserRemote) {
                    Toast.makeText(this@LoginActivity, "Conexión ONLINE ${value.role.description}", Toast.LENGTH_SHORT).show()

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

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    alertDialog?.hide()
                }

                override fun onError(e: Throwable?) {
                    Toast.makeText(this@LoginActivity, "Usuario o contraseña inválida", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Login onError: ${e?.message}")
                    alertDialog?.hide()
                    e!!.printStackTrace()
                }
            })
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
            Constants.USER_REF -> {
                selectedUser = item

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