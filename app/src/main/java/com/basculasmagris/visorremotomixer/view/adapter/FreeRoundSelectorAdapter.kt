package com.basculasmagris.visorremotomixer.view.adapter

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R

/**
 * Adapter genérico para el diálogo de selección de producto/corral en ronda libre.
 * Mismo comportamiento que en spi-mixer:
 *   - 1er tap → resalta la card (feedback visual)
 *   - 2do tap (≤ 500ms, misma posición) → confirma la selección
 *   - Sin 2do tap → la card vuelve a normal sin hacer nada
 */
class FreeRoundSelectorAdapter<T>(
    private val items: List<T>,
    private val nameProvider: (T) -> String,
    private val descProvider: (T) -> String = { "" },
    private val onDoubleClick: (T) -> Unit
) : RecyclerView.Adapter<FreeRoundSelectorAdapter<T>.ViewHolder>() {

    companion object {
        private const val DOUBLE_TAP_MS = 500L
    }

    private var pendingPosition = -1
    private val handler = Handler(Looper.getMainLooper())
    private var resetRunnable: Runnable? = null

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView        = itemView.findViewById(R.id.tv_selector_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_selector_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_free_round_selector, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = nameProvider(item)
        val desc = descProvider(item)
        if (desc.isNotEmpty()) {
            holder.tvDescription.text = desc
            holder.tvDescription.visibility = android.view.View.VISIBLE
        } else {
            holder.tvDescription.visibility = android.view.View.GONE
        }
        updateCardHighlight(holder, position == pendingPosition)

        holder.itemView.setOnLongClickListener(null)
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos == RecyclerView.NO_ID.toInt()) return@setOnClickListener
            if (pendingPosition == pos) {
                cancelPendingReset()
                pendingPosition = -1
                notifyItemChanged(pos)
                onDoubleClick(item)
            } else {
                val prev = pendingPosition
                pendingPosition = pos
                if (prev >= 0) notifyItemChanged(prev)
                notifyItemChanged(pos)
                cancelPendingReset()
                resetRunnable = Runnable {
                    pendingPosition = -1
                    if (pos < itemCount) notifyItemChanged(pos)
                }.also { handler.postDelayed(it, DOUBLE_TAP_MS) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        updateCardHighlight(holder, false)
    }

    private fun cancelPendingReset() {
        resetRunnable?.let { handler.removeCallbacks(it) }
        resetRunnable = null
    }

    private fun updateCardHighlight(holder: ViewHolder, highlighted: Boolean) {
        val ctx = holder.itemView.context
        if (highlighted) {
            (holder.itemView as? com.google.android.material.card.MaterialCardView)
                ?.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.color_acent_green))
            holder.tvName.setTextColor(Color.WHITE)
            holder.tvDescription.setTextColor(Color.WHITE)
        } else {
            (holder.itemView as? com.google.android.material.card.MaterialCardView)
                ?.setCardBackgroundColor(ContextCompat.getColor(ctx, android.R.color.white))
            holder.tvName.setTextColor(Color.BLACK)
            holder.tvDescription.setTextColor(ContextCompat.getColor(ctx, R.color.color_medium_grey))
        }
    }
}
