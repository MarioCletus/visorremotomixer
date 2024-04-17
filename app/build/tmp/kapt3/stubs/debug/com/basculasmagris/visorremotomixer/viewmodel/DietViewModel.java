package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\bJ\u0016\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$J\u0019\u0010&\u001a\u00020\'2\u0006\u0010#\u001a\u00020$H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010(J\u0014\u0010)\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010*\u001a\u00020$J\u001a\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u001a0\u00062\u0006\u0010,\u001a\u00020-J\u001a\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0\u00070\u00062\u0006\u00100\u001a\u00020$J\u0014\u00101\u001a\b\u0012\u0004\u0012\u00020/0\u001a2\u0006\u00100\u001a\u00020$J\u000e\u00102\u001a\u00020 2\u0006\u0010!\u001a\u00020\bJ\u000e\u00103\u001a\u00020 2\u0006\u00104\u001a\u00020\u000eJ\u0019\u00105\u001a\u00020$2\u0006\u0010!\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00106J\u0016\u00107\u001a\u00020 2\u0006\u0010*\u001a\u00020$2\u0006\u00108\u001a\u00020-J\u0016\u00109\u001a\u00020 2\u0006\u0010*\u001a\u00020$2\u0006\u0010:\u001a\u00020$J\u000e\u0010;\u001a\u00020 2\u0006\u0010!\u001a\u00020\bJ.\u0010<\u001a\u00020 2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$2\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@2\u0006\u0010A\u001a\u00020@J\u0019\u0010B\u001a\u00020\'2\u0006\u0010!\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00106R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\nR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001f\u0010\u0019\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0018R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006C"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/DietViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/basculasmagris/visorremotomixer/model/database/DietRepository;", "(Lcom/basculasmagris/visorremotomixer/model/database/DietRepository;)V", "activeDietList", "Landroidx/lifecycle/LiveData;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Diet;", "getActiveDietList", "()Landroidx/lifecycle/LiveData;", "allDietList", "getAllDietList", "allDietProductList", "Lcom/basculasmagris/visorremotomixer/model/entities/DietProduct;", "getAllDietProductList", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "dietApiService", "Lcom/basculasmagris/visorremotomixer/model/network/DietApiService;", "dietsLoadingError", "Landroidx/lifecycle/MutableLiveData;", "", "getDietsLoadingError", "()Landroidx/lifecycle/MutableLiveData;", "dietsResponse", "", "Lcom/basculasmagris/visorremotomixer/model/entities/DietRemote;", "getDietsResponse", "loadDiet", "getLoadDiet", "delete", "Lkotlinx/coroutines/Job;", "diet", "deleteProductBy", "dietId", "", "productId", "deleteProductByDiet", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDietById", "id", "getFilteredDietList", "value", "", "getProductsBy", "Lcom/basculasmagris/visorremotomixer/model/entities/DietProductDetail;", "idDiet", "getSyncProductsBy", "insert", "insertDietProduct", "dietProduct", "insertSync", "(Lcom/basculasmagris/visorremotomixer/model/entities/Diet;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "setUpdatedRemoteId", "remoteId", "update", "updateProductBy", "order", "", "weight", "", "percentage", "updateSync", "app_debug"})
public final class DietViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.database.DietRepository repository = null;
    private final com.basculasmagris.visorremotomixer.model.network.DietApiService dietApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadDiet = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietRemote>> dietsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> dietsLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> allDietList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> activeDietList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProduct>> allDietProductList = null;
    
    public DietViewModel(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.DietRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLoadDiet() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietRemote>> getDietsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getDietsLoadingError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insertDietProduct(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.DietProduct dietProduct) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getAllDietList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getActiveDietList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProduct>> getAllDietProductList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProductDetail>> getProductsBy(long idDiet) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProductDetail> getSyncProductsBy(long idDiet) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.basculasmagris.visorremotomixer.model.entities.Diet> getDietById(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getFilteredDietList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateProductBy(long dietId, long productId, int order, double weight, double percentage) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedRemoteId(long id, long remoteId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteProductBy(long dietId, long productId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteProductByDiet(long dietId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}