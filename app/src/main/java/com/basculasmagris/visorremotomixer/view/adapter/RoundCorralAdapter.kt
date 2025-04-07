package com.basculasmagris.visorremotomixer.view.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundCorralBinding
import com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper
import kotlin.math.roundToInt


class RoundCorralAdapter (
    private  val activity: Activity,
    private var usePercentage: Boolean,
    private var totalWeight: Double,
    private var imgVerify: ImageView?,
    private var summaryPercentage: EditText?,
    private var summaryWeight: EditText?,
    private var tvTotalWeight: EditText?,
    private var readOnly: Boolean = false
) : RecyclerView.Adapter<RoundCorralAdapter.ViewHolder>(),
    Filterable {

    private var originalCorrals: MutableList<RoundCorralDetail> = ArrayList()
    private var roundCorrals: MutableList<RoundCorralDetail> = ArrayList()
    private var filteredRoundCorrals: MutableList<RoundCorralDetail> = ArrayList()
    private var corralId: Long = 0
    private var setFocus: Boolean = false
    private var changedAnimalQuantity : Boolean = false
    private var changedFoodByAnimal : Boolean = false
    private var changedWeight : Boolean = false

    class ViewHolder (view: ItemLineRoundCorralBinding) : RecyclerView.ViewHolder(view.root){
        val tvCorralName = view.tvCorralName
        val tvCorralDescription = view.tvCorralDescription
        val etPercentage = view.etPercentage
        val etWeightCorral = view.etWeightCorral
        val tvOrder = view.tvCorralOrder
        val tvCorralAnimalCount = view.tvCorralAnimalCount
        val tiWeight = view.tiWeightCorral
        val etAnimalQuantity = view.etAnimalQuantity
        val tiAnimalQuantity = view.tiAnimalQuantity
        val etFoodByAnimal = view.etFoodByAnimals
        val tiFoodByAnimal = view.tiFoodByAnimals
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundCorralBinding = ItemLineRoundCorralBinding.inflate(
            LayoutInflater.from(activity), parent, false)
        binding.etWeightCorral.transformationMethod = null
        if(usePercentage){
            binding.etWeightCorral.requestFocus()
        }else{
            binding.etPercentage.requestFocus()
        }
        return ViewHolder(binding)
    }

//    fun calcPercentage(){
//        var totalWeight = 0.0
//        roundCorrals.forEach { roundCorralDetail ->
//            if (roundCorralDetail.weight >= 0){
//                totalWeight += roundCorralDetail.weight
//            }
//        }
//
//        roundCorrals.forEach { roundCorralDetail ->
//
//            if (roundCorralDetail.weight >= 0 && totalWeight > 0){
//                roundCorralDetail.percentage = round((roundCorralDetail.weight * 100 / totalWeight))
//            } else {
//                roundCorralDetail.percentage = 0.0
//            }
//        }
//    }


    fun verifyPercentageAndWeight() {
        var currentTotalPercentage = 0.0
        var currentTotalWeight = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual del orden ${roundCorralDetail.order} ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.percentage >= 0){
                currentTotalPercentage += roundCorralDetail.percentage
            }
            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
        }

        Log.i("FAT", "PORCENTAJE: Valor actual $currentTotalPercentage / 100")
        Log.i("FAT", "PESO: Valor actual $currentTotalWeight / $totalWeight")
        if (currentTotalPercentage.roundToInt() != 100 || currentTotalWeight.roundToInt() != totalWeight.roundToInt()){
            imgVerify?.setColorFilter(ContextCompat.getColor(activity, R.color.red_500_light), android.graphics.PorterDuff.Mode.SRC_IN)

        } else {
            imgVerify?.setColorFilter(ContextCompat.getColor(activity, R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

     fun updateTotalPercentage(){
         var currentTotalPercentage = 0.0
         roundCorrals.forEach { roundCorralDetail ->
             Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

             if (roundCorralDetail.percentage >= 0){
                 currentTotalPercentage += roundCorralDetail.percentage
             }

         }
         summaryPercentage?.setText(currentTotalPercentage.roundToInt().toString())
     }

    fun updateTotalWeight(){
        var currentTotalWeight = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
        }
        summaryWeight?.setText(currentTotalWeight.toString())
        tvTotalWeight?.setText(currentTotalWeight.toString())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundCorral = roundCorrals[position]
        holder.etPercentage.setText(roundCorral.percentage.toString())
        holder.etWeightCorral.setText(roundCorral.weight.toLong().toString())
        holder.tvCorralName.text = roundCorral.corralName
        holder.tvOrder.text = roundCorral.order.toString()
        holder.etWeightCorral.isEnabled = !usePercentage and !readOnly
        holder.tvCorralAnimalCount.text = roundCorral.animalQuantity.toString()
        holder.tvCorralDescription.text = roundCorral.corralDescription.ifEmpty { activity.getString(R.string.sin_descripcion) }
        holder.etAnimalQuantity.setText(roundCorral.animalQuantity.toString())
        if(roundCorral.animalQuantity!=0){
            val foodByAnimal = roundCorral.weight/roundCorral.animalQuantity
            holder.etFoodByAnimal.setText(Helper.getNumberWithDecimals(foodByAnimal,2))
        }else{
            holder.etFoodByAnimal.setText("0.0")
        }

        if (usePercentage) {
            holder.tiWeight.visibility = View.INVISIBLE
            holder.etWeightCorral.visibility  = View.INVISIBLE
            holder.etAnimalQuantity.visibility = View.INVISIBLE
            holder.tiAnimalQuantity.visibility = View.INVISIBLE
            holder.etFoodByAnimal.visibility = View.INVISIBLE
            holder.tiFoodByAnimal.visibility = View.INVISIBLE
        } else {
            holder.tiWeight.visibility = View.VISIBLE
            holder.etWeightCorral.visibility  = View.VISIBLE
            holder.etAnimalQuantity.visibility = View.VISIBLE
            holder.tiAnimalQuantity.visibility = View.VISIBLE
            holder.etFoodByAnimal.visibility = View.VISIBLE
            holder.tiFoodByAnimal.visibility = View.VISIBLE
        }

        holder.etAnimalQuantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.toString().toIntOrNull() != null && !usePercentage){
                    roundCorrals[holder.adapterPosition].animalQuantity = s.toString().toInt()
                    if (s.toString().toInt() == 0) {
                        return
                    }

                    if(holder.etFoodByAnimal.text.toString().toDoubleOrNull() == null || holder.etFoodByAnimal.text.toString().toDouble() == 0.0){
                        return
                    }
                    changedAnimalQuantity = true
                    val foodByAnimal = holder.etFoodByAnimal.text.toString().toDouble()
                    roundCorrals[holder.adapterPosition].weight = roundCorrals[holder.adapterPosition].animalQuantity * foodByAnimal

                    holder.etWeightCorral.setText(Helper.getNumberWithDecimals(roundCorrals[holder.adapterPosition].weight,0))

                    Log.i("FAT", "Total $totalWeight")
                    val newValue: String =
                        (s.toString().toDouble()  * 100 / totalWeight).roundToInt().toString()
                    Log.i("FAT", "Total $totalWeight. El nuevo valor en % es $newValue")
                    updateTotalWeight()
                    updateTotalPercentage()
                    verifyPercentageAndWeight()
                    changedAnimalQuantity = false
                }

            }
        })

        holder.etFoodByAnimal.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(changedAnimalQuantity || changedWeight){
                    return
                }
                if (s.toString().toDoubleOrNull() != null && !usePercentage){
                    if (s.toString().toDouble() == 0.0 || roundCorrals[holder.adapterPosition].animalQuantity == 0) {
                        return
                    }
                    changedFoodByAnimal =  true
                    val foodByAnimal = s.toString().toDouble()
                    roundCorrals[holder.adapterPosition].weight = roundCorrals[holder.adapterPosition].animalQuantity * foodByAnimal
                    holder.etWeightCorral.setText(Helper.getNumberWithDecimals(roundCorrals[holder.adapterPosition].weight,0))
                    Log.i("FAT", "Total $totalWeight")
                    val newValue: String =
                        (s.toString().toDouble()  * 100 / totalWeight).roundToInt().toString()
                    Log.i("FAT", "Total $totalWeight. El nuevo valor en % es $newValue")
                    updateTotalWeight()
                    updateTotalPercentage()
                    verifyPercentageAndWeight()
                    changedFoodByAnimal =  false
                }

            }
        })


        holder.etWeightCorral.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(changedAnimalQuantity || changedFoodByAnimal){
                    return
                }

                if (s.toString().toDoubleOrNull() != null && !usePercentage){
                    changedWeight = true
                    roundCorrals[holder.adapterPosition].weight = s.toString().toDouble()
                    if(roundCorrals[holder.adapterPosition].animalQuantity != 0){
                        holder.etFoodByAnimal.setText(Helper.getNumberWithDecimals(roundCorrals[holder.adapterPosition].weight/roundCorrals[holder.adapterPosition].animalQuantity,2))
                    }
                    if (totalWeight == 0.0 && s.toString().toDouble() == 0.0) {
                        changedWeight = false
                        return
                    }

                    Log.i("FAT", "Total $totalWeight")
                    val newValue: String =
                        (s.toString().toDouble()  * 100 / totalWeight).roundToInt().toString()
                    Log.i("FAT", "Total $totalWeight. El nuevo valor en % es $newValue")
                    //holder.etPercentage.setText(newValue, TextView.BufferType.EDITABLE);
                    //roundCorrals[holder.adapterPosition].percentage = newValue.toDouble()
                    updateTotalWeight()
                    updateTotalPercentage()

                } else if (s.toString().toDoubleOrNull() == null) {

                    if (!usePercentage){
                        roundCorrals[holder.adapterPosition].weight = 0.0
                        //holder.etPercentage.setText("0.0", TextView.BufferType.EDITABLE);
                        //roundCorrals[holder.adapterPosition].percentage = 0.0
                        updateTotalWeight()
                        updateTotalPercentage()
                    }
                }
                verifyPercentageAndWeight()
                changedWeight = false
            }
        })

        holder.etPercentage.isEnabled = usePercentage and !readOnly
        holder.etPercentage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().toDoubleOrNull() != null && usePercentage){
                    roundCorrals[holder.adapterPosition].percentage = s.toString().toDouble()
                    val newValue: String =
                        Helper.getNumberWithDecimals(totalWeight * s.toString().toDouble() / 100, 0)
                    holder.etWeightCorral.setText(newValue, TextView.BufferType.EDITABLE)
                    Log.i("FAT", "Total $totalWeight. El nuevo valor es de peso es $newValue")
                    roundCorrals[holder.adapterPosition].weight = newValue.toDouble()
                    updateTotalPercentage()
                    //updateTotalWeight()
                } else if (s.toString().toDoubleOrNull() == null) {

                    if (usePercentage){
                        roundCorrals[holder.adapterPosition].percentage = 0.0
                        holder.etWeightCorral.setText("0", TextView.BufferType.EDITABLE)
                        roundCorrals[holder.adapterPosition].weight = 0.0
                        updateTotalPercentage()
                        //updateTotalWeight()
                    }
                }

                verifyPercentageAndWeight()
                //updateTotal()
            }
        })

