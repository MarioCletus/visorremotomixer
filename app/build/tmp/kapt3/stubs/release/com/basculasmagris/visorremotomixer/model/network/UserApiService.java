package com.basculasmagris.visorremotomixer.model.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\u0012\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u0007J\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fJ\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/network/UserApiService;", "Lcom/basculasmagris/visorremotomixer/model/network/BaseService;", "()V", "api", "Lcom/basculasmagris/visorremotomixer/model/network/UserAPI;", "kotlin.jvm.PlatformType", "addUser", "Lio/reactivex/rxjava3/core/Single;", "Lcom/basculasmagris/visorremotomixer/model/entities/UserRemote;", "user", "Lcom/basculasmagris/visorremotomixer/model/entities/User;", "getUsers", "", "login", "username", "", "password", "updateUser", "app_release"})
public final class UserApiService extends com.basculasmagris.visorremotomixer.model.network.BaseService {
    private final com.basculasmagris.visorremotomixer.model.network.UserAPI api = null;
    
    public UserApiService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.UserRemote> login(@org.jetbrains.annotations.NotNull
    java.lang.String username, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<java.util.List<com.basculasmagris.visorremotomixer.model.entities.UserRemote>> getUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.UserRemote> addUser(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.User user) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.UserRemote> updateUser(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.User user) {
        return null;
    }
}