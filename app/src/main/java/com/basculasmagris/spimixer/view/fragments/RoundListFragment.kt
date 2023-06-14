package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentRoundListBinding
import com.basculasmagris.visorremotomixer.model.entities.Round
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.RoundData
import com.basculasmagris.visorremotomixer.view.adapter.RoundAdapter
import com.basculasmagris.visorremotomixer.viewmodel.CorralRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModelFactory

class RoundListFragment : Fragment() {

    private lateinit var mBinding: FragmentRoundListBinding

    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((requireActivity().application as SpiMixerApplication).roundRepository)
    }

    private var mLocalRounds: List<Round>? = null

    private var mRoundViewModelRemote: RoundRemoteViewModel? = null
    private var mCorralViewModelRemote: CorralRemoteViewModel? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mRoundViewModel.allRoundList) {
            if (it != null) {
                liveDataMerger.value = RoundData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(viewLifecycleOwner, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is RoundData -> mLocalRounds = it.rounds
                    else -> {}
                }

                if (mLocalRounds != null) {
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentRoundListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocalData()
        getLocalRound()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_round_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_round)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvRoundsList.adapter as RoundAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvRoundsList.adapter as RoundAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_round -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateRoundActivity::class.java))
                        goToAddUpdateRound()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun getLocalRound(){

        mBinding.rvRoundsList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val roundAdapter =  RoundAdapter(this@RoundListFragment)
        mBinding.rvRoundsList.adapter = roundAdapter

        mRoundViewModel.allRoundList.observe(viewLifecycleOwner) { rounds ->
            rounds.let{ _rounds ->
                if (_rounds?.isNotEmpty() == true){
                    mLocalRounds = _rounds
                    _rounds.filter { round ->
                        round.archiveDate.isNullOrEmpty()
                    }.let {

                        if (it.isEmpty()){
                            mBinding.rvRoundsList.visibility = View.GONE
                            mBinding.tvNoData.visibility = View.VISIBLE
                        } else {
                            Log.i("SYNC", "Se actualiza UI Roundas: ${it.size} ")
                            mBinding.rvRoundsList.visibility = View.VISIBLE
                            mBinding.tvNoData.visibility = View.GONE
                            roundAdapter.roundList(it)
                        }
                    }
                } else {
                    mBinding.rvRoundsList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToRoundDetails(round: Round){
        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToRoundDetailFragment(
            round
        ))
    }

    fun goToAddUpdateRound(){
        findNavController().navigate(RoundListFragmentDirections.actionRoundListFragmentToAddUpdateRoundActivity())
    }

    fun deleteRound(round: Round){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_round))
        builder.setMessage(resources.getString(R.string.msg_delete_round_dialog, round.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mRoundViewModel.delete(round)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanObservers()
    }

    private fun cleanObservers(){
        mRoundViewModelRemote?.roundsResponse?.value = null
        mRoundViewModelRemote?.roundsLoadingError?.value = null
        mRoundViewModelRemote?.loadRound?.value = null
        mRoundViewModelRemote?.addRoundsResponse?.value = null
        mRoundViewModelRemote?.addRoundErrorResponse?.value = null
        mRoundViewModelRemote?.addRoundsLoad?.value = null
        mRoundViewModelRemote?.updateRoundsResponse?.value = null
        mRoundViewModelRemote?.updateRoundsErrorResponse?.value = null
        mRoundViewModelRemote?.updateRoundsLoad?.value = null

        mCorralViewModelRemote?.corralsResponse?.value = null
        mCorralViewModelRemote?.corralsLoadingError?.value = null
        mCorralViewModelRemote?.loadCorral?.value = null
        mCorralViewModelRemote?.addCorralsResponse?.value = null
        mCorralViewModelRemote?.addCorralErrorResponse?.value = null
        mCorralViewModelRemote?.addCorralsLoad?.value = null
        mCorralViewModelRemote?.updateCorralsResponse?.value = null
        mCorralViewModelRemote?.updateCorralsErrorResponse?.value = null
        mCorralViewModelRemote?.updateCorralsLoad?.value = null

        mRoundViewModelRemote = null
        mCorralViewModelRemote = null
        mLocalRounds = null
    }

}