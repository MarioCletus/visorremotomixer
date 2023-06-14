package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralResumeBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper

class RoundRunCorralResumeAdapter (
    private val fragment: Fragment
    ) : RecyclerView.Adapter<RoundRunCorralResumeAdapter.ViewHolder>() {

    private var filteredCorrals: List<CorralDetail> = listOf()

    class ViewHolder (view: ItemLineRoundRunCorralResumeBinding) : RecyclerView.ViewHolder(view.root) {
        val tvCorralName = view.tvCorralName
        val tvCorralDifference = view.tvCorralDifference
        val tvCurrentCorralWeightOf = view.tvCurrentCorralWeightOf
        val tvCurrentCorralPercentage = view.tvCurrentCorralPercentage
        val pbCurrentCorral = view.pbCurrentCorral
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunCorralResumeBinding = ItemLineRoundRunCorralResumeBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corral = filteredCorrals[position]

        val targetCorralWeight = corral.actualTargetWeight
        holder.tvCorralName.text = corral.name
        var percentage = 0.0
        val difference = (corral.initialWeight - corral.finalWeight) - targetCorralWeight

        if (targetCorralWeight > 0) {
            percentage = (corral.initialWeight - corral.finalWeight)*100/targetCorralWeight
        }

        holder.tvCurrentCorralPercentage.text = "${Helper.getNumberWithDecimals (percentage, 0)}%"
        holder.tvCurrentCorralWeightOf.text = "${Helper.getNumberWithDecimals (corral.initialWeight - corral.finalWeight , 0)}Kg de ${Helper.getNumberWithDecimals (targetCorralWeight,0)}Kg"
        holder.pbCurrentCorral.progress = percentage.toInt()

        if (difference == 0.0){
            holder.tvCorralDifference.text = "${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else if (difference >= 0){
            holder.tvCorralDifference.text = "+${Helper.getNumberWithDecimals(difference, 0)}Kg"
        } else {
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