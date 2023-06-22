package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import kotlinx.coroutines.flow.Flow

class RemoteViewerRepository (private  val remoteViewerDao: RemoteViewerDao) {

    @WorkerThread
    suspend fun insertRemoteViewerData(remoteViewer: RemoteViewer) : Long {
        return remoteViewerDao.insertRemoteViewer(remoteViewer)
    }

    val allRemoteViewerList: Flow<MutableList<RemoteViewer>> = remoteViewerDao.getAllRemoteViewerList()
    val activeRemoteViewerList: Flow<List<RemoteViewer>> = remoteViewerDao.getActiveRemoteViewerList()

    fun getRemoteViewerById(id: Long) : Flow<RemoteViewer> = remoteViewerDao.getRemoteViewerById(id)

    fun getFilteredRemoteViewerList(value: String): Flow<List<RemoteViewer>> = remoteViewerDao.getFilteredRemoteViewerList(value)


    @WorkerThread
    suspend fun updateRemoteViewerData(remoteViewer: RemoteViewer){
        remoteViewerDao.updateRemoteViewer(remoteViewer)
    }


    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        remoteViewerDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedMacAddress(id: Long, mac: String){
        remoteViewerDao.setUpdatedMacAddress(id, mac)
    }



    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        remoteViewerDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteRemoteViewerData(remoteViewer: RemoteViewer){
        remoteViewerDao.deleteRemoteViewer(remoteViewer)
    }
}