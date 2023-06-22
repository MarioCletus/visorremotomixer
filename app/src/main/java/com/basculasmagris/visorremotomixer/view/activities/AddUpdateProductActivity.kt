package com.basculasmagris.visorremotomixer.view.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateProductBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomImageSelectionBinding
import com.basculasmagris.visorremotomixer.model.entities.Product
import com.basculasmagris.visorremotomixer.viewmodel.ProductViewModel
import com.basculasmagris.visorremotomixer.viewmodel.ProductViewModelFactory
import com.karumi.dexter.Dexter
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.view.View.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.utils.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.runBlocking
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AddUpdateProductActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityAddUpdateProductBinding

    private var mImagePath: String = ""
    private var mProductDetails: Product? = null

    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((application as SpiMixerApplication).productRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_DETAILS)){
            mProductDetails = intent.getParcelableExtra(Constants.EXTRA_PRODUCT_DETAILS)
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
                            saveProduct()
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

        mProductDetails?.let {
            if (it.id != 0L){
                mImagePath = it.image
                Glide.with(this@AddUpdateProductActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(binding.ivProductImage)

                binding.tiProductName.setText(it.name)
                binding.tiProductDescription.setText(it.description)
                binding.tiRfid.setText(it.rfid.toString())
                binding.tiSpecificWeight.setText(it.specificWeight.toString())
                binding.btnAddProduct.text = resources.getString(R.string.lbl_update_product)
            }
        }

        binding.btnAddProduct.visibility = GONE
        binding.ivProductImage.setOnClickListener(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mProductDetails != null && mProductDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_product)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_product)
            }
        }

        binding.toolbarAddUpdateProduct.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun saveProduct(){
        val productName = binding.tiProductName.text.toString().trim { it <= ' ' }
        val productDescription = binding.tiProductDescription.text.toString().trim { it <= ' ' }
        val productSpecificWeight = binding.tiSpecificWeight.text.toString().trim { it <= ' ' }
        val productRfId = binding.tiRfid.text.toString().trim { it <= ' ' }
        var remoteId = 0L

        mProductDetails?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(productName) -> {
                Toast.makeText(
                    this@AddUpdateProductActivity,
                    resources.getString(R.string.err_msg_product_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var productId = 0L
                var imageSource = Constants.PRODUCT_IMAGE_SOURCE_LOCAL
                var updatedDate = ""

                mProductDetails?.let {
                    if (it.id != 0L){
                        productId = it.id
                        imageSource = it.imageSource
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    }
                }

                val product = Product(
                    productName,
                    productDescription,
                    if (productSpecificWeight.isEmpty()) 0.0 else productSpecificWeight.toDouble(),
                    if (productRfId.isEmpty()) 0 else productRfId.toLong(),
                    mImagePath,
                    imageSource,
                    remoteId,
                    updatedDate,
                    "",
                    productId
                )

                runBlocking {
                    if (productId == 0L){
                        mProductViewModel.insertSync(product)
                    } else {
                        Log.i("sync", "Se actualiza producto $productName con fecha $updatedDate")
                        mProductViewModel.updateSync(product)
                        Log.i("sync", "Se actualiza producto $productName con fecha $updatedDate")

                    }
                    finish()
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.iv_product_image -> {
                    Log.i("DEBUG","Holis iv_add_product_image")
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private  fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener{
            Dexter.withContext(this@AddUpdateProductActivity).withPermissions(
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
                Dexter.withContext(this@AddUpdateProductActivity).withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object : PermissionListener{

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        resultGalleryLauncher.launch(galleryIntent)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateProductActivity, "Permiso denegado", Toast.LENGTH_SHORT).show()
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
                    .into(binding.ivProductImage)

                mImagePath = saveImageToInternalStorage(thumbnail)

                binding.ivAddProductImage.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
                binding.ivAddProductImage.visibility = VISIBLE
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
                .into(binding.ivProductImage)
            binding.ivAddProductImage.setImageDrawable(
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

