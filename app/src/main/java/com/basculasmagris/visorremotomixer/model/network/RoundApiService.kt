package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RoundRemote
import com.basculasmagris.visorremotomixer.model.entities.RoundRun
import com.basculasmagris.visorremotomixer.model.entities.RoundRunBody
import com.basculasmagris.visorremotomixer.model.entities.RoundRunRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RoundApiService : BaseService() {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RoundAPI::class.java)

    fun getRounds(): Single<MutableList<RoundRemote>> {
        return api.getRounds()
    }

    fun addRound(round: RoundRemote): Single<RoundRemote> {
        return api.addRound(round)
    }

    fun addRoundRun(roundRunBody: RoundRunBody): Single<RoundRunRemote> {
        return api.addRoundRun(roundRunBody)
    }

    fun updateRounds(round: RoundRemote): Single<RoundRemote> {
        return api.updateRound(round)
    }
}