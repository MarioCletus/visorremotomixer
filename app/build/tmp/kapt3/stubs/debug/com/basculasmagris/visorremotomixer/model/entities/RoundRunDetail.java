package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b6\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001By\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\u0006\u0010\r\u001a\u00020\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0011\u0012\u0006\u0010\u0013\u001a\u00020\u0011\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0017J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\t\u0010<\u001a\u00020\u0011H\u00c6\u0003J\t\u0010=\u001a\u00020\u0011H\u00c6\u0003J\t\u0010>\u001a\u00020\u0011H\u00c6\u0003J\t\u0010?\u001a\u00020\u0015H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\u0005H\u00c6\u0003J\t\u0010B\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010D\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010E\u001a\u00020\u0005H\u00c6\u0003J\t\u0010F\u001a\u00020\u0005H\u00c6\u0003J\t\u0010G\u001a\u00020\u0005H\u00c6\u0003J\t\u0010H\u001a\u00020\u0003H\u00c6\u0003J\u0099\u0001\u0010I\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u00112\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0003H\u00c6\u0001J\t\u0010J\u001a\u00020\u0015H\u00d6\u0001J\u0013\u0010K\u001a\u00020L2\b\u0010M\u001a\u0004\u0018\u00010NH\u00d6\u0003J\t\u0010O\u001a\u00020\u0015H\u00d6\u0001J\t\u0010P\u001a\u00020\u0005H\u00d6\u0001J\u0019\u0010Q\u001a\u00020R2\u0006\u0010S\u001a\u00020T2\u0006\u0010U\u001a\u00020\u0015H\u00d6\u0001R\u001a\u0010\u0013\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0019\"\u0004\b\u001d\u0010\u001bR\u001a\u0010\u0012\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0019\"\u0004\b\u001f\u0010\u001bR\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u001a\u0010\u0016\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\'\"\u0004\b(\u0010)R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010#R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u001a\u0010\f\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u0010!\"\u0004\b2\u00103R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010!R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010!R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010#\u00a8\u0006V"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunDetail;", "Landroid/os/Parcelable;", "userId", "", "userDisplayName", "", "round", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundDetail;", "mixer", "Lcom/basculasmagris/visorremotomixer/model/entities/MixerDetail;", "mixerBluetoothDevice", "Landroid/bluetooth/BluetoothDevice;", "startDate", "updatedDate", "endDate", "remoteId", "customPercentage", "", "customTara", "addedBlend", "state", "", "id", "(JLjava/lang/String;Lcom/basculasmagris/visorremotomixer/model/entities/RoundDetail;Lcom/basculasmagris/visorremotomixer/model/entities/MixerDetail;Landroid/bluetooth/BluetoothDevice;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JDDDIJ)V", "getAddedBlend", "()D", "setAddedBlend", "(D)V", "getCustomPercentage", "setCustomPercentage", "getCustomTara", "setCustomTara", "getEndDate", "()Ljava/lang/String;", "getId", "()J", "setId", "(J)V", "getMixer", "()Lcom/basculasmagris/visorremotomixer/model/entities/MixerDetail;", "setMixer", "(Lcom/basculasmagris/visorremotomixer/model/entities/MixerDetail;)V", "getMixerBluetoothDevice", "()Landroid/bluetooth/BluetoothDevice;", "setMixerBluetoothDevice", "(Landroid/bluetooth/BluetoothDevice;)V", "getRemoteId", "getRound", "()Lcom/basculasmagris/visorremotomixer/model/entities/RoundDetail;", "getStartDate", "setStartDate", "(Ljava/lang/String;)V", "getState", "()I", "setState", "(I)V", "getUpdatedDate", "getUserDisplayName", "getUserId", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
public final class RoundRunDetail implements android.os.Parcelable {
    private final long userId = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String userDisplayName = null;
    @org.jetbrains.annotations.NotNull
    private final com.basculasmagris.visorremotomixer.model.entities.RoundDetail round = null;
    @org.jetbrains.annotations.Nullable
    private com.basculasmagris.visorremotomixer.model.entities.MixerDetail mixer;
    @org.jetbrains.annotations.Nullable
    private android.bluetooth.BluetoothDevice mixerBluetoothDevice;
    @org.jetbrains.annotations.NotNull
    private java.lang.String startDate;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String updatedDate = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String endDate = null;
    private final long remoteId = 0L;
    private double customPercentage;
    private double customTara;
    private double addedBlend;
    private int state;
    private long id;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail copy(long userId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundDetail round, @org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.MixerDetail mixer, @org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice mixerBluetoothDevice, @org.jetbrains.annotations.NotNull
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
    
    public RoundRunDetail(long userId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundDetail round, @org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.MixerDetail mixer, @org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice mixerBluetoothDevice, @org.jetbrains.annotations.NotNull
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.NotNull
    java.lang.String endDate, long remoteId, double customPercentage, double customTara, double addedBlend, int state, long id) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getUserId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUserDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundDetail component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.RoundDetail getRound() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.model.entities.MixerDetail component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.model.entities.MixerDetail getMixer() {
        return null;
    }
    
    public final void setMixer(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.MixerDetail p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.bluetooth.BluetoothDevice component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.bluetooth.BluetoothDevice getMixerBluetoothDevice() {
        return null;
    }
    
    public final void setMixerBluetoothDevice(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
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
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUpdatedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEndDate() {
        return null;
    }
    
    public final long component9() {
        return 0L;
    }
    
    public final long getRemoteId() {
        return 0L;
    }
    
    public final double component10() {
        return 0.0;
    }
    
    public final double getCustomPercentage() {
        return 0.0;
    }
    
    public final void setCustomPercentage(double p0) {
    }
    
    public final double component11() {
        return 0.0;
    }
    
    public final double getCustomTara() {
        return 0.0;
    }
    
    public final void setCustomTara(double p0) {
    }
    
    public final double component12() {
        return 0.0;
    }
    
    public final double getAddedBlend() {
        return 0.0;
    }
    
    public final void setAddedBlend(double p0) {
    }
    
    public final int component13() {
        return 0;
    }
    
    public final int getState() {
        return 0;
    }
    
    public final void setState(int p0) {
    }
    
    public final long component14() {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail[] newArray(int size) {
            return null;
        }
    }
}