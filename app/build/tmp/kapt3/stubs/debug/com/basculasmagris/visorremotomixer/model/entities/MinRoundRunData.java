package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

/**
 * ******************************************
 * MinData
 */
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0010\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B=\u0012\u0016\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005\u0012\u0016\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005H\u00c6\u0003J\u0019\u0010\u0016\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\tH\u00c6\u0003JG\u0010\u0018\u001a\u00020\u00002\u0018\b\u0002\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u00052\u0018\b\u0002\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u00d6\u0003J\t\u0010\u001f\u001a\u00020\u001aH\u00d6\u0001J\t\u0010 \u001a\u00020!H\u00d6\u0001J\u0019\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u001aH\u00d6\u0001R*\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R*\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000e\u00a8\u0006\'"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundRunData;", "Landroid/os/Parcelable;", "corralsData", "Ljava/util/ArrayList;", "Lcom/basculasmagris/visorremotomixer/model/entities/MinCorralData;", "Lkotlin/collections/ArrayList;", "productsData", "Lcom/basculasmagris/visorremotomixer/model/entities/MinProductData;", "id", "", "(Ljava/util/ArrayList;Ljava/util/ArrayList;J)V", "getCorralsData", "()Ljava/util/ArrayList;", "setCorralsData", "(Ljava/util/ArrayList;)V", "getId", "()J", "setId", "(J)V", "getProductsData", "setProductsData", "component1", "component2", "component3", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
public final class MinRoundRunData implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull
    private java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralData> corralsData;
    @org.jetbrains.annotations.NotNull
    private java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductData> productsData;
    private long id;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.MinRoundRunData> CREATOR = null;
    
    /**
     * ******************************************
     * MinData
     */
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MinRoundRunData copy(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralData> corralsData, @org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductData> productsData, long id) {
        return null;
    }
    
    /**
     * ******************************************
     * MinData
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * ******************************************
     * MinData
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * ******************************************
     * MinData
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public MinRoundRunData(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralData> corralsData, @org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductData> productsData, long id) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralData> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralData> getCorralsData() {
        return null;
    }
    
    public final void setCorralsData(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralData> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductData> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductData> getProductsData() {
        return null;
    }
    
    public final void setProductsData(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductData> p0) {
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final void setId(long p0) {
    }
    
    @java.lang.Override
    public int describeContents() {
        return 0;
    }
    
    @java.lang.Override
    public void writeToParcel(@org.jetbrains.annotations.NotNull
    android.os.Parcel parcel, int flags) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 3)
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.MinRoundRunData> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.MinRoundRunData createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.MinRoundRunData[] newArray(int size) {
            return null;
        }
    }
}