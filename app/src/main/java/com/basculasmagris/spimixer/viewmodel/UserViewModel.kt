package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.visorremotomixer.model.database.UserRepository
import com.basculasmagris.visorremotomixer.model.entities.User
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository) : ViewModel() {

    fun insert(user: User) = viewModelScope.launch {
        repository.insertUserData(user)
    }

    suspend fun insertSync(user: User) = repository.insertUserData(user)

    val allUserList: LiveData<MutableList<User>> = repository.allUserList.asLiveData()
    
    fun update(user: User) = viewModelScope.launch {
        repository.updateUserData(user)
    }

    suspend fun updateSync(user: User) = repository.updateUserData(user)

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.deleteUserData(user)
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
           return UserViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}