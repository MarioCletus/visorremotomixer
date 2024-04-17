package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@androidx.room.Entity(tableName = "round_run_progress_load", primaryKeys = {"round_run_id", "diet_id", "product_id"}, foreignKeys = {@androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.RoundRun.class, childColumns = {"round_run_id"}, onDelete = 5, parentColumns = {"id"}), @androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Diet.class, childColumns = {"diet_id"}, parentColumns = {"id"}), @androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Product.class, childColumns = {"product_id"}, parentColumns = {"id"})})
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b%\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n\u0012\u0006\u0010\r\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000eJ\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\nH\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\nH\u00c6\u0003J\t\u0010,\u001a\u00020\nH\u00c6\u0003J\t\u0010-\u001a\u00020\nH\u00c6\u0003Jm\u0010.\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\nH\u00c6\u0001J\t\u0010/\u001a\u000200H\u00d6\u0001J\u0013\u00101\u001a\u0002022\b\u00103\u001a\u0004\u0018\u000104H\u00d6\u0003J\t\u00105\u001a\u000200H\u00d6\u0001J\t\u00106\u001a\u000207H\u00d6\u0001J\u0019\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u000200H\u00d6\u0001R\u001e\u0010\u000b\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001e\u0010\f\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0010\"\u0004\b\u0016\u0010\u0012R\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0010\"\u0004\b\u0018\u0010\u0012R\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0014R\u001e\u0010\u0006\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0014\"\u0004\b\u001b\u0010\u001cR\u001e\u0010\b\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0014\"\u0004\b\u001e\u0010\u001cR\u001e\u0010\u0004\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0014\"\u0004\b \u0010\u001cR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0014R\u001e\u0010\r\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0010\"\u0004\b#\u0010\u0012\u00a8\u0006="}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressLoad;", "Landroid/os/Parcelable;", "roundRunId", "", "remoteRoundRunId", "dietId", "remoteDietId", "productId", "remoteProductId", "initialWeight", "", "currentWeight", "finalWeight", "targetWeight", "(JJJJJJDDDD)V", "getCurrentWeight", "()D", "setCurrentWeight", "(D)V", "getDietId", "()J", "getFinalWeight", "setFinalWeight", "getInitialWeight", "setInitialWeight", "getProductId", "getRemoteDietId", "setRemoteDietId", "(J)V", "getRemoteProductId", "setRemoteProductId", "getRemoteRoundRunId", "setRemoteRoundRunId", "getRoundRunId", "getTargetWeight", "setTargetWeight", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_release"})
public final class RoundRunProgressLoad implements android.os.Parcelable {
    @androidx.room.ColumnInfo(name = "round_run_id")
    private final long roundRunId = 0L;
    @androidx.room.ColumnInfo(name = "remote_round_run_id")
    private long remoteRoundRunId;
    @androidx.room.ColumnInfo(name = "diet_id")
    private final long dietId = 0L;
    @androidx.room.ColumnInfo(name = "remote_diet_id")
    private long remoteDietId;
    @androidx.room.ColumnInfo(name = "product_id")
    private final long productId = 0L;
    @androidx.room.ColumnInfo(name = "remote_product_id")
    private long remoteProductId;
    @androidx.room.ColumnInfo(name = "initial_weight")
    private double initialWeight;
    @androidx.room.ColumnInfo(name = "current_weight")
    private double currentWeight;
    @androidx.room.ColumnInfo(name = "final_weight")
    private double finalWeight;
    @androidx.room.ColumnInfo(name = "target_weight")
    private double targetWeight;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad copy(long roundRunId, long remoteRoundRunId, long dietId, long remoteDietId, long productId, long remoteProductId, double initialWeight, double currentWeight, double finalWeight, double targetWeight) {
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
    
    public RoundRunProgressLoad(long roundRunId, long remoteRoundRunId, long dietId, long remoteDietId, long productId, long remoteProductId, double initialWeight, double currentWeight, double finalWeight, double targetWeight) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getRoundRunId() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long getRemoteRoundRunId() {
        return 0L;
    }
    
    public final void setRemoteRoundRunId(long p0) {
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long getDietId() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long getRemoteDietId() {
        return 0L;
    }
    
    public final void setRemoteDietId(long p0) {
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long getProductId() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final long getRemoteProductId() {
        return 0L;
    }
    
    public final void setRemoteProductId(long p0) {
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double getInitialWeight() {
        return 0.0;
    }
    
    public final void setInitialWeight(double p0) {
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final double getCurrentWeight() {
        return 0.0;
    }
    
    public final void setCurrentWeight(double p0) {
    }
    
    public final double component9() {
        return 0.0;
    }
    
    public final double getFinalWeight() {
        return 0.0;
    }
    
    public final void setFinalWeight(double p0) {
    }
    
    public final double component10() {
        return 0.0;
    }
    
    public final double getTargetWeight() {
        return 0.0;
    }
    
    public final void setTargetWeight(double p0) {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad[] newArray(int size) {
            return null;
        }
    }
}