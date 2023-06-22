package com.basculasmagris.visorremotomixer.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.visorremotomixer.model.entities.*
import kotlinx.coroutines.flow.Flow

class RoundRunReportRepository (private  val roundRunReportDao: RoundRunReportDao) {

    @WorkerThread
    suspend fun insert(roundRunReport: RoundRunReport) : Long{
        return roundRunReportDao.insert(roundRunReport)
    }

    val activeRoundRunReportList: Flow<MutableList<RoundRunReport>> = roundRunReportDao.getActiveRoundRunReportList()
}