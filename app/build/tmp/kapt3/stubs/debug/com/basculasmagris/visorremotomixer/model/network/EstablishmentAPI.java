package com.basculasmagris.visorremotomixer.model.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\b0\u0003H\'J\u0018\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\n"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/network/EstablishmentAPI;", "", "addEstablishment", "Lio/reactivex/rxjava3/core/Single;", "Lcom/basculasmagris/visorremotomixer/model/entities/EstablishmentRemote;", "establishment", "Lcom/basculasmagris/visorremotomixer/model/entities/Establishment;", "getEstablishments", "", "updateEstablishment", "app_debug"})
public abstract interface EstablishmentAPI {
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.GET(value = "establishment//all/")
    public abstract io.reactivex.rxjava3.core.Single<java.util.List<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote>> getEstablishments();
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "establishment/")
    public abstract io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote> addEstablishment(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.PUT(value = "establishment/")
    public abstract io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.EstablishmentRemote> updateEstablishment(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.basculasmagris.visorremotomixer.model.entities.Establishment establishment);
}