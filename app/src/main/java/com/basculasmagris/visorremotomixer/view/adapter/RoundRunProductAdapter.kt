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
import com.basculasmagris.visorremotomixer.model.entities.MinDietDetail
import com.basculasmagris.visorremotomixer.model.entities.MinProductDetail
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail


class RoundRunProductAdapter (
    private val fragment: Fragment,
    private val diet: MinDietDetail,
    private val step: Double
    ) : RecyclerView.Adapter<RoundRunProductAdapter.ViewHolder>(),
    Filterable {

    private val TAG: String = "DEBRRA"
    private var products: List<MinProductDetail> = listOf()
    private var filteredProducts: List<MinProductDetail> = listOf()
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
            }
            else if( position < selectedPosition || endLoad){
                holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_ready_bkg)
                val value = (product.finalWeight - product.initialWeight)-product.targetWeight
                if(value >= 1){
                    holder.tvDiffWeight.text = "+${value}Kg"
                }else {
                    holder.tvDiffWeight.text = "${value}Kg"
                }
            }else{
                holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_bkg)
            }
            holder.tvProductName.text = product.name
            holder.tvCurrentWeight.text = "${product.targetWeight}Kg"
        }

    }

    override fun getItemCount(): Int {
        return filteredProducts.size
    }


    fun productList(list: ArrayList<MinProductDetail>){
        products = list
        filteredProducts = list
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredProducts = if (charString.isEmpty()) products else {
                    val filteredList = ArrayList<MinProductDetail>()
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
                    results.values as ArrayList<MinProductDetail>
                notifyDataSetChanged()
            }
        }
    }

    fun selectProduct(position : Int){
        if(position < filteredProducts.size){
            selectedPosition = position
            notifyDataSetChanged()
        }
    }


    fun updateRound(roundRunDetail: MinRoundRunDetail) {
        var position : Int = 0
        filteredProducts.forEach{productDetail ->
            productDetail.initialWeight = roundRunDetail.round.diet.products[position].initialWeight
            productDetail.finalWeight = roundRunDetail.round.diet.products[position].finalWeight
            productDetail.targetWeight = roundRunDetail.round.diet.products[position].targetWeight
            position++
        }
    }

}