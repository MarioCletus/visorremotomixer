package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u001fJ\u000e\u0010#\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0007R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0007R\u001f\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00140\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0007R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0007R\u0019\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0007\u00a8\u0006$"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/TabletMixerRemoteViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "addTabletMixerErrorResponse", "Landroidx/lifecycle/MutableLiveData;", "", "getAddTabletMixerErrorResponse", "()Landroidx/lifecycle/MutableLiveData;", "addTabletMixersLoad", "getAddTabletMixersLoad", "addTabletMixersResponse", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixerRemote;", "getAddTabletMixersResponse", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "loadTabletMixer", "getLoadTabletMixer", "remoteViewersLoadingError", "getRemoteViewersLoadingError", "remoteViewersResponse", "", "getRemoteViewersResponse", "tabletMixerApiService", "Lcom/basculasmagris/visorremotomixer/model/network/TabletMixerApiService;", "updateTabletMixersErrorResponse", "getUpdateTabletMixersErrorResponse", "updateTabletMixersLoad", "getUpdateTabletMixersLoad", "updateTabletMixersResponse", "getUpdateTabletMixersResponse", "addTabletMixerFromAPI", "", "tabletMixer", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "getTabletMixersFromAPI", "updateTabletMixerFromAPI", "app_release"})
public final class TabletMixerRemoteViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.network.TabletMixerApiService tabletMixerApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadTabletMixer = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote>> remoteViewersResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> remoteViewersLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> addTabletMixersLoad = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote> addTabletMixersResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> addTabletMixerErrorResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> updateTabletMixersLoad = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote> updateTabletMixersResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> updateTabletMixersErrorResponse = null;
    
    public TabletMixerRemoteViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLoadTabletMixer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote>> getRemoteViewersResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getRemoteViewersLoadingError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getAddTabletMixersLoad() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote> getAddTabletMixersResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getAddTabletMixerErrorResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getUpdateTabletMixersLoad() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<com.basculasmagris.visorremotomixer.model.entities.TabletMixerRemote> getUpdateTabletMixersResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getUpdateTabletMixersErrorResponse() {
        return null;
    }
    
    public final void getTabletMixersFromAPI() {
    }
    
    public final void addTabletMixerFromAPI(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer) {
    }
    
    public final void updateTabletMixerFromAPI(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer) {
    }
}