package com.basculasmagris.visorremotomixer.model.network
import com.basculasmagris.visorremotomixer.model.entities.Establishment
import com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



class EstablishmentApiService: BaseService() {

    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(EstablishmentAPI::class.java)

    fun getEstablishments(): Single<MutableList<EstablishmentRemote>> {
        return api.getEstablishments()
    }

    fun addEstablishment(establishment: Establishment): Single<EstablishmentRemote> {
        return api.addEstablishment(establishment)
    }

    fun updateEstablishments(establishment: Establishment): Single<EstablishmentRemote> {
        return api.updateEstablishment(establishment)
    }
}