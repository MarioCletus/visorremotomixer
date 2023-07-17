package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TabletMixerAPI {
    @GET("${Constants.API_MIXER_ENDPOINT}/all/")
    fun getTabletMixers() : Single<MutableList<TabletMixerRemote>>

    @POST(Constants.API_MIXER_ENDPOINT)
    fun addTabletMixer(@Body tabletMixer: TabletMixer) : Single<TabletMixerRemote>

    @PUT(Constants.API_MIXER_ENDPOINT)
    fun updateTabletMixer(@Body tabletMixer: TabletMixer) : Single<TabletMixerRemote>
}