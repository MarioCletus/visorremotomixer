package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote
import com.basculasmagris.visorremotomixer.model.network.TabletMixerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class TabletMixerRemoteViewModel : ViewModel() {

    private val tabletMixerApiService = TabletMixerApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadTabletMixer = MutableLiveData<Boolean>()
    val remoteViewersResponse = MutableLiveData<MutableList<TabletMixerRemote>?>()
    val remoteViewersLoadingError = MutableLiveData<Boolean>()

    //Post
    val addTabletMixersLoad = MutableLiveData<Boolean>()
    val addTabletMixersResponse = MutableLiveData<TabletMixerRemote?>()
    val addTabletMixerErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateTabletMixersLoad = MutableLiveData<Boolean>()
    val updateTabletMixersResponse = MutableLiveData<TabletMixerRemote?>()
    val updateTabletMixersErrorResponse = MutableLiveData<Boolean>()

    fun getTabletMixersFromAPI() {
        loadTabletMixer.value = true
        compositeDisposable.add(
            tabletMixerApiService.getTabletMixers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<TabletMixerRemote>>() {
                    override fun onSuccess(value: MutableList<TabletMixerRemote>?) {
                        loadTabletMixer.value = false
                        remoteViewersResponse.value = value
                        remoteViewersLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadTabletMixer.value = false
                        remoteViewersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addTabletMixerFromAPI(tabletMixer: TabletMixer){
        addTabletMixersLoad.value = true
        compositeDisposable.add(
            tabletMixerApiService.addTabletMixer(tabletMixer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TabletMixerRemote>() {
                    override fun onSuccess(value: TabletMixerRemote?) {
                        addTabletMixersResponse.value = value
                        addTabletMixerErrorResponse.value = false
                        addTabletMixersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addTabletMixersLoad.value = false
                        addTabletMixerErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateTabletMixerFromAPI(tabletMixer: TabletMixer){
        updateTabletMixersLoad.value = true
        compositeDisposable.add(
            tabletMixerApiService.updateTabletMixer(tabletMixer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TabletMixerRemote>() {
                    override fun onSuccess(value: TabletMixerRemote?) {
                        updateTabletMixersResponse.value = value
                        updateTabletMixersErrorResponse.value = false
                        updateTabletMixersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateTabletMixersLoad.value = false
                        updateTabletMixersErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}