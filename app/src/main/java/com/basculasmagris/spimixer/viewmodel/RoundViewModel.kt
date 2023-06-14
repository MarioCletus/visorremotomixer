package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.visorremotomixer.model.database.RoundRepository
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.model.network.RoundApiService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class RoundViewModel(private val repository: RoundRepository) : ViewModel() {

    // Remote
    private val roundApiService = RoundApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadRound = MutableLiveData<Boolean>()
    val roundsResponse = MutableLiveData<List<RoundRemote>?>()
    val roundsLoadingError = MutableLiveData<Boolean>()

    // Local
    fun insert(round: Round) = viewModelScope.launch {
        repository.insertRoundData(round)
    }

    suspend fun insertSync(round: Round) = repository.insertRoundData(round)

    fun insertRoundCorral(roundCorral: RoundCorral) = viewModelScope.launch {
        repository.insertRoundCorralData(roundCorral)
    }

    val allRoundList: LiveData<MutableList<Round>> = repository.allRoundList.asLiveData()
    val activeRoundList: LiveData<MutableList<Round>> = repository.activeRoundList.asLiveData()
    val allRoundCorralList: LiveData<MutableList<RoundCorral>> = repository.getAllRoundCorralList.asLiveData()

    val allRoundRunList: LiveData<MutableList<RoundRun>> = repository.allRoundRunList.asLiveData()
    val allRoundRunProgressLoadList: LiveData<MutableList<RoundRunProgressLoad>> = repository.allRoundRunProgressLoadList.asLiveData()
    val allRoundRunProgressDownloadList: LiveData<MutableList<RoundRunProgressDownload>> = repository.allRoundRunProgressDownloadList.asLiveData()

    fun getCorralsBy(idRound: Long) : LiveData<MutableList<RoundCorralDetail>> = repository.getCorralsBy(idRound).asLiveData()

    fun getRoundById(id: Long) : LiveData<Round> = repository.getRoundById(id).asLiveData()

    fun getFilteredRoundList(value: String): LiveData<List<Round>> =
        repository.getFilteredRoundList(value).asLiveData()

    fun update(round: Round) = viewModelScope.launch {
        repository.updateRoundData(round)
    }

    fun updateProgressLoad(roundRunId: Long, dietId: Long, productId:Long, initialWeight:Double, currentWeight:Double, finalWeight:Double, targetWeight: Double) = viewModelScope.launch {
        repository.updateRoundProgressLoadData(roundRunId, dietId, productId, initialWeight, currentWeight, finalWeight, targetWeight)
    }

    fun updateProgressDownload(roundRunId: Long, corralId: Long,  initialWeight:Double,  currentWeight:Double,  finalWeight:Double, customTargetWeight: Double, actualTargetWeight: Double) = viewModelScope.launch {
        repository.updateRoundProgressDownloadData(roundRunId, corralId,  initialWeight,  currentWeight,  finalWeight, customTargetWeight, actualTargetWeight)
    }

    fun addProgressLoad(roundRunId: Long, dietId: Long, productId:Long, initialWeight:Double, actualWeight:Double, finalWeight:Double, targetWeight:Double) = viewModelScope.launch {
        repository.addRoundProgressLoadData(roundRunId, dietId, productId, initialWeight, actualWeight, finalWeight, targetWeight)
    }

    fun addProgressDownload(roundRunId: Long, corralId: Long,  initialWeight:Double,  currentWeight:Double,  finalWeight:Double, customTargetWeight: Double, actualTargetWeight:Double) = viewModelScope.launch {
        repository.addRoundProgressDownloadData(roundRunId, corralId,  initialWeight,  currentWeight,  finalWeight, customTargetWeight, actualTargetWeight)
    }

    /*
    fun addRoundRun(roundId: Long, mixerId: Long) = viewModelScope.launch {
        repository.addRoundRunData(roundId, mixerId)
    }*/

    suspend  fun addRoundRunSync(roundId: Long, mixerId: Long, customPercentage: Double, customTara: Double, addedBlend: Double, state: Int, userId: Long, userDisplayName: String) : Long = repository.addRoundRunData(roundId, mixerId, customPercentage, customTara, addedBlend, state, userId, userDisplayName)
    suspend  fun updateRoundRunSync(roundRunId: Long, mixerId: Long, customPercentage: Double, customTara: Double, addedBlend: Double, state: Int) = repository.updateRoundRunData(roundRunId, mixerId, customPercentage, customTara, addedBlend, state)
    suspend  fun finishRoundRunSync(roundRunId: Long) = repository.finishRoundRunData(roundRunId)
    suspend  fun finishRoundRunSyncWState(roundRunId: Long, state: Int) = repository.finishRoundRunDataWState(roundRunId,state)

    suspend fun updateSync(round: Round) = repository.updateRoundData(round)

    fun updateCorralBy(roundId: Long, corralId: Long, order: Int, weight: Double, percentage: Double) = viewModelScope.launch {
        repository.updateRoundCorralData(roundId, corralId, order, weight, percentage)
    }

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun setUpdatedRemoteRoundRunId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteRoundRunId(id, remoteId)
    }

    fun delete(round: Round) = viewModelScope.launch {
        repository.deleteRoundData(round)
    }

    fun deleteRounById(round_id: Long) = viewModelScope.launch {
        repository.deleteRoundDataById(round_id)
    }


    fun deleteRoundRun(roundRun: RoundRun) = viewModelScope.launch {
        repository.deleteRoundRunData(roundRun)
    }

    fun deleteRoundRunById(round_id: Long) = viewModelScope.launch {
        repository.deleteRoundRunDataById(round_id)
    }

    fun deleteCorralBy(roundId: Long, corralId: Long) = viewModelScope.launch {
        repository.deleteRoundCorralData(roundId, corralId)
    }

    suspend fun deleteCorralByRound(roundId: Long) = repository.deleteCorralByRoundData(roundId)

}

class RoundViewModelFactory(private val repository: RoundRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RoundViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}