package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemMixerLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.fragments.MixerListFragment


class MixerAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<MixerAdapter.ViewHolder>(),
    Filterable {

    private var mixers: MutableList<Mixer>  = ArrayList()
    private var filteredMixers: MutableList<Mixer>  = ArrayList()
    var selectedMixer : Mixer? = null
    private var previousMixer : Mixer? = null

    class ViewHolder (view: ItemMixerLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvMixerTitle = view.tvMixerTitle
        val btnEditMixer = view.btnEditMixer
        val btnDeleteMixer = view.btnDeleteMixer
        val etMixerDescription = view.etMixerDescription
        val tvTara = view.tvTara
        val tvCalibration = view.tvCalibration
        val tvMac = view.tvMac
        val mixerCard = view.mixerCard
        val ibMore    = view.ibMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMixerLayoutBinding = ItemMixerLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mixerInPosition = filteredMixers[position]

        holder.tvMixerTitle.text = mixerInPosition.name
        holder.etMixerDescription.text = mixerInPosition.description.ifEmpty { fragment.getString(R.string.lbl_no_description_short) }
        holder.tvMac.text = mixerInPosition.mac
        holder.tvCalibration.text = "${mixerInPosition.calibration}Kg"
        holder.tvTara.text =  "${mixerInPosition.tara}Kg"

        if(selectedMixer != null && selectedMixer?.id == mixerInPosition.id){
            if(fragment is MixerListFragment && selectedMixer != null && selectedMixer != previousMixer){
                fragment.myMenu?.findItem(R.id.menu_selected_mixer)?.title = "   " + mixerInPosition.name
                previousMixer = selectedMixer
            }
            holder.mixerCard.strokeWidth = 4
            if(fragment.context != null){
                holder.mixerCard.strokeColor = fragment.context?.let { getColor(it,R.color.color_dark_grey) }!!
            }
        }else{
            holder.mixerCard.strokeWidth = 1
            if(fragment.context != null){
                holder.mixerCard.strokeColor = fragment.context?.let { getColor(it,R.color.color_dark_grey) }!!
            }
        }

        holder.itemView.setOnClickListener {
            if (fragment is MixerListFragment) {
                fragment.configMixer(mixerInPosition,true)
            }
        }

        holder.btnEditMixer.setOnClickListener{
            if ( fragment is MixerListFragment){
                fragment.configMixer(mixerInPosition,false)
            }
        }

        holder.btnDeleteMixer.setOnClickListener{
            if (fragment is MixerListFragment) {
                fragment.deleteMixer(mixerInPosition)
            }
        }

        holder.itemView.setOnLongClickListener{
            if(fragment is MixerListFragment){
                (fragment.requireActivity() as MainActivity).saveMixer(mixerInPosition)
                fragment.selectedMixer = mixerInPosition
                selectedMixer = mixerInPosition
                previousMixer = null
                notifyDataSetChanged()
            }
            return@setOnLongClickListener true
        }


        holder.ibMore.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter_mixer, popup.menu)
            popup.setOnMenuItemClickListener {menuItem ->
                if (menuItem.itemId  == R.id.action_select_mixer){
                    if(fragment is MixerListFragment){
                        (fragment.requireActivity() as MainActivity).saveMixer(mixerInPosition)
                        fragment.selectedMixer = mixerInPosition
                        selectedMixer = mixerInPosition
                        previousMixer = null
                        notifyDataSetChanged()
                    }
                }
                true
            }

            popup.show()
        }

    }

    override fun getItemCount(): Int {
        return filteredMixers.size
    }

    fun mixerList(list: MutableList<Mixer>){
        mixers = list
        filteredMixers = list
        notifyDataSetChanged()
    }

    fun statusConnexion(mixer: Mixer, connected: Boolean){
        val index = mixers.indexOf(mixer)
        val indexFiltered = filteredMixers.indexOf(mixer)

        if (indexFiltered != -1 && index != -1){
            mixers[index].linked = connected
            filteredMixers[indexFiltered].linked = connected
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredMixers = if (charString.isEmpty()) mixers else {
                    val filteredList = ArrayList<Mixer>()
                    mixers
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredMixers }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredMixers = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Mixer>
                notifyDataSetChanged()
            }
        }
    }

}