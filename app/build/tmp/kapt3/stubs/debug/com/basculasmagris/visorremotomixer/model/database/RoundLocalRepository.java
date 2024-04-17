package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\f\u001a\u00020\rJ\u0011\u0010\u000e\u001a\u00020\u000fH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0019\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0019\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0012\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalRepository;", "", "roundLocalDao", "Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalDao;", "(Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalDao;)V", "allRoundLocalList", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundLocal;", "getAllRoundLocalList", "()Lkotlinx/coroutines/flow/Flow;", "allRoundLocalListByMac", "mac", "", "deleteAllRoundLocal", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundLocalData", "roundLocal", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundLocal;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundLocalDataById", "round_local_id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertRoundLocalData", "updateRoundLocalData", "app_debug"})
public final class RoundLocalRepository {
    private final com.basculasmagris.visorremotomixer.model.database.RoundLocalDao roundLocalDao = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal>> allRoundLocalList = null;
    
    public RoundLocalRepository(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.RoundLocalDao roundLocalDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insertRoundLocalData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundLocal roundLocal, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateRoundLocalData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundLocal roundLocal, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundLocalData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundLocal roundLocal, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteAllRoundLocal(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundLocalDataById(long round_local_id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal>> getAllRoundLocalList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal>> allRoundLocalListByMac(@org.jetbrains.annotations.NotNull
    java.lang.String mac) {
        return null;
    }
}