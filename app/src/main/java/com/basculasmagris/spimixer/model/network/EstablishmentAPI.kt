package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Establishment
import com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface EstablishmentAPI {
    @GET("${Constants.API_ESTABLISHMENT_ENDPOINT}/all/")
    fun getEstablishments() : Single<MutableList<EstablishmentRemote>>

    @POST(Constants.API_ESTABLISHMENT_ENDPOINT)
    fun addEstablishment(@Body establishment: Establishment) : Single<EstablishmentRemote>

    @PUT(Constants.API_ESTABLISHMENT_ENDPOINT)
    fun updateEstablishment(@Body establishment: Establishment) : Single<EstablishmentRemote>
}