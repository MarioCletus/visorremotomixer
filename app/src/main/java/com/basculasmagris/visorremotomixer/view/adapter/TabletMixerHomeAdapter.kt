package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer

class TabletMixerHomeAdapter(
    private var tabletMixers: List<TabletMixer>,
    private val onItemPressed: (TabletMixer) -> Unit
) : RecyclerView.Adapter<TabletMixerHomeAdapter.TabletMixerViewHolder>() {

    inner class TabletMixerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTablet: TextView = itemView.findViewById(R.id.tvTablet)
        val tvMixer: TextView = itemView.findViewById(R.id.tvMixer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabletMixerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tablet_mixer_home, parent, false)

        return TabletMixerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabletMixerViewHolder, position: Int) {
        val tabletMixer = tabletMixers[position]

        holder.tvTablet.text = tabletMixer.name
        holder.tvMixer.text = tabletMixer.mixerName

        holder.itemView.setOnClickListener {
            onItemPressed(tabletMixer)
        }

        holder.itemView.isSelected = tabletMixer.id == selectedId
    }

    override fun getItemCount(): Int = tabletMixers.size

    fun updateList(newList: List<TabletMixer>) {
        tabletMixers = newList
        notifyDataSetChanged()
    }

    private var selectedId: Long? = null


}