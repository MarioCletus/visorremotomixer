package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

/**
 * **************************
 */
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00c6\u0003J1\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\t\u0010\u001a\u001a\u00020\bH\u00d6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u00d6\u0003J\t\u0010\u001f\u001a\u00020\bH\u00d6\u0001J\t\u0010 \u001a\u00020\u0005H\u00d6\u0001J\u0019\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006&"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/MedRoundRunDetail;", "Landroid/os/Parcelable;", "round", "Lcom/basculasmagris/visorremotomixer/model/entities/MinRound;", "startDate", "", "endDate", "state", "", "(Lcom/basculasmagris/visorremotomixer/model/entities/MinRound;Ljava/lang/String;Ljava/lang/String;I)V", "getEndDate", "()Ljava/lang/String;", "getRound", "()Lcom/basculasmagris/visorremotomixer/model/entities/MinRound;", "getStartDate", "setStartDate", "(Ljava/lang/String;)V", "getState", "()I", "setState", "(I)V", "component1", "component2", "component3", "component4", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
public final class MedRoundRunDetail implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull
    private final com.basculasmagris.visorremotomixer.model.entities.MinRound round = null;
    @org.jetbrains.annotations.NotNull
    private java.lang.String startDate;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String endDate = null;
    private int state;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail> CREATOR = null;
    
    /**
     * **************************
     */
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail copy(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.MinRound round, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, int state) {
        return null;
    }
    
    /**
     * **************************
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * **************************
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * **************************
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public MedRoundRunDetail(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.MinRound round, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, int state) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MinRound component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MinRound getRound() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getStartDate() {
        return null;
    }
    
    public final void setStartDate(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEndDate() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int getState() {
        return 0;
    }
    
    public final void setState(int p0) {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail[] newArray(int size) {
            return null;
        }
    }
}