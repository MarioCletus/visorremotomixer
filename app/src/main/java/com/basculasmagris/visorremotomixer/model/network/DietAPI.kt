package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Diet
import com.basculasmagris.visorremotomixer.model.entities.DietRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface DietAPI {
    @GET("${Constants.API_DIET_ENDPOINT}/all/")
    fun getDiets() : Single<MutableList<DietRemote>>

    @POST(Constants.API_DIET_ENDPOINT)
    fun addDiet(@Body diet: DietRemote) : Single<DietRemote>

    @PUT(Constants.API_DIET_ENDPOINT)
    fun updateDiet(@Body diet: DietRemote) : Single<DietRemote>
}