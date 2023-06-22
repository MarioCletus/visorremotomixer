package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewerRemote
import com.basculasmagris.visorremotomixer.model.database.RemoteViewerRepository
import com.basculasmagris.visorremotomixer.model.network.RemoteViewerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class RemoteViewerViewModel (private val repository: RemoteViewerRepository) : ViewModel() {

    // Remote
    private val remoteViewerApiService = RemoteViewerApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadRemoteViewer = MutableLiveData<Boolean>()
    val remoteViewersResponse = MutableLiveData<List<RemoteViewerRemote>?>()
    val remoteViewersLoadingError = MutableLiveData<Boolean>()

    fun getRemoteViewersFromAPI() {
        loadRemoteViewer.value = true
        compositeDisposable.add(
            remoteViewerApiService.getRemoteViewers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<RemoteViewerRemote>>() {
                    override fun onSuccess(value: List<RemoteViewerRemote>?) {
                        loadRemoteViewer.value = true
                        remoteViewersResponse.value = value
                        remoteViewersLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadRemoteViewer.value = false
                        remoteViewersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }
                )
        )
    }

    // Local
    fun insert(remoteViewer: RemoteViewer) = viewModelScope.launch {
        repository.insertRemoteViewerData(remoteViewer)
    }

    suspend fun insertSync(remoteViewer: RemoteViewer) = repository.insertRemoteViewerData(remoteViewer)

    val allRemoteViewerList: LiveData<MutableList<RemoteViewer>> = repository.allRemoteViewerList.asLiveData()
    val activeRemoteViewerList: LiveData<List<RemoteViewer>> = repository.activeRemoteViewerList.asLiveData()

    fun getRemoteViewerById(id: Long) : LiveData<RemoteViewer> = repository.getRemoteViewerById(id).asLiveData()

    fun getFilteredRemoteViewerList(value: String): LiveData<List<RemoteViewer>> =
        repository.getFilteredRemoteViewerList(value).asLiveData()

    fun update(remoteViewer: RemoteViewer) = viewModelScope.launch {
        repository.updateRemoteViewerData(remoteViewer)
    }

    suspend fun updateSync(remoteViewer: RemoteViewer) = repository.updateRemoteViewerData(remoteViewer)


    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedMacAddress(id: Long, mac: String) = viewModelScope.launch {
        repository.setUpdatedMacAddress(id, mac)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(remoteViewer: RemoteViewer) = viewModelScope.launch {
        repository.deleteRemoteViewerData(remoteViewer)
    }
}

class RemoteViewerViewModelFactory(private val repository: RemoteViewerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteViewerViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RemoteViewerViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}