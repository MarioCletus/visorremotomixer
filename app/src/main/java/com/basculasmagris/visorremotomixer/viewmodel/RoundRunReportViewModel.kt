package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.visorremotomixer.model.database.RoundRunReportRepository
import com.basculasmagris.visorremotomixer.model.entities.*
import kotlinx.coroutines.launch

class RoundRunReportViewModel(private val repository: RoundRunReportRepository) : ViewModel() {

    // Local
    fun insert(roundRunReport: RoundRunReport) = viewModelScope.launch {
        repository.insert(roundRunReport)
    }
    suspend fun insertSync(roundRunReport: RoundRunReport) = repository.insert(roundRunReport)
    val activeReportList: LiveData<MutableList<RoundRunReport>> = repository.activeRoundRunReportList.asLiveData()
}

class RoundRunReportViewModelFactory(private val repository: RoundRunReportRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundRunReportViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RoundRunReportViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}