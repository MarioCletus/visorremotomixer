package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.TabletRemote
import com.basculasmagris.visorremotomixer.model.network.TabletApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.withContext


class TabletRemoteViewModel : ViewModel() {
    private val tabletApiService = TabletApiService()

    val loadTablet = MutableLiveData<Boolean>()
    var tabletResponse = MutableLiveData<MutableList<TabletRemote>?>()
    val tabletLoadingError = MutableLiveData<Boolean>()

    suspend fun getTabletFromAPI_SUSPEND(codeClient :String): MutableList<TabletRemote> =
        withContext(Dispatchers.Main) {

            loadTablet.value = true
            tabletLoadingError.value = false

            try {
                val value = tabletApiService.getTablets(codeClient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .await()

                tabletResponse.value = value   // opcional para LiveData/UI
                loadTablet.value = false
                value
            } catch (t: Throwable) {
                loadTablet.value = false
                tabletLoadingError.value = true
                throw t
            }
        }

}


