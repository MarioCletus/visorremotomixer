package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductResumeBinding
import com.basculasmagris.visorremotomixer.model.entities.MinProductDetail

class RoundRunProductResumeAdapter (
    private val fragment: Fragment
    ) : RecyclerView.Adapter<RoundRunProductResumeAdapter.ViewHolder>() {

    private var filteredProducts: List<MinProductDetail> = listOf()

    class ViewHolder (view: ItemLineRoundRunProductResumeBinding) : RecyclerView.ViewHolder(view.root) {
        val tvProductName = view.tvProductName
        val tvProductDifference = view.tvProductDifference
        val tvCurrentProductWeightOf = view.tvCurrentProductWeightOf
        val tvCurrentProductPercentage = view.tvCurrentProductPercentage
        val pbCurrentProduct = view.pbCurrentProduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunProductResumeBinding = ItemLineRoundRunProductResumeBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = filteredProducts[position]
        val targetProductWeight = product.targetWeight
        val difference = (product.finalWeight-product.initialWeight) - targetProductWeight
        val percentage = if(targetProductWeight != 0L)(product.finalWeight-product.initialWeight)*100/targetProductWeight else 0

        holder.tvProductName.text = product.name
        holder.tvCurrentProductPercentage.text = "${percentage}%"
        holder.tvCurrentProductWeightOf.text = "${product.finalWeight-product.initialWeight}kg de ${targetProductWeight}kg"
        holder.pbCurrentProduct.progress = percentage.toInt()

        if (difference == 0L){
            holder.tvProductDifference.text = "${difference}kg"
            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.green_500_primary))
        } else if (difference > 0){
            holder.tvProductDifference.text = "+${difference}kg"
            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
        } else {
            holder.tvProductDifference.text = "${difference}kg"
            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
        }
    }

    override fun getItemCount(): Int {
        return filteredProducts.size
    }

    fun productList(list: MutableList<MinProductDetail>){
        filteredProducts = list
        notifyDataSetChanged()
    }
}