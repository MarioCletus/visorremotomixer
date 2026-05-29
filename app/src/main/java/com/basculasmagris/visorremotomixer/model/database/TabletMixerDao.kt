package com.basculasmagris.visorremotomixer.model.database

import androidx.room.*
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import kotlinx.coroutines.flow.Flow

@Dao
interface TabletMixerDao {
    @Insert
    suspend fun insertTabletMixer(tabletMixer: TabletMixer) : Long

    @Query("SELECT * FROM TABLET_MIXER ORDER BY sort_order ASC, name ASC")
    fun getAllTabletMixerList(): Flow<MutableList<TabletMixer>>

    @Query("SELECT * FROM TABLET_MIXER ORDER BY sort_order ASC, name ASC")
    fun getActiveTabletMixerList(): Flow<List<TabletMixer>>

    @Update
    suspend fun updateAllTabletMixers(tablets: List<TabletMixer>)

    @Query("SELECT * FROM TABLET_MIXER ORDER BY name LIKE :name")
    fun getFilteredTabletMixerList(name: String): Flow<List<TabletMixer>>

    @Query("SELECT * FROM TABLET_MIXER WHERE id = :id")
    fun getTabletMixerById(id: Long): Flow<TabletMixer>

    @Update
    suspend fun updateTabletMixer(tabletMixer: TabletMixer)

    @Query("UPDATE TABLET_MIXER SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE TABLET_MIXER SET mac = :mac, updated_date = datetime('now','localtime') WHERE id = :id")
    suspend fun setUpdatedMacAddress(id: Long, mac: String)

    @Delete
    suspend fun deleteTabletMixer(tabletMixer: TabletMixer)
}