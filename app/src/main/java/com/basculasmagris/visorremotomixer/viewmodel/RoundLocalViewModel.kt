package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.basculasmagris.visorremotomixer.model.database.RoundLocalRepository
import com.basculasmagris.visorremotomixer.model.entities.Round
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal
import kotlinx.coroutines.launch

class RoundLocalViewModel (private val repository: RoundLocalRepository) : ViewModel(){

    fun insert(roundLocal: RoundLocal) = viewModelScope.launch {
        repository.insertRoundLocalData(roundLocal)
    }

    fun delete(roundLocal: RoundLocal) = viewModelScope.launch {
        repository.deleteRoundLocalData(roundLocal)
    }

    fun deleteAllRoundLocal() = viewModelScope.launch {
        repository.deleteAllRoundLocal()
    }

    fun deleteRounLocalById(round_local_id: Long) = viewModelScope.launch {
        repository.deleteRoundLocalDataById(round_local_id)
    }

    fun update(roundLocal: RoundLocal) = viewModelScope.launch {
        repository.updateRoundLocalData(roundLocal)
    }

    fun allRoundLocalListByMac(mac:String): LiveData<MutableList<RoundLocal>> = repository.allRoundLocalListByMac(mac).asLiveData()

    val allRoundLocalList: LiveData<MutableList<RoundLocal>> = repository.allRoundLocalList.asLiveData()

}

class RoundLocalViewModelFactory(private val repository: RoundLocalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundLocalViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RoundLocalViewModel(repository) as T
        }
        throw IllegalAccessException("Unknown ViewModel Class")
    }
}