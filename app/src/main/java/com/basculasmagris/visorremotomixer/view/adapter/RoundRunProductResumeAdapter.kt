package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductResumeBinding
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity

class RoundRunProductResumeAdapter (
    private val fragment: Fragment
    ) : RecyclerView.Adapter<RoundRunProductResumeAdapter.ViewHolder>() {

    private var filteredProducts: List<ProductDetail> = listOf()

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
        val activity = (fragment.requireActivity() as RoundRunActivity)
        activity.getCustomRoundRunTargetWeight()
        val targetProductWeight = product.targetWeight//Helper.getNumberWithDecimals(product.percentage*roundTargetWeight/100,0).toDouble()
        val difference = (product.finalWeight-product.initialWeight) - targetProductWeight
        val percentage = (product.finalWeight-product.initialWeight)*100/targetProductWeight

        holder.tvProductName.text = product.name
        holder.tvCurrentProductPercentage.text = "${Helper.getNumberWithDecimals (percentage, 0)}%"
        holder.tvCurrentProductWeightOf.text = "${Helper.getNumberWithDecimals ((product.finalWeight-product.initialWeight) , 0)}Kg de ${targetProductWeight}Kg"
        holder.pbCurrentProduct.progress = percentage.toInt()

        if (difference == 0.0){
//            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.green_500_primary))
            holder.tvProductDifference.text = "${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else if (difference > 0){
//            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.blue_900_light))
            holder.tvProductDifference.text = "+${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else {
//            holder.tvProductDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
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