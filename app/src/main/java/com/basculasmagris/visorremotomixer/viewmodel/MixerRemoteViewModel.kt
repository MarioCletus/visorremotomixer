package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.MixerRemote
import com.basculasmagris.visorremotomixer.model.network.MixerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.withContext

class MixerRemoteViewModel : ViewModel() {

    private val mixerApiService = MixerApiService()

    // Get
    val loadMixer = MutableLiveData<Boolean>()
    val mixersResponse = MutableLiveData<MutableList<MixerRemote>?>()
    val mixersLoadingError = MutableLiveData<Boolean>()

    suspend fun getMixersFromAPI_SUSPEND(): MutableList<MixerRemote> =
        withContext(Dispatchers.Main) {

            loadMixer.value = true
            mixersLoadingError.value = false

            try {
                val value = mixerApiService.getMixers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .await()

                mixersResponse.value = value   // opcional para LiveData/UI
                loadMixer.value = false
                value
            } catch (t: Throwable) {
                loadMixer.value = false
                mixersLoadingError.value = true
                throw t
            }
        }

}