package com.basculasmagris.visorremotomixer.view.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer

class TabletMixerHomeAdapter(
    initialList: List<TabletMixer>,
    private val onItemPressed: (TabletMixer) -> Unit,
    private val onDeletePressed: (TabletMixer) -> Unit,
    /** Llamado cuando el usuario termina de arrastrar. Recibe la lista reordenada. */
    private val onOrderChanged: (List<TabletMixer>) -> Unit
) : RecyclerView.Adapter<TabletMixerHomeAdapter.TabletMixerViewHolder>() {

    // Lista mutable interna — el adapter la gestiona durante el drag
    private val items: MutableList<TabletMixer> = initialList.toMutableList()

    private var itemTouchHelper: ItemTouchHelper? = null

    /** Llamado desde HomeFragment para entregar la referencia al ItemTouchHelper. */
    fun attachTouchHelper(helper: ItemTouchHelper) {
        itemTouchHelper = helper
    }

    inner class TabletMixerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTablet: TextView    = itemView.findViewById(R.id.tvTablet)
        val tvMixer: TextView     = itemView.findViewById(R.id.tvMixer)
        val ivDragHandle: ImageView = itemView.findViewById(R.id.ivDragHandle)
        val ivDelete: ImageView   = itemView.findViewById(R.id.ivDelete)
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

        // Tap en la tarjeta → seleccionar/conectar
        holder.itemView.setOnClickListener { onItemPressed(tablet) }

        // Tap en delete → callback al fragment para mostrar dialogo
        holder.ivDelete.setOnClickListener { onDeletePressed(tablet) }

        // Toque en el drag handle → iniciar drag
        holder.ivDragHandle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                itemTouchHelper?.startDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int = items.size

    // ── Drag callbacks (llamados por ItemTouchHelper.Callback) ───────────────

    /** Mueve el item en la lista interna y notifica al RecyclerView. */
    fun onItemMoved(from: Int, to: Int) {
        val moved = items.removeAt(from)
        items.add(to, moved)
        notifyItemMoved(from, to)
    }

    /** Llamado al soltar. Asigna sortOrder según posición y persiste. */
    fun onDragEnded() {
        val updated = items.mapIndexed { i, t -> t.copy(sortOrder = i) }
        // Actualizar la lista interna con los nuevos sortOrders
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
