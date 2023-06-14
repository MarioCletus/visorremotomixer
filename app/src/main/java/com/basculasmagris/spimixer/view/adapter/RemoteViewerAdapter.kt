package com.basculasmagris.visorremotomixer.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemRemoteViewerLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.AddUpdateRemoteViewerActivity
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.fragments.RemoteViewerListFragment

class RemoteViewerAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<RemoteViewerAdapter.ViewHolder>(),
    Filterable {

    private var remoteViewers: MutableList<RemoteViewer>  = ArrayList()
    private var filteredRemoteViewers: MutableList<RemoteViewer>  = ArrayList()

    class ViewHolder (view: ItemRemoteViewerLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvRemoteViewerTitle = view.tvRemoteViewerTitle
        val btnEditRemoteViewer = view.btnEditRemoteViewer
        val btnDeleteRemoteViewer = view.btnDeleteRemoteViewer
        val etRemoteViewerDescription = view.etRemoteViewerDescription
        val tvMac = view.tvMac

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRemoteViewerLayoutBinding = ItemRemoteViewerLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val remoteViewer = filteredRemoteViewers[position]
        holder.tvRemoteViewerTitle.text = remoteViewer.name
        holder.etRemoteViewerDescription.text = remoteViewer.description.ifEmpty { fragment.getString(R.string.lbl_no_description_short) }
        holder.tvMac.text = remoteViewer.mac

        holder.itemView.setOnClickListener {
            if (fragment is RemoteViewerListFragment) {
                if(!fragment.configRemoteViewer(remoteViewer,true)){
                    fragment.linkDevice(remoteViewer)
                }
            }
        }

        holder.itemView.setOnLongClickListener{
            if(fragment is RemoteViewerListFragment){
                (fragment.requireActivity() as MainActivity).saveRemoteViewer(remoteViewer)
                fragment.menu?.findItem(R.id.menu_selected_remote_viewer)?.title = "   " + remoteViewer.name
            }
            return@setOnLongClickListener true
        }

        holder.btnEditRemoteViewer.setOnClickListener{
            val intent = Intent(fragment.requireActivity(), AddUpdateRemoteViewerActivity::class.java)
            intent.putExtra(Constants.EXTRA_REMOTE_VIEWER_DETAILS, remoteViewer)
            fragment.requireActivity().startActivity(intent)
            if ( fragment is RemoteViewerListFragment){
                fragment.configRemoteViewer(remoteViewer,false)
            }
        }

        holder.btnDeleteRemoteViewer.setOnClickListener{
            if (fragment is RemoteViewerListFragment) {
                fragment.deleteRemoteViewer(remoteViewer)
            }
        }


    }

    override fun getItemCount(): Int {
        return filteredRemoteViewers.size
    }

    fun remoteViewerList(list: MutableList<RemoteViewer>){
        remoteViewers = list
        filteredRemoteViewers = list
        notifyDataSetChanged()
    }

    fun statusConnexion(remoteViewer: RemoteViewer, connected: Boolean){
        val index = remoteViewers.indexOf(remoteViewer)
        val indexFiltered = filteredRemoteViewers.indexOf(remoteViewer)

        if (indexFiltered != -1 && index != -1){
            remoteViewers[index].linked = connected
            filteredRemoteViewers[indexFiltered].linked = connected
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredRemoteViewers = if (charString.isEmpty()) remoteViewers else {
                    val filteredList = ArrayList<RemoteViewer>()
                    remoteViewers
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredRemoteViewers }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredRemoteViewers = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<RemoteViewer>
                notifyDataSetChanged()
            }
        }
    }

    private fun alertRemoteViewerNull(msg: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(fragment.requireActivity())
        builder.setTitle("Alerta")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}