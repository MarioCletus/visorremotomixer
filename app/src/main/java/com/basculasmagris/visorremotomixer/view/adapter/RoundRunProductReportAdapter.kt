package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductReportBinding
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.utils.Helper

class RoundRunProductReportAdapter (
    private val fragment: Fragment,
    private val roundRunDetail: RoundRunDetail
    ) : RecyclerView.Adapter<RoundRunProductReportAdapter.ViewHolder>() {

    private var filteredProducts: List<ProductDetail> = listOf()

    class ViewHolder (view: ItemLineRoundRunProductReportBinding) : RecyclerView.ViewHolder(view.root) {
        val tvProductName = view.tvProductName
        val tvProductDifference = view.tvProductDifference
        val tvCurrentProductWeightOf = view.tvCurrentProductWeightOf
        val tvCurrentProductPercentage = view.tvCurrentProductPercentage
        val pbCurrentProduct = view.pbCurrentProduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunProductReportBinding = ItemLineRoundRunProductReportBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = filteredProducts[position]
        val roundTargetWeight = roundRunDetail.round.customRoundRunWeight
        val targetProductWeight = Helper.getNumberWithDecimals(product.percentage*roundTargetWeight/100,0).toDouble()
        val difference = product.currentWeight - targetProductWeight
        val percentage = if(targetProductWeight != 0.0) product.currentWeight*100/targetProductWeight else 0.0

        holder.tvProductName.text = product.name
        holder.tvCurrentProductPercentage.text = "${Helper.getNumberWithDecimals (percentage, 0)}%"
        holder.tvCurrentProductWeightOf.text = "${Helper.getNumberWithDecimals (product.currentWeight , 0)}Kg de ${targetProductWeight}Kg"
        holder.pbCurrentProduct.progress = percentage.toInt()

        if (difference == 0.0){
            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.green_500_primary))
            holder.tvProductDifference.text = "${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else if (difference > 0){
            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.blue_900_light))
            holder.tvProductDifference.text = "+${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else {
            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
            holder.tvProductDifference.text = "${Helper.getNumberWithDecimals(difference, 0)}Kg"
        }
    }

    override fun getItemCount(): Int {
        return filteredProducts.size
    }

    fun productList(list: MutableList<ProductDetail>){
        filteredProducts = list
        notifyDataSetChanged()
    }
}