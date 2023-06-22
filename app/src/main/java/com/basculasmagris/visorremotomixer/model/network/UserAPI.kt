package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.RoundRemote
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.model.entities.UserRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

internal interface UserAPI {
    @POST("${Constants.API_USER_ENDPOINT}/auth")
    fun login(@Body user: HashMap<String,String>) : Single<UserRemote>

    @GET("${Constants.API_USER_ENDPOINT}")
    fun getUsers() : Single<MutableList<UserRemote>>

    @POST(Constants.API_USER_ENDPOINT)
    fun addUser(@Body user: User) : Single<UserRemote>

    @PUT(Constants.API_USER_ENDPOINT)
    fun updateUser(@Body user: User) : Single<UserRemote>
}