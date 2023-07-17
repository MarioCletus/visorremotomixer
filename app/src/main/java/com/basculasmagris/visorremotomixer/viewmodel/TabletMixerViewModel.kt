package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote
import com.basculasmagris.visorremotomixer.model.database.TabletMixerRepository
import com.basculasmagris.visorremotomixer.model.network.TabletMixerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class TabletMixerViewModel (private val repository: TabletMixerRepository) : ViewModel() {

    // Remote
    private val tabletMixerApiService = TabletMixerApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadTabletMixer = MutableLiveData<Boolean>()
    val tabletMixersResponse = MutableLiveData<List<TabletMixerRemote>?>()
    val tabletMixersLoadingError = MutableLiveData<Boolean>()

    fun getTabletMixersFromAPI() {
        loadTabletMixer.value = true
        compositeDisposable.add(
            tabletMixerApiService.getTabletMixers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<TabletMixerRemote>>() {
                    override fun onSuccess(value: List<TabletMixerRemote>?) {
                        loadTabletMixer.value = true
                        tabletMixersResponse.value = value
                        tabletMixersLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadTabletMixer.value = false
                        tabletMixersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }
                )
        )
    }

    // Local
    fun insert(tabletMixer: TabletMixer) = viewModelScope.launch {
        repository.insertTabletMixerData(tabletMixer)
    }

    suspend fun insertSync(tabletMixer: TabletMixer) = repository.insertTabletMixerData(tabletMixer)

    val allTabletMixerList: LiveData<MutableList<TabletMixer>> = repository.allTabletMixerList.asLiveData()
    val activeTabletMixerList: LiveData<List<TabletMixer>> = repository.activeTabletMixerList.asLiveData()

    fun getTabletMixerById(id: Long) : LiveData<TabletMixer> = repository.getTabletMixerById(id).asLiveData()

    fun getFilteredTabletMixerList(value: String): LiveData<List<TabletMixer>> =
        repository.getFilteredTabletMixerList(value).asLiveData()

    fun update(tabletMixer: TabletMixer) = viewModelScope.launch {
        repository.updateTabletMixerData(tabletMixer)
    }

    suspend fun updateSync(tabletMixer: TabletMixer) = repository.updateTabletMixerData(tabletMixer)


    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedMacAddress(id: Long, mac: String) = viewModelScope.launch {
        repository.setUpdatedMacAddress(id, mac)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(tabletMixer: TabletMixer) = viewModelScope.launch {
        repository.deleteTabletMixerData(tabletMixer)
    }
}

class TabletMixerViewModelFactory(private val repository: TabletMixerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabletMixerViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TabletMixerViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}