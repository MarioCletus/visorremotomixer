package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@androidx.room.Entity(tableName = "round_run", foreignKeys = {@androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Round.class, childColumns = {"round_id"}, onDelete = 5, parentColumns = {"id"}), @androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Mixer.class, childColumns = {"mixer_id"}, parentColumns = {"id"})})
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b6\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u007f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\u0006\u0010\u0011\u001a\u00020\u000f\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0015J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u000fH\u00c6\u0003J\t\u0010;\u001a\u00020\u000fH\u00c6\u0003J\t\u0010<\u001a\u00020\u000fH\u00c6\u0003J\t\u0010=\u001a\u00020\u0013H\u00c6\u0003J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\t\u0010?\u001a\u00020\u0005H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\t\u0010B\u001a\u00020\u0003H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\t\u0010D\u001a\u00020\u0005H\u00c6\u0003J\t\u0010E\u001a\u00020\u0005H\u00c6\u0003J\t\u0010F\u001a\u00020\u0005H\u00c6\u0003J\u009f\u0001\u0010G\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0003H\u00c6\u0001J\t\u0010H\u001a\u00020\u0013H\u00d6\u0001J\u0013\u0010I\u001a\u00020J2\b\u0010K\u001a\u0004\u0018\u00010LH\u00d6\u0003J\t\u0010M\u001a\u00020\u0013H\u00d6\u0001J\t\u0010N\u001a\u00020\u0005H\u00d6\u0001J\u0019\u0010O\u001a\u00020P2\u0006\u0010Q\u001a\u00020R2\u0006\u0010S\u001a\u00020\u0013H\u00d6\u0001R\u001e\u0010\u0011\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001e\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0017\"\u0004\b\u001b\u0010\u0019R\u001e\u0010\u0010\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0017\"\u0004\b\u001d\u0010\u0019R\u0016\u0010\f\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001e\u0010\u0014\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0016\u0010\b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010!R\u001e\u0010\r\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010!\"\u0004\b&\u0010#R\u001e\u0010\t\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010!\"\u0004\b(\u0010#R\u001e\u0010\u0007\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010!\"\u0004\b*\u0010#R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010!\"\u0004\b,\u0010#R\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010!R\u001e\u0010\n\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u001f\"\u0004\b/\u00100R\u001e\u0010\u0012\u001a\u00020\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u0016\u0010\u000b\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u001fR\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u001f\"\u0004\b7\u00100\u00a8\u0006T"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundRun;", "Landroid/os/Parcelable;", "remoteUserId", "", "userDisplayName", "", "roundId", "remoteRoundId", "mixerId", "remoteMixerId", "startDate", "updatedDate", "endDate", "remoteId", "customPercentage", "", "customTara", "addedBlend", "state", "", "id", "(JLjava/lang/String;JJJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JDDDIJ)V", "getAddedBlend", "()D", "setAddedBlend", "(D)V", "getCustomPercentage", "setCustomPercentage", "getCustomTara", "setCustomTara", "getEndDate", "()Ljava/lang/String;", "getId", "()J", "setId", "(J)V", "getMixerId", "getRemoteId", "setRemoteId", "getRemoteMixerId", "setRemoteMixerId", "getRemoteRoundId", "setRemoteRoundId", "getRemoteUserId", "setRemoteUserId", "getRoundId", "getStartDate", "setStartDate", "(Ljava/lang/String;)V", "getState", "()I", "setState", "(I)V", "getUpdatedDate", "getUserDisplayName", "setUserDisplayName", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
public final class RoundRun implements android.os.Parcelable {
    @androidx.room.ColumnInfo(name = "remote_user_id")
    private long remoteUserId;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "user_display_name")
    private java.lang.String userDisplayName;
    @androidx.room.ColumnInfo(name = "round_id")
    private final long roundId = 0L;
    @androidx.room.ColumnInfo(name = "remote_round_id")
    private long remoteRoundId;
    @androidx.room.ColumnInfo(name = "mixer_id")
    private final long mixerId = 0L;
    @androidx.room.ColumnInfo(name = "remote_mixer_id")
    private long remoteMixerId;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "start_date")
    private java.lang.String startDate;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "updated_date")
    private final java.lang.String updatedDate = null;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "end_date")
    private final java.lang.String endDate = null;
    @androidx.room.ColumnInfo(name = "remote_id")
    private long remoteId;
    @androidx.room.ColumnInfo(name = "custom_percentage")
    private double customPercentage;
    @androidx.room.ColumnInfo(name = "custom_tara")
    private double customTara;
    @androidx.room.ColumnInfo(name = "added_blend")
    private double addedBlend;
    @androidx.room.ColumnInfo(name = "state")
    private int state;
    @androidx.room.PrimaryKey(autoGenerate = true)
    private long id;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRun> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundRun copy(long remoteUserId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, long roundId, long remoteRoundId, long mixerId, long remoteMixerId, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, long remoteId, double customPercentage, double customTara, double addedBlend, int state, long id) {
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
    
    public RoundRun(long remoteUserId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, long roundId, long remoteRoundId, long mixerId, long remoteMixerId, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, long remoteId, double customPercentage, double customTara, double addedBlend, int state, long id) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getRemoteUserId() {
        return 0L;
    }
    
    public final void setRemoteUserId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUserDisplayName() {
        return null;
    }
    
    public final void setUserDisplayName(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long getRoundId() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long getRemoteRoundId() {
        return 0L;
    }
    
    public final void setRemoteRoundId(long p0) {
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long getMixerId() {
        return 0L;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final long getRemoteMixerId() {
        return 0L;
    }
    
    public final void setRemoteMixerId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
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
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUpdatedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEndDate() {
        return null;
    }
    
    public final long component10() {
        return 0L;
    }
    
    public final long getRemoteId() {
        return 0L;
    }
    
    public final void setRemoteId(long p0) {
    }
    
    public final double component11() {
        return 0.0;
    }
    
    public final double getCustomPercentage() {
        return 0.0;
    }
    
    public final void setCustomPercentage(double p0) {
    }
    
    public final double component12() {
        return 0.0;
    }
    
    public final double getCustomTara() {
        return 0.0;
    }
    
    public final void setCustomTara(double p0) {
    }
    
    public final double component13() {
        return 0.0;
    }
    
    public final double getAddedBlend() {
        return 0.0;
    }
    
    public final void setAddedBlend(double p0) {
    }
    
    public final int component14() {
        return 0;
    }
    
    public final int getState() {
        return 0;
    }
    
    public final void setState(int p0) {
    }
    
    public final long component15() {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRun> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRun createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRun[] newArray(int size) {
            return null;
        }
    }
}