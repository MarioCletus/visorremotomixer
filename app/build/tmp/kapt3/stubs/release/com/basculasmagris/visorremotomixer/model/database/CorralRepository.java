package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010\u0013\u001a\u00020\u0014J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010\u0013\u001a\u00020\u0016J\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\u0018\u001a\u00020\u0019J\u0019\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u0019H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001dJ!\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J!\u0010!\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J!\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\u0016H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\u0019\u0010&\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\'"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/CorralRepository;", "", "corralDao", "Lcom/basculasmagris/visorremotomixer/model/database/CorralDao;", "(Lcom/basculasmagris/visorremotomixer/model/database/CorralDao;)V", "activeCorralList", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Corral;", "getActiveCorralList", "()Lkotlinx/coroutines/flow/Flow;", "allCorralList", "", "getAllCorralList", "deleteCorralData", "", "corral", "(Lcom/basculasmagris/visorremotomixer/model/entities/Corral;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCorralByEstablishment", "id", "", "getCorralById", "", "getFilteredCorralList", "value", "", "insertCorralData", "setUpdatedDate", "date", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedEstablishmentRemoteId", "remoteId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedRemoteId", "updateCorralAnimals", "corralId", "animalQuantity", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCorralData", "app_release"})
public final class CorralRepository {
    private final com.basculasmagris.visorremotomixer.model.database.CorralDao corralDao = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Corral>> allCorralList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Corral>> activeCorralList = null;
    
    public CorralRepository(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.CorralDao corralDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insertCorralData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Corral corral, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Corral>> getAllCorralList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Corral>> getActiveCorralList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.Corral> getCorralById(int id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.Corral> getCorralByEstablishment(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Corral>> getFilteredCorralList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateCorralData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Corral corral, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object setUpdatedRemoteId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object setUpdatedEstablishmentRemoteId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteCorralData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Corral corral, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateCorralAnimals(int corralId, int animalQuantity, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}