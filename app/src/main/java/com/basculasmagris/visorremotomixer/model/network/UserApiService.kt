package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Establishment
import com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UserApiService : BaseService()  {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(UserAPI::class.java)

    fun login(username: String, password: String): Single<UserRemote> {
        val map: HashMap<String, String> = HashMap()
        map["username"] = username
        map["password"] = password
        return api.login(map)
    }

    fun getUsers(): Single<MutableList<UserRemote>> {
        return api.getUsers()
    }

    fun addUser(user: User): Single<UserRemote> {
        return api.addUser(user)
    }

    fun updateUser(user: User): Single<UserRemote> {
        return api.updateUser(user)
    }
}