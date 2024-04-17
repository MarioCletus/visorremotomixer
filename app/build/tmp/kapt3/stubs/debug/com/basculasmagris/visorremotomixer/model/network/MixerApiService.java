package com.basculasmagris.visorremotomixer.model.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\u0012\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u0007J\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/network/MixerApiService;", "Lcom/basculasmagris/visorremotomixer/model/network/BaseService;", "()V", "api", "Lcom/basculasmagris/visorremotomixer/model/network/MixerAPI;", "kotlin.jvm.PlatformType", "addMixer", "Lio/reactivex/rxjava3/core/Single;", "Lcom/basculasmagris/visorremotomixer/model/entities/MixerRemote;", "mixer", "Lcom/basculasmagris/visorremotomixer/model/entities/Mixer;", "getMixers", "", "updateMixers", "app_debug"})
public final class MixerApiService extends com.basculasmagris.visorremotomixer.model.network.BaseService {
    private final com.basculasmagris.visorremotomixer.model.network.MixerAPI api = null;
    
    public MixerApiService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<java.util.List<com.basculasmagris.visorremotomixer.model.entities.MixerRemote>> getMixers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.MixerRemote> addMixer(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.MixerRemote> updateMixers(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixer) {
        return null;
    }
}