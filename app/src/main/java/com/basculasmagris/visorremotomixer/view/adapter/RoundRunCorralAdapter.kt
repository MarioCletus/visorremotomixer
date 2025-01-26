package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralConfigBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper


class RoundRunCorralAdapter (
    private  val fragment: Fragment
) : RecyclerView.Adapter<RoundRunCorralAdapter.ViewHolder>(),
    Filterable {

    private val TAG: String = "DEBRRCA"
    private var originalCorrals: ArrayList<MinCorralDetail> = ArrayList()
    private var roundCorrals: ArrayList<MinCorralDetail> = ArrayList()
    private var filteredRoundCorrals: ArrayList<MinCorralDetail> = ArrayList()


    class ViewHolder (view: ItemLineRoundRunCorralConfigBinding) : RecyclerView.ViewHolder(view.root){
        val tvCorralName = view.tvCorralName
        val tvWeight = view.tvCorralWeight
        val tvCorralAnimalCount = view.tvCorralAnimalCount

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunCorralConfigBinding = ItemLineRoundRunCorralConfigBinding.inflate(
            LayoutInflater.from(fragment.requireActivity()), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundCorral = roundCorrals[position]
        fragment.context?.let{
            val newWeight = roundCorral.actualTargetWeight
            holder.tvWeight.text = "${newWeight}Kg"
            holder.tvCorralName.text = roundCorral.name
            holder.tvCorralAnimalCount.text = roundCorral.animalQuantity.toString()
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
                if (charString.isEmpty()) filteredRoundCorrals = roundCorrals else {
                    val filteredList = ArrayList<MinCorralDetail>()
                    roundCorrals
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredRoundCorrals = filteredList

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

}