package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Product
import com.basculasmagris.visorremotomixer.model.entities.ProductRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProductAPI {
    @GET("${Constants.API_PRODUCT_ENDPOINT}/all/")
    fun getProducts() : Single<MutableList<ProductRemote>>

    @POST(Constants.API_PRODUCT_ENDPOINT)
    fun addProduct(@Body product: Product) : Single<ProductRemote>

    @PUT(Constants.API_PRODUCT_ENDPOINT)
    fun updateProduct(@Body product: Product) : Single<ProductRemote>
}