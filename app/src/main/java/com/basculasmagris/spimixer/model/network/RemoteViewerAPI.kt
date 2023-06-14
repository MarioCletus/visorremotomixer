package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RemoteViewerRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RemoteViewerAPI {
    @GET("${Constants.API_MIXER_ENDPOINT}/all/")
    fun getRemoteViewers() : Single<MutableList<RemoteViewerRemote>>

    @POST(Constants.API_MIXER_ENDPOINT)
    fun addRemoteViewer(@Body remoteViewer: RemoteViewer) : Single<RemoteViewerRemote>

    @PUT(Constants.API_MIXER_ENDPOINT)
    fun updateRemoteViewer(@Body remoteViewer: RemoteViewer) : Single<RemoteViewerRemote>
}