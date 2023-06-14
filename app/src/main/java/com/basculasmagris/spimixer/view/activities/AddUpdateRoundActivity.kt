package com.basculasmagris.visorremotomixer.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateRoundBinding
import com.basculasmagris.visorremotomixer.databinding.DialogCustomListBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItemAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundCorralAdapter
import com.basculasmagris.visorremotomixer.viewmodel.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddUpdateRoundActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateRoundBinding
    private lateinit var mCustomListDialog : Dialog

    private var currentRound: Round? = null
    private var positionFrom = -1
    private var positionTo = -1
    private var selectedDiet: CustomListItem? = null

    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((application as SpiMixerApplication).roundRepository)
    }

//    private lateinit var mRoundViewModelRemote: RoundRemoteViewModel
//    private var mProgressDialog: Dialog? = null

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((this.application as SpiMixerApplication).corralRepository)
    }

    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((this.application as SpiMixerApplication).dietRepository)
    }

    private var mLocalRoundCorralDetail: List<RoundCorralDetail>? = null
    private var mLocalCorral: List<Corral>? = null
    private var mLocalDiet: List<Diet>? = null
    private val mCustomListItem: ArrayList<CustomListItem> = ArrayList()
    private val mCustomDietListItem: ArrayList<CustomListItem> = ArrayList()
    private var roundCorralAdapter: RoundCorralAdapter? = null
    private var mRound : List<Round>? = null
    // Drop and drag

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                Log.i("FAT", "onMove")
                //getting the adapter
                val adapter = recyclerView.adapter as RoundCorralAdapter

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
                val adapter = (binding.rvRoundCorralList.adapter as RoundCorralAdapter)
                if (adapter.getItems().size == 1) {
                    binding.rvRoundCorralList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.llTotal.visibility = View.GONE
                }
                roundCorralAdapter?.onItemSwiped(viewHolder.layoutPosition)
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

                val adapter = recyclerView.adapter as RoundCorralAdapter

                if (positionFrom != -1 || positionTo != -1){
                    if (positionFrom < positionTo){

                        for (index in positionFrom..positionTo){
                            val roundCorralDetailFrom = adapter.getItemFrom(position = index)
                            var newOrder = index
                            if (index == positionFrom){
                                newOrder = positionTo+1
                            }
                            roundCorralDetailFrom.order = newOrder

                            Log.i("FAT", "Se actualiza ${roundCorralDetailFrom.corralName} con " +
                                    "\n --> orden: $newOrder" +
                                    "\n --> weight: ${roundCorralDetailFrom.weight}" +
                                    "\n --> percentage: ${roundCorralDetailFrom.percentage}" +
                                    "")
                        }
                    } else {
                        Log.i("FAT", "clearView $positionFrom to $positionTo")
                        for (index in positionFrom downTo positionTo){

                            val roundCorralDetailFrom = adapter.getItemFrom(position = index)
                            Log.i("FAT", "index $index")
                            var newOrder = roundCorralDetailFrom.order + 1
                            if (index == positionFrom){
                                newOrder = positionTo+1
                            }

                            roundCorralDetailFrom.order = newOrder

                            Log.i("FAT", "Se actualiza ${roundCorralDetailFrom.corralName} con " +
                                    "\n --> orden: $newOrder / ${adapter.getItemFrom(position = index).order}" +
                                    "\n --> weight: ${roundCorralDetailFrom.weight}" +
                                    "\n --> percentage: ${roundCorralDetailFrom.percentage}" +
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

    private fun fetchLocalData(roundId: Long): MediatorLiveData<MergedLocalData> {
        Log.i("FAT", "Se busca para ID $roundId")
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mRoundViewModel.getCorralsBy(roundId)) {
            if (it != null) {
                liveDataMerger.value = RoundCorralDetailData(it)
            }
        }
        liveDataMerger.addSource(mCorralViewModel.allCorralList) {
            if (it != null) {
                liveDataMerger.value = CorralData(it)
            }
        }
        liveDataMerger.addSource(mDietViewModel.activeDietList) {
            if (it != null) {
                liveDataMerger.value = DietData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(roundId: Long){
        // Sync local data
        val liveData = fetchLocalData(roundId)
        liveData.observe(this, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is RoundCorralDetailData -> mLocalRoundCorralDetail = it.roundCorralDetail
                    is CorralData -> mLocalCorral = it.corrals
                    is DietData -> mLocalDiet = it.diets
                    is RoundData -> mRound = it.rounds
                    else -> {}
                }

                if (mLocalRoundCorralDetail != null && mLocalCorral != null && mLocalDiet != null) {

                    mLocalCorral?.forEach {
                        if (it.archiveDate.isNullOrEmpty()){
                            val item = CustomListItem(it.id, it.remoteId, it.name, it.description, R.drawable.ic_corral)
                            mCustomListItem.add(item)
                        }
                    }

                    var firstDiet: CustomListItem? = null
                    mLocalDiet?.forEach {

                        Log.i("DIET", "Name: ${it.name} | ${it.id} | ${it.archiveDate}")
                        if (it.archiveDate.isNullOrEmpty()){
                            val item = CustomListItem(it.id, it.remoteId, it.name, it.description, R.drawable.ic_local_dining)
                            if (firstDiet == null){
                                firstDiet = item
                            }

                            Log.i("DIET", "Added: Name: ${it.name} | ${it.id} | ${it.archiveDate}")
                            mCustomDietListItem.add(item)

                            if (it.id == currentRound?.dietId){
                                binding.etRoundDiet.setText(it.name)
                                selectedDiet = item
                            }
                        }
                    }

                    Log.i("FAT", "Diet ${firstDiet?.name}")
                    if (selectedDiet == null){
                        selectedDiet = firstDiet
                        binding.etRoundDiet.setText(firstDiet?.name)
                    }

                    setRoundCorrals()
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    fun setRoundCorrals(){
        Log.i("FAT", "Hay ${mLocalRoundCorralDetail?.size} corrales asociados")
        mLocalRoundCorralDetail?.let {
            if (it.isNotEmpty()){
                binding.rvRoundCorralList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.llTotal.visibility = View.VISIBLE
                (roundCorralAdapter as RoundCorralAdapter).roundList(it)
            } else {
                binding.rvRoundCorralList.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
                binding.llTotal.visibility = View.GONE
            }
        }

    }

    fun saveRound(){
        val roundName = binding.tiRoundName.text.toString().trim { it <= ' ' }
        val roundDescription = binding.tiRoundDescription.text.toString().trim { it <= ' ' }
        val usePercentage = binding.switchPercentage.isChecked
        val customPercentage = binding.etCustomPercentage.text.toString().trim { it <= ' ' }
        val weight = binding.etTotalWeight.text.toString().trim { it <= ' ' }
        var dietId = 0L
        var remoteDietId = 0L

        selectedDiet?.let { customListItem ->
            dietId = customListItem.id
            remoteDietId = customListItem.remoteId
        }
        if(dietId <= 0){
            Toast.makeText(
                this@AddUpdateRoundActivity,
                resources.getString(R.string.debe_seleccionar_dieta),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        var remoteId = 0L

        currentRound?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(roundName) -> {
                Toast.makeText(
                    this@AddUpdateRoundActivity,
                    resources.getString(R.string.err_msg_round_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var roundId = 0L
                var updatedDate = ""

                currentRound?.let {
                    if (it.id != 0L){
                        roundId = it.id
                        updatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    }
                }

                Log.i("SYNC", "Current round --> id:${currentRound?.id} | $roundId")

                val round = Round(
                    roundName,
                    roundDescription,
                    remoteId,
                    updatedDate,
                    "",
                     if (weight.isEmpty()) 0.0 else weight.toDouble(),
                    usePercentage,
                    if (customPercentage.isEmpty()) 0.0 else customPercentage.toDouble(),
                    dietId,
                    remoteDietId,
                    roundId
                )

                runBlocking {
                    if (roundId == 0L){
                        val roundIdOfInsert = mRoundViewModel.insertSync(round)
                        saveCorralsInRound(roundIdOfInsert)
                    } else {
                        // Se actualizan datos base de la rounda
                        mRoundViewModel.updateSync(round)
                        Log.i("SYNC", "Se actualiza ronda con fecha ${round.updatedDate}")
                        saveCorralsInRound(roundId)
                    }
                    finish()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentRoundId = 0L

        binding = ActivityAddUpdateRoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_ROUND_DETAILS)){
            currentRound = intent.getParcelableExtra(Constants.EXTRA_ROUND_DETAILS)
        }

        if (intent.hasExtra(Constants.EXTRA_ROUND_RUN_DETAILS)){
            val currentRoundRunDetail : RoundRunDetail? = intent.getParcelableExtra(Constants.EXTRA_ROUND_RUN_DETAILS)
            currentRoundRunDetail.let {roundRunDetail->
                val round = mRound?.firstOrNull{_round ->
                    _round.id == roundRunDetail!!.id
                }
                round.let { _round->
                    currentRound = _round
                }
            }
        }

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
                        if(!isKeyBoardShowing()){
                            saveRound()
                        }else{
                            hideSoftKeyboard()
                        }
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
        binding.rvRoundCorralList.layoutManager = GridLayoutManager(this, 1)

        setupActionBar()
        binding.etTotalWeight.setText("0.0", TextView.BufferType.EDITABLE)
        binding.etCustomPercentage.setText("100", TextView.BufferType.EDITABLE)

        currentRound?.let {
            if (it.id != 0L){
                currentRoundId = it.id
                binding.tiRoundName.setText(it.name)
                binding.tiRoundDescription.setText(it.description)
                binding.switchPercentage.isChecked = it.usePercentage
                binding.etTotalWeight.setText(it.weight.toLong().toString(), TextView.BufferType.EDITABLE)
                binding.etSummaryWeight.setText(Helper.getNumberWithDecimals(it.weight, 0), TextView.BufferType.EDITABLE)
                binding.etCustomPercentage.setText(it.customPercentage.toString(), TextView.BufferType.EDITABLE)
            }
        }

        val totalWeight = if (binding.etTotalWeight.text.toString().isEmpty()) 0.0 else binding.etTotalWeight.text.toString().toDouble()
        roundCorralAdapter =  RoundCorralAdapter(
            this@AddUpdateRoundActivity,
            binding.switchPercentage.isChecked,
            totalWeight,
            binding.imgVerify,
            binding.etSummaryPer,
            binding.etSummaryWeight,
            binding.etTotalWeight
        )

        binding.rvRoundCorralList.adapter = roundCorralAdapter
        itemTouchHelper.attachToRecyclerView(binding.rvRoundCorralList)
        binding.etTotalWeight.isEnabled = binding.switchPercentage.isChecked
        binding.switchPercentage.setOnCheckedChangeListener { _, isChecked ->

            binding.etTotalWeight.isEnabled = isChecked

            binding.rvRoundCorralList.forEach {
                val etPer = it.findViewById<EditText>(R.id.et_percentage)
                val etWeight = it.findViewById<EditText>(R.id.et_weight_corral)
                val tiWeight = it.findViewById<TextInputLayout>(R.id.ti_weight_corral)
                val tiQuantity = it.findViewById<TextInputLayout>(R.id.ti_animal_quantity)
                val etQuantity = it.findViewById<EditText>(R.id.et_animal_quantity)
                val tiFoodByAnimals = it.findViewById<TextInputLayout>(R.id.ti_food_by_animals)
                val etFoodByAnimals = it.findViewById<EditText>(R.id.et_food_by_animals)

                if (isChecked){
                    etPer.isEnabled = true
                    etWeight.isEnabled = false
                    etWeight.visibility = View.INVISIBLE
                    tiWeight.visibility = View.INVISIBLE
                    tiQuantity.visibility = View.INVISIBLE
                    etQuantity.visibility = View.INVISIBLE
                    tiFoodByAnimals.visibility = View.INVISIBLE
                    etFoodByAnimals.visibility = View.INVISIBLE
                } else {
                    etPer.isEnabled = false
                    etWeight.isEnabled = true
                    etWeight.visibility = View.VISIBLE
                    tiWeight.visibility = View.VISIBLE
                    tiQuantity.visibility = View.VISIBLE
                    etQuantity.visibility = View.VISIBLE
                    tiFoodByAnimals.visibility = View.VISIBLE
                    etFoodByAnimals.visibility = View.VISIBLE
                }

               (binding.rvRoundCorralList.adapter as RoundCorralAdapter).updatePercentageUse(isChecked)

            }
        }


        binding.etTotalWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var newTotal = 0.0
                val adapter = (binding.rvRoundCorralList.adapter as RoundCorralAdapter)
                if (s.toString().toDoubleOrNull() != null){
                    adapter.update(s.toString().toDouble())
                    newTotal = s.toString().toDouble()
                    binding.etSummaryWeight.setText(Helper.getNumberWithDecimals(newTotal, 0))
                } else {
                    adapter.update(0.0)
                    binding.etSummaryWeight.setText("0")
                }

                binding.rvRoundCorralList.forEach {
                    val etPer = it.findViewById<EditText>(R.id.et_percentage)
                    val etWeight = it.findViewById<EditText>(R.id.et_weight_corral)
                    val etOrder = it.findViewById<TextView>(R.id.tv_corral_order)
                    val orderValue = etOrder.text.toString().toInt()

                    if (binding.switchPercentage.isChecked){
                        if (etPer.text.toString().toDoubleOrNull() != null){
                            val currentPercentageValue = etPer.text.toString().toDouble()
                            val newWeightValue = Helper.getNumberWithDecimals(currentPercentageValue * newTotal / 100, 0)
                            etWeight.setText(newWeightValue)
                            adapter.updateWeightToItem(orderValue, newWeightValue.toDouble())
                            adapter.verifyPercentageAndWeight()
                        } else {
                            etWeight.setText("0")
                            adapter.updateWeightToItem(orderValue, 0.0)
                            adapter.verifyPercentageAndWeight()
                        }
                    } else {
                        if (etWeight.text.toString().toDoubleOrNull() != null && newTotal != 0.0){
                            val currentWeightValue = etWeight.text.toString().toDouble()
                            val newPercentageValue = Helper.getNumberWithDecimals((currentWeightValue * 100 / newTotal), 2)
                            etPer.setText(newPercentageValue)
                            adapter.updatePercentageToItem(orderValue, newPercentageValue.toDouble())
                        } else {
                            etPer.setText("0.0")
                            adapter.updatePercentageToItem(orderValue, 0.0)
                        }

                        //roundCorrals[holder.adapterPosition].percentage = newValue.toDouble()
                    }

                }

                //(binding.rvRoundCorralList.adapter as RoundCorralAdapter).verifyPercentageAndWeight()
                //(binding.rvRoundCorralList.adapter as RoundCorralAdapter).updateTotal()
            }
        })
        binding.etCustomPercentage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().toDoubleOrNull() != null){
                    (binding.rvRoundCorralList.adapter as RoundCorralAdapter).updateCustomPercentage()

                } else {
                   // (binding.rvRoundCorralList.adapter as RoundCorralAdapter).updateCustomPercentage(0.0)
                }


            }
        })

        getLocalData(currentRoundId)
        binding.btnAddCorralToRound.setOnClickListener(this)
        binding.etRoundDiet.setOnClickListener(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateRound)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (currentRound != null && currentRound!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_round)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_round)
            }
        }

        binding.toolbarAddUpdateRound.setNavigationOnClickListener {
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

        var corralsCurrentValues : List<RoundCorralDetail>  = ArrayList()
        when (selection){
            Constants.CORRAL_REF -> {
                corralsCurrentValues = (binding.rvRoundCorralList.adapter as RoundCorralAdapter).getItems().toMutableList()
            }
            Constants.DIET_REF -> {
            }
        }

        val avaiableValues = ArrayList<CustomListItem>()
        itemsList.forEach { itemList ->

            when (selection){
                Constants.CORRAL_REF -> {
                    val alreadyExist = corralsCurrentValues.firstOrNull{
                        it.corralId == itemList.id
                    }
                    if (alreadyExist == null){
                        avaiableValues.add(itemList)
                    }                }
                Constants.DIET_REF -> {
                    avaiableValues.add(itemList)
                }
            }
        }
        val adapter = CustomListItemAdapter(this, avaiableValues, selection)
        dialogBinding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        Log.i("DEBUG", "Se seleccionó ${item.name} - ${item.id} / [$selection]  | mLocalCorral.size ${mLocalCorral?.size}")
        when (selection){
            Constants.CORRAL_REF -> {
                val corralDetail = mLocalCorral?.firstOrNull {
                    it.id == item.id
                }
                if(corralDetail!=null){
                    Log.i("DEBUG","corralDetail $corralDetail")
                }

                val roundCorral = RoundCorralDetail(
                    item.id,
                    0,
                    item.name,
                    item.description,
                    0,
                    0,
                    0.0,
                    0.0,
                    (binding.rvRoundCorralList.adapter as RoundCorralAdapter).getItems().size+1,
                corralDetail?.animalQuantity  ?: 0)

                val adapterCorral = (binding.rvRoundCorralList.adapter as RoundCorralAdapter)
                adapterCorral.addItem(roundCorral)
                adapterCorral.notifyDataSetChanged()
                adapterCorral.setSelected(roundCorral.corralId)


                (binding.rvRoundCorralList.adapter as RoundCorralAdapter).verifyPercentageAndWeight()
                binding.rvRoundCorralList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.llTotal.visibility = View.VISIBLE
                mCustomListDialog.dismiss()
            }
            Constants.DIET_REF -> {
                selectedDiet = item
                binding.etRoundDiet.setText(item.name)
                mCustomListDialog.dismiss()
            }
        }
    }

    private fun saveCorralsInRound(roundId : Long){
        Log.i("FAT", "Se guarda rounda con id $roundId ")
        // Se actualizan corralos de las roundas.
        val originalCorrals = (roundCorralAdapter as RoundCorralAdapter).getOriginalItems()
        val updatedCorrals = (roundCorralAdapter as RoundCorralAdapter).getItems()
        val deletedCorrals: ArrayList<RoundCorralDetail> = ArrayList()

        // Buscamos los corralos que se borraron
        originalCorrals.forEach { originalCorral ->
            val hasCorral = updatedCorrals.any{ updatedCorral ->
                updatedCorral.corralId == originalCorral.corralId
            }

            if (!hasCorral){
                deletedCorrals.add(originalCorral)
            }
        }

        // Se borran los corralos que se quitaron de la rounda
        deletedCorrals.forEach{ deletedCorral ->
            Log.i("FAT", "Se Borra " +
                    "\n --> Rounda / Corralo: ${deletedCorral.roundId} / ${deletedCorral.corralId} " +
                    "")
            mRoundViewModel.deleteCorralBy(deletedCorral.roundId, deletedCorral.corralId)
        }

        // Se actualizan los corralos que quedaron en la rounda
        updatedCorrals.forEach {
            Log.i("FAT", "Se actualiza " +
                    "\n --> Rounda / Corralo: $roundId / ${it.corralId} " +
                    "\n --> orden: ${it.order}" +
                    "\n --> weight: ${it.weight}" +
                    "\n --> percentage: ${it.percentage}" +
                    "")

            val hasCorral = originalCorrals.any{ originalCorral ->
                originalCorral.corralId == it.corralId
            }

            if (!hasCorral){
                val roundCorral = RoundCorral(
                    roundId,
                    it.corralId,
                    it.remoteRoundId,
                    it.remoteCorralId,
                    it.order,
                    it.weight,
                    it.percentage,
                    0,
                    "",
                    null)
                mRoundViewModel.insertRoundCorral(roundCorral)
            } else {
                mRoundViewModel.updateCorralBy(
                    roundId,
                    it.corralId,
                    it.order,
                    it.weight,
                    it.percentage
                )
            }

            mCorralViewModel.updateCorralAnimals(it.corralId.toInt(),it.animalQuantity)

        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.btn_add_corral_to_round -> {
                    customItemsLDialog("Corrales disponibles", mCustomListItem, Constants.CORRAL_REF)
                }
                R.id.et_round_diet -> {
                    customItemsLDialog("Dietas disponibles", mCustomDietListItem, Constants.DIET_REF)
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

    private fun Activity.privateShowKeyboard() { // Or View.showKeyboard()
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
        binding.svRound.scrollY = 235 + 105 * position
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