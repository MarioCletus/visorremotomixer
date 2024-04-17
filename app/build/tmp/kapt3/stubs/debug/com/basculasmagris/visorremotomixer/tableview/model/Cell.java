package com.basculasmagris.visorremotomixer.tableview.model;

import java.lang.System;

/**
 * Created by evrencoskun on 11/06/2017.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\b\u0016\u0018\u00002\u00020\u00012\u00020\u0002B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007J\n\u0010\u000b\u001a\u0004\u0018\u00010\u0006H\u0016J\b\u0010\f\u001a\u00020\u0004H\u0016J\b\u0010\r\u001a\u00020\u0004H\u0016R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/model/Cell;", "Lcom/evrencoskun/tableview/sort/ISortableModel;", "Lcom/evrencoskun/tableview/filter/IFilterableModel;", "mId", "", "data", "", "(Ljava/lang/String;Ljava/lang/Object;)V", "getData", "()Ljava/lang/Object;", "mFilterKeyword", "getContent", "getFilterableKeyword", "getId", "app_debug"})
public class Cell implements com.evrencoskun.tableview.sort.ISortableModel, com.evrencoskun.tableview.filter.IFilterableModel {
    private final java.lang.String mId = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Object data = null;
    private final java.lang.String mFilterKeyword = null;
    
    public Cell(@org.jetbrains.annotations.NotNull
    java.lang.String mId, @org.jetbrains.annotations.Nullable
    java.lang.Object data) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getData() {
        return null;
    }
    
    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String getId() {
        return null;
    }
    
    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String getFilterableKeyword() {
        return null;
    }
}