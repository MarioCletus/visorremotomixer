package com.basculasmagris.visorremotomixer.model.network

import com.basculasmagris.visorremotomixer.model.entities.TabletRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

internal interface TabletAPI {

    @POST("${Constants.API_TABLET_ENDPOINT}/tablet_get_list_for_vr")
    fun getTablets(@Body tablet: HashMap<String,String>) : Single<MutableList<TabletRemote>>

    @POST("${Constants.API_TABLET_ENDPOINT}/requestSerialValidation")
    fun requestSerial(@Body tablet: HashMap<String,String>) : Single<TabletRemote>

    @PUT("${Constants.API_TABLET_ENDPOINT}/update_version")
    fun updateTablet(@Body tablet: HashMap<String,String>) : Single<TabletRemote>

    @POST("${Constants.API_TABLET_ENDPOINT}/isRegistered")
    fun isRegistered(@Body tablet: HashMap<String,String>) : Single<Boolean>

}