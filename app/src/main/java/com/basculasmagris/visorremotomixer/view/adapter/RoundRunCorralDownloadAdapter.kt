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
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralDownloadBinding
import com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail
import com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail
import com.basculasmagris.visorremotomixer.view.fragments.RemoteMixerFragment


class RoundRunCorralDownloadAdapter (
    private  val fragment: Fragment
) : RecyclerView.Adapter<RoundRunCorralDownloadAdapter.ViewHolder>(),
    Filterable {

    private var originalCorrals: ArrayList<MinCorralDetail> = ArrayList()
    private var roundCorrals: ArrayList<MinCorralDetail> = ArrayList()
    private var filteredRoundCorrals: ArrayList<MinCorralDetail> = ArrayList()
    var selectedPosition = 0
//    var endLoad = false


    class ViewHolder (view: ItemLineRoundRunCorralDownloadBinding) : RecyclerView.ViewHolder(view.root){
        val tvCorralName = view.tvCorralName
        val tvDiffWeight = view.tvDiffWeight
        val tvCurrentWeight = view.tvCurrentWeight
        val llBarra1 = view.llBarra1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunCorralDownloadBinding = ItemLineRoundRunCorralDownloadBinding.inflate(
            LayoutInflater.from(fragment.requireActivity()), parent, false)
        binding.tvDiffWeight.visibility = View.GONE
        binding.llBarra2.visibility = View.GONE
        val paramsCorralName = binding.tvCorralName.layoutParams as LinearLayout.LayoutParams
        paramsCorralName.weight = 0.7f
        binding.tvCorralName.layoutParams = paramsCorralName

        val paramsCurrentWeight = binding.tvCurrentWeight.layoutParams as LinearLayout.LayoutParams
        paramsCurrentWeight.weight = 0.3f
        binding.tvCurrentWeight.layoutParams = paramsCurrentWeight
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corral = filteredRoundCorrals[position]
        fragment.context?.let {context->
            if((fragment as RemoteMixerFragment).isInFree()){
                if(position < filteredRoundCorrals.size-1){
                    holder.itemView.background = ContextCompat.getDrawable(context, R.drawable.item_round_run_product_ready_bkg)
                    holder.tvCorralName.text = if(corral.description == "")corral.name else "${corral.name}\n${corral.description}"
                    holder.tvCurrentWeight.text = "${corral.customTargetWeight}Kg"
                    holder.tvCurrentWeight.visibility = View.VISIBLE
                    holder.llBarra1.visibility = View.VISIBLE
                }else{
                    holder.itemView.background = ContextCompat.getDrawable(context, R.drawable.item_round_run_product_select_bkg)
                    holder.tvCorralName.text = corral.name
                    holder.tvCurrentWeight.visibility = View.GONE
                    holder.llBarra1.visibility = View.GONE
                }
//                holder.itemView.background = ContextCompat.getDrawable(context,R.drawable.item_round_run_product_ready_bkg)
//                holder.tvDiffWeight.text = "0Kg"
            }else{
                if(selectedPosition == position) {
                    holder.itemView.background = ContextCompat.getDrawable(context,R.drawable.item_round_run_product_select_bkg)
                }
                else if( position < selectedPosition){
                    holder.itemView.background = ContextCompat.getDrawable(context,R.drawable.item_round_run_product_ready_bkg)
                    val diff = (corral.initialWeight - corral.finalWeight) - corral.actualTargetWeight
                    if(diff >= 1){
                        holder.tvDiffWeight.text = "+${diff}Kg"
                    }else {
                        holder.tvDiffWeight.text = "${diff}Kg"
                    }
                }else{
                    holder.itemView.background = ContextCompat.getDrawable(context,R.drawable.item_round_run_product_bkg)
                }
            }
            holder.tvCorralName.text = corral.name
            holder.tvCurrentWeight.text = "${corral.actualTargetWeight}Kg"
        }

    }

//    fun updateCorralWeight(weight: Double){
//        MainScope().launch {
//            withContext(Dispatchers.Default) {
//                filteredRoundCorrals[selectedPosition].finalWeight = weight.roundToLong()
//            }
//            notifyDataSetChanged()
//        }
//    }

    fun selectCorral(position : Int){
        if(position < filteredRoundCorrals.size){
            selectedPosition = position
        }
    }


    override fun getItemCount(): Int {
        return roundCorrals.size
    }

    fun corralList(list: ArrayList<MinCorralDetail>){
        roundCorrals = list
        filteredRoundCorrals = list
        originalCorrals = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredRoundCorrals = if (charString.isEmpty()) roundCorrals else {
                    val filteredList = ArrayList<MinCorralDetail>()
                    roundCorrals
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredRoundCorrals }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredRoundCorrals = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<MinCorralDetail>
                notifyDataSetChanged()
            }
        }
    }


    fun updateRound(roundRunDetail: MinRoundRunDetail) {
        var position : Int = 0
        filteredRoundCorrals.forEach{corralDetail ->
            corralDetail.initialWeight = roundRunDetail.round.corrals[position].initialWeight
            corralDetail.finalWeight = roundRunDetail.round.corrals[position].finalWeight
            corralDetail.actualTargetWeight = roundRunDetail.round.corrals[position].actualTargetWeight
            corralDetail.customTargetWeight = roundRunDetail.round.corrals[position].customTargetWeight
            position++
        }
    }
}