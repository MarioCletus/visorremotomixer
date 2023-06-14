package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewerRemote
import com.basculasmagris.visorremotomixer.model.network.RemoteViewerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RemoteViewerRemoteViewModel : ViewModel() {

    private val remoteViewerApiService = RemoteViewerApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadRemoteViewer = MutableLiveData<Boolean>()
    val remoteViewersResponse = MutableLiveData<MutableList<RemoteViewerRemote>?>()
    val remoteViewersLoadingError = MutableLiveData<Boolean>()

    //Post
    val addRemoteViewersLoad = MutableLiveData<Boolean>()
    val addRemoteViewersResponse = MutableLiveData<RemoteViewerRemote?>()
    val addRemoteViewerErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateRemoteViewersLoad = MutableLiveData<Boolean>()
    val updateRemoteViewersResponse = MutableLiveData<RemoteViewerRemote?>()
    val updateRemoteViewersErrorResponse = MutableLiveData<Boolean>()

    fun getRemoteViewersFromAPI() {
        loadRemoteViewer.value = true
        compositeDisposable.add(
            remoteViewerApiService.getRemoteViewers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<RemoteViewerRemote>>() {
                    override fun onSuccess(value: MutableList<RemoteViewerRemote>?) {
                        loadRemoteViewer.value = false
                        remoteViewersResponse.value = value
                        remoteViewersLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadRemoteViewer.value = false
                        remoteViewersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addRemoteViewerFromAPI(remoteViewer: RemoteViewer){
        addRemoteViewersLoad.value = true
        compositeDisposable.add(
            remoteViewerApiService.addRemoteViewer(remoteViewer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RemoteViewerRemote>() {
                    override fun onSuccess(value: RemoteViewerRemote?) {
                        addRemoteViewersResponse.value = value
                        addRemoteViewerErrorResponse.value = false
                        addRemoteViewersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addRemoteViewersLoad.value = false
                        addRemoteViewerErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateRemoteViewerFromAPI(remoteViewer: RemoteViewer){
        updateRemoteViewersLoad.value = true
        compositeDisposable.add(
            remoteViewerApiService.updateRemoteViewers(remoteViewer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RemoteViewerRemote>() {
                    override fun onSuccess(value: RemoteViewerRemote?) {
                        updateRemoteViewersResponse.value = value
                        updateRemoteViewersErrorResponse.value = false
                        updateRemoteViewersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateRemoteViewersLoad.value = false
                        updateRemoteViewersErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}