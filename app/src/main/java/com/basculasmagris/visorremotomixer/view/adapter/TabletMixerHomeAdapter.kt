package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer

class TabletMixerHomeAdapter(
    initialList: List<TabletMixer>,
    private val onItemPressed: (TabletMixer) -> Unit,
    /** Llamado cuando el usuario termina de arrastrar. Recibe la lista reordenada. */
    private val onOrderChanged: (List<TabletMixer>) -> Unit
) : RecyclerView.Adapter<TabletMixerHomeAdapter.TabletMixerViewHolder>() {

    private val items: MutableList<TabletMixer> = initialList.toMutableList()

    /** Altura de cada item en px. -1 = usa wrap_content. */
    private var itemHeightPx: Int = -1

    fun setItemHeight(heightPx: Int) {
        itemHeightPx = heightPx
    }

    inner class TabletMixerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTablet: TextView = itemView.findViewById(R.id.tvTablet)
        val tvMixer: TextView  = itemView.findViewById(R.id.tvMixer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabletMixerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tablet_mixer_home, parent, false)
        return TabletMixerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabletMixerViewHolder, position: Int) {
        val tablet = items[position]
        holder.tvTablet.text = tablet.name
        holder.tvMixer.text  = tablet.mixerName
        holder.itemView.isSelected = tablet.id == selectedId
        holder.itemView.setOnClickListener { onItemPressed(tablet) }

        // Aplicar altura dinámica (set cada bind para cubrir vistas recicladas)
        val params = holder.itemView.layoutParams
        params.height = if (itemHeightPx > 0) itemHeightPx else ViewGroup.LayoutParams.WRAP_CONTENT
        holder.itemView.layoutParams = params
    }

    override fun getItemCount(): Int = items.size

    fun getItemAt(position: Int): TabletMixer? = items.getOrNull(position)

    // ── Drag ────────────────────────────────────────────────────────────────

    fun onItemMoved(from: Int, to: Int) {
        val moved = items.removeAt(from)
        items.add(to, moved)
        notifyItemMoved(from, to)
    }

    fun onDragEnded() {
        val updated = items.mapIndexed { i, t -> t.copy(sortOrder = i) }
        updated.forEachIndexed { i, t -> items[i] = t }
        onOrderChanged(updated)
    }

    // ── Actualización desde LiveData ─────────────────────────────────────────

    fun updateList(newList: List<TabletMixer>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    // ── Selección activa ─────────────────────────────────────────────────────

    private var selectedId: Long? = null

    fun setSelected(id: Long?) {
        selectedId = id
        notifyDataSetChanged()
    }
}
