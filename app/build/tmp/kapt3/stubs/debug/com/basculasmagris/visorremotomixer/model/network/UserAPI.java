package com.basculasmagris.visorremotomixer.model.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\b`\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\b0\u0003H\'J4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032$\b\u0001\u0010\u0005\u001a\u001e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\nj\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b`\fH\'J\u0018\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\u000e"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/network/UserAPI;", "", "addUser", "Lio/reactivex/rxjava3/core/Single;", "Lcom/basculasmagris/visorremotomixer/model/entities/UserRemote;", "user", "Lcom/basculasmagris/visorremotomixer/model/entities/User;", "getUsers", "", "login", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "updateUser", "app_debug"})
public abstract interface UserAPI {
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "users//auth")
    public abstract io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.UserRemote> login(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    java.util.HashMap<java.lang.String, java.lang.String> user);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.GET(value = "users/")
    public abstract io.reactivex.rxjava3.core.Single<java.util.List<com.basculasmagris.visorremotomixer.model.entities.UserRemote>> getUsers();
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "users/")
    public abstract io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.UserRemote> addUser(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.basculasmagris.visorremotomixer.model.entities.User user);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.PUT(value = "users/")
    public abstract io.reactivex.rxjava3.core.Single<com.basculasmagris.visorremotomixer.model.entities.UserRemote> updateUser(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.basculasmagris.visorremotomixer.model.entities.User user);
}