package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Diet
import com.basculasmagris.visorremotomixer.model.entities.DietRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DietApiService : BaseService()  {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(DietAPI::class.java)

    fun getDiets(): Single<MutableList<DietRemote>> {
        return api.getDiets()
    }

    fun addDiet(diet: DietRemote): Single<DietRemote> {
        return api.addDiet(diet)
    }

    fun updateDiets(diet: DietRemote): Single<DietRemote> {
        return api.updateDiet(diet)
    }
}