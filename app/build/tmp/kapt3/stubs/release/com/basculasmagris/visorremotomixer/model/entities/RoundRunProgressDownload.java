package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@androidx.room.Entity(tableName = "round_run_progress_download", primaryKeys = {"round_run_id", "corral_id"}, foreignKeys = {@androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.RoundRun.class, childColumns = {"round_run_id"}, onDelete = 5, parentColumns = {"id"}), @androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Corral.class, childColumns = {"corral_id"}, parentColumns = {"id"})})
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b$\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\b\u0012\u0006\u0010\u000b\u001a\u00020\b\u0012\u0006\u0010\f\u001a\u00020\b\u00a2\u0006\u0002\u0010\rJ\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\bH\u00c6\u0003J\t\u0010\'\u001a\u00020\bH\u00c6\u0003J\t\u0010(\u001a\u00020\bH\u00c6\u0003J\t\u0010)\u001a\u00020\bH\u00c6\u0003J\t\u0010*\u001a\u00020\bH\u00c6\u0003Jc\u0010+\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\bH\u00c6\u0001J\t\u0010,\u001a\u00020-H\u00d6\u0001J\u0013\u0010.\u001a\u00020/2\b\u00100\u001a\u0004\u0018\u000101H\u00d6\u0003J\t\u00102\u001a\u00020-H\u00d6\u0001J\t\u00103\u001a\u000204H\u00d6\u0001J\u0019\u00105\u001a\u0002062\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020-H\u00d6\u0001R\u001e\u0010\f\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\t\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011R\u001e\u0010\u000b\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R\u001e\u0010\n\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000f\"\u0004\b\u0019\u0010\u0011R\u001e\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u000f\"\u0004\b\u001b\u0010\u0011R\u001e\u0010\u0006\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0013\"\u0004\b\u001d\u0010\u001eR\u001e\u0010\u0004\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0013\"\u0004\b \u0010\u001eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0013\u00a8\u0006:"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressDownload;", "Landroid/os/Parcelable;", "roundRunId", "", "remoteRoundRunId", "corralId", "remoteCorralId", "initialWeight", "", "currentWeight", "finalWeight", "customTargetWeight", "actualTargetWeight", "(JJJJDDDDD)V", "getActualTargetWeight", "()D", "setActualTargetWeight", "(D)V", "getCorralId", "()J", "getCurrentWeight", "setCurrentWeight", "getCustomTargetWeight", "setCustomTargetWeight", "getFinalWeight", "setFinalWeight", "getInitialWeight", "setInitialWeight", "getRemoteCorralId", "setRemoteCorralId", "(J)V", "getRemoteRoundRunId", "setRemoteRoundRunId", "getRoundRunId", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_release"})
public final class RoundRunProgressDownload implements android.os.Parcelable {
    @androidx.room.ColumnInfo(name = "round_run_id")
    private final long roundRunId = 0L;
    @androidx.room.ColumnInfo(name = "remote_round_run_id")
    private long remoteRoundRunId;
    @androidx.room.ColumnInfo(name = "corral_id")
    private final long corralId = 0L;
    @androidx.room.ColumnInfo(name = "remote_corral_id")
    private long remoteCorralId;
    @androidx.room.ColumnInfo(name = "initial_weight")
    private double initialWeight;
    @androidx.room.ColumnInfo(name = "current_weight")
    private double currentWeight;
    @androidx.room.ColumnInfo(name = "final_weight")
    private double finalWeight;
    @androidx.room.ColumnInfo(name = "custom_target_weight")
    private double customTargetWeight;
    @androidx.room.ColumnInfo(name = "actual_target_weight")
    private double actualTargetWeight;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload copy(long roundRunId, long remoteRoundRunId, long corralId, long remoteCorralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight) {
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
    
    public RoundRunProgressDownload(long roundRunId, long remoteRoundRunId, long corralId, long remoteCorralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight) {
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
    
    public final long getCorralId() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long getRemoteCorralId() {
        return 0L;
    }
    
    public final void setRemoteCorralId(long p0) {
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double getInitialWeight() {
        return 0.0;
    }
    
    public final void setInitialWeight(double p0) {
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double getCurrentWeight() {
        return 0.0;
    }
    
    public final void setCurrentWeight(double p0) {
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double getFinalWeight() {
        return 0.0;
    }
    
    public final void setFinalWeight(double p0) {
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final double getCustomTargetWeight() {
        return 0.0;
    }
    
    public final void setCustomTargetWeight(double p0) {
    }
    
    public final double component9() {
        return 0.0;
    }
    
    public final double getActualTargetWeight() {
        return 0.0;
    }
    
    public final void setActualTargetWeight(double p0) {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload[] newArray(int size) {
            return null;
        }
    }
}