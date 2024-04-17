package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bJ\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010 \u001a\u00020!J\u001a\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010#\u001a\u00020$J\u000e\u0010%\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bJ\u0019\u0010&\u001a\u00020!2\u0006\u0010\u001e\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\'J\u0016\u0010(\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020!2\u0006\u0010)\u001a\u00020$J\u0016\u0010*\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020!2\u0006\u0010+\u001a\u00020!J\u000e\u0010,\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bJ\u0019\u0010-\u001a\u00020.2\u0006\u0010\u001e\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\'R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001f\u0010\u0017\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u00070\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0016R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006/"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/EstablishmentViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/basculasmagris/visorremotomixer/model/database/EstablishmentRepository;", "(Lcom/basculasmagris/visorremotomixer/model/database/EstablishmentRepository;)V", "activeEstablishmentList", "Landroidx/lifecycle/LiveData;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Establishment;", "getActiveEstablishmentList", "()Landroidx/lifecycle/LiveData;", "allEstablishmentList", "", "getAllEstablishmentList", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "establishmentApiService", "Lcom/basculasmagris/visorremotomixer/model/network/EstablishmentApiService;", "establishmentsLoadingError", "Landroidx/lifecycle/MutableLiveData;", "", "getEstablishmentsLoadingError", "()Landroidx/lifecycle/MutableLiveData;", "establishmentsResponse", "Lcom/basculasmagris/visorremotomixer/model/entities/EstablishmentRemote;", "getEstablishmentsResponse", "loadEstablishment", "getLoadEstablishment", "delete", "Lkotlinx/coroutines/Job;", "establishment", "getEstablishmentById", "id", "", "getFilteredEstablishmentList", "value", "", "insert", "insertSync", "(Lcom/basculasmagris/visorremotomixer/model/entities/Establishment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "setUpdatedRemoteId", "remoteId", "update", "updateSync", "", "app_debug"})
public final class EstablishmentViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.database.EstablishmentRepository repository = null;
    private final com.basculasmagris.visorremotomixer.model.network.EstablishmentApiService establishmentApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadEstablishment = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote>> establishmentsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> establishmentsLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Establishment>> allEstablishmentList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Establishment>> activeEstablishmentList = null;
    
    public EstablishmentViewModel(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.EstablishmentRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLoadEstablishment() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote>> getEstablishmentsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getEstablishmentsLoadingError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Establishment>> getAllEstablishmentList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Establishment>> getActiveEstablishmentList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.basculasmagris.visorremotomixer.model.entities.Establishment> getEstablishmentById(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Establishment>> getFilteredEstablishmentList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedRemoteId(long id, long remoteId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment) {
        return null;
    }
}