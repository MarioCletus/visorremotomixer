package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RoundRunReportRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ReportApiService : BaseService() {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ReportAPI::class.java)

    fun getReports(): Single<MutableList<RoundRunReportRemote>> {
        return api.getReports()
    }

    fun addReport(report: RoundRunReportRemote): Single<RoundRunReportRemote> {
        return api.addReport(report)
    }
}