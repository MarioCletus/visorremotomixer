package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductStepLoadBinding
import com.basculasmagris.visorremotomixer.model.entities.DietDetail
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.utils.Helper
import android.util.Log
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.fragments.RemoteMixerFragment
import com.basculasmagris.visorremotomixer.view.fragments.StepLoadFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RoundRunProductAdapter (
    private val fragment: Fragment,
    private val diet: DietDetail,
    private val step: Double
    ) : RecyclerView.Adapter<RoundRunProductAdapter.ViewHolder>(),
    Filterable {

    private val TAG: String = "DEBRRA"
    private val RUIDO: Int = 2
    private var lastProduct: Boolean = false
    private var products: List<ProductDetail> = listOf()
    private var filteredProducts: List<ProductDetail> = listOf()
    var endLoad = false
    var selectedPosition = 0


    class ViewHolder (view: ItemLineRoundRunProductStepLoadBinding) : RecyclerView.ViewHolder(view.root) {
        val tvProductName = view.tvProductName
        val tvCurrentWeight = view.tvCurrentWeight
        val tvDiffWeight = view.tvDiffWeight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunProductStepLoadBinding = ItemLineRoundRunProductStepLoadBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = filteredProducts[position]
        fragment.context?.let {
            if(selectedPosition == position && !endLoad) {
                holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_select_bkg)
                if (fragment is StepLoadFragment){
                    fragment.selectProduct(product)
                }
            }
            else if( position < selectedPosition || endLoad){
                holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_ready_bkg)
                val value = (product.currentWeight-product.initialWeight)-product.targetWeight
                if(value >= 1){
                    holder.tvDiffWeight.text = "+${Helper.getNumberWithDecimals(value, 0)}Kg"
                }else {
                    holder.tvDiffWeight.text = "${Helper.getNumberWithDecimals(value, 0)}Kg"
                }
            }else{
                holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_bkg)
            }
            holder.tvProductName.text = product.name
            holder.tvCurrentWeight.text = "${Helper.getNumberWithDecimals(product.targetWeight, 0)}Kg"
        }

    }

    override fun getItemCount(): Int {
        return filteredProducts.size
    }


    fun productList(list: MutableList<ProductDetail>){
        products = list
        filteredProducts = list
        notifyDataSetChanged()
    }

    fun updateWeight(weight: Double){
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredProducts[selectedPosition].currentWeight = weight
            }
            notifyDataSetChanged()
        }
    }

    fun updateInicial(weight: Double){
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredProducts[selectedPosition].initialWeight = weight
            }
            notifyDataSetChanged()
        }

    }

 
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredProducts = if (charString.isEmpty()) products else {
                    val filteredList = ArrayList<ProductDetail>()
                    products
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredProducts }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredProducts = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<ProductDetail>
                notifyDataSetChanged()
            }
        }
    }

    fun lastProductClose(){
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredProducts[selectedPosition].finalWeight = filteredProducts[selectedPosition].currentWeight
                (fragment.requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.diet?.products?.firstOrNull{
                    it.id == filteredProducts[selectedPosition].id
                }?.finalWeight = filteredProducts[selectedPosition].currentWeight
                (fragment.requireActivity() as RoundRunActivity).saveRoundLoadStatus()
                (fragment.requireActivity() as RoundRunActivity).changeStep(1)
                if(fragment is StepLoadFragment){
                    fragment.cleanAll()
                }
            }
            notifyDataSetChanged()
        }
    }

    fun selectProduct(position : Int){
        if(position < filteredProducts.size){
            selectedPosition = position
        }
    }

    fun nextProduct(){
        if(selectedPosition < filteredProducts.size-1){
            selectedPosition += 1
            notifyDataSetChanged()
        }
    }

    fun isLastProduct(): Boolean {
        return lastProduct
    }

    fun getProduct(pos : Int) : ProductDetail?{
        if(pos < filteredProducts.size && pos > 0)
            return filteredProducts[pos]
        return null
    }

    fun tare(mixerWeight: Double) {
        Log.i(TAG,"Tare: $mixerWeight")
        MainScope().launch {
            withContext(Dispatchers.Default) {
                Log.i(TAG,"Tare: $mixerWeight")
                filteredProducts[selectedPosition].initialWeight = mixerWeight//filteredProducts[selectedPosition].currentWeight
                (fragment.requireActivity() as RoundRunActivity).currentRoundRunDetail?.round?.diet?.products?.firstOrNull{
                    filteredProducts[selectedPosition].id == it.id
                }?.initialWeight = mixerWeight
                (fragment.requireActivity() as RoundRunActivity).saveRoundLoadStatus()
            }
            notifyDataSetChanged()
        }

    }

    fun updateRound(roundRunDetail: RoundRunDetail) {
        var position : Int = 0
        filteredProducts.forEach{productDetail ->
            productDetail.initialWeight = roundRunDetail.round.diet.products[position].initialWeight
            productDetail.finalWeight = roundRunDetail.round.diet.products[position].finalWeight
            productDetail.currentWeight = roundRunDetail.round.diet.products[position].currentWeight
            position++
        }
    }

}