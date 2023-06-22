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
import com.basculasmagris.visorremotomixer.databinding.FragmentUserListBinding
import com.basculasmagris.visorremotomixer.model.entities.Corral
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.view.activities.CorralData
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.UserData
import com.basculasmagris.visorremotomixer.view.adapter.UserAdapter
import com.basculasmagris.visorremotomixer.viewmodel.*

class UserListFragment : Fragment() {

    private lateinit var mBinding: FragmentUserListBinding

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as SpiMixerApplication).userRepository)
    }

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as SpiMixerApplication).corralRepository)
    }

    private var mLocalUsers: List<User>? = null
    private var mLocalCorrals: List<Corral>? = null

    private var mUserViewModelRemote: UserRemoteViewModel? = null
    private var mCorralViewModelRemote: CorralRemoteViewModel? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mUserViewModel.allUserList) {
            if (it != null) {
                liveDataMerger.value = UserData(it)
            }
        }
        liveDataMerger.addSource(mCorralViewModel.allCorralList) {
            if (it != null) {
                liveDataMerger.value = CorralData(it)
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
                    is UserData -> mLocalUsers = it.users.filter { user -> user.codeRole != 1 }
                    is CorralData -> mLocalCorrals = it.corrals
                    else -> {}
                }

                if (mLocalUsers != null && mLocalCorrals != null) {
                    Log.i("Sync", "EST Cantidad de corrales: ${mLocalCorrals?.size}")
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
        mBinding = FragmentUserListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocalData()
        getLocalUser()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_user_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_user)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvUsersList.adapter as UserAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvUsersList.adapter as UserAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_user -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateUserActivity::class.java))
                        goToAddUpdateUser()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Content
        //getLocalUser()
    }

    private fun getLocalUser(){
        mBinding.rvUsersList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val userAdapter =  UserAdapter(this@UserListFragment)
        mBinding.rvUsersList.adapter = userAdapter
        mUserViewModel.allUserList.observe(viewLifecycleOwner) { users ->
            users.let{ _users ->
                if (_users?.isNotEmpty() == true){
                    mLocalUsers = _users.filter { user -> user.codeRole != 1 }
                    _users.filter { user ->
                        user.archiveDate.isNullOrEmpty() && user.codeRole != 1
                    }.let {

                        if (it.isEmpty()){
                            mBinding.rvUsersList.visibility = View.GONE
                            mBinding.tvNoData.visibility = View.VISIBLE
                        } else {
                            Log.i("SYNC", "Se actualiza UI establecimientos: ${it.size} ")
                            mBinding.rvUsersList.visibility = View.VISIBLE
                            mBinding.tvNoData.visibility = View.GONE
                            userAdapter.userList(it)
                        }

                    }
                } else {
                    mBinding.rvUsersList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }

        mCorralViewModel.allCorralList.observe(viewLifecycleOwner) { corrals ->
            corrals.let{ _corrals ->
                if (_corrals?.isNotEmpty() == true){
                    mLocalCorrals = _corrals
                    _corrals.filter { corral ->
                        corral.archiveDate.isNullOrEmpty()
                    }.let {
                        Log.i("SYNC", "Se actualiza lista ade Corral: ${mLocalCorrals?.size}")

                        mLocalUsers?.let { _users ->
                            (mBinding.rvUsersList.adapter as UserAdapter).userList(_users.filter { user ->
                                user.archiveDate.isNullOrEmpty()
                            })
                        }
                    }
                }
            }
        }
    }

    fun goToAddUpdateUser(){
        findNavController().navigate(UserListFragmentDirections.actionUserListFragmentToAddUpdateUserActivity())
    }

    fun deleteUser(user: User){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_user))
        builder.setMessage(resources.getString(R.string.msg_delete_user_dialog, user.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mUserViewModel.delete(user)
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
        mUserViewModelRemote?.usersResponse?.value = null
        mUserViewModelRemote?.usersLoadingError?.value = null
        mUserViewModelRemote?.loadUser?.value = null
        mUserViewModelRemote?.addUsersResponse?.value = null
        mUserViewModelRemote?.addUserErrorResponse?.value = null
        mUserViewModelRemote?.addUsersLoad?.value = null
        mUserViewModelRemote?.updateUsersResponse?.value = null
        mUserViewModelRemote?.updateUsersErrorResponse?.value = null
        mUserViewModelRemote?.updateUsersLoad?.value = null

        mCorralViewModelRemote?.corralsResponse?.value = null
        mCorralViewModelRemote?.corralsLoadingError?.value = null
        mCorralViewModelRemote?.loadCorral?.value = null
        mCorralViewModelRemote?.addCorralsResponse?.value = null
        mCorralViewModelRemote?.addCorralErrorResponse?.value = null
        mCorralViewModelRemote?.addCorralsLoad?.value = null
        mCorralViewModelRemote?.updateCorralsResponse?.value = null
        mCorralViewModelRemote?.updateCorralsErrorResponse?.value = null
        mCorralViewModelRemote?.updateCorralsLoad?.value = null

        mUserViewModelRemote = null
        mCorralViewModelRemote = null
        mLocalUsers = null
        mLocalCorrals = null
    }

}