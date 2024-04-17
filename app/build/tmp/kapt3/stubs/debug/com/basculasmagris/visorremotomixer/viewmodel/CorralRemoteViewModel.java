package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u001fJ\u000e\u0010#\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0007R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0007R\u001f\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00140\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0007R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0007R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u0019\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007\u00a8\u0006$"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/CorralRemoteViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "addCorralErrorResponse", "Landroidx/lifecycle/MutableLiveData;", "", "getAddCorralErrorResponse", "()Landroidx/lifecycle/MutableLiveData;", "addCorralsLoad", "getAddCorralsLoad", "addCorralsResponse", "Lcom/basculasmagris/visorremotomixer/model/entities/CorralRemote;", "getAddCorralsResponse", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "corralApiService", "Lcom/basculasmagris/visorremotomixer/model/network/CorralApiService;", "corralsLoadingError", "getCorralsLoadingError", "corralsResponse", "", "getCorralsResponse", "loadCorral", "getLoadCorral", "updateCorralsErrorResponse", "getUpdateCorralsErrorResponse", "updateCorralsLoad", "getUpdateCorralsLoad", "updateCorralsResponse", "getUpdateCorralsResponse", "addCorralFromAPI", "", "corral", "Lcom/basculasmagris/visorremotomixer/model/entities/Corral;", "getCorralsFromAPI", "updateCorralFromAPI", "app_debug"})
public final class CorralRemoteViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.network.CorralApiService corralApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadCorral = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.CorralRemote>> corralsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> corralsLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> addCorralsLoad = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.CorralRemote> addCorralsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> addCorralErrorResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> updateCorralsLoad = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.CorralRemote> updateCorralsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> updateCorralsErrorResponse = null;
    
    public CorralRemoteViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLoadCorral() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.CorralRemote>> getCorralsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getCorralsLoadingError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getAddCorralsLoad() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.CorralRemote> getAddCorralsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getAddCorralErrorResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getUpdateCorralsLoad() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.CorralRemote> getUpdateCorralsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getUpdateCorralsErrorResponse() {
        return null;
    }
    
    public final void getCorralsFromAPI() {
    }
    
    public final void addCorralFromAPI(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Corral corral) {
    }
    
    public final void updateCorralFromAPI(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Corral corral) {
    }
}