package com.basculasmagris.visorremotomixer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ItemEstablishmentLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.Corral
import com.basculasmagris.visorremotomixer.model.entities.Establishment
import com.basculasmagris.visorremotomixer.view.activities.CorralData
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.fragments.EstablishmentListFragment
import com.basculasmagris.visorremotomixer.viewmodel.CorralViewModel
import com.basculasmagris.visorremotomixer.viewmodel.CorralViewModelFactory

class EstablishmentAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<EstablishmentAdapter.ViewHolder>(),
    Filterable {

    private var establishments: List<Establishment> = listOf()
    private var filteredEstablishments: List<Establishment> = listOf()
    private var mLocalCorrals: List<Corral>? = null

    private val mCorralViewModel: CorralViewModel by fragment.viewModels {
        CorralViewModelFactory((fragment.requireActivity().application as SpiMixerApplication).corralRepository)
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mCorralViewModel.allCorralList) {
            if (it != null) {
                liveDataMerger.value = CorralData(it)
            }
        }
        return liveDataMerger
    }
    private fun getLocalData(holder: ViewHolder, establishment: Establishment){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(fragment.requireActivity(), object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is CorralData -> mLocalCorrals = it.corrals
                    else -> {}
                }

                if (mLocalCorrals != null) {
                    Log.i("SYNC", "ADP Cantidad de corrales ${mLocalCorrals?.size}")
                    getLocalCorral(holder, establishment)
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    private fun getLocalCorral(holder: ViewHolder, establishment: Establishment){
        val corralCount = mLocalCorrals?.filter { corral ->
            corral.establishmentId == establishment.id
        }
        holder.tvCorralQuantity.text = corralCount?.size.toString()
    }

    class ViewHolder (view: ItemEstablishmentLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvEstablishmentTitle = view.tvEstablishmentTitle
        val ibMore = view.ibMore
        val btnEditEstablishment = view.btnEditEstablishment
        val btnDeleteEstablishment = view.btnDeleteEstablishment
        val tvEstablishmentDescription = view.tvEstablishmentDescription
        val tvCorralQuantity = view.tvCorralQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEstablishmentLayoutBinding = ItemEstablishmentLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val establishment = filteredEstablishments[position]
        getLocalData(holder, establishment)
        holder.tvEstablishmentTitle.text = establishment.name
        holder.tvEstablishmentDescription.text = establishment.description.ifEmpty { fragment.getString(R.string.lbl_no_description_short) }

        holder.itemView.setOnClickListener {
            if (fragment is EstablishmentListFragment){
                fragment.goToEstablishmentDetails(establishment)
            }
        }

//        holder.ibMore.setOnClickListener{
//            val popup =  PopupMenu(fragment.context, holder.ibMore)
//            popup.menuInflater.inflate(R.menu.menu_adapter_establishment, popup.menu)
//
//            // Si el establishmento está sincronizado no se permite el borrado.
//            if (establishment.remoteId != 0L) {
//                popup.menu.getItem(1).isVisible = false
//            }
//
//            popup.setOnMenuItemClickListener {
//                if (it.itemId  == R.id.action_edit_establishment){
//                    val intent = Intent(fragment.requireActivity(), AddUpdateEstablishmentActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS, establishment)
//                    fragment.requireActivity().startActivity(intent)
//                } else if (it.itemId == R.id.action_delete_establishment){
//                    if (fragment is EstablishmentListFragment) {
//                        fragment.deleteEstablishment(establishment)
//                    }
//                }
//                true
//            }
//
//            popup.show()
//        }
//
//        holder.btnEditEstablishment.setOnClickListener{
//            val intent = Intent(fragment.requireActivity(), AddUpdateEstablishmentActivity::class.java)
//            intent.putExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS, establishment)
//            fragment.requireActivity().startActivity(intent)
//        }

        holder.btnDeleteEstablishment.setOnClickListener{
            if (fragment is EstablishmentListFragment) {
                fragment.deleteEstablishment(establishment)
            }
        }
        
    }

    override fun getItemCount(): Int {
        return filteredEstablishments.size
    }

    fun establishmentList(list: List<Establishment>){
        establishments = list
        filteredEstablishments = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredEstablishments = if (charString.isEmpty()) establishments else {
                    val filteredList = ArrayList<Establishment>()
                    establishments
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredEstablishments }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredEstablishments = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Establishment>
                notifyDataSetChanged()
            }
        }
    }
}