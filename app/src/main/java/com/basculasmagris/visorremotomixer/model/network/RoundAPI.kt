package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RoundRemote
import com.basculasmagris.visorremotomixer.model.entities.RoundRunBody
import com.basculasmagris.visorremotomixer.model.entities.RoundRunRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RoundAPI {
    @GET("${Constants.API_ROUND_ENDPOINT}/all/")
    fun getRounds() : Single<MutableList<RoundRemote>>

    @POST(Constants.API_ROUND_ENDPOINT)
    fun addRound(@Body round: RoundRemote) : Single<RoundRemote>

    @POST("${Constants.API_ROUND_ENDPOINT}/run/")
    fun addRoundRun(@Body roundRunBody: RoundRunBody) : Single<RoundRunRemote>

    @PUT(Constants.API_ROUND_ENDPOINT)
    fun updateRound(@Body round: RoundRemote) : Single<RoundRemote>
}