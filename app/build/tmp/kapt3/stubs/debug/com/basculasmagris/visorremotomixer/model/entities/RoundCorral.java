package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@androidx.room.Entity(tableName = "round_corral", primaryKeys = {"round_id", "corral_id"}, foreignKeys = {@androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Corral.class, childColumns = {"corral_id"}, parentColumns = {"id"}), @androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Round.class, childColumns = {"round_id"}, onDelete = 5, parentColumns = {"id"})})
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b!\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BW\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\u0002\u0010\u0010J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\bH\u00c6\u0003J\t\u0010)\u001a\u00020\nH\u00c6\u0003J\t\u0010*\u001a\u00020\nH\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u000eH\u00c6\u0003Jo\u0010-\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u00c6\u0001J\t\u0010.\u001a\u00020\bH\u00d6\u0001J\u0013\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u000102H\u00d6\u0003J\t\u00103\u001a\u00020\bH\u00d6\u0001J\t\u00104\u001a\u00020\u000eH\u00d6\u0001J\u0019\u00105\u001a\u0002062\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020\bH\u00d6\u0001R\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\u000b\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001e\u0010\u0006\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0014\"\u0004\b\u001a\u0010\u001bR\u001e\u0010\f\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0014\"\u0004\b\u001d\u0010\u001bR\u001e\u0010\u0005\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0014\"\u0004\b\u001f\u0010\u001bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0014R\u0016\u0010\r\u001a\u00020\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0012R\u0016\u0010\t\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018\u00a8\u0006:"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorral;", "Landroid/os/Parcelable;", "roundId", "", "corralId", "remoteRoundId", "remoteCorralId", "order", "", "weight", "", "percentage", "remoteId", "updatedDate", "", "archiveDate", "(JJJJIDDJLjava/lang/String;Ljava/lang/String;)V", "getArchiveDate", "()Ljava/lang/String;", "getCorralId", "()J", "getOrder", "()I", "getPercentage", "()D", "getRemoteCorralId", "setRemoteCorralId", "(J)V", "getRemoteId", "setRemoteId", "getRemoteRoundId", "setRemoteRoundId", "getRoundId", "getUpdatedDate", "getWeight", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
public final class RoundCorral implements android.os.Parcelable {
    @androidx.room.ColumnInfo(name = "round_id")
    private final long roundId = 0L;
    @androidx.room.ColumnInfo(name = "corral_id")
    private final long corralId = 0L;
    @androidx.room.ColumnInfo(name = "remote_round_id")
    private long remoteRoundId;
    @androidx.room.ColumnInfo(name = "remote_corral_id")
    private long remoteCorralId;
    @androidx.room.ColumnInfo
    private final int order = 0;
    @androidx.room.ColumnInfo
    private final double weight = 0.0;
    @androidx.room.ColumnInfo
    private final double percentage = 0.0;
    @androidx.room.ColumnInfo(name = "remote_id")
    private long remoteId;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "updated_date")
    private final java.lang.String updatedDate = null;
    @org.jetbrains.annotations.Nullable
    @androidx.room.ColumnInfo(name = "archive_date")
    private final java.lang.String archiveDate = null;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundCorral> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundCorral copy(long roundId, long corralId, long remoteRoundId, long remoteCorralId, int order, double weight, double percentage, long remoteId, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.Nullable
    java.lang.String archiveDate) {
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
    
    public RoundCorral(long roundId, long corralId, long remoteRoundId, long remoteCorralId, int order, double weight, double percentage, long remoteId, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.Nullable
    java.lang.String archiveDate) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getRoundId() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long getCorralId() {
        return 0L;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long getRemoteRoundId() {
        return 0L;
    }
    
    public final void setRemoteRoundId(long p0) {
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long getRemoteCorralId() {
        return 0L;
    }
    
    public final void setRemoteCorralId(long p0) {
    }
    
    public final int component5() {
        return 0;
    }
    
    public final int getOrder() {
        return 0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double getWeight() {
        return 0.0;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double getPercentage() {
        return 0.0;
    }
    
    public final long component8() {
        return 0L;
    }
    
    public final long getRemoteId() {
        return 0L;
    }
    
    public final void setRemoteId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUpdatedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getArchiveDate() {
        return null;
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundCorral> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundCorral createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundCorral[] newArray(int size) {
            return null;
        }
    }
}