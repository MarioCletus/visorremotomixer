package com.basculasmagris.visorremotomixer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductStepLoadBinding
import com.basculasmagris.visorremotomixer.model.entities.MinProductDetail
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.view.fragments.RemoteMixerFragment


class RoundRunProductAdapter (
    private val fragment: Fragment
    ) : RecyclerView.Adapter<RoundRunProductAdapter.ViewHolder>(),
    Filterable {

    private val TAG: String = "DEBRRA"
    private var products: List<MinProductDetail> = listOf()
    private var filteredProducts: List<MinProductDetail> = listOf()
    var selectedPosition = 0


    class ViewHolder (view: ItemLineRoundRunProductStepLoadBinding) : RecyclerView.ViewHolder(view.root) {
        val tvProductName = view.tvProductName
        val tvCurrentWeight = view.tvCurrentWeight
        val tvDiffWeight = view.tvDiffWeight
        val llBarra1 = view.llBarra1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunProductStepLoadBinding = ItemLineRoundRunProductStepLoadBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        if((fragment as RemoteMixerFragment).isInFree()){
            binding.tvDiffWeight.visibility = View.GONE
            binding.llBarra2.visibility = View.GONE
            val paramsProductName = binding.tvProductName.layoutParams as LinearLayout.LayoutParams
            paramsProductName.weight = 0.7f
            binding.tvProductName.layoutParams = paramsProductName

            val paramsCurrentWeight = binding.tvCurrentWeight.layoutParams as LinearLayout.LayoutParams
            paramsCurrentWeight.weight = 0.3f
            binding.tvCurrentWeight.layoutParams = paramsCurrentWeight
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = filteredProducts[position]
        fragment.context?.let {
            if((fragment as RemoteMixerFragment).isInFree()){
                if(position < filteredProducts.size-1){
                    Log.i(TAG,"onBindViewHolder isInFree position < filteredProducts.size-1 $position $product")
                    holder.itemView.background = ContextCompat.getDrawable(it, R.drawable.item_round_run_product_ready_bkg)
                    holder.tvProductName.text = if(product.description == "")product.name else "${product.name}\n${product.description}"
                    holder.tvCurrentWeight.text = "${product.targetWeight}kg"
                    holder.tvCurrentWeight.visibility = View.VISIBLE
                    holder.llBarra1.visibility = View.VISIBLE
                    holder.tvCurrentWeight.layoutParams.width = 50
                }else{
                    Log.i(TAG,"onBindViewHolder isInFree position >= filteredProducts.size-1 $position $product")
                    holder.itemView.background = ContextCompat.getDrawable(it, R.drawable.item_round_run_product_select_bkg)
                    holder.tvProductName.text = product.name
                    holder.tvCurrentWeight.visibility = View.GONE
                    holder.llBarra1.visibility = View.GONE

                }
            }else{
                if(selectedPosition == position) {
                    Log.i(TAG,"onBindViewHolder position = selectedPosition $position $product")
                    holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_select_bkg)
                }
                else if( position < selectedPosition){
                    Log.i(TAG,"onBindViewHolder position < selectedPosition $position < $selectedPosition $product")
                    holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_ready_bkg)
                    val value = (product.finalWeight - product.initialWeight)-product.targetWeight
                    if(value >= 1){
                        holder.tvDiffWeight.text = "+${value}kg"
                    }else {
                        holder.tvDiffWeight.text = "${value}kg"
                    }
                }else{
                    Log.i(TAG,"onBindViewHolder default position $position selectedPosition $selectedPosition $product")
                    holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_bkg)
                }
            }
            holder.tvProductName.text = product.name
            holder.tvCurrentWeight.text = "${product.targetWeight}kg"
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
            Log.i(TAG,"selectProduct $selectedPosition ${filteredProducts[position]}")
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

    fun getItems(): List<MinProductDetail> {
        return  filteredProducts
    }

}