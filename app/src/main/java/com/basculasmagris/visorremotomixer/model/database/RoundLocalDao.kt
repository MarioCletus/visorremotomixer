package com.basculasmagris.visorremotomixer.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.basculasmagris.visorremotomixer.model.entities.Round
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundLocalDao {

    @Insert
    suspend fun insertRoundLocal(roundLocal: RoundLocal) : Long

    @Update
    suspend fun updateRoundLocal(roundLocal: RoundLocal)

    @Query("SELECT * FROM RoundLocal ORDER BY id DESC")
    fun getAllRoundLocalList(): Flow<MutableList<RoundLocal>>

    @Query("SELECT * FROM RoundLocal WHERE :mac == tablet_mixer_mac ORDER BY id DESC")
    fun getAllRoundLocalListByMac(mac: String): Flow<MutableList<RoundLocal>>


    @Delete
    suspend fun deleteRoundLocal(roundLocal: RoundLocal)

    @Query("DELETE FROM ROUNDLOCAL WHERE RoundLocal.ID = :round_local_id")
    suspend fun deleteRoundLocalById(round_local_id: Long)


    @Query("DELETE FROM ROUNDLOCAL")
    suspend fun deleteAllRoundLocal()

}