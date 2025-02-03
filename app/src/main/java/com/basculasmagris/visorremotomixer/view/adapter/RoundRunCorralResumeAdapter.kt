package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralResumeBinding
import com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper

class RoundRunCorralResumeAdapter (
    private val fragment: Fragment
    ) : RecyclerView.Adapter<RoundRunCorralResumeAdapter.ViewHolder>() {

    private var filteredCorrals: List<MinCorralDetail> = listOf()

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
        var percentage = 0L
        val difference = (corral.initialWeight - corral.finalWeight) - targetCorralWeight

        if (targetCorralWeight > 0) {
            percentage = (corral.initialWeight - corral.finalWeight)*100/targetCorralWeight
        }

        holder.tvCurrentCorralPercentage.text = "${percentage}%"
        holder.tvCurrentCorralWeightOf.text = "${corral.initialWeight - corral.finalWeight}Kg de ${targetCorralWeight}Kg"
        holder.pbCurrentCorral.progress = percentage.toInt()

        if (difference == 0L){
            holder.tvCorralDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.green_500_primary))
            holder.tvCorralDifference.text = "${difference}Kg"
        } else if (difference >= 0){
            holder.tvCorralDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
            holder.tvCorralDifference.text = "+${difference}Kg"
        } else {
            holder.tvCorralDifference.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
            holder.tvCorralDifference.text = "${difference}Kg"
        }
    }

    override fun getItemCount(): Int {
        return filteredCorrals.size
    }

    fun corralList(list: MutableList<MinCorralDetail>){
        filteredCorrals = list
        notifyDataSetChanged()
    }
}