package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import kotlinx.coroutines.flow.Flow

class MixerRepository (private  val mixerDao: MixerDao) {

    @WorkerThread
    suspend fun insertMixerData(mixer: Mixer) : Long {
        return mixerDao.insertMixer(mixer)
    }

    val allMixerList: Flow<MutableList<Mixer>> = mixerDao.getAllMixerList()
    val activeMixerList: Flow<List<Mixer>> = mixerDao.getActiveMixerList()

    fun getMixerById(id: Long) : Flow<Mixer> = mixerDao.getMixerById(id)

    fun getFilteredMixerList(value: String): Flow<List<Mixer>> = mixerDao.getFilteredMixerList(value)


    @WorkerThread
    suspend fun updateMixerData(mixer: Mixer){
        mixerDao.updateMixer(mixer)
    }

    @WorkerThread
    suspend fun updateTaraData(id: Long, weight: Double){
        mixerDao.updateTaraMixer(id, weight)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        mixerDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedMacAddress(id: Long, mac: String){
        mixerDao.setUpdatedMacAddress(id, mac)
    }



    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        mixerDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteMixerData(mixer: Mixer){
        mixerDao.deleteMixer(mixer)
    }
}