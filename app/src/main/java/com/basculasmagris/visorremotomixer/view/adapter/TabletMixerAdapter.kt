package com.basculasmagris.visorremotomixer.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemTabletMixerLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.AddUpdateTabletMixerActivity
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.fragments.TabletMixerListFragment

class TabletMixerAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<TabletMixerAdapter.ViewHolder>(),
    Filterable {

    private var tabletMixers: MutableList<TabletMixer>  = ArrayList()
    private var filteredTabletMixers: MutableList<TabletMixer>  = ArrayList()
    private var weight : Long = 0L

    class ViewHolder (view: ItemTabletMixerLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvTabletMixerTitle = view.tvTableMixerTitle
        val btnEditTabletMixer = view.btnEditTabletMixer
        val btnDeleteTabletMixer = view.btnDeleteTabletMixer
        val etTabletMixerDescription = view.etTabletMixerDescription
        val tvWeight                = view.tvTableMixerWeight

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTabletMixerLayoutBinding = ItemTabletMixerLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tabletMixer = filteredTabletMixers[position]
        holder.tvTabletMixerTitle.text = tabletMixer.name
        holder.etTabletMixerDescription.text = tabletMixer.description.ifEmpty { fragment.getString(R.string.lbl_no_description_short) }
        holder.tvWeight.text = "${weight}Kg"

        holder.itemView.setOnClickListener {
            if (fragment is TabletMixerListFragment) {
                fragment.goToRemoteMixerFragment(tabletMixer)
            }
        }

        holder.itemView.setOnLongClickListener{
            if(fragment is TabletMixerListFragment){
                (fragment.requireActivity() as MainActivity).saveTabletMixer(tabletMixer)
                fragment.menu?.findItem(R.id.menu_selected_tabler_mixer)?.title = "   " + tabletMixer.name
            }
            return@setOnLongClickListener true
        }

        holder.btnEditTabletMixer.setOnClickListener{
            val intent = Intent(fragment.requireActivity(), AddUpdateTabletMixerActivity::class.java)
            intent.putExtra(Constants.EXTRA_TABLET_MIXER_DETAILS, tabletMixer)
            fragment.requireActivity().startActivity(intent)
            if ( fragment is TabletMixerListFragment){
                fragment.configTabletMixer(tabletMixer,false)
            }
        }

        holder.btnDeleteTabletMixer.setOnClickListener{
            if (fragment is TabletMixerListFragment) {
                fragment.deleteTabletMixer(tabletMixer)
            }
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

    fun statusConnexion(tabletMixer: TabletMixer, connected: Boolean){
        val index = tabletMixers.indexOf(tabletMixer)
        val indexFiltered = filteredTabletMixers.indexOf(tabletMixer)

        if (indexFiltered != -1 && index != -1){
            tabletMixers[index].linked = connected
            filteredTabletMixers[indexFiltered].linked = connected
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
                                    (it.description.lowercase().contains(charString.lowercase()))

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
        builder.setTitle("Alerta")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}