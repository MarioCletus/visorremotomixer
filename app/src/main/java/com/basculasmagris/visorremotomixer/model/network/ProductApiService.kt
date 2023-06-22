package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.Product
import com.basculasmagris.visorremotomixer.model.entities.ProductRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProductApiService : BaseService()  {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ProductAPI::class.java)

    fun getProducts(): Single<MutableList<ProductRemote>> {
        return api.getProducts()
    }

    fun addProduct(product: Product): Single<ProductRemote> {
        return api.addProduct(product)
    }

    fun updateProducts(product: Product): Single<ProductRemote> {
        return api.updateProduct(product)
    }
}