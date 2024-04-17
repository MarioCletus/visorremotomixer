package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\tH\u00c6\u0003J\t\u0010!\u001a\u00020\u000bH\u00c6\u0003J=\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\t\u0010#\u001a\u00020\tH\u00d6\u0001J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u00d6\u0003J\t\u0010(\u001a\u00020\tH\u00d6\u0001J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001J\u0019\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020\tH\u00d6\u0001R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006/"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundRunDetail;", "Landroid/os/Parcelable;", "userDisplayName", "", "round", "Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundDetail;", "mixer", "Lcom/basculasmagris/visorremotomixer/model/entities/MinMixerDetail;", "state", "", "id", "", "(Ljava/lang/String;Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundDetail;Lcom/basculasmagris/visorremotomixer/model/entities/MinMixerDetail;IJ)V", "getId", "()J", "setId", "(J)V", "getMixer", "()Lcom/basculasmagris/visorremotomixer/model/entities/MinMixerDetail;", "setMixer", "(Lcom/basculasmagris/visorremotomixer/model/entities/MinMixerDetail;)V", "getRound", "()Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundDetail;", "getState", "()I", "setState", "(I)V", "getUserDisplayName", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "component5", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_release"})
public final class MinRoundRunDetail implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String userDisplayName = null;
    @org.jetbrains.annotations.NotNull
    private final com.basculasmagris.visorremotomixer.model.entities.MinRoundDetail round = null;
    @org.jetbrains.annotations.Nullable
    private com.basculasmagris.visorremotomixer.model.entities.MinMixerDetail mixer;
    private int state;
    private long id;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail copy(@org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.MinRoundDetail round, @org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.MinMixerDetail mixer, int state, long id) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public MinRoundRunDetail(@org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.MinRoundDetail round, @org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.MinMixerDetail mixer, int state, long id) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUserDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MinRoundDetail component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.MinRoundDetail getRound() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.model.entities.MinMixerDetail component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.model.entities.MinMixerDetail getMixer() {
        return null;
    }
    
    public final void setMixer(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.MinMixerDetail p0) {
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int getState() {
        return 0;
    }
    
    public final void setState(int p0) {
    }
    
    public final long component5() {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail[] newArray(int size) {
            return null;
        }
    }
}