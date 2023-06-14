package com.basculasmagris.visorremotomixer.model.database

import androidx.room.*
import com.basculasmagris.visorremotomixer.model.entities.Mixer
import kotlinx.coroutines.flow.Flow

@Dao
interface MixerDao {
    @Insert
    suspend fun insertMixer(mixer: Mixer) : Long

    @Query("SELECT * FROM mixer WHERE archive_date IS NULL OR archive_date = ''  ORDER BY name")
    fun getAllMixerList(): Flow<MutableList<Mixer>>

    @Query("SELECT * FROM mixer ORDER BY name")
    fun getActiveMixerList(): Flow<List<Mixer>>

    @Query("SELECT * FROM mixer WHERE archive_date IS NULL OR archive_date = ''  ORDER BY name LIKE :name")
    fun getFilteredMixerList(name: String): Flow<List<Mixer>>

    @Query("SELECT * FROM mixer WHERE id = :id")
    fun getMixerById(id: Long): Flow<Mixer>

    @Update
    suspend fun updateMixer(mixer: Mixer)

    @Query("UPDATE mixer SET tara = :weight, updated_date = datetime('now','localtime') WHERE id = :id")
    suspend fun updateTaraMixer(id: Long, weight: Double)

    @Query("UPDATE mixer SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE mixer SET mac = :mac, updated_date = datetime('now','localtime') WHERE id = :id")
    suspend fun setUpdatedMacAddress(id: Long, mac: String)

    @Query("UPDATE mixer SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteMixer(mixer: Mixer)
}