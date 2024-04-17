package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@androidx.room.Entity(tableName = "round_run_report", foreignKeys = {@androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Round.class, childColumns = {"round_id"}, onDelete = 5, parentColumns = {"id"}), @androidx.room.ForeignKey(entity = com.basculasmagris.visorremotomixer.model.entities.Mixer.class, childColumns = {"mixer_id"}, parentColumns = {"id"})})
@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b>\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0097\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\u000b\u001a\u00020\t\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\u0006\u0010\u0011\u001a\u00020\u0005\u0012\u0006\u0010\u0012\u001a\u00020\u0005\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\u0006\u0010\u0014\u001a\u00020\u0005\u0012\u0006\u0010\u0015\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0017J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0005H\u00c6\u0003J\t\u00109\u001a\u00020\u0005H\u00c6\u0003J\t\u0010:\u001a\u00020\u0003H\u00c6\u0003J\t\u0010;\u001a\u00020\u0005H\u00c6\u0003J\t\u0010<\u001a\u00020\u0005H\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\u0005H\u00c6\u0003J\t\u0010?\u001a\u00020\u0005H\u00c6\u0003J\t\u0010@\u001a\u00020\u0005H\u00c6\u0003J\t\u0010A\u001a\u00020\tH\u00c6\u0003J\t\u0010B\u001a\u00020\tH\u00c6\u0003J\t\u0010C\u001a\u00020\tH\u00c6\u0003J\t\u0010D\u001a\u00020\u0003H\u00c6\u0003J\t\u0010E\u001a\u00020\u0005H\u00c6\u0003J\u00bd\u0001\u0010F\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00052\b\b\u0002\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u0003H\u00c6\u0001J\t\u0010G\u001a\u00020HH\u00d6\u0001J\u0013\u0010I\u001a\u00020J2\b\u0010K\u001a\u0004\u0018\u00010LH\u00d6\u0003J\t\u0010M\u001a\u00020HH\u00d6\u0001J\t\u0010N\u001a\u00020\u0005H\u00d6\u0001J\u0019\u0010O\u001a\u00020P2\u0006\u0010Q\u001a\u00020R2\u0006\u0010S\u001a\u00020HH\u00d6\u0001R\u001e\u0010\n\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001e\u0010\u000b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0019\"\u0004\b\u001d\u0010\u001bR\u0016\u0010\u0015\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0016\u0010\u0013\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0016\u0010\u0014\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001fR\u0016\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001fR\u001e\u0010\u0016\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010!\"\u0004\b%\u0010&R\u0016\u0010\u000e\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001fR\u0016\u0010\f\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010!R\u0016\u0010\u000f\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001fR\u0016\u0010\r\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001fR\u0016\u0010\u0012\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001fR\u0016\u0010\u0010\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010!R\u0016\u0010\u0011\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010!R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u001f\"\u0004\b0\u00101R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001fR\u0016\u0010\b\u001a\u00020\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u0019\u00a8\u0006T"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunReport;", "Landroid/os/Parcelable;", "roundRunId", "", "startDate", "", "updatedDate", "endDate", "weight", "", "customPercentage", "customTara", "mixerId", "mixerName", "mixerDescription", "mixerMac", "roundId", "roundName", "roundDescription", "dietId", "dietName", "dietDescription", "id", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;DDDJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;J)V", "getCustomPercentage", "()D", "setCustomPercentage", "(D)V", "getCustomTara", "setCustomTara", "getDietDescription", "()Ljava/lang/String;", "getDietId", "()J", "getDietName", "getEndDate", "getId", "setId", "(J)V", "getMixerDescription", "getMixerId", "getMixerMac", "getMixerName", "getRoundDescription", "getRoundId", "getRoundName", "getRoundRunId", "getStartDate", "setStartDate", "(Ljava/lang/String;)V", "getUpdatedDate", "getWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
public final class RoundRunReport implements android.os.Parcelable {
    @androidx.room.ColumnInfo(name = "round_run_id")
    private final long roundRunId = 0L;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "start_date")
    private java.lang.String startDate;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "updated_date")
    private final java.lang.String updatedDate = null;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "end_date")
    private final java.lang.String endDate = null;
    @androidx.room.ColumnInfo(name = "weight")
    private final double weight = 0.0;
    @androidx.room.ColumnInfo(name = "custom_percentage")
    private double customPercentage;
    @androidx.room.ColumnInfo(name = "custom_tara")
    private double customTara;
    @androidx.room.ColumnInfo(name = "mixer_id")
    private final long mixerId = 0L;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "mixer_name")
    private final java.lang.String mixerName = null;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "mixer_description")
    private final java.lang.String mixerDescription = null;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "mixer_mac")
    private final java.lang.String mixerMac = null;
    @androidx.room.ColumnInfo(name = "round_id")
    private final long roundId = 0L;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "round_name")
    private final java.lang.String roundName = null;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "round_description")
    private final java.lang.String roundDescription = null;
    @androidx.room.ColumnInfo(name = "diet_id")
    private final long dietId = 0L;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "diet_name")
    private final java.lang.String dietName = null;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "diet_description")
    private final java.lang.String dietDescription = null;
    @androidx.room.PrimaryKey(autoGenerate = true)
    private long id;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunReport> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundRunReport copy(long roundRunId, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, double weight, double customPercentage, double customTara, long mixerId, @org.jetbrains.annotations.NotNull
    java.lang.String mixerName, @org.jetbrains.annotations.NotNull
    java.lang.String mixerDescription, @org.jetbrains.annotations.NotNull
    java.lang.String mixerMac, long roundId, @org.jetbrains.annotations.NotNull
    java.lang.String roundName, @org.jetbrains.annotations.NotNull
    java.lang.String roundDescription, long dietId, @org.jetbrains.annotations.NotNull
    java.lang.String dietName, @org.jetbrains.annotations.NotNull
    java.lang.String dietDescription, long id) {
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
    
    public RoundRunReport(long roundRunId, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, double weight, double customPercentage, double customTara, long mixerId, @org.jetbrains.annotations.NotNull
    java.lang.String mixerName, @org.jetbrains.annotations.NotNull
    java.lang.String mixerDescription, @org.jetbrains.annotations.NotNull
    java.lang.String mixerMac, long roundId, @org.jetbrains.annotations.NotNull
    java.lang.String roundName, @org.jetbrains.annotations.NotNull
    java.lang.String roundDescription, long dietId, @org.jetbrains.annotations.NotNull
    java.lang.String dietName, @org.jetbrains.annotations.NotNull
    java.lang.String dietDescription, long id) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getRoundRunId() {
        return 0L;
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
    public final java.lang.String getUpdatedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEndDate() {
        return null;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double getWeight() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double getCustomPercentage() {
        return 0.0;
    }
    
    public final void setCustomPercentage(double p0) {
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double getCustomTara() {
        return 0.0;
    }
    
    public final void setCustomTara(double p0) {
    }
    
    public final long component8() {
        return 0L;
    }
    
    public final long getMixerId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMixerName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMixerDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMixerMac() {
        return null;
    }
    
    public final long component12() {
        return 0L;
    }
    
    public final long getRoundId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoundName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoundDescription() {
        return null;
    }
    
    public final long component15() {
        return 0L;
    }
    
    public final long getDietId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDietName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDietDescription() {
        return null;
    }
    
    public final long component18() {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunReport> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunReport createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunReport[] newArray(int size) {
            return null;
        }
    }
}