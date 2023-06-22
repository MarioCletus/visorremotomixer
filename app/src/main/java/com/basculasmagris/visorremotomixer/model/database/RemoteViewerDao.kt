package com.basculasmagris.visorremotomixer.model.database

import androidx.room.*
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteViewerDao {
    @Insert
    suspend fun insertRemoteViewer(remoteViewer: RemoteViewer) : Long

    @Query("SELECT * FROM REMOTE_VIEWER WHERE archive_date IS NULL OR archive_date = ''  ORDER BY name")
    fun getAllRemoteViewerList(): Flow<MutableList<RemoteViewer>>

    @Query("SELECT * FROM REMOTE_VIEWER ORDER BY name")
    fun getActiveRemoteViewerList(): Flow<List<RemoteViewer>>

    @Query("SELECT * FROM REMOTE_VIEWER WHERE archive_date IS NULL OR archive_date = ''  ORDER BY name LIKE :name")
    fun getFilteredRemoteViewerList(name: String): Flow<List<RemoteViewer>>

    @Query("SELECT * FROM REMOTE_VIEWER WHERE id = :id")
    fun getRemoteViewerById(id: Long): Flow<RemoteViewer>

    @Update
    suspend fun updateRemoteViewer(remoteViewer: RemoteViewer)

    @Query("UPDATE REMOTE_VIEWER SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE REMOTE_VIEWER SET mac = :mac, updated_date = datetime('now','localtime') WHERE id = :id")
    suspend fun setUpdatedMacAddress(id: Long, mac: String)

    @Query("UPDATE REMOTE_VIEWER SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteRemoteViewer(remoteViewer: RemoteViewer)
}