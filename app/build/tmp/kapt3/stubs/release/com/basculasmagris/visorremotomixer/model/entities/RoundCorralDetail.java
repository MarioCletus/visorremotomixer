package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b#\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u00a2\u0006\u0002\u0010\u0010J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u000eH\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0006H\u00c6\u0003J\t\u0010)\u001a\u00020\u0006H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u000bH\u00c6\u0003J\t\u0010-\u001a\u00020\u000bH\u00c6\u0003J\t\u0010.\u001a\u00020\u000eH\u00c6\u0003Jm\u0010/\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eH\u00c6\u0001J\t\u00100\u001a\u00020\u000eH\u00d6\u0001J\u0013\u00101\u001a\u0002022\b\u00103\u001a\u0004\u0018\u000104H\u00d6\u0003J\t\u00105\u001a\u00020\u000eH\u00d6\u0001J\t\u00106\u001a\u00020\u0006H\u00d6\u0001J\u0019\u00107\u001a\u0002082\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020\u000eH\u00d6\u0001R\u001a\u0010\u000f\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0016R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0012\"\u0004\b\u001b\u0010\u0014R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0018R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018R\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001d\"\u0004\b$\u0010\u001f\u00a8\u0006<"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorralDetail;", "Landroid/os/Parcelable;", "corralId", "", "remoteCorralId", "corralName", "", "corralDescription", "roundId", "remoteRoundId", "percentage", "", "weight", "order", "", "animalQuantity", "(JJLjava/lang/String;Ljava/lang/String;JJDDII)V", "getAnimalQuantity", "()I", "setAnimalQuantity", "(I)V", "getCorralDescription", "()Ljava/lang/String;", "getCorralId", "()J", "getCorralName", "getOrder", "setOrder", "getPercentage", "()D", "setPercentage", "(D)V", "getRemoteCorralId", "getRemoteRoundId", "getRoundId", "getWeight", "setWeight", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_release"})
public final class RoundCorralDetail implements android.os.Parcelable {
    private final long corralId = 0L;
    private final long remoteCorralId = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String corralName = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String corralDescription = null;
    private final long roundId = 0L;
    private final long remoteRoundId = 0L;
    private double percentage;
    private double weight;
    private int order;
    private int animalQuantity;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail copy(long corralId, long remoteCorralId, @org.jetbrains.annotations.NotNull
    java.lang.String corralName, @org.jetbrains.annotations.NotNull
    java.lang.String corralDescription, long roundId, long remoteRoundId, double percentage, double weight, int order, int animalQuantity) {
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
    
    public RoundCorralDetail(long corralId, long remoteCorralId, @org.jetbrains.annotations.NotNull
    java.lang.String corralName, @org.jetbrains.annotations.NotNull
    java.lang.String corralDescription, long roundId, long remoteRoundId, double percentage, double weight, int order, int animalQuantity) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getCorralId() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long getRemoteCorralId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCorralName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCorralDescription() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long getRoundId() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final long getRemoteRoundId() {
        return 0L;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double getPercentage() {
        return 0.0;
    }
    
    public final void setPercentage(double p0) {
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final double getWeight() {
        return 0.0;
    }
    
    public final void setWeight(double p0) {
    }
    
    public final int component9() {
        return 0;
    }
    
    public final int getOrder() {
        return 0;
    }
    
    public final void setOrder(int p0) {
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int getAnimalQuantity() {
        return 0;
    }
    
    public final void setAnimalQuantity(int p0) {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail[] newArray(int size) {
            return null;
        }
    }
}