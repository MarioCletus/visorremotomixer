package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.model.network.UserApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class UserRemoteViewModel : ViewModel() {

    private val userApiService = UserApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadUser = MutableLiveData<Boolean>()
    var usersResponse = MutableLiveData<MutableList<UserRemote>?>()
    val usersLoadingError = MutableLiveData<Boolean>()

    //Post
    val addUsersLoad = MutableLiveData<Boolean>()
    val addUsersResponse = MutableLiveData<UserRemote?>()
    val addUserErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateUsersLoad = MutableLiveData<Boolean>()
    val updateUsersResponse = MutableLiveData<UserRemote?>()
    val updateUsersErrorResponse = MutableLiveData<Boolean>()

    fun getUsersFromAPI() {
        loadUser.value = true
        compositeDisposable.add(
            userApiService.getUsers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<UserRemote>>() {
                    override fun onSuccess(value: MutableList<UserRemote>?) {
                        usersResponse.value = value
                        usersLoadingError.value = false
                        loadUser.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadUser.value = false
                        usersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addUserFromAPI(user: User){
        addUsersLoad.value = true
        compositeDisposable.add(
            userApiService.addUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserRemote>() {
                    override fun onSuccess(value: UserRemote?) {
                        addUsersResponse.value = value
                        addUserErrorResponse.value = false
                        addUsersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addUsersLoad.value = false
                        addUserErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateUserFromAPI(user: User){
        updateUsersLoad.value = true
        compositeDisposable.add(
            userApiService.updateUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserRemote>() {
                    override fun onSuccess(value: UserRemote?) {
                        updateUsersResponse.value = value
                        updateUsersErrorResponse.value = false
                        updateUsersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateUsersLoad.value = false
                        updateUsersErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}