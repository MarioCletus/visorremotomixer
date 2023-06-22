package com.basculasmagris.visorremotomixer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.visorremotomixer.model.entities.RoundRunReportRemote
import com.basculasmagris.visorremotomixer.model.network.ReportApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RoundRunReportRemoteViewModel : ViewModel() {

    private val reportApiService = ReportApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadReport = MutableLiveData<Boolean>()
    var reportsResponse = MutableLiveData<MutableList<RoundRunReportRemote>?>()
    val reportsLoadingError = MutableLiveData<Boolean>()

    //Post
    val addReportsLoad = MutableLiveData<Boolean>()
    val addReportsResponse = MutableLiveData<RoundRunReportRemote?>()
    val addReportErrorResponse = MutableLiveData<Boolean>()

    fun getReportsFromAPI() {
        loadReport.value = true
        compositeDisposable.add(
            reportApiService.getReports()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<RoundRunReportRemote>>() {
                    override fun onSuccess(value: MutableList<RoundRunReportRemote>?) {
                        reportsResponse.value = value
                        reportsLoadingError.value = false
                        loadReport.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadReport.value = false
                        reportsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addReportFromAPI(report: RoundRunReportRemote){
        addReportsLoad.value = true
        compositeDisposable.add(
            reportApiService.addReport(report)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RoundRunReportRemote>() {
                    override fun onSuccess(value: RoundRunReportRemote?) {
                        addReportsResponse.value = value
                        addReportErrorResponse.value = false
                        addReportsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addReportsLoad.value = false
                        addReportErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }
}