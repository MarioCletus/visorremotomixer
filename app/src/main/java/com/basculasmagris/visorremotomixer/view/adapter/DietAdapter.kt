package com.basculasmagris.visorremotomixer.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ItemDietLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.*
import com.basculasmagris.visorremotomixer.view.fragments.DietListFragment
import com.basculasmagris.visorremotomixer.viewmodel.DietViewModel
import com.basculasmagris.visorremotomixer.viewmodel.DietViewModelFactory

class DietAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<DietAdapter.ViewHolder>(),
    Filterable {

    private var diets: List<Diet> = listOf()
    private var filteredDiets: List<Diet> = listOf()
    private var mLocalDietProductDetail: List<DietProductDetail>? = null

    private val mDietViewModel: DietViewModel by fragment.viewModels {
        DietViewModelFactory((fragment.requireActivity().application as SpiMixerApplication).dietRepository)
    }

    private fun fetchLocalData(dietId: Long): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mDietViewModel.activeDietList) {
            if (it != null) {
                liveDataMerger.value = DietData(it)
            }
        }
        liveDataMerger.addSource(mDietViewModel.getProductsBy(dietId)) {
            Log.i("Fat", "DietAdapter - addSource 01 ${it?.size}")
            if (it != null) {
                Log.i("Fat", "DietAdapter - addSource ${it.size}")
                liveDataMerger.value = DietProductDetailData(it)
            }
        }
        return liveDataMerger
    }
    private fun getLocalData(holder: ViewHolder, diet: Diet){
        // Sync local data
        Log.i("Fat", "DietAdapter - getLocalData")
        val liveData = fetchLocalData(diet.id)
        liveData.observe(fragment.requireActivity(), object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is DietData -> diets = it.diets
                    is DietProductDetailData -> mLocalDietProductDetail = it.dietProductsDetail
                    else -> {}
                }

                Log.i("Fat", "DietAdapter - diets ${diets.size}")
                Log.i("Fat", "DietAdapter - mLocalDietProductDetail ${mLocalDietProductDetail?.size}")
                if (mLocalDietProductDetail != null) {
                    setProductsData(holder, diet)
                    liveData.removeObserver(this)
                    liveData.value = null
                    liveData.removeSource(mDietViewModel.activeDietList)
                    liveData.removeSource(mDietViewModel.getProductsBy(diet.id))
                }
            }
        })
    }

    private fun setProductsData(holder: ViewHolder, diet: Diet){
        Log.i("Fat", "DietAdapter - setProductsData ${mLocalDietProductDetail?.size}")
        holder.tvProductQuantity.text = mLocalDietProductDetail?.size.toString()
        mLocalDietProductDetail = null
    }

    class ViewHolder (view: ItemDietLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvDietTitle = view.tvDietTitle
        val ibMore = view.ibMore
        val btnEditDiet = view.btnEditDiet
        val btnDeleteDiet = view.btnDeleteDiet
        val tvDietDescription = view.tvDietDescription
        val tvProductQuantity = view.tvProductQuantity

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDietLayoutBinding = ItemDietLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diet = filteredDiets[position]
        getLocalData(holder, diet)
        /*
        if (mLocalDietProducts == null){
            getLocalData(holder, diet)
        } else {
            setProductsData(holder, diet)
        }*/
        holder.tvDietTitle.text = diet.name
        holder.tvDietDescription.text = diet.description.ifEmpty { fragment.getString(R.string.lbl_no_description_short) }
        holder.itemView.setOnClickListener {
            if (fragment is DietListFragment){
                fragment.goToDietDetails(diet)
            }
        }

//        holder.ibMore.setOnClickListener{
//            val popup =  PopupMenu(fragment.context, holder.ibMore)
//            popup.menuInflater.inflate(R.menu.menu_adapter_diet, popup.menu)
//
//            // Si el dieto está sincronizado no se permite el borrado.
//            if (diet.remoteId != 0L) {
//                popup.menu.getItem(1).isVisible = false
//            }
//
//            popup.setOnMenuItemClickListener {
//                if (it.itemId  == R.id.action_edit_diet){
//                    val intent = Intent(fragment.requireActivity(), AddUpdateDietActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_DIET_DETAILS, diet)
//                    fragment.requireActivity().startActivity(intent)
//                } else if (it.itemId == R.id.action_delete_diet){
//                    if (fragment is DietListFragment) {
//                        fragment.deleteDiet(diet)
//                    }
//                }
//                true
//            }
//
//            popup.show()
//        }
//
//            holder.btnEditDiet.setOnClickListener{
//                val intent = Intent(fragment.requireActivity(), AddUpdateDietActivity::class.java)
//                intent.putExtra(Constants.EXTRA_DIET_DETAILS, diet)
//                fragment.requireActivity().startActivity(intent)
//            }
//            holder.btnDeleteDiet.setOnClickListener{
//                if (fragment is DietListFragment) {
//                    fragment.deleteDiet(diet)
//                }
//            }
    }

    override fun getItemCount(): Int {
        return filteredDiets.size
    }

    fun dietList(list: List<Diet>){
        diets = list
        filteredDiets = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredDiets = if (charString.isEmpty()) diets else {
                    val filteredList = ArrayList<Diet>()
                    diets
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredDiets }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredDiets = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Diet>
                notifyDataSetChanged()
            }
        }
    }
}