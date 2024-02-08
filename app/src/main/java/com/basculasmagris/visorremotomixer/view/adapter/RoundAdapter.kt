package com.basculasmagris.visorremotomixer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.basculasmagris.visorremotomixer.databinding.ItemRoundLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.Round
import com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.RoundCorralDetailData
import com.basculasmagris.visorremotomixer.view.activities.RoundData
import com.basculasmagris.visorremotomixer.view.fragments.RoundListFragment
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModelFactory

class RoundAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<RoundAdapter.ViewHolder>(),
    Filterable {

    private var rounds: List<Round> = listOf()
    private var filteredRounds: List<Round> = listOf()
    private var mLocalRoundCorralDetail: List<RoundCorralDetail>? = null

    private val mRoundViewModel: RoundViewModel by fragment.viewModels {
        RoundViewModelFactory((fragment.requireActivity().application as SpiMixerApplication).roundRepository)
    }

    private fun fetchLocalData(roundId: Long): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mRoundViewModel.activeRoundList) {
            if (it != null) {
                liveDataMerger.value = RoundData(it)
            }
        }
        liveDataMerger.addSource(mRoundViewModel.getCorralsBy(roundId)) {
            Log.i("Fat", "RoundAdapter - addSource 01 ${it?.size}")
            if (it != null) {
                Log.i("Fat", "RoundAdapter - addSource ${it.size}")
                liveDataMerger.value = RoundCorralDetailData(it)
            }
        }
        return liveDataMerger
    }
    private fun getLocalData(holder: ViewHolder, round: Round){
        // Sync local data
        Log.i("Fat", "RoundAdapter - getLocalData")
        val liveData = fetchLocalData(round.id)
        liveData.observe(fragment.requireActivity(), object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is RoundData -> rounds = it.rounds
                    is RoundCorralDetailData -> mLocalRoundCorralDetail = it.roundCorralDetail
                    else -> {}
                }

                Log.i("Fat", "RoundAdapter - rounds ${rounds.size}")
                Log.i("Fat", "RoundAdapter - mLocalRoundCorralDetail ${mLocalRoundCorralDetail?.size}")
                if (mLocalRoundCorralDetail != null) {
                    setCorralsData(holder, round)
                    liveData.removeObserver(this)
                    liveData.value = null
                    liveData.removeSource(mRoundViewModel.activeRoundList)
                    liveData.removeSource(mRoundViewModel.getCorralsBy(round.id))
                }
            }
        })
    }

    private fun setCorralsData(holder: ViewHolder, round: Round){
        Log.i("Fat", "RoundAdapter - setCorralsData ${mLocalRoundCorralDetail?.size}")
        holder.tvCorralQuantity.text = mLocalRoundCorralDetail?.size.toString()
        mLocalRoundCorralDetail = null
    }

    class ViewHolder (view: ItemRoundLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvRoundTitle = view.tvRoundTitle
        val ibMore = view.ibMore
        val tvRoundDescription = view.tvRoundDescription
        val tvCorralQuantity = view.tvCorralQuantity
        val tvRoundWeight = view.tvRoundWeight

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRoundLayoutBinding = ItemRoundLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val round = filteredRounds[position]
        getLocalData(holder, round)
        /*
        if (mLocalRoundCorrals == null){
            getLocalData(holder, round)
        } else {
            setCorralsData(holder, round)
        }*/
        holder.tvRoundTitle.text = round.name
        holder.tvRoundWeight.text = Helper.getFormattedWeightKg(round.weight, fragment.requireContext())
        holder.tvRoundDescription.text = round.description.ifEmpty { fragment.getString(R.string.lbl_no_description_short) }
        holder.itemView.setOnClickListener {
            if (fragment is RoundListFragment){
                fragment.goToRoundDetails(round)
            }
        }

//        holder.ibMore.setOnClickListener{
//            val popup =  PopupMenu(fragment.context, holder.ibMore)
//            popup.menuInflater.inflate(R.menu.menu_adapter_round, popup.menu)
//
//            // Si el roundo está sincronizado no se permite el borrado.
//            if (round.remoteId != 0L) {
//                popup.menu.getItem(1).isVisible = false
//            }
//
//            popup.setOnMenuItemClickListener {
//                if (it.itemId  == R.id.action_edit_round){
//                    val intent = Intent(fragment.requireActivity(), AddUpdateRoundActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_ROUND_DETAILS, round)
//                    fragment.requireActivity().startActivity(intent)
//                } else if (it.itemId == R.id.action_delete_round){
//                    if (fragment is RoundListFragment) {
//                        fragment.deleteRound(round)
//                    }
//                }
//                true
//            }
//
//            popup.show()
//        }

        if (fragment is RoundListFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredRounds.size
    }

    fun roundList(list: List<Round>){
        rounds = list
        filteredRounds = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredRounds = if (charString.isEmpty()) rounds else {
                    val filteredList = ArrayList<Round>()
                    rounds
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredRounds }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredRounds = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Round>
                notifyDataSetChanged()
            }
        }
    }
}