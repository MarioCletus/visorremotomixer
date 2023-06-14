package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RoundRunReportRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReportAPI {
    @GET("${Constants.API_REPORT_ENDPOINT}")
    fun getReports() : Single<MutableList<RoundRunReportRemote>>

    @POST(Constants.API_REPORT_ENDPOINT)
    fun addReport(@Body report: RoundRunReportRemote) : Single<RoundRunReportRemote>
}