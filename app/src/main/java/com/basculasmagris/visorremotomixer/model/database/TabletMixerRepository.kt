package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import kotlinx.coroutines.flow.Flow

class TabletMixerRepository (private  val tabletMixerDao: TabletMixerDao) {

    @WorkerThread
    suspend fun insertTabletMixerData(tabletMixer: TabletMixer) : Long {
        return tabletMixerDao.insertTabletMixer(tabletMixer)
    }

    val allTabletMixerList: Flow<MutableList<TabletMixer>> = tabletMixerDao.getAllTabletMixerList()
    val activeTabletMixerList: Flow<List<TabletMixer>> = tabletMixerDao.getActiveTabletMixerList()

    fun getTabletMixerById(id: Long) : Flow<TabletMixer> = tabletMixerDao.getTabletMixerById(id)

    fun getFilteredTabletMixerList(value: String): Flow<List<TabletMixer>> = tabletMixerDao.getFilteredTabletMixerList(value)


    @WorkerThread
    suspend fun updateTabletMixerData(tabletMixer: TabletMixer){
        tabletMixerDao.updateTabletMixer(tabletMixer)
    }


    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        tabletMixerDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedMacAddress(id: Long, mac: String){
        tabletMixerDao.setUpdatedMacAddress(id, mac)
    }



    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        tabletMixerDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteTabletMixerData(tabletMixer: TabletMixer){
        tabletMixerDao.deleteTabletMixer(tabletMixer)
    }
}