package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bJ\u0006\u0010\u0011\u001a\u00020\u000fJ\u000e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bJ\u000e\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/RoundLocalViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalRepository;", "(Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalRepository;)V", "allRoundLocalList", "Landroidx/lifecycle/LiveData;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundLocal;", "getAllRoundLocalList", "()Landroidx/lifecycle/LiveData;", "allRoundLocalListByMac", "mac", "", "delete", "Lkotlinx/coroutines/Job;", "roundLocal", "deleteAllRoundLocal", "deleteRounLocalById", "round_local_id", "", "insert", "update", "app_release"})
public final class RoundLocalViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.database.RoundLocalRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal>> allRoundLocalList = null;
    
    public RoundLocalViewModel(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.RoundLocalRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundLocal roundLocal) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundLocal roundLocal) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteAllRoundLocal() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteRounLocalById(long round_local_id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundLocal roundLocal) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal>> allRoundLocalListByMac(@org.jetbrains.annotations.NotNull
    java.lang.String mac) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal>> getAllRoundLocalList() {
        return null;
    }
}