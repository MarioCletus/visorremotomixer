package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.Round
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal
import kotlinx.coroutines.flow.Flow

class RoundLocalRepository(private  val roundLocalDao: RoundLocalDao) {

    @WorkerThread
    suspend fun insertRoundLocalData(roundLocal: RoundLocal) : Long{
        return roundLocalDao.insertRoundLocal(roundLocal)
    }

    @WorkerThread
    suspend fun updateRoundLocalData(roundLocal: RoundLocal){
        roundLocalDao.updateRoundLocal(roundLocal)
    }

    @WorkerThread
    suspend fun deleteRoundLocalData(roundLocal: RoundLocal){
        roundLocalDao.deleteRoundLocal(roundLocal)
    }


    @WorkerThread
    suspend fun deleteAllRoundLocal(){
        roundLocalDao.deleteAllRoundLocal()
    }

    @WorkerThread
    suspend fun deleteRoundLocalDataById(round_local_id: Long){
        roundLocalDao.deleteRoundLocalById(round_local_id)
    }


    val allRoundLocalList: Flow<MutableList<RoundLocal>> = roundLocalDao.getAllRoundLocalList()
    fun allRoundLocalListByMac(mac:String): Flow<MutableList<RoundLocal>> = roundLocalDao.getAllRoundLocalListByMac(mac)


}