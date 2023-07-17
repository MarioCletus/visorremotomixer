package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TabletMixerApiService : BaseService()  {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(TabletMixerAPI::class.java)

    fun getTabletMixers(): Single<MutableList<TabletMixerRemote>> {
        return api.getTabletMixers()
    }

    fun addTabletMixer(tabletMixer: TabletMixer): Single<TabletMixerRemote> {
        return api.addTabletMixer(tabletMixer)
    }

    fun updateTabletMixer(tabletMixer: TabletMixer): Single<TabletMixerRemote> {
        return api.updateTabletMixer(tabletMixer)
    }
}