package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@androidx.room.Dao
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J!\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000eH\'J\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000eH\'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u000f0\u000eH\'J\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e2\u0006\u0010\u0014\u001a\u00020\bH\'J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00160\u000e2\u0006\u0010\u0017\u001a\u00020\u0018H\'J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u000f0\u000e2\u0006\u0010\u001b\u001a\u00020\bH\'J\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00162\u0006\u0010\u001b\u001a\u00020\bH\'J\u0019\u0010\u001d\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u0012H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J!\u0010!\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\u0018H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J!\u0010$\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010%\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010&\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J9\u0010\'\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006."}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/DietDao;", "", "deleteDiet", "", "diet", "Lcom/basculasmagris/visorremotomixer/model/entities/Diet;", "(Lcom/basculasmagris/visorremotomixer/model/entities/Diet;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "dietId", "", "productId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteProductsByDiet", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveDietList", "Lkotlinx/coroutines/flow/Flow;", "", "getAllDietList", "getAllDietProductList", "Lcom/basculasmagris/visorremotomixer/model/entities/DietProduct;", "getDietById", "id", "getFilteredDietList", "", "name", "", "getProductsBy", "Lcom/basculasmagris/visorremotomixer/model/entities/DietProductDetail;", "idDiet", "getSyncProductsBy", "insertDiet", "insertDietProduct", "dietProduct", "(Lcom/basculasmagris/visorremotomixer/model/entities/DietProduct;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedRemoteId", "remoteId", "updateDiet", "updateDietProduct", "order", "", "weight", "", "percentage", "(JJIDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface DietDao {
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert
    public abstract java.lang.Object insertDiet(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM diet WHERE archive_date IS NULL OR archive_date = \'\' ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getActiveDietList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM diet ORDER BY id DESC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getAllDietList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM diet WHERE archive_date IS NULL OR archive_date = \'\' ORDER BY name LIKE :name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Diet>> getFilteredDietList(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM diet WHERE id = :id")
    public abstract kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.Diet> getDietById(long id);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Update
    public abstract java.lang.Object updateDiet(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE diet SET updated_date = :date WHERE id = :id")
    public abstract java.lang.Object setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE diet SET remote_id = :remoteId WHERE id = :id")
    public abstract java.lang.Object setUpdatedRemoteId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Delete
    public abstract java.lang.Object deleteDiet(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Diet diet, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM diet_product WHERE diet_id = :dietId AND product_id = :productId")
    public abstract java.lang.Object deleteDiet(long dietId, long productId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM diet_product WHERE diet_id = :dietId")
    public abstract java.lang.Object deleteProductsByDiet(long dietId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT dp.remote_diet_id as remoteDietId, dp.remote_product_id as remoteProductId, dp.product_id as productId, p.name as productName, p.description as productDescription, dp.diet_id as dietId,dp.percentage, dp.weight, dp.`order` FROM diet_product as dp JOIN product as p ON p.id = dp.product_id WHERE diet_id = :idDiet ORDER BY dp.`order`")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProductDetail>> getProductsBy(long idDiet);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT  dp.remote_diet_id as remoteDietId, dp.remote_product_id as remoteProductId, dp.product_id as productId, p.name as productName, p.description as productDescription, dp.diet_id as dietId,dp.percentage, dp.weight, dp.`order` FROM diet_product as dp JOIN product as p ON p.id = dp.product_id WHERE diet_id = :idDiet ORDER BY dp.`order`")
    public abstract java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProductDetail> getSyncProductsBy(long idDiet);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM diet_product as dp ORDER BY dp.`order`")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.DietProduct>> getAllDietProductList();
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert
    public abstract java.lang.Object insertDietProduct(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.DietProduct dietProduct, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE diet_product SET `order` = :order, weight = :weight, percentage = :percentage WHERE diet_id = :dietId AND product_id = :productId")
    public abstract java.lang.Object updateDietProduct(long dietId, long productId, int order, double weight, double percentage, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
}