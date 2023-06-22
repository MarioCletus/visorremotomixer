package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Mixer
import com.basculasmagris.visorremotomixer.model.entities.MixerRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MixerAPI {
    @GET("${Constants.API_MIXER_ENDPOINT}/all/")
    fun getMixers() : Single<MutableList<MixerRemote>>

    @POST(Constants.API_MIXER_ENDPOINT)
    fun addMixer(@Body mixer: Mixer) : Single<MixerRemote>

    @PUT(Constants.API_MIXER_ENDPOINT)
    fun updateMixer(@Body mixer: Mixer) : Single<MixerRemote>
}