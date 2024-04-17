package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0010\u0006\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bJ\u001a\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010 \u001a\u00020!J\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010#\u001a\u00020$J\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bJ\u0019\u0010(\u001a\u00020$2\u0006\u0010\u001e\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\u0016\u0010*\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u0010+\u001a\u00020!J\u0016\u0010,\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u0010-\u001a\u00020!J\u0016\u0010.\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u0010/\u001a\u00020$J\u000e\u00100\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bJ\u0019\u00101\u001a\u00020&2\u0006\u0010\u001e\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\u0016\u00102\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u00103\u001a\u000204R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u001f\u0010\u0019\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00065"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/MixerViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/basculasmagris/visorremotomixer/model/database/MixerRepository;", "(Lcom/basculasmagris/visorremotomixer/model/database/MixerRepository;)V", "activeMixerList", "Landroidx/lifecycle/LiveData;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Mixer;", "getActiveMixerList", "()Landroidx/lifecycle/LiveData;", "allMixerList", "", "getAllMixerList", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "loadMixer", "Landroidx/lifecycle/MutableLiveData;", "", "getLoadMixer", "()Landroidx/lifecycle/MutableLiveData;", "mixerApiService", "Lcom/basculasmagris/visorremotomixer/model/network/MixerApiService;", "mixersLoadingError", "getMixersLoadingError", "mixersResponse", "Lcom/basculasmagris/visorremotomixer/model/entities/MixerRemote;", "getMixersResponse", "delete", "Lkotlinx/coroutines/Job;", "mixer", "getFilteredMixerList", "value", "", "getMixerById", "id", "", "getMixersFromAPI", "", "insert", "insertSync", "(Lcom/basculasmagris/visorremotomixer/model/entities/Mixer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "setUpdatedMacAddress", "mac", "setUpdatedRemoteId", "remoteId", "update", "updateSync", "updateTara", "weight", "", "app_debug"})
public final class MixerViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.database.MixerRepository repository = null;
    private final com.basculasmagris.visorremotomixer.model.network.MixerApiService mixerApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadMixer = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.MixerRemote>> mixersResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> mixersLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Mixer>> allMixerList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Mixer>> activeMixerList = null;
    
    public MixerViewModel(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.MixerRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLoadMixer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.MixerRemote>> getMixersResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getMixersLoadingError() {
        return null;
    }
    
    public final void getMixersFromAPI() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Mixer>> getAllMixerList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Mixer>> getActiveMixerList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.basculasmagris.visorremotomixer.model.entities.Mixer> getMixerById(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Mixer>> getFilteredMixerList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateTara(long id, double weight) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedMacAddress(long id, @org.jetbrains.annotations.NotNull
    java.lang.String mac) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedRemoteId(long id, long remoteId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer) {
        return null;
    }
}