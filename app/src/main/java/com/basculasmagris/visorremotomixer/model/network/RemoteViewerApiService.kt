package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RemoteViewerRemote
import com.basculasmagris.visorremotomixer.model.network.BaseService
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.model.entities.RemoteViewer
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RemoteViewerApiService : BaseService()  {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RemoteViewerAPI::class.java)

    fun getRemoteViewers(): Single<MutableList<RemoteViewerRemote>> {
        return api.getRemoteViewers()
    }

    fun addRemoteViewer(remoteViewer: RemoteViewer): Single<RemoteViewerRemote> {
        return api.addRemoteViewer(remoteViewer)
    }

    fun updateRemoteViewers(remoteViewer: RemoteViewer): Single<RemoteViewerRemote> {
        return api.updateRemoteViewer(remoteViewer)
    }
}