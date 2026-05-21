package com.basculasmagris.visorremotomixer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemTabletLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.fragments.TabletListFragment

class TabletMixerAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<TabletMixerAdapter.ViewHolder>(),
    Filterable {

    private var tabletMixers: MutableList<TabletMixer>  = ArrayList()
    private var filteredTabletMixers: MutableList<TabletMixer>  = ArrayList()
    private var weight : Long = 0L
    var selectedTablet : TabletMixer? = null
    private var previousTablet : TabletMixer? = null
    class ViewHolder (view: ItemTabletLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvTabletTitle = view.tvTableTitle
        val btnEditTablet = view.btnEditTablet
        val btnDeleteTablet = view.btnDeleteTablet
        val etTabletMixerName = view.etTabletMixerName
        val btnSelect = view.btnSelect
        val mixerCard = view.tabletMixerCard

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTabletLayoutBinding = ItemTabletLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tabletMixer = filteredTabletMixers[position]
        holder.tvTabletTitle.text = tabletMixer.name
        holder.etTabletMixerName.text = tabletMixer.mixerName.ifEmpty { fragment.getString(R.string.lbl_sin_nombre_de_mixer_short) }
//        holder.tvWeight.text = "${weight}kg"

        if(selectedTablet != null && selectedTablet?.id == tabletMixer.id){
            if(fragment is TabletListFragment && selectedTablet != null && selectedTablet != previousTablet){
                fragment.menu?.findItem(R.id.menu_selected_mixer)?.title = "   " + tabletMixer.name
                previousTablet = selectedTablet
                Log.v("VER","fragment.myMenu?.findItem(R.id.menu_selected_mixer)?.title ${tabletMixer.name}")
            }
            holder.mixerCard.strokeWidth = 4
            if(fragment.context != null){
                holder.mixerCard.strokeColor = fragment.context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.color_dark_grey
                    )
                }!!
            }
        }else{
            holder.mixerCard.strokeWidth = 1
            if(fragment.context != null){
                holder.mixerCard.strokeColor = fragment.context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.color_dark_grey
                    )
                }!!
            }
        }


        holder.itemView.setOnClickListener {
            if (fragment is TabletListFragment) {
                fragment.goToConfigMixer(tabletMixer)
            }
        }

        holder.itemView.setOnLongClickListener{
            selectTabletMixer(tabletMixer)
            return@setOnLongClickListener true
        }

        holder.btnSelect.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.btnSelect)
            popup.menuInflater.inflate(R.menu.menu_adapter_mixer, popup.menu)
            popup.setOnMenuItemClickListener {menuItem ->
                if (menuItem.itemId  == R.id.action_select_mixer){
                    if(fragment is TabletListFragment){
                        (fragment.requireActivity() as MainActivity).saveTabletMixer(tabletMixer)
                        fragment.selectedTabletInFragment = tabletMixer
                        selectedTablet = tabletMixer
                        previousTablet = null
                        notifyDataSetChanged()
                    }
                }
                true
            }

            popup.show()
        }

        holder.btnEditTablet.setOnClickListener{
            if ( fragment is TabletListFragment){
                fragment.goToConfigMixer(tabletMixer,false)
            }
        }

        holder.btnDeleteTablet.setOnClickListener{
            if (fragment is TabletListFragment) {
                fragment.deleteTabletMixer(tabletMixer)
            }
        }


    }

    private fun selectTabletMixer(mixer: TabletMixer) {
        if(fragment is TabletListFragment){
            fragment.selectTabletMixer(mixer)
            selectedTablet = mixer
            previousTablet = null
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return filteredTabletMixers.size
    }

    fun tabletMixerList(list: MutableList<TabletMixer>){
        tabletMixers = list
        filteredTabletMixers = list
        notifyDataSetChanged()
    }

    fun statusConnexion(tabletMixer: TabletMixer, connected: Boolean){//TODO revisar linked status
        val index = tabletMixers.indexOf(tabletMixer)
        val indexFiltered = filteredTabletMixers.indexOf(tabletMixer)

        if (indexFiltered != -1 && index != -1){
//            tabletMixers[index].linked = connected
//            filteredTabletMixers[indexFiltered].linked = connected
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredTabletMixers = if (charString.isEmpty()) tabletMixers else {
                    val filteredList = ArrayList<TabletMixer>()
                    tabletMixers
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.mixerName.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredTabletMixers }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredTabletMixers = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<TabletMixer>
                notifyDataSetChanged()
            }
        }
    }

    fun setWeight(weight : Long){
        this.weight = weight
        notifyDataSetChanged()
    }


    private fun alertTabletMixerNull(msg: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(fragment.requireActivity())
        builder.setTitle(fragment.getString(R.string.warning))
        builder.setMessage(msg)
        builder.setPositiveButton(fragment.getString(R.string.aceptar)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}