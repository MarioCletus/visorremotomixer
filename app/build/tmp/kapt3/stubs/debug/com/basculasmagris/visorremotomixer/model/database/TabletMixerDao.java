package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@androidx.room.Dao
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000b\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\bH\'J\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\r\u001a\u00020\u000eH\'J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u0010\u001a\u00020\u0011H\'J\u0019\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J!\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000eH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J!\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u000eH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J!\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u0011H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001aJ\u0019\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001c"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/TabletMixerDao;", "", "deleteTabletMixer", "", "tabletMixer", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "(Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveTabletMixerList", "Lkotlinx/coroutines/flow/Flow;", "", "getAllTabletMixerList", "", "getFilteredTabletMixerList", "name", "", "getTabletMixerById", "id", "", "insertTabletMixer", "setUpdatedDate", "date", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedMacAddress", "mac", "setUpdatedRemoteId", "remoteId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTabletMixer", "app_debug"})
public abstract interface TabletMixerDao {
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert
    public abstract java.lang.Object insertTabletMixer(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM TABLET_MIXER WHERE archive_date IS NULL OR archive_date = \'\'  ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixer>> getAllTabletMixerList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM TABLET_MIXER ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixer>> getActiveTabletMixerList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM TABLET_MIXER WHERE archive_date IS NULL OR archive_date = \'\'  ORDER BY name LIKE :name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixer>> getFilteredTabletMixerList(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM TABLET_MIXER WHERE id = :id")
    public abstract kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.TabletMixer> getTabletMixerById(long id);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Update
    public abstract java.lang.Object updateTabletMixer(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE TABLET_MIXER SET updated_date = :date WHERE id = :id")
    public abstract java.lang.Object setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE TABLET_MIXER SET mac = :mac, updated_date = datetime(\'now\',\'localtime\') WHERE id = :id")
    public abstract java.lang.Object setUpdatedMacAddress(long id, @org.jetbrains.annotations.NotNull
    java.lang.String mac, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE TABLET_MIXER SET remote_id = :remoteId WHERE id = :id")
    public abstract java.lang.Object setUpdatedRemoteId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Delete
    public abstract java.lang.Object deleteTabletMixer(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
}