package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020!J\u000e\u0010%\u001a\u00020!2\u0006\u0010\"\u001a\u00020#R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0007R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0007R(\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00140\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0007\"\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0007R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007R\u0019\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0007\u00a8\u0006&"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/EstablishmentRemoteViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "addEstablishmentErrorResponse", "Landroidx/lifecycle/MutableLiveData;", "", "getAddEstablishmentErrorResponse", "()Landroidx/lifecycle/MutableLiveData;", "addEstablishmentsLoad", "getAddEstablishmentsLoad", "addEstablishmentsResponse", "Lcom/basculasmagris/visorremotomixer/model/entities/EstablishmentRemote;", "getAddEstablishmentsResponse", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "establishmentApiService", "Lcom/basculasmagris/visorremotomixer/model/network/EstablishmentApiService;", "establishmentsLoadingError", "getEstablishmentsLoadingError", "establishmentsResponse", "", "getEstablishmentsResponse", "setEstablishmentsResponse", "(Landroidx/lifecycle/MutableLiveData;)V", "loadEstablishment", "getLoadEstablishment", "updateEstablishmentsErrorResponse", "getUpdateEstablishmentsErrorResponse", "updateEstablishmentsLoad", "getUpdateEstablishmentsLoad", "updateEstablishmentsResponse", "getUpdateEstablishmentsResponse", "addEstablishmentFromAPI", "", "establishment", "Lcom/basculasmagris/visorremotomixer/model/entities/Establishment;", "getEstablishmentsFromAPI", "updateEstablishmentFromAPI", "app_debug"})
public final class EstablishmentRemoteViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.network.EstablishmentApiService establishmentApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadEstablishment = null;
    @org.jetbrains.annotations.NotNull
    private androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote>> establishmentsResponse;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> establishmentsLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> addEstablishmentsLoad = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote> addEstablishmentsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> addEstablishmentErrorResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> updateEstablishmentsLoad = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote> updateEstablishmentsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> updateEstablishmentsErrorResponse = null;
    
    public EstablishmentRemoteViewModel() {
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
    
    public final void setEstablishmentsResponse(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote>> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getEstablishmentsLoadingError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getAddEstablishmentsLoad() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote> getAddEstablishmentsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getAddEstablishmentErrorResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getUpdateEstablishmentsLoad() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote> getUpdateEstablishmentsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getUpdateEstablishmentsErrorResponse() {
        return null;
    }
    
    public final void getEstablishmentsFromAPI() {
    }
    
    public final void addEstablishmentFromAPI(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment) {
    }
    
    public final void updateEstablishmentFromAPI(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment) {
    }
}