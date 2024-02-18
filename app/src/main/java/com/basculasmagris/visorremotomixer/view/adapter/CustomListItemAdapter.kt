package com.basculasmagris.visorremotomixer.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemCustomListBinding
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.fragments.MixerListFragment

class CustomListItemAdapter (
    private val activity: Activity,
    private var listItems: List<CustomListItem>,
    private val selection: String) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root){
        val tvTxt = view.tvText
        val tvDescription = view.tvDescription
        val iconItem = view.iconItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvTxt.text = item.name
        if (item.resourceIcon > 0){
            holder.iconItem.setImageDrawable(activity.getDrawable(item.resourceIcon))
        } else {
            holder.iconItem.setImageDrawable(activity.getDrawable(R.drawable.ic_adjust))
        }

        holder.tvDescription.text = item.description.ifEmpty { activity.getString(R.string.no_description) }

        holder.itemView.setOnClickListener{

            if (activity is AddUpdateRoundActivity){
                activity.selectedListItem(item, selection)
            }

            if (activity is LoginActivity){
                activity.selectedListItem(item, selection)
            }

            if (activity is AddUpdateUserActivity){
                activity.selectedListItem(item, selection)
            }

            if (activity is MixerConfigActivity){
                activity.selectedListItem(item, selection)
            }

        }
    }


    override fun getItemCount(): Int {
        return listItems.size
    }

    fun updateList(item: CustomListItem) {
        (listItems as ArrayList).add(item)
        notifyDataSetChanged()
    }
}

class CustomListItemAdapterFragment (
    private val fragment: Fragment,
    private val listItems: List<CustomListItem>,
    private val selection: String) : RecyclerView.Adapter<CustomListItemAdapterFragment.ViewHolder>() {

    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root){
        val tvTxt = view.tvText
        val tvDescription = view.tvDescription
        val iconItem = view.iconItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(fragment.requireActivity()), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvTxt.text = item.name
        if (item.resourceIcon > 0){
            holder.iconItem.setImageDrawable(fragment.requireActivity().getDrawable(item.resourceIcon))
        } else {
            holder.iconItem.setImageDrawable(fragment.requireActivity().getDrawable(R.drawable.ic_adjust))
        }

        holder.tvDescription.text = item.description.ifEmpty { fragment.requireActivity().getString(R.string.no_description) }

        holder.itemView.setOnClickListener{
            if (fragment is MixerListFragment){
                fragment.selectedListItem(item, selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }


}

class CustomDynamicListItemAdapterFragment (
    private val fragment: Fragment,
    private val listItems: MutableList<CustomListItem>,
    private val selection: String) : RecyclerView.Adapter<CustomDynamicListItemAdapterFragment.ViewHolder>() {

    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root){
        val tvTxt = view.tvText
        val tvDescription = view.tvDescription
        val iconItem = view.iconItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(fragment.requireActivity()), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvTxt.text = item.name
        if (item.resourceIcon > 0){
            holder.iconItem.setImageDrawable(fragment.requireActivity().getDrawable(item.resourceIcon))
        } else {
            holder.iconItem.setImageDrawable(fragment.requireActivity().getDrawable(R.drawable.ic_adjust))
        }

        holder.tvDescription.text = item.description.ifEmpty { fragment.requireActivity().getString(R.string.no_description) }

        holder.itemView.setOnClickListener{
            if (fragment is MixerListFragment){
                fragment.selectedListItem(item, selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun updateList(itemToAdd: CustomListItem){
        listItems.add(itemToAdd)
        notifyDataSetChanged()
    }
}

class CustomListItem {
    var id: Long = 0
    var remoteId: Long = 0
    var name: String = ""
    var description = ""
    var resourceIcon: Int = 0


    constructor(id: Long, remoteId: Long, name: String){
        this.id = id
        this.remoteId = remoteId
        this.name = name
    }

    constructor(id: Long, remoteId: Long, name: String, description: String, resourceIcon: Int){
        this.id = id
        this.remoteId = remoteId
        this.name = name
        this.description = description
        this.resourceIcon = resourceIcon
    }
}