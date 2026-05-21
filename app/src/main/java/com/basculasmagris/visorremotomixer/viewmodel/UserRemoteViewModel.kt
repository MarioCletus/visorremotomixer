package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.UserApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.withContext

class UserRemoteViewModel : ViewModel() {

    private val userApiService = UserApiService()


    // Get
    val loadUser = MutableLiveData<Boolean>()
    var usersResponse = MutableLiveData<MutableList<UserRemote>?>()
    val usersLoadingError = MutableLiveData<Boolean>()

    suspend fun getUsersFromAPI_SUSPEND(): MutableList<UserRemote> = withContext(Dispatchers.Main) {
        loadUser.value = true
        usersLoadingError.value = false
        try {
            val value = userApiService.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .await()

            usersResponse.value = value
            loadUser.value = false
            value
        } catch (t: Throwable) {
            loadUser.value = false
            usersLoadingError.value = true
            throw t
        }
    }


}