//        holder.fbAdjust.setOnClickListener{
//            var currentTotalPercentage = 0.0
//            var currentTotalWeight = 0.0
//            roundCorrals.forEach { roundCorralDetail ->
//                if (roundCorralDetail.percentage >= 0){
//                    currentTotalPercentage += roundCorralDetail.percentage
//                }
//                if (roundCorralDetail.weight >= 0){
//                    currentTotalWeight += roundCorralDetail.weight
//                }
//            }
//
//            Log.i("FAT", "Total Por: $currentTotalPercentage / Peso: $currentTotalWeight")
//            if (usePercentage){
//                val currentValue = holder.etPercentage.text.toString().toDouble()
//                when {
//                    currentTotalPercentage > 100.0 -> {
//                        val diffValue = currentTotalPercentage-100.0
//                        val newValue = currentValue-diffValue
//                        holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE)
//                        roundCorrals[holder.adapterPosition].percentage = newValue
//                    }
//                    currentTotalPercentage < 100.0 -> {
//                        val diffValue = 100-currentTotalPercentage
//                        val newValue = currentValue+diffValue
//                        holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE)
//                        roundCorrals[holder.adapterPosition].percentage = newValue
//                    }
//                    else -> {
//                        holder.etPercentage.setText(currentValue.toString(), TextView.BufferType.EDITABLE)
//                        roundCorrals[holder.adapterPosition].percentage = currentValue
//                    }
//                }
//            } else {
//                val currentValue = holder.etWeight.text.toString().toDouble()
//                when {
//                    currentTotalWeight > totalWeight -> {
//                        val diffValue = currentTotalWeight-totalWeight
//                        var newValue = currentValue-diffValue
//                        newValue = if (newValue < 0) 0.0 else newValue
//                        holder.etWeight.setText(newValue.toLong().toString(), TextView.BufferType.EDITABLE)
//                        roundCorrals[holder.adapterPosition].weight = newValue
//                    }
//                    currentTotalWeight < totalWeight -> {
//                        val diffValue = totalWeight-currentTotalWeight
//                        var newValue = currentValue+diffValue
//                        newValue = if (newValue < 0) 0.0 else newValue
//                        holder.etWeight.setText(newValue.toLong().toString(), TextView.BufferType.EDITABLE)
//                        roundCorrals[holder.adapterPosition].weight = newValue
//                    }
//                    else -> {
//                        holder.etWeight.setText(currentValue.toString(), TextView.BufferType.EDITABLE)
//                        roundCorrals[holder.adapterPosition].weight = currentValue
//                    }
//                }
//
//                /*
//                if (currentTotalPercentage > 100.0) {
//                    val diffValue = currentTotalPercentage-100.0
//                    val newValue = currentValue-diffValue
//                    holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE);
//                    roundCorrals[holder.adapterPosition].percentage = newValue
//                } else if (currentTotalPercentage < 100.0) {
//                    val diffValue = 100-currentTotalPercentage
//                    val newValue = currentValue+diffValue
//                    holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE);
//                    roundCorrals[holder.adapterPosition].percentage = newValue
//                } */
//            }
//        }


        verifyPercentageAndWeight()
        updateTotalPercentage()
        //updateTotalWeight()
    }

    override fun getItemCount(): Int {
        return roundCorrals.size
    }

    fun roundList(list: List<RoundCorralDetail>){
        roundCorrals.forEach {
            Log.i("FAT", "roundList ${it.corralName} ${it.weight} ${it.percentage}")
        }
        roundCorrals = list.toMutableList()
        filteredRoundCorrals = list.toMutableList()
        originalCorrals = list.toMutableList()
        notifyDataSetChanged()
    }

    fun updateList(list: List<RoundCorralDetail>){
        roundCorrals.forEach {
            Log.i("FAT", "updateList ${it.corralName} ${it.weight} ${it.percentage}")
        }
        roundCorrals = list.toMutableList()
        filteredRoundCorrals = list.toMutableList()
        notifyDataSetChanged()
    }

    fun update(_totalWeight: Double){
        totalWeight = _totalWeight
    }

    fun updatePercentageToItem(order: Int, percentage: Double){
        val itemToUpdate = roundCorrals.first {
            it.order == order
        }

        itemToUpdate.percentage = percentage
    }

    fun updateWeightToItem(order: Int, weight: Double){
        val itemToUpdate = roundCorrals.first {
            it.order == order
        }

        Log.i("FAT", "Se actualiza el orden $order con valor $weight")
        itemToUpdate.weight = weight
    }

    fun updatePercentageUse(with: Boolean){
        usePercentage = with
    }

    fun updateCustomPercentage() {
        //customPercentage = with
    }

    /*
    fun reOrderItems(): List<RoundCorralDetail>{
        var order = 1
        roundCorrals.forEach { roundCorralDetail ->
            roundCorralDetail.order = order
            order++
        }
        filteredRoundCorrals = roundCorrals
        notifyDataSetChanged()
        return roundCorrals
    }
    */


    fun getItems(): List<RoundCorralDetail> {
        return roundCorrals.sortedBy {
            it.order
        }
    }

    fun getOriginalItems(): List<RoundCorralDetail> {
        return originalCorrals.sortedBy {
            it.order
        }
    }

    fun addItem(roundCorralDetail: RoundCorralDetail) {
        roundCorrals.add(roundCorralDetail)
        roundCorrals.forEach {
            Log.i("FAT", "ADD ${it.corralName} ${it.weight} ${it.percentage}")
        }
        notifyDataSetChanged()
    }

    fun getItemFrom(position: Int): RoundCorralDetail {
        return roundCorrals[position]
    }

    /*
    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
*/

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredRoundCorrals = if (charString.isEmpty()) roundCorrals else {
                    val filteredList = ArrayList<RoundCorralDetail>()
                    roundCorrals
                        .filter {
                            (it.corralName.lowercase().contains(charString.lowercase()))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = roundCorrals }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                roundCorrals = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<RoundCorralDetail>
                notifyDataSetChanged()
            }
        }
    }

    fun onItemSwiped(position: Int) {
        Log.i("FAT", "onItemSwiped $position size ${roundCorrals.size}")
        roundCorrals.removeAt(position)
        //filteredRoundCorrals.removeAt(position)
        Log.i("FAT", "onItemSwiped $position size ${roundCorrals.size}")

        var currentTotalWeight = 0.0
        var currentTotalPercentage = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
            if (roundCorralDetail.percentage >= 0){
                currentTotalPercentage += roundCorralDetail.percentage
            }
        }

        for (index in 0 until roundCorrals.size){
            if (index >= position){
                roundCorrals[index].order = roundCorrals[index].order-1
            }

            if (usePercentage){
                roundCorrals[index].weight = Helper.getNumberWithDecimals(roundCorrals[index].percentage * currentTotalWeight / 100,2).toDouble()
            } else {
                roundCorrals[index].percentage = Helper.getNumberWithDecimals(roundCorrals[index].weight * 100 / currentTotalWeight,2).toDouble()
            }

        }


        notifyDataSetChanged()
    }

    fun setSelected(selected : Long) {
        Log.i("DEBUG","setSelected id: $selected ")
        corralId = selected
        setFocus = true
        notifyDataSetChanged()
    }
}