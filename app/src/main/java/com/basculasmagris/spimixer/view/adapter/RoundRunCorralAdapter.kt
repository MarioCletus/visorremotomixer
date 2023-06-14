package com.basculasmagris.visorremotomixer.view.adapter
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralConfigBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.fragments.StepConfigurationFragment


class RoundRunCorralAdapter (
    private  val fragment: Fragment
) : RecyclerView.Adapter<RoundRunCorralAdapter.ViewHolder>(),
    Filterable {
    
    private var originalCorrals: MutableList<CorralDetail> = ArrayList()
    private var roundCorrals: MutableList<CorralDetail> = ArrayList()
    private var filteredRoundCorrals: MutableList<CorralDetail> = ArrayList()
    private val step = 1

    class ViewHolder (view: ItemLineRoundRunCorralConfigBinding) : RecyclerView.ViewHolder(view.root){
        val tvCorralName = view.tvCorralName
        val tvWeight = view.tvCorralWeight
        val tvCorralAnimalCount = view.tvCorralAnimalCount
        val ibUpCustomWeight = view.ibUpCustomWeight
        val ibDownCustomWeight = view.ibDownCustomWeight
        val ibUpCustomAnimals = view.ibUpCustomAnimals
        val ibDownCustomAnimals = view.ibDownCustomAnimals
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding: ItemLineRoundRunCorralConfigBinding = ItemLineRoundRunCorralConfigBinding.inflate(
                LayoutInflater.from(fragment.requireActivity()), parent, false)
            return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundCorral = roundCorrals[position]
        fragment.context?.let{

            val newWeight = roundCorral.actualTargetWeight//roundCorral.customTargetWeight//roundCorral.weight*roundTargetPercentage/100
            holder.tvWeight.text = "${Helper.getNumberWithDecimals(newWeight, 0)}Kg"
            holder.tvCorralName.text = roundCorral.name
            holder.tvCorralAnimalCount.text = roundCorral.animalQuantity.toString()

            holder.ibUpCustomWeight.setOnClickListener {
                (fragment as StepConfigurationFragment)
                roundCorral.customTargetWeight = roundCorral.customTargetWeight+step
                roundCorral.actualTargetWeight = roundCorral.actualTargetWeight+step
                roundCorral.weight             = roundCorral.weight+step
                holder.tvWeight.text = "${Helper.getNumberWithDecimals(roundCorral.customTargetWeight, 0)}Kg"
                fragment.refreshCustomRoundRunTargetWeight()
            }

            holder.ibDownCustomWeight.setOnClickListener {
                (fragment as StepConfigurationFragment)
                if (roundCorral.customTargetWeight-step < 0){
                    holder.tvWeight.text = "${Helper.getNumberWithDecimals(0.0, 0)}Kg"
                } else {
                    roundCorral.customTargetWeight = roundCorral.customTargetWeight-step
                    roundCorral.actualTargetWeight = roundCorral.actualTargetWeight-step
                    roundCorral.weight             = roundCorral.weight-step
                    holder.tvWeight.text = "${Helper.getNumberWithDecimals(roundCorral.customTargetWeight, 0)}Kg"
                }
                fragment.refreshCustomRoundRunTargetWeight()
            }

            holder.ibUpCustomAnimals.setOnClickListener{
                val weightByHeads = roundCorral.actualTargetWeight.div(roundCorral.animalQuantity)
                roundCorral.actualTargetWeight = (weightByHeads * (roundCorral.animalQuantity + 1))
                roundCorral.customTargetWeight = roundCorral.actualTargetWeight
                roundCorral.weight             = roundCorral.actualTargetWeight
                roundCorral.animalQuantity = roundCorral.animalQuantity + 1
                holder.tvCorralAnimalCount.text = roundCorral.animalQuantity.toString()
                Log.i("DEBUG","roundCorral.actualTargetWeight ${roundCorral.actualTargetWeight}")
                holder.tvWeight.text = "${Helper.getNumberWithDecimals(roundCorral.actualTargetWeight, 0)}Kg"
                (fragment as StepConfigurationFragment)
                (fragment.activity as RoundRunActivity).currentRoundRunDetail?.round?.corrals?.get(position)?.animalQuantity = roundCorral.animalQuantity
                fragment.refreshCustomRoundRunTargetWeight()
            }

            holder.ibDownCustomAnimals.setOnClickListener{
                (fragment as StepConfigurationFragment)
                if (roundCorral.animalQuantity > 1){
                    val weightByHeads = roundCorral.actualTargetWeight.div(roundCorral.animalQuantity)
                    roundCorral.actualTargetWeight = (weightByHeads * (roundCorral.animalQuantity - 1))
                    roundCorral.customTargetWeight = roundCorral.actualTargetWeight
                    roundCorral.weight             = roundCorral.actualTargetWeight
                    roundCorral.animalQuantity = roundCorral.animalQuantity - 1
                    (fragment.activity as RoundRunActivity).currentRoundRunDetail?.round?.corrals?.get(position)?.animalQuantity = roundCorral.animalQuantity
                }
                holder.tvCorralAnimalCount.text = roundCorral.animalQuantity.toString()
                Log.i("DEBUG","roundCorral.actualTargetWeight ${roundCorral.actualTargetWeight}")
                holder.tvWeight.text = "${Helper.getNumberWithDecimals(roundCorral.actualTargetWeight, 0)}Kg"
                fragment.refreshCustomRoundRunTargetWeight()
            }

        }
    }

    override fun getItemCount(): Int {
        return roundCorrals.size
    }

    fun corralList(list: MutableList<CorralDetail>){
        roundCorrals = list.toMutableList()
        filteredRoundCorrals = list.toMutableList()
        originalCorrals = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredRoundCorrals = roundCorrals else {
                    val filteredList = ArrayList<CorralDetail>()
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
                    results.values as ArrayList<CorralDetail>
                notifyDataSetChanged()
            }
        }
    }

}