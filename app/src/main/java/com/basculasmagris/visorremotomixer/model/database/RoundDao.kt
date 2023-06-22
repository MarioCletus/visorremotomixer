package com.basculasmagris.visorremotomixer.model.database

import androidx.room.*
import com.basculasmagris.visorremotomixer.model.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundDao {
    @Insert
    suspend fun insertRound(round: Round) : Long

    @Query("SELECT * FROM round WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveRoundList(): Flow<MutableList<Round>>

    @Query("SELECT * FROM round ORDER BY id DESC")
    fun getAllRoundList(): Flow<MutableList<Round>>

    @Query("SELECT * FROM round WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredRoundList(name: String): Flow<List<Round>>

    @Query("SELECT * FROM round WHERE id = :id")
    fun getRoundById(id: Long): Flow<Round>

    @Update
    suspend fun updateRound(round: Round)

    @Query("UPDATE round SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE round SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Query("UPDATE round_run SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteRoundRunId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteRound(round: Round)

    @Query("DELETE FROM ROUND WHERE ROUND.ID = :round_id")
    suspend fun deleteRoundById(round_id: Long)

    @Delete
    suspend fun deleteRoundRun(round_run: RoundRun)

    @Query("DELETE FROM ROUND_RUN WHERE ROUND_RUN.ID = :round_run_id")
    suspend fun deleteRoundRunById(round_run_id: Long)

    @Query("DELETE FROM round_corral WHERE round_id = :roundId AND corral_id = :corralId")
    suspend fun deleteRound(roundId: Long, corralId: Long)

    @Query("DELETE FROM round_corral WHERE round_id = :roundId")
    suspend fun deleteCorralByRound(roundId: Long)

    // RoundCorral
    @Query("SELECT dp.remote_round_id as remoteRoundId, dp.remote_corral_id as remoteCorralId, dp.corral_id as corralId, p.name as corralName, p.description as corralDescription, dp.round_id as roundId,dp.percentage, dp.weight, dp.`order`, p.animal_quantity as animalQuantity FROM round_corral as dp JOIN corral as p ON p.id = dp.corral_id WHERE round_id = :idRound ORDER BY dp.`order`")
    //@Query("SELECT * FROM round_corral WHERE round_id = :idRound")
    fun getCorralsBy(idRound: Long): Flow<MutableList<RoundCorralDetail>>

    @Query("SELECT rc.`order`, rc.weight, rc.percentage, rc.remote_id, rc.archive_date, rc.corral_id, rc.remote_corral_id, rc.remote_round_id, rc.round_id, rc.updated_date, c.animal_quantity as animalQuantity FROM round_corral rc JOIN corral c ON c.id == rc.corral_id")
    fun getAllRoundCorralList(): Flow<MutableList<RoundCorral>>

    @Insert
    suspend fun insertRoundCorral(roundCorral: RoundCorral)

    @Query("UPDATE round_corral SET `order` = :order, weight = :weight, percentage = :percentage WHERE round_id = :roundId AND corral_id = :corralId")
    suspend fun updateRoundCorral(roundId: Long, corralId: Long, order: Int, weight: Double, percentage: Double)

    @Query("UPDATE round_run_progress_load SET  initial_weight = :initialWeight,current_weight = :currentWeight,final_weight = :finalWeight, target_weight = :targetWeight WHERE round_run_id = :roundRunId AND diet_id = :dietId AND  product_id = :productId")
    suspend fun updateRoundProgressLoad(roundRunId: Long, dietId: Long, productId:Long, initialWeight:Double, currentWeight:Double, finalWeight:Double, targetWeight: Double)

    @Query("UPDATE round_run_progress_download SET  initial_weight = :initialWeight,current_weight = :currentWeight,final_weight = :finalWeight, custom_target_weight = :customTargetWeight, actual_target_weight = :actualTargetWeight WHERE round_run_id = :roundRunId AND corral_id = :corralId")
    suspend fun updateRoundProgressDownload(roundRunId: Long, corralId: Long,  initialWeight:Double,  currentWeight:Double,  finalWeight:Double, customTargetWeight: Double, actualTargetWeight: Double)


    @Query("INSERT INTO round_run_progress_load (round_run_id, diet_id, product_id, initial_weight, current_weight, final_weight, target_weight, remote_round_run_id, remote_diet_id, remote_product_id) VALUES (:roundRunId, :dietId, :productId, :initialWeight, :currentWeight, :finalWeight, :customTargetWeight, 0, 0, 0)")
    suspend fun addRoundProgressLoad(roundRunId:Long, dietId: Long, productId:Long, initialWeight:Double, currentWeight:Double, finalWeight:Double, customTargetWeight:Double)

    @Query("INSERT INTO round_run (round_id, remote_round_id, start_date, updated_date, end_date, remote_id, mixer_id, remote_mixer_id, custom_percentage, custom_tara, added_blend, state, remote_user_id, user_display_name) VALUES (:roundId, 0, datetime('now','localtime'), datetime('now','localtime'), '', 0, :mixerId, 0, :customPercentage, :customTara, :addedBlend, :state, :userId, :userDisplayName)")
    suspend fun addRoundRun(roundId: Long, mixerId: Long, customPercentage: Double, customTara: Double, addedBlend: Double, state: Int, userId: Long, userDisplayName: String) : Long

    @Query("UPDATE round_run SET mixer_id = :mixerId, updated_date = datetime('now','localtime'), custom_percentage = :customPercentage, custom_tara = :customTara, added_blend = :addedBlend, state = :state WHERE id = :roundRunId")
    suspend fun updateRoundRun(roundRunId: Long, mixerId: Long, customPercentage: Double, customTara: Double, addedBlend: Double, state: Int)

    @Query("UPDATE round_run SET updated_date = datetime('now','localtime'), end_date = datetime('now','localtime')  WHERE id = :roundRunId")
    suspend fun finishRoundRun(roundRunId: Long)

    @Query("UPDATE round_run SET updated_date = datetime('now','localtime'), end_date = datetime('now','localtime'), state = :state  WHERE id = :roundRunId")
    suspend fun finishRoundRunWState(roundRunId: Long, state: Int)

    @Query("INSERT INTO round_run_progress_download (round_run_id, corral_id, initial_weight, current_weight, final_weight, custom_target_weight, actual_target_weight, remote_round_run_id, remote_corral_id) VALUES (:roundRunId, :corralId, :initialWeight, :currentWeight, :finalWeight, :customTargetWeight, :actualTargetWeight, 0, 0)")
    suspend fun addRoundProgressDownload(roundRunId: Long, corralId: Long,  initialWeight:Double,  currentWeight:Double,  finalWeight:Double, customTargetWeight: Double, actualTargetWeight:Double)

    @Query("SELECT rr.id, rr.updated_date , rr.round_id , rr.remote_round_id ,rr.remote_id , rr.end_date , rr.start_date, rr.mixer_id, rr.remote_mixer_id, rr.custom_percentage, rr.custom_tara, rr.added_blend, rr.state, rr.remote_user_id, rr.user_display_name FROM round_run rr")
    fun getAllRoundRunList(): Flow<MutableList<RoundRun>>

    @Query("SELECT rrpl.product_id , rrpl.diet_id , rrpl.initial_weight , rrpl.current_weight, rrpl.final_weight , rrpl.remote_diet_id , rrpl.remote_product_id ,rrpl.remote_round_run_id , rrpl.round_run_id ,rrpl.target_weight  FROM round_run_progress_load rrpl")
    fun getAllRoundRunProgressLoadList(): Flow<MutableList<RoundRunProgressLoad>>

    @Query("SELECT rrpd.initial_weight ,rrpd.current_weight ,rrpd.final_weight ,rrpd.remote_round_run_id , rrpd.round_run_id ,rrpd.actual_target_weight , rrpd.corral_id , rrpd.remote_corral_id, rrpd.custom_target_weight  FROM round_run_progress_download rrpd")
    fun getAllRoundRunProgressDownloadlist(): Flow<MutableList<RoundRunProgressDownload>>
}