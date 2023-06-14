package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Corral
import com.basculasmagris.visorremotomixer.model.entities.CorralRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface CorralAPI {
    @GET("${Constants.API_CORRAL_ENDPOINT}/all/")
    fun getCorrals() : Single<MutableList<CorralRemote>>

    @POST(Constants.API_CORRAL_ENDPOINT)
    fun addCorral(@Body corral: Corral) : Single<CorralRemote>

    @PUT(Constants.API_CORRAL_ENDPOINT)
    fun updateCorral(@Body corral: Corral) : Single<CorralRemote>
}