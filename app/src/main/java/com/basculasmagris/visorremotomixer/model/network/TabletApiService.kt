package com.basculasmagris.visorremotomixer.model.network



import com.basculasmagris.visorremotomixer.model.entities.TabletRemote
import com.basculasmagris.visorremotomixer.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TabletApiService : BaseService() {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(defaultHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(TabletAPI::class.java)


    fun requestSerial(username: String, password: String,serial: String, appVersion: String): Single<TabletRemote> {
        val tablet: HashMap<String, String> = HashMap()
        tablet["username"] = username
        tablet["password"] = password
        tablet["serial"] = serial
        tablet["app_version"] = appVersion
        return api.requestSerial(tablet)
    }

    fun getTablets(codeClient: String): Single<MutableList<TabletRemote>> {
        val tablet: HashMap<String, String> = HashMap()
        tablet["code_client"] = codeClient
        return api.getTablets(tablet)
    }

    fun updateTablet(codeClient: String, serial: String, appVersion: String): Single<TabletRemote>  {
        val tablet: HashMap<String, String> = HashMap()
        tablet["code_client"] = codeClient
        tablet["serial"] = serial
        tablet["app_version"] = appVersion
        return api.updateTablet(tablet)
    }

    fun isRegistered(codeClient: String, serial: String): Single<Boolean> {
        val tablet: HashMap<String, String> = HashMap()
        tablet["code_client"] = codeClient
        tablet["serial"] = serial
        return api.isRegistered(tablet)
    }


}