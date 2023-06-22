package com.basculasmagris.visorremotomixer.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateUserBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomImageSelectionBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddUpdateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateUserBinding

    private var mImagePath: String = ""
    private val maskPassword = "********"
    private lateinit var mCustomListDialog : Dialog
    private var mUserDetails: User? = null
    private var mNewUserDetails: User? = null

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as SpiMixerApplication).userRepository)
    }

    private var roles: ArrayList<Pair<Long, String>> = arrayListOf(
        Pair(2L, "Operario"),
        Pair(3L, "Supervisor")
    )
    private val mRolesList: ArrayList<CustomListItem> = ArrayList<CustomListItem>()
    private  var selectedRole: CustomListItem? = null

    private  fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener{
            Dexter.withContext(this@AddUpdateUserActivity).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    report?.let {
                        if (report.areAllPermissionsGranted()){
                            val intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            resultCameraLauncher.launch(intent)
                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRelationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener{
            Dexter.withContext(this@AddUpdateUserActivity).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {

                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    resultGalleryLauncher.launch(galleryIntent)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@AddUpdateUserActivity, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRelationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    var resultCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.extras?.let {
                val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap

                Glide.with(this)
                    .load(thumbnail)
                    .centerCrop()
                    .into(binding.ivUserImage)

                mImagePath = saveImageToInternalStorage(thumbnail)

                binding.ivAddUserImage.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))

                binding.ivAddUserImage.visibility = INVISIBLE
            }
        }
    }

    var resultGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedPhotoUri = data?.data
            Glide.with(this)
                .load(selectedPhotoUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            val bitmap: Bitmap = resource.toBitmap()
                            mImagePath = saveImageToInternalStorage(bitmap)
                        }

                        return false
                    }

                })
                .centerCrop()
                .into(binding.ivUserImage)
            binding.ivAddUserImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
        }
    }

    private fun showRelationalDialogForPermissions(){
        AlertDialog.Builder(this)
            .setMessage("SPI Mixer solicita permisos para acceder a la cámara. Puedes habilitarlo accediendo a la configuración")
            .setPositiveButton("Configuración")
            {_,_ -> try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)

            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            }
            .setNegativeButton("Cancelar"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) : String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }

        return file.absolutePath
    }

    companion object {
        private const val IMAGE_DIRECTORY = "SpiMixerImages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)
        }

        Log.i("USER", "mUserDetails: $mUserDetails")


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
                            saveUser()
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

        mRolesList.clear()
        roles.forEach {
            val item = CustomListItem(it.first, it.first, it.second, it.second, R.drawable.ic_face)
            mRolesList.add(item)
        }

        mUserDetails?.let {
            if (it.id != 0L){
                binding.etUserName.setText(it.name)
                binding.etUserLastname.setText(it.lastname)
                binding.btnAddUser.text = resources.getString(R.string.lbl_update_user)
                val currentRole = mRolesList.first { role -> role.id ==  it.codeRole.toLong()}
                binding.etRoleRef.setText(currentRole.name)
                binding.etUserMail.setText(it.mail)
                binding.etUserPassword.setText(maskPassword)
            } else {
                val currentRole = mRolesList[0]
                binding.etRoleRef.setText(currentRole.name)
            }
        }

        binding.etRoleRef.setOnClickListener {
            customItemsLDialog("Establecimientos disponibles", mRolesList, Constants.ROLE_REF)

        }

        binding.ivUserImage.setOnClickListener{
            Log.i("DEBUG","Holis AddUpdateUser")
            customImageSelectionDialog()
        }

        binding.btnAddUser.visibility = GONE

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_HIDE_NAVIGATION

        binding.etUserPassword.setOnFocusChangeListener { _, b ->
            if(b){
                binding.svUser.scrollTo(0,150)
            }else{
                binding.svUser.scrollTo(0,0)
            }
        }


    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mUserDetails != null && mUserDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_user)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_user)
            }
        }

        binding.toolbarAddUpdateUser.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    private fun getUserName(user: User): String {
        var username = "${user.name}.${user.lastname}".lowercase(Locale.getDefault())
        username = username.replace(Regex("/[^a-zA-Z.]/g"), "")
        return username
    }

    fun saveUser(){
        val sharedpreferences = getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE)
        val userName = binding.etUserName.text.toString().trim { it <= ' ' }
        val userLastName = binding.etUserLastname.text.toString().trim { it <= ' ' }
        val userPassword = binding.etUserPassword.text.toString().trim { it <= ' ' }
        var remoteId = 0L
        val mail = binding.etUserMail.text.toString().trim { it <= ' ' }
        var codeRole = 2
        selectedRole?.id?.let {
            codeRole = it.toInt()
        }
        val codeClient = sharedpreferences.getString(Constants.PREF_LOGIN_KEY_CODE_CLIENT, "").toString()

        mUserDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(userName) -> {
                Toast.makeText(
                    this@AddUpdateUserActivity,
                    resources.getString(R.string.err_msg_user_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var userId = 0L
                var updatedDate = ""
                var username = ""
                mUserDetails?.let {
                    username = getUserName(it)
                }

                mUserDetails?.let {
                    if (it.id != 0L) {
                        userId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        username = it.username
                    }
                }

                val user = User(
                    username,
                    userName,
                    userLastName,
                    mail,
                    userPassword,
                    remoteId,
                    updatedDate,
                    "",
                    codeRole,
                    codeClient,
                    userId
                )

                mNewUserDetails = user

                runBlocking {

                    Log.i("USER", "Data")
                    Log.i("USER", "userId: $userId")
                    Log.i("USER", "Usuario: $user")
                    Log.i("USER", "updatedDate: $updatedDate")
                    Log.i("USER", "userPassword: $userPassword | ${user.password}")
                    if (userId == 0L) {
                        mUserViewModel.insertSync(user)
                    } else {
                        mUserViewModel.updateSync(user)
                    }

                    Log.i("SYNC", "Se modifica establecimiento con fecha ${user.updatedDate}")
                    finish()
                }
            }
        }
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        when (selection){
            Constants.ROLE_REF -> {
                selectedRole = item
                binding.etRoleRef.setText(item.name)
                mCustomListDialog.dismiss()
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

    fun toggleKeyboard() {
        if(isKeyBoardShowing()){
            hideSoftKeyboard()
        }else{
            showKeyboard()
        }
    }
    fun Activity.hideSoftKeyboard() {
        val view = binding.root
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