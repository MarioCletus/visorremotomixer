package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository (private  val userDao: UserDao) {

    @WorkerThread
    suspend fun insertUserData(user: User) : Long {
        return userDao.insertUser(user)
    }

    val allUserList: Flow<MutableList<User>> = userDao.getAllUserList()


    @WorkerThread
    suspend fun updateUserData(user: User){
        userDao.updateUser(user)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        userDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        userDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteUserData(user: User){
        userDao.deleteUser(user)
    }
}