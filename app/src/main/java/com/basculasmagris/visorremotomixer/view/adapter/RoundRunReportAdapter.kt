package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunReportBinding
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper

class RoundRunReportAdapter (
    private val fragment: Fragment,
    private val type: Int
    ) : RecyclerView.Adapter<RoundRunReportAdapter.ViewHolder>() {

    private var filteredRoundRunDetail: List<RoundRunDetail> = listOf()

    class ViewHolder (view: ItemLineRoundRunReportBinding) : RecyclerView.ViewHolder(view.root) {
        val tvRoundName = view.tvRoundName
        val tvDietName = view.tvDietName
        val tvStartDate = view.tvStartDate
        val tvEndDate = view.tvEndDate
        val rvRoundReportProducts = view.rvRoundReportProducts
        val rvRoundReportCorrals = view.rvRoundReportCorrals

        val tvTotal = view.tvTotal
        val tvDifferenceWeight = view.tvDifferenceWeight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunReportBinding = ItemLineRoundRunReportBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundRunDetail = filteredRoundRunDetail[position]
        holder.tvRoundName.text = roundRunDetail.round.name
        holder.tvDietName.text = roundRunDetail.round.diet.name
        holder.tvStartDate.text = Helper.formattedDate(roundRunDetail.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)
        holder.tvEndDate.text = Helper.formattedDate(roundRunDetail.endDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)

        var targetCorrals = 0.0
        var currentCorrals = 0.0
        roundRunDetail.round.corrals.forEach {
            targetCorrals += it.customTargetWeight
            currentCorrals += it.currentWeight
        }

        var targetProducts = 0.0
        var currentProducts = 0.0
        roundRunDetail.round.diet.products.forEach {
            targetProducts += it.targetWeight
            currentProducts += it.currentWeight
        }


        when (type) {
            R.id.rb_opt_carga -> {
                holder.rvRoundReportProducts.visibility = VISIBLE
                holder.rvRoundReportCorrals.visibility = GONE
                val params = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.weight = 1.0f
                holder.rvRoundReportProducts.layoutParams = params
                holder.rvRoundReportProducts.layoutManager = GridLayoutManager(fragment.requireActivity(), 2)
                val adapter =  RoundRunProductReportAdapter(
                    fragment,
                    roundRunDetail)
                adapter.productList(roundRunDetail.round.diet.products)
                holder.rvRoundReportProducts.adapter = adapter
                holder.tvDifferenceWeight.text = ""
                holder.tvTotal.text = "${Helper.getNumberWithDecimals(currentProducts, 0)}Kg de ${Helper.getNumberWithDecimals(targetCorrals, 0)}Kg"
                //holder.tvDifferenceWeight.text = "${Helper.getNumberWithDecimals(targetCorrals - currentProducts, 0)}Kg"

                val loadDiff = currentProducts-targetCorrals

                if (loadDiff == 0.0){
                    holder.tvDifferenceWeight.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.green_500_primary))
                    holder.tvDifferenceWeight.text = "${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
                } else if (loadDiff > 0){
                    holder.tvDifferenceWeight.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.blue_900_light))
                    holder.tvDifferenceWeight.text = "+${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
                } else {
                    holder.tvDifferenceWeight.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
                    holder.tvDifferenceWeight.text = "${loadDiff}Kg"
                }
            }
            R.id.rb_opt_descarga -> {
                holder.rvRoundReportCorrals.visibility = VISIBLE
                holder.rvRoundReportProducts.visibility = GONE

               val params = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.weight = 1.0f
                holder.rvRoundReportCorrals.layoutParams = params

                holder.rvRoundReportCorrals.layoutManager = GridLayoutManager(fragment.requireActivity(), 2)
                val adapter =  RoundRunCorralReportAdapter(
                    fragment,
                    roundRunDetail)
                adapter.corralList(roundRunDetail.round.corrals)
                holder.rvRoundReportCorrals.adapter = adapter

                holder.tvTotal.text = "${Helper.getNumberWithDecimals(currentCorrals, 0)}Kg de ${Helper.getNumberWithDecimals(targetCorrals, 0)}Kg"
                //holder.tvDifferenceWeight.text = "${Helper.getNumberWithDecimals(targetCorrals - currentCorrals, 0)}Kg"

                val loadDiff = currentCorrals-targetCorrals

                if (loadDiff == 0.0){
                    holder.tvDifferenceWeight.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.green_500_primary))
                    holder.tvDifferenceWeight.text = "${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
                } else if (loadDiff > 0){
                    holder.tvDifferenceWeight.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.blue_900_light))
                    holder.tvDifferenceWeight.text = "+${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
                } else {
                    holder.tvDifferenceWeight.setTextColor(ContextCompat.getColor(fragment.requireActivity(), R.color.red_500_primary))
                    holder.tvDifferenceWeight.text = "${loadDiff}Kg"
                }
            }
            R.id.rb_opt_ronda -> {
                holder.rvRoundReportCorrals.visibility = VISIBLE
                holder.rvRoundReportProducts.visibility = VISIBLE

                val params = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.weight = 0.5f
                holder.rvRoundReportProducts.layoutParams = params
                holder.rvRoundReportCorrals.layoutParams = params

                holder.rvRoundReportProducts.layoutManager = GridLayoutManager(fragment.requireActivity(), 2)
                val adapter1 =  RoundRunProductReportAdapter(
                    fragment,
                    roundRunDetail)
                adapter1.productList(roundRunDetail.round.diet.products)
                holder.rvRoundReportProducts.adapter = adapter1

                holder.rvRoundReportCorrals.layoutManager = GridLayoutManager(fragment.requireActivity(), 2)
                val adapter =  RoundRunCorralReportAdapter(
                    fragment,
                    roundRunDetail)
                adapter.corralList(roundRunDetail.round.corrals)
                holder.rvRoundReportCorrals.adapter = adapter
            }
        }

    }

    override fun getItemCount(): Int {
        return filteredRoundRunDetail.size
    }
    
    fun roundRunList(list: MutableList<RoundRunDetail>){
        filteredRoundRunDetail = list
        notifyDataSetChanged()
    }
}