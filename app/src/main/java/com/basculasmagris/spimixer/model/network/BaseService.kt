package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.utils.Session
import okhttp3.OkHttpClient
import okhttp3.Request

open class BaseService {
    var defaultHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("access-token", Session.accessToken).build()
            chain.proceed(request)
        }.build()
}