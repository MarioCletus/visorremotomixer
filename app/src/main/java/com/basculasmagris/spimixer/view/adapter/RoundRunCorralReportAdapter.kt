package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralReportBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.utils.Helper

class RoundRunCorralReportAdapter (
    private val fragment: Fragment,
    private val roundRunDetail: RoundRunDetail
    ) : RecyclerView.Adapter<RoundRunCorralReportAdapter.ViewHolder>() {

    private var filteredCorrals: List<CorralDetail> = listOf()

    class ViewHolder (view: ItemLineRoundRunCorralReportBinding) : RecyclerView.ViewHolder(view.root) {
        val tvCorralName = view.tvCorralName
        val tvCorralDifference = view.tvCorralDifference
        val tvCurrentCorralWeightOf = view.tvCurrentCorralWeightOf
        val tvCurrentCorralPercentage = view.tvCurrentCorralPercentage
        val pbCurrentCorral = view.pbCurrentCorral
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunCorralReportBinding = ItemLineRoundRunCorralReportBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corral = filteredCorrals[position]

        val targetProductWeight = corral.customTargetWeight
        holder.tvCorralName.text = corral.name
        var percentage = 0.0
        val difference = corral.currentWeight - targetProductWeight

        if (targetProductWeight > 0) {
            percentage = corral.currentWeight*100/targetProductWeight
        }

        holder.tvCurrentCorralPercentage.text = "${Helper.getNumberWithDecimals (percentage, 0)}%"
        holder.tvCurrentCorralWeightOf.text = "${Helper.getNumberWithDecimals (corral.currentWeight , 0)}Kg de ${Helper.getNumberWithDecimals (targetProductWeight,0)}Kg"
        holder.pbCurrentCorral.progress = percentage.toInt()

        if (difference >= 0){
            holder.tvCorralDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.blue_900_light))
            holder.tvCorralDifference.text = "+${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else {
            holder.tvCorralDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
            holder.tvCorralDifference.text = "${Helper.getNumberWithDecimals(difference, 0)}Kg"
        }
    }

    override fun getItemCount(): Int {
        return filteredCorrals.size
    }

    fun corralList(list: MutableList<CorralDetail>){
        filteredCorrals = list
        notifyDataSetChanged()
    }
}