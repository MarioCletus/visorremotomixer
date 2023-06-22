package com.basculasmagris.visorremotomixer.model.database

import androidx.room.*
import com.basculasmagris.visorremotomixer.model.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundRunReportDao {
    @Insert
    suspend fun insert(round: RoundRunReport) : Long

    @Query("SELECT * FROM round_run_report ORDER BY start_date")
    fun getActiveRoundRunReportList(): Flow<MutableList<RoundRunReport>>
}