package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J!\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018J\u0019\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001aJ\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010\u001c\u001a\u00020\u0016J\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u001e0\u00062\u0006\u0010\u001f\u001a\u00020 J\u001a\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\u00070\u00062\u0006\u0010#\u001a\u00020\u0016J\u0016\u0010$\u001a\b\u0012\u0004\u0012\u00020\"0\u001e2\u0006\u0010#\u001a\u00020\u0016H\u0007J\u0019\u0010%\u001a\u00020\u00162\u0006\u0010\u0012\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010&\u001a\u00020\u00112\u0006\u0010\'\u001a\u00020\u000eH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010(J!\u0010)\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010*\u001a\u00020 H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+J!\u0010,\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010-\u001a\u00020\u0016H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018J\u0019\u0010.\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J9\u0010/\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000203H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00066"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/DietRepository;", "", "dietDao", "Lcom/basculasmagris/visorremotomixer/model/database/DietDao;", "(Lcom/basculasmagris/visorremotomixer/model/database/DietDao;)V", "activeDietList", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Diet;", "getActiveDietList", "()Lkotlinx/coroutines/flow/Flow;", "allDietList", "getAllDietList", "getAllDietProductList", "Lcom/basculasmagris/visorremotomixer/model/entities/DietProduct;", "getGetAllDietProductList", "deleteDietData", "", "diet", "(Lcom/basculasmagris/visorremotomixer/model/entities/Diet;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteDietProductData", "dietId", "", "productId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteProductsByDietData", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDietById", "id", "getFilteredDietList", "", "value", "", "getProductsBy", "Lcom/basculasmagris/visorremotomixer/model/entities/DietProductDetail;", "idDiet", "getSyncProductsBy", "insertDietData", "insertDietProductData", "dietProduct", "(Lcom/basculasmagris/visorremotomixer/model/entities/DietProduct;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedRemoteId", "remoteId", "updateDietData", "updateDietProductData", "order", "", "weight", "", "percentage", "(JJIDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class DietRepository {
    private final com.basculasmagris.visorremotomixer.model.database.DietDao dietDao = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> allDietList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> activeDietList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProduct>> getAllDietProductList = null;
    
    public DietRepository(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.DietDao dietDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insertDietData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insertDietProductData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.DietProduct dietProduct, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getAllDietList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getActiveDietList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.Diet> getDietById(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getFilteredDietList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateDietData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateDietProductData(long dietId, long productId, int order, double weight, double percentage, @org.jetbrains.annotations.NotNull
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
    public final java.lang.Object deleteDietData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteDietProductData(long dietId, long productId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteProductsByDietData(long dietId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProductDetail>> getProductsBy(long idDiet) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.annotation.WorkerThread
    public final java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProductDetail> getSyncProductsBy(long idDiet) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProduct>> getGetAllDietProductList() {
        return null;
    }
}