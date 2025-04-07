package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentUserListBinding
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.UserData
import com.basculasmagris.visorremotomixer.view.adapter.UserAdapter
import com.basculasmagris.visorremotomixer.viewmodel.UserRemoteViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModel
import com.basculasmagris.visorremotomixer.viewmodel.UserViewModelFactory

class UserListFragment : Fragment() {

    private lateinit var mBinding: FragmentUserListBinding

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as SpiMixerVRApplication).userRepository)
    }

    private var mLocalUsers: List<User>? = null
    private var mUserViewModelRemote: UserRemoteViewModel? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mUserViewModel.allUserList) {
            if (it != null) {
                liveDataMerger.value = UserData(it)
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
                    is UserData -> mLocalUsers = it.users //.filter { user -> user.codeRole != 1 }
                    else -> {}
                }

                if (mLocalUsers != null ) {
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

    }

    private fun getLocalUser(){
        mBinding.rvUsersList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val userAdapter =  UserAdapter(this@UserListFragment)
        mBinding.rvUsersList.adapter = userAdapter
        mUserViewModel.allUserList.observe(viewLifecycleOwner) { users ->
            users.let{ _users ->
                if (_users?.isNotEmpty() == true){
                    mLocalUsers = _users //.filter { user -> user.codeRole != 1 }
                    _users.filter { user ->
                        user.archiveDate.isNullOrEmpty()
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
        mUserViewModelRemote = null
        mLocalUsers = null
    }

}