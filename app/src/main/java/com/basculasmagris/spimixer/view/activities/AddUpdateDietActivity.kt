package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateDietBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.Diet
import com.basculasmagris.visorremotomixer.model.entities.DietProduct
import com.basculasmagris.visorremotomixer.model.entities.DietProductDetail
import com.basculasmagris.visorremotomixer.model.entities.Product
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.view.adapter.DietProductAdapter
import com.basculasmagris.visorremotomixer.viewmodel.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateDietActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDietBinding

    private lateinit var mCustomListDialog : Dialog
    private var mNewDietDetails: Diet? = null
    private var currentDiet: Diet? = null
    private var positionFrom = -1
    private var positionTo = -1
    private var saveNewDiet = false

    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((application as SpiMixerApplication).dietRepository)
    }

    private lateinit var mDietViewModelRemote: DietRemoteViewModel
    private var mProgressDialog: Dialog? = null

    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((this.application as SpiMixerApplication).productRepository)
    }

    private var mLocalDietProductDetail: List<DietProductDetail>? = null
    private var mLocalProduct: List<Product>? = null
    private val mCustomListItem: ArrayList<CustomListItem> = ArrayList<CustomListItem>()
    private var dietProductAdapter: DietProductAdapter? = null

    // Drop and drag

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {


            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                Log.i("FAT", "onMove")
                //getting the adapter
                val adapter = recyclerView.adapter as DietProductAdapter

                //the position from where iteSwipeRecyclerViewHelperCallbackm has been moved
                val from = viewHolder.adapterPosition

                //the position where the item is moved
                val to = target.adapterPosition

                if (positionFrom == -1){
                    positionFrom = from
                }

                positionTo = to

                Log.i("FAT", "from $positionFrom to $positionTo")
                //telling the adapter to move the item
                adapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.i("FAT", "onSwiped")

                if ((binding.rvDietProductList.adapter as DietProductAdapter).getItems().size == 1) {
                    binding.rvDietProductList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.llTotal.visibility = View.GONE
                }

                dietProductAdapter?.onItemSwiped(viewHolder.layoutPosition)

            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                Log.i("FAT", "onSelectedChanged")
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
                }
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int = makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START or ItemTouchHelper.END
            )


            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                Log.i("FAT", "clearView")
                viewHolder.itemView.setBackgroundColor(Color.WHITE)

                val adapter = recyclerView.adapter as DietProductAdapter

                if (positionFrom != -1 || positionTo != -1){
                    if (positionFrom < positionTo){

                        for (index in positionFrom..positionTo){
                            val dietProductDetailFrom = adapter.getItemFrom(position = index)
                            var newOrder = index
                            if (index == positionFrom){
                                newOrder = positionTo+1
                            }
                            dietProductDetailFrom.order = newOrder

                            Log.i("FAT", "Se actualiza ${dietProductDetailFrom.productName} con " +
                                    "\n --> orden: $newOrder" +
                                    "\n --> weight: ${dietProductDetailFrom.weight}" +
                                    "\n --> percentage: ${dietProductDetailFrom.percentage}" +
                                    "")
                        }
                    } else {
                        Log.i("FAT", "clearView $positionFrom to $positionTo")
                        for (index in positionFrom downTo positionTo){

                            val dietProductDetailFrom = adapter.getItemFrom(position = index)
                            Log.i("FAT", "index $index")
                            var newOrder = dietProductDetailFrom.order + 1
                            if (index == positionFrom){
                                newOrder = positionTo+1
                            }

                            dietProductDetailFrom.order = newOrder

                            Log.i("FAT", "Se actualiza ${dietProductDetailFrom.productName} con " +
                                    "\n --> orden: $newOrder / ${adapter.getItemFrom(position = index).order}" +
                                    "\n --> weight: ${dietProductDetailFrom.weight}" +
                                    "\n --> percentage: ${dietProductDetailFrom.percentage}" +
                                    "")
                        }
                    }

                    adapter.updateList(adapter.getItems())
                }

                positionFrom = -1
                positionTo = -1
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }


    private fun showCustomProgressDialog(){
        if(mProgressDialog != null && mProgressDialog!!.isShowing){
            return
        }
        mProgressDialog = Dialog(this)
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    private fun fetchLocalData(dietId: Long): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mDietViewModel.getProductsBy(dietId)) {
            if (it != null) {
                liveDataMerger.value = DietProductDetailData(it)
            }
        }
        liveDataMerger.addSource(mProductViewModel.allProductList) {
            if (it != null) {
                liveDataMerger.value = ProductData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(dietId: Long){
        // Sync local data
        val liveData = fetchLocalData(dietId)
        liveData.observe(this, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is DietProductDetailData -> mLocalDietProductDetail = it.dietProductsDetail
                    is ProductData -> mLocalProduct = it.products
                    else -> {}
                }

                if (mLocalDietProductDetail != null && mLocalProduct != null) {

                    mLocalProduct?.forEach {
                        if (it.archiveDate.isNullOrEmpty()){
                            val item = CustomListItem(it.id, it.remoteId, it.name, it.description, R.drawable.ic_product)
                            mCustomListItem.add(item)
                        }
                    }
                    setDietProducts()
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    fun setDietProducts(){
        mLocalDietProductDetail?.let {
            val adapter = binding.rvDietProductList.adapter as DietProductAdapter


            if (it.isNotEmpty()){
                binding.rvDietProductList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.llTotal.visibility = View.VISIBLE
                adapter.dietList(it)
            } else {
                binding.rvDietProductList.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
                binding.llTotal.visibility = View.GONE
            }

            var totalWeight = 0.0
            var totalPercentage = 0.0
            it.forEach { dietProductDetail ->
                totalWeight += dietProductDetail.weight
                totalPercentage += dietProductDetail.percentage
            }
            binding.etSummaryWeight.setText(Helper.getNumberWithDecimals(totalWeight, 0))
            binding.etSummaryPer.setText(Helper.getNumberWithDecimals(totalPercentage, 0))
            adapter.updateTotalWeight()
            adapter.verifyPercentage()

        }


    }

    fun saveDiet(){
        val dietName = binding.tiDietName.text.toString().trim { it <= ' ' }
        val dietDescription = binding.tiDietDescription.text.toString().trim { it <= ' ' }
        val usePercentage = binding.switchPercentage.isChecked
        var remoteId: Long = 0

        currentDiet?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(dietName) -> {
                Toast.makeText(
                    this@AddUpdateDietActivity,
                    resources.getString(R.string.err_msg_diet_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var dietId = 0L
                var updatedDate = ""

                currentDiet?.let {
                    if (it.id != 0L){
                        dietId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    }
                }

                val diet = Diet(
                    dietName,
                    dietDescription,
                    remoteId,
                    updatedDate,
                    "",
                    usePercentage,
                    dietId
                )

                mNewDietDetails = diet

                runBlocking {
                    if (dietId == 0L){
                        //mDietViewModelRemote.addDietFromAPI(diet)
                        //saveNewDiet = true
                        saveProductsInDiet(mDietViewModel.insertSync(diet))
                    } else {
                        // Se actualizan datos base de la dieta
                        mDietViewModel.updateSync(diet)
                        Log.i("SYNC", "Se actualiza dieta con fecha ${diet.updatedDate}")
                        saveProductsInDiet(dietId)
                        //mDietViewModelRemote.updateDietFromAPI(diet)
                    }

                    finish()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentDietId: Long = 0

        binding = ActivityAddUpdateDietBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_DIET_DETAILS)){
            currentDiet = intent.getParcelableExtra(Constants.EXTRA_DIET_DETAILS)
        }

        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_add_update_diet, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_diet -> {
                        if(!isKeyBoardShowing()){
                            saveDiet()
                        }else{
                            hideSoftKeyboard()
                        }
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


        binding.rvDietProductList.layoutManager = GridLayoutManager(this, 1)

        setupActionBar()

        currentDiet?.let {
            if (it.id != 0L){
                currentDietId = it.id
                binding.tiDietName.setText(it.name)
                binding.tiDietDescription.setText(it.description)
                binding.switchPercentage.isChecked = it.usePercentage
                binding.etSummaryWeight.visibility = if (it.usePercentage) View.INVISIBLE else View.VISIBLE
                binding.tiSummaryWeight.visibility = if (it.usePercentage) View.INVISIBLE else View.VISIBLE
            }
        }

        dietProductAdapter =  DietProductAdapter(
            this@AddUpdateDietActivity,
            binding.switchPercentage.isChecked,
            binding.imgVerify,
            binding.etSummaryWeight,
            binding.etSummaryPer
        )
        binding.rvDietProductList.adapter = dietProductAdapter
        itemTouchHelper.attachToRecyclerView(binding.rvDietProductList)

        binding.switchPercentage.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked){
                binding.etSummaryWeight.visibility = View.INVISIBLE
                binding.tiSummaryWeight.visibility = View.INVISIBLE
            } else {
                binding.etSummaryWeight.visibility = View.VISIBLE
                binding.tiSummaryWeight.visibility = View.VISIBLE
            }
            binding.rvDietProductList.forEach {
                val etPer = it.findViewById<EditText>(R.id.et_percentage)
                val etWeight = it.findViewById<EditText>(R.id.et_weight)
                val tiWeight = it.findViewById<TextInputLayout>(R.id.ti_weight)

                if (isChecked){
                    etPer.isEnabled = true
                    etWeight.isEnabled = false
                    etWeight.visibility = View.INVISIBLE
                    tiWeight.visibility = View.INVISIBLE
                } else {
                    etWeight.isEnabled = true
                    etPer.isEnabled = false
                    etWeight.visibility = View.VISIBLE
                    tiWeight.visibility = View.VISIBLE
                }

                (binding.rvDietProductList.adapter as DietProductAdapter).updatePercentageUse(isChecked)

            }
        }

        binding.etSummaryWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, afRoundCorralAdapterter: Int
            ) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val adapter = (binding.rvDietProductList.adapter as DietProductAdapter)
                val newTotal: Double = if (s.toString().toDoubleOrNull() != null){
                    s.toString().toDouble()
                    //adapter.update(s.toString().toDouble())
                } else {
                    //adapter.update(0.0)
                    0.0
                }

                Log.i("FAT", "etSummaryWeight Nuevo total: $newTotal / Cant.: ${binding.rvDietProductList.size} / Cant. Adapter: ${adapter.getItems().size}")

                binding.rvDietProductList.forEach {
                    val etPer = it.findViewById<EditText>(R.id.et_percentage)
                    val etWeight = it.findViewById<EditText>(R.id.et_weight)
                    val etOrder = it.findViewById<TextView>(R.id.tv_product_order)
                    val orderValue = etOrder.text.toString().toInt()
                    Log.i("FAT", "etSummaryWeight orderValue: $orderValue")
                    Log.i("FAT", "etSummaryWeight ---------------------")

                    if (binding.switchPercentage.isChecked){
                        if (etPer.text.toString().toDoubleOrNull() != null){
                            val currentPercentageValue = etPer.text.toString().toDouble()
                            val newWeightValue = Helper.getNumberWithDecimals(currentPercentageValue * newTotal / 100, 2)
                            etWeight.setText(newWeightValue)
                            adapter.updateWeightToItem(orderValue, newWeightValue.toDouble())
                            adapter.verifyPercentage()
                        } else {
                            etWeight.setText("0")
                            adapter.updateWeightToItem(orderValue, 0.0)
                            adapter.verifyPercentage()
                        }
                    } else {

                        if (etWeight.text.toString().toDoubleOrNull() != null){
                            val currentWeightValue = etWeight.text.toString().toDouble()
                            val newPercentageValue = Helper.getNumberWithDecimals((currentWeightValue * 100 / newTotal), 2)
                            Log.i("FAT", "etSummaryWeight currentWeightValue: $currentWeightValue / $newPercentageValue")
                            etPer.setText(newPercentageValue)
                            adapter.updatePercentageToItem(orderValue, newPercentageValue.toDouble())
                            adapter.verifyPercentage()
                        } else {
                            etPer.setText("0.0")
                            adapter.updatePercentageToItem(orderValue, 0.0)
                            adapter.verifyPercentage()
                        }

                        //roundCorrals[holder.adapterPosition].percentage = newValue.toDouble()
                    }

                }


            }
        })

        /*
        mDietViewModel.allDietList.observe(this, { diets ->

            Log.i("FAT", "Flag de guardado $saveNewDiet / ${diets.size}")

            if (saveNewDiet) {
                Log.i("FAT", "saveProductsInDiet 02 ${diets[0].id}")
                saveProductsInDiet(diets[0].id)
                finish()
            }
        })

         */



        getLocalData(currentDietId)
        binding.btnAddProductToDiet.setOnClickListener(this)

        mDietViewModelRemote = ViewModelProvider(this)[DietRemoteViewModel::class.java]
        dietViewModelRemoteObserver()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }


    private fun dietViewModelRemoteObserver(){
        mDietViewModelRemote.addDietsLoad.observe(this) { loadDiet ->
            loadDiet?.let {
                if (loadDiet) {
                    showCustomProgressDialog()
                } else {
                    hideCustomProgressDialog()
                }
            }
        }
        mDietViewModelRemote.addDietsResponse.observe(this) { dietsResponse ->
            dietsResponse?.let { remoteDiet ->
                Log.i("Add diet ID", remoteDiet.id.toString())

                val diet = Diet(
                    remoteDiet.name,
                    remoteDiet.description,
                    remoteDiet.id,
                    "",
                    remoteDiet.archiveDate,
                    remoteDiet.usePercentage,
                )

                Log.i("FAT", "El id es ${diet.id}")
                mDietViewModel.insert(diet)

            }

        }
        mDietViewModelRemote.addDietErrorResponse.observe(this) { errorData ->
            errorData?.let {

                if (errorData) {
                    Log.e("Add diets error", "$errorData")
                    mNewDietDetails?.let {
                        val diet = Diet(
                            it.name,
                            it.description,
                            0,
                            "",
                            "",
                            it.usePercentage
                        )

                        Log.i("FAT [error]", "El id es ${diet.id}")
                        saveNewDiet = true
                        mDietViewModel.insert(diet)
                    }
                }
            }
        }

        mDietViewModelRemote.updateDietsLoad.observe(this) { loadDiet ->
            loadDiet?.let {

                if (loadDiet) {
                    showCustomProgressDialog()
                } else {
                    hideCustomProgressDialog()
                }
            }
        }
        mDietViewModelRemote.updateDietsResponse.observe(this) {
                dietsResponse -> dietsResponse?.let { remoteDiet ->
            Log.i("Updated diet ID", remoteDiet.id.toString())
            finish()
           }
        }
        mDietViewModelRemote.updateDietsErrorResponse.observe(this) { errorData ->
            errorData?.let {

                if (errorData) {
                    Log.e("Add diets error", "$errorData")
                    mNewDietDetails?.let { _diet ->
                        val diet = Diet(
                            _diet.name,
                            _diet.description,
                            _diet.remoteId,
                            Date().toString(),
                            "",
                            _diet.usePercentage,
                            _diet.id
                        )
                        mDietViewModel.update(diet)
                        finish()
                    }
                }
            }
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateDiet)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (currentDiet != null && currentDiet!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_diet)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_diet)
            }
        }

        binding.toolbarAddUpdateDiet.setNavigationOnClickListener {
            hideSoftKeyboard()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(this)
        val dialogBinding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(dialogBinding.root)
        dialogBinding.tvTitle.text = title
        dialogBinding.rvList.layoutManager = LinearLayoutManager(this)

        val currentValues = (binding.rvDietProductList.adapter as DietProductAdapter).getItems()
        val availableValues = ArrayList<CustomListItem>()
        itemsList.forEach { itemList ->
            val alreadyExist = currentValues.firstOrNull{
                it.productId == itemList.id
            }
            if (alreadyExist == null){
                availableValues.add(itemList)
            }
        }

        val adapter = CustomListItemAdapter(this, availableValues, selection)
        dialogBinding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        when (selection){
            Constants.PRODUCT_REF -> {

                val dietProduct = DietProductDetail(
                    item.id,
                    item.remoteId,
                    item.name,
                    item.description,
                    0,
                    0,
                    0.0,
                    0.0,
                    (binding.rvDietProductList.adapter as DietProductAdapter).getItems().size+1)
                //mDietViewModel.insertDietProduct(dietProduct)
                Log.i("DEBUG", "Se ingresa al adapter " +
                        "\n --> Dieta: ${dietProduct.dietId} / Producto: ${dietProduct.productId} " +
                        "  - orden: ${dietProduct.order}" +
                        "  - weight: ${dietProduct.weight}" +
                        "  - percentage: ${dietProduct.percentage}" +
                        "")
                val adapterProduct = (binding.rvDietProductList.adapter as DietProductAdapter)
                adapterProduct.addItem(dietProduct)
                adapterProduct.notifyDataSetChanged()
                adapterProduct.setSelected(dietProduct.productId)
                adapterProduct.updatePercentageUse(binding.switchPercentage.isChecked)

                if(binding.tvNoData.isVisible){
                    binding.rvDietProductList.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    binding.llTotal.visibility = View.VISIBLE
                }

                mCustomListDialog.dismiss()
            }
        }
    }

    private fun saveProductsInDiet(dietId : Long){
        Log.i("FAT", "Se guarda dieta con id $dietId ")
        // Se actualizan productos de las dietas.
        val originalProducts = (dietProductAdapter as DietProductAdapter).getOriginalItems()
        val updatedProducts = (dietProductAdapter as DietProductAdapter).getItems()
        val deletedProducts: ArrayList<DietProductDetail> = ArrayList()

        // Buscamos los productos que se borraron
        originalProducts.forEach { originalProduct ->
            val hasProduct = updatedProducts.any{ updatedProduct ->
                updatedProduct.productId == originalProduct.productId
            }

            if (!hasProduct){
                deletedProducts.add(originalProduct)
            }
        }

        // Se borran los productos que se quitaron de la dieta
        deletedProducts.forEach{ deletedProduct ->
            Log.i("FAT", "Se Borra " +
                    "\n --> Dieta / Producto: ${deletedProduct.dietId} / ${deletedProduct.productId} " +
                    "")
            mDietViewModel.deleteProductBy(deletedProduct.dietId, deletedProduct.productId)
        }

        // Se actualizan los productos que quedaron en la dieta
        updatedProducts.forEach {
            Log.i("FAT", "Se actualiza " +
                    "\n --> Dieta / Producto: $dietId / ${it.productId} " +
                    "\n --> orden: ${it.order}" +
                    "\n --> weight: ${it.weight}" +
                    "\n --> percentage: ${it.percentage}" +
                    "")

            val hasProduct = originalProducts.any{ originalProduct ->
                originalProduct.productId == it.productId
            }

            if (!hasProduct){
                val dietProduct = DietProduct(
                    dietId,
                    it.productId,
                    0,
                    0,
                    it.order,
                    it.weight,
                    it.percentage,
                    0,
                    "",
                    null)
                mDietViewModel.insertDietProduct(dietProduct)
            } else {
                mDietViewModel.updateProductBy(
                    dietId,
                    it.productId,
                    it.order,
                    it.weight,
                    it.percentage
                )
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.btn_add_product_to_diet -> {
                    customItemsLDialog("Productos disponibles", mCustomListItem, Constants.PRODUCT_REF)
                }
            }
        }
    }

    fun toggleKeyboard() {
        if(isKeyBoardShowing()){
            hideSoftKeyboard()
        }else{
            privateShowKeyboard()
        }
    }

    fun Activity.hideSoftKeyboard() {
        val view: View = binding.root
        Log.i("DEBUG","hideKeyboard")
        view.let { _view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(_view.windowToken, 0)
        }
    }

    private fun Activity.privateShowKeyboard() { // Or View.showKeyboard()
        Log.i("DEBUG","showKeyboard $this")
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethod.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun showKeyboard(){
        privateShowKeyboard()
    }

    fun isKeyBoardShowing(): Boolean {
        val view: View = binding.root
        return view.rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())
    }

    fun scrollToSelectedItem(position : Int){
        Log.i("DEBUG","scroll to $position")
        binding.svDiet.scrollY = 225 + 105 * position
    }

    fun focusAndShowKeyboard(view: View) {
        /**
         * This is to be called when the window already has focus.
         */
        fun View.showTheKeyboardNow() {
            if (isFocused) {
                post {
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        view.requestFocus()
        if (hasWindowFocus()) {
            view.showTheKeyboardNow()
        } else {
            view.viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        if (hasFocus) {
                            view.showTheKeyboardNow()
                            view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
        }
    }
}