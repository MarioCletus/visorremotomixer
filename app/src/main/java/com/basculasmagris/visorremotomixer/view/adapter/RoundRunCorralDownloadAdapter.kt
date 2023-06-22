package com.basculasmagris.visorremotomixer.view.adapter
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralDownloadBinding
import com.basculasmagris.visorremotomixer.model.entities.CorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.fragments.StepDownloadFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RoundRunCorralDownloadAdapter (
    private  val fragment: Fragment
) : RecyclerView.Adapter<RoundRunCorralDownloadAdapter.ViewHolder>(),
    Filterable {

    private var originalCorrals: MutableList<CorralDetail> = ArrayList()
    private var roundCorrals: MutableList<CorralDetail> = ArrayList()
    private var filteredRoundCorrals: MutableList<CorralDetail> = ArrayList()
    var selectedPosition = 0
    private var lastCorral: Boolean = false
    private val RUIDO: Int = 2

    class ViewHolder (view: ItemLineRoundRunCorralDownloadBinding) : RecyclerView.ViewHolder(view.root){
        val tvCorralName = view.tvCorralName
        val tvDiffWeight = view.tvDiffWeight
        val tvCurrentWeight = view.tvCurrentWeight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundRunCorralDownloadBinding = ItemLineRoundRunCorralDownloadBinding.inflate(
            LayoutInflater.from(fragment.requireActivity()), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundCorral = roundCorrals[position]

        if (fragment is StepDownloadFragment) {
            holder.itemView.setOnClickListener {
                val isPrevPosition = selectedPosition == position + 1
                val pendienteEnHolderPrevio = (roundCorrals[position].initialWeight.minus(roundCorrals[position].finalWeight))-roundCorrals[position].actualTargetWeight
                val descargadoEnHolderActual = roundCorrals[selectedPosition].initialWeight.minus(roundCorrals[selectedPosition].currentWeight)
                Log.i("DEBUG","itemView.setOnClick isPrevPosition $isPrevPosition  " +
                        "\nRestante en el corral previo $pendienteEnHolderPrevio " +
                        "\nDescargado en el corral actual $descargadoEnHolderActual" +
                        "\nroundCorrals[$position].initialWeight ${roundCorrals[position].initialWeight} roundCorrals[$position].finalWeight ${roundCorrals[position].finalWeight}  roundCorrals[$position].targetWeight ${roundCorrals[position].actualTargetWeight} "+
                        "\nroundCorrals[$selectedPosition].initialWeight ${roundCorrals[selectedPosition].initialWeight} roundCorrals[$selectedPosition].finalWeight ${roundCorrals[selectedPosition].finalWeight}  roundCorrals[$selectedPosition].targetWeight ${roundCorrals[selectedPosition].actualTargetWeight} ")
                if(isPrevPosition &&
                    pendienteEnHolderPrevio < RUIDO &&
                    descargadoEnHolderActual<RUIDO
                    && descargadoEnHolderActual >-1*RUIDO ){
                    Log.i("DEBUG","Corral previo $position " +
                            "\nroundCorral.initialWeight ${roundCorral.initialWeight} " +
                            "\nroundCorral.currentWeight ${roundCorral.currentWeight} " +
                            "\nroundCorral.finalWeight ${roundCorral.finalWeight} " +
                            "")
                    selectedPosition = position
                    lastCorral = false
                    roundCorral.finalWeight = 0.0
                    fragment.setCorral(roundCorral)
                    notifyDataSetChanged()
                }
            }
        }

        fragment.context?.let{
            if (fragment is StepDownloadFragment) {
                if(selectedPosition == position) {
                    holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_select_bkg)
                    fragment.selectCorral(roundCorral)
                }
                else if( position < selectedPosition){
                    holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_ready_bkg)
                    val value = (roundCorral.initialWeight-roundCorral.currentWeight) - roundCorral.actualTargetWeight
                    if(value >= 1){
                        holder.tvDiffWeight.text = "+${Helper.getNumberWithDecimals(value, 0)}Kg"
                    }else {
                        holder.tvDiffWeight.text = "${Helper.getNumberWithDecimals(value, 0)}Kg"
                    }
                }else{
                    holder.itemView.background = ContextCompat.getDrawable(it,R.drawable.item_round_run_product_bkg)
                }
            holder.tvCorralName.text = roundCorral.name
            }
        }
        holder.tvCorralName.text = roundCorral.name
        holder.tvCurrentWeight.text = "${Helper.getNumberWithDecimals(roundCorral.actualTargetWeight, 0)}Kg"
    }

    fun updateCorralTargetWeight(){
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredRoundCorrals.forEach {  corralDetail ->
                    val roundRunActivity = fragment.requireActivity() as RoundRunActivity
                    val newTarget = roundRunActivity.getRealRoundRunCorralTargetWeight(corralDetail)
                    corralDetail.actualTargetWeight =newTarget
                }
            }
            notifyDataSetChanged()
        }
    }

    fun updateCorralWeight(weight: Double){
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredRoundCorrals[selectedPosition].currentWeight = weight
            }
            notifyDataSetChanged()
        }
    }

    private fun getTargetCorral(corral: CorralDetail) : Double {
        val roundTargetWeight = corral.actualTargetWeight//(fragment.requireActivity() as RoundRunActivity).getRealRoundRunTargetWeight()
        return corral.percentage*roundTargetWeight/100
    }

    fun lastCorralClose(){
        Log.i("DEBUG","Last corral close")

        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredRoundCorrals[selectedPosition].finalWeight = filteredRoundCorrals[selectedPosition].currentWeight
            }
            (fragment.requireActivity() as RoundRunActivity).changeStep(1)
            if(fragment is StepDownloadFragment){
                fragment.cleanAll()
            }
            notifyDataSetChanged()
        }

    }

    fun selectNextCorral(){
        if(lastCorral){
            Log.i("DEBUG","no deberia pasar")
            (fragment.requireActivity() as RoundRunActivity).changeStep(1)
            if(fragment is StepDownloadFragment){
                fragment.cleanAll()
            }
            return
        }
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredRoundCorrals[selectedPosition].finalWeight = filteredRoundCorrals[selectedPosition].currentWeight
                (fragment.requireActivity() as RoundRunActivity).saveRoundLoadStatus()
                selectedPosition += 1
                Log.i("DEBUG","Se pasa de corral $selectedPosition  " +
                        "\nfilteredRoundCorrals[${selectedPosition-1}].initialWeight ${ filteredRoundCorrals[selectedPosition-1].initialWeight}" +
                        "\nfilteredRoundCorrals[${selectedPosition-1}].currentWeight ${ filteredRoundCorrals[selectedPosition-1].currentWeight}" +
                        "\nfilteredRoundCorrals[${selectedPosition-1}].finalWeight ${ filteredRoundCorrals[selectedPosition-1].finalWeight}" +
                        "\nfilteredRoundCorrals[$selectedPosition].initialWeight ${ filteredRoundCorrals[selectedPosition].initialWeight}" +
                        "\nfilteredRoundCorrals[$selectedPosition].currentWeight ${ filteredRoundCorrals[selectedPosition].currentWeight}" +
                        "\nfilteredRoundCorrals[$selectedPosition].finalWeight ${ filteredRoundCorrals[selectedPosition].finalWeight}" +
                        "" )
                if (selectedPosition < filteredRoundCorrals.size-1){
                } else {
                    Log.i("DEBUG", "Es el último corral")
                    if(fragment is StepDownloadFragment){
                        fragment.lastCorral()
                        lastCorral = true
                    }
                }
            }

            notifyDataSetChanged()
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
                filteredRoundCorrals = if (charString.isEmpty()) roundCorrals else {
                    val filteredList = ArrayList<CorralDetail>()
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
                    results.values as ArrayList<CorralDetail>
                notifyDataSetChanged()
            }
        }
    }

    fun updateInicial(mixerWeight: Double) {
        MainScope().launch {
            withContext(Dispatchers.Default) {
                filteredRoundCorrals[selectedPosition].initialWeight = mixerWeight
            }
            notifyDataSetChanged()
        }
    }

    fun tare(mixerWeight: Double) {
        val currentCorralDetail = filteredRoundCorrals[selectedPosition]
        Log.i("DEBUG", "[RoundRun] Tare - Initial: ${currentCorralDetail.initialWeight}    | MixerWeight $mixerWeight  | currentWeight ${currentCorralDetail.currentWeight}" )
        currentCorralDetail.currentWeight.let {
            currentCorralDetail.initialWeight = mixerWeight
            currentCorralDetail.currentWeight = mixerWeight
        }
    }

    fun isLastCorral(): Boolean {
        return lastCorral
    }


    fun getCorral(pos : Int) : CorralDetail?{
        if(pos < filteredRoundCorrals.size && pos > 0)
            return filteredRoundCorrals[pos]
        return null
    }

}