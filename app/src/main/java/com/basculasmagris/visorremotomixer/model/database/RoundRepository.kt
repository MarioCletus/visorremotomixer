package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.*
import kotlinx.coroutines.flow.Flow

class RoundRepository (private  val roundDao: RoundDao) {

    @WorkerThread
    suspend fun insertRoundData(round: Round) : Long{
        return roundDao.insertRound(round)
    }

    @WorkerThread
    suspend fun insertRoundCorralData(roundCorral: RoundCorral){
        roundDao.insertRoundCorral(roundCorral)
    }

    val allRoundList: Flow<MutableList<Round>> = roundDao.getAllRoundList()
    val activeRoundList: Flow<MutableList<Round>> = roundDao.getActiveRoundList()
    fun getRoundById(id: Long) : Flow<Round> = roundDao.getRoundById(id)

    fun getFilteredRoundList(value: String): Flow<List<Round>> = roundDao.getFilteredRoundList(value)


    @WorkerThread
    suspend fun updateRoundData(round: Round){
        roundDao.updateRound(round)
    }

    @WorkerThread
    suspend fun updateRoundProgressLoadData(roundRunId: Long, dietId: Long, productId:Long, initialWeight:Double, currentWeight:Double, finalWeight:Double, targetWeight: Double){
        roundDao.updateRoundProgressLoad(roundRunId, dietId, productId, initialWeight, currentWeight, finalWeight, targetWeight)
    }

    @WorkerThread
    suspend fun updateRoundProgressDownloadData(roundRunId: Long, corralId: Long,  initialWeight:Double,  currentWeight:Double,  finalWeight:Double, customTargetWeight: Double, actualTargetWeight: Double){
        roundDao.updateRoundProgressDownload(roundRunId, corralId,  initialWeight,  currentWeight,  finalWeight, customTargetWeight, actualTargetWeight)
    }

    @WorkerThread
    suspend fun addRoundProgressLoadData(roundRunId: Long, dietId: Long, productId:Long, initialWeight:Double, currentWeight:Double, finalWeight:Double, targetWeight:Double){
        roundDao.addRoundProgressLoad(roundRunId, dietId, productId, initialWeight, currentWeight, finalWeight, targetWeight)
    }

    @WorkerThread
    suspend fun addRoundProgressDownloadData(roundRunId: Long, corralId: Long,  initialWeight:Double,  currentWeight:Double,  finalWeight:Double, customTargetWeight: Double, actualTargetWeight:Double){
        roundDao.addRoundProgressDownload(roundRunId, corralId,  initialWeight,  currentWeight,  finalWeight, customTargetWeight, actualTargetWeight)
    }
    /*
    @WorkerThread
    suspend fun addRoundRunData(roundRunId: Long, dietId: Long, productId:Long, actualWeight:Double, targetWeight:Double){
        roundDao.addRoundProgressLoad(roundRunId, dietId, productId, actualWeight, targetWeight)
    }*/


    @WorkerThread
    suspend fun updateRoundCorralData(roundId: Long, corralId: Long, order: Int, weight: Double, percentage: Double){
        roundDao.updateRoundCorral(roundId, corralId, order, weight, percentage)
    }

    @WorkerThread
    suspend fun addRoundRunData(roundId: Long, mixerId: Long, customPercentage: Double, customTara: Double, addedBlend: Double, state: Int, userId: Long, userDisplayName: String): Long {
        return roundDao.addRoundRun(roundId, mixerId, customPercentage, customTara, addedBlend, state, userId, userDisplayName)
    }

    @WorkerThread
    suspend fun updateRoundRunData(roundRunId: Long, mixerId: Long, customPercentage: Double, customTara: Double, addedBlend: Double, state: Int) {
        roundDao.updateRoundRun(roundRunId, mixerId, customPercentage, customTara, addedBlend,state)
    }

    @WorkerThread
    suspend fun finishRoundRunData(roundRunId: Long) {
        roundDao.finishRoundRun(roundRunId)
    }

    @WorkerThread
    suspend fun finishRoundRunDataWState(roundRunId: Long, state: Int){
        roundDao.finishRoundRunWState(roundRunId,state)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        roundDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        roundDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteRoundRunId(id: Long, remoteId: Long){
        roundDao.setUpdatedRemoteRoundRunId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteRoundData(round: Round){
        roundDao.deleteRound(round)
    }

    @WorkerThread
    suspend fun deleteRoundDataById(round_id: Long){
        roundDao.deleteRoundById(round_id)
    }


    @WorkerThread
    suspend fun deleteRoundRunData(round_run: RoundRun){
        roundDao.deleteRoundRun(round_run)
    }

    @WorkerThread
    suspend fun deleteRoundRunDataById(round_run_id: Long){
        roundDao.deleteRoundRunById(round_run_id)
    }

    @WorkerThread
    suspend fun deleteRoundCorralData(roundId: Long, corralId: Long){
        roundDao.deleteRound(roundId, corralId)
    }

    @WorkerThread
    suspend fun deleteCorralByRoundData(roundId: Long){
        roundDao.deleteCorralByRound(roundId)
    }


    fun getCorralsBy(idRound: Long) : Flow<MutableList<RoundCorralDetail>> = roundDao.getCorralsBy(idRound)

    val getAllRoundCorralList: Flow<MutableList<RoundCorral>> = roundDao.getAllRoundCorralList()

    val allRoundRunList: Flow<MutableList<RoundRun>> = roundDao.getAllRoundRunList()
    val allRoundRunProgressLoadList: Flow<MutableList<RoundRunProgressLoad>> = roundDao.getAllRoundRunProgressLoadList()
    val allRoundRunProgressDownloadList: Flow<MutableList<RoundRunProgressDownload>> = roundDao.getAllRoundRunProgressDownloadlist()


}