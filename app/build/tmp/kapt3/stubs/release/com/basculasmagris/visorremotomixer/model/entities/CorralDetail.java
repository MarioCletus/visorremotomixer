package com.basculasmagris.visorremotomixer.model.entities;

import java.lang.System;

@kotlinx.parcelize.Parcelize
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b@\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u008f\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\b\u0012\u0006\u0010\u000e\u001a\u00020\f\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0010\u0012\u0006\u0010\u0012\u001a\u00020\u0010\u0012\u0006\u0010\u0013\u001a\u00020\u0010\u0012\u0006\u0010\u0014\u001a\u00020\u0010\u0012\u0006\u0010\u0015\u001a\u00020\u0010\u0012\u0006\u0010\u0016\u001a\u00020\u0010\u0012\u0006\u0010\u0017\u001a\u00020\b\u00a2\u0006\u0002\u0010\u0018J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\u0010H\u00c6\u0003J\t\u0010?\u001a\u00020\u0010H\u00c6\u0003J\t\u0010@\u001a\u00020\u0010H\u00c6\u0003J\t\u0010A\u001a\u00020\u0010H\u00c6\u0003J\t\u0010B\u001a\u00020\u0010H\u00c6\u0003J\t\u0010C\u001a\u00020\u0010H\u00c6\u0003J\t\u0010D\u001a\u00020\u0010H\u00c6\u0003J\t\u0010E\u001a\u00020\bH\u00c6\u0003J\t\u0010F\u001a\u00020\u0005H\u00c6\u0003J\t\u0010G\u001a\u00020\u0005H\u00c6\u0003J\t\u0010H\u001a\u00020\bH\u00c6\u0003J\t\u0010I\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010J\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010K\u001a\u00020\fH\u00c6\u0003J\t\u0010L\u001a\u00020\bH\u00c6\u0003J\t\u0010M\u001a\u00020\fH\u00c6\u0003J\u00b5\u0001\u0010N\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u00102\b\b\u0002\u0010\u0014\u001a\u00020\u00102\b\b\u0002\u0010\u0015\u001a\u00020\u00102\b\b\u0002\u0010\u0016\u001a\u00020\u00102\b\b\u0002\u0010\u0017\u001a\u00020\bH\u00c6\u0001J\t\u0010O\u001a\u00020\fH\u00d6\u0001J\u0013\u0010P\u001a\u00020Q2\b\u0010R\u001a\u0004\u0018\u00010SH\u00d6\u0003J\t\u0010T\u001a\u00020\fH\u00d6\u0001J\t\u0010U\u001a\u00020\u0005H\u00d6\u0001J\u0019\u0010V\u001a\u00020W2\u0006\u0010X\u001a\u00020Y2\u0006\u0010Z\u001a\u00020\fH\u00d6\u0001R\u001a\u0010\u0011\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u001a\u0010\u0014\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001a\"\u0004\b$\u0010\u001cR\u001a\u0010\u0012\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u001a\"\u0004\b&\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\"R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u001a\u0010\u0015\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u001a\"\u0004\b+\u0010\u001cR\u001a\u0010\u0017\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u001a\u0010\u0013\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b0\u0010\u001a\"\u0004\b1\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\"R\u0011\u0010\u000e\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001eR\u0011\u0010\u0016\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\u001aR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010-\"\u0004\b6\u0010/R\u0011\u0010\r\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010-R\u001a\u0010\t\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\"\"\u0004\b9\u0010:R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010\u001a\"\u0004\b<\u0010\u001c\u00a8\u0006["}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/entities/CorralDetail;", "Landroid/os/Parcelable;", "establishment", "Lcom/basculasmagris/visorremotomixer/model/entities/EstablishmentDetail;", "name", "", "description", "remoteId", "", "updatedDate", "archiveDate", "animalQuantity", "", "rfid", "order", "weight", "", "actualTargetWeight", "customTargetWeight", "initialWeight", "currentWeight", "finalWeight", "percentage", "id", "(Lcom/basculasmagris/visorremotomixer/model/entities/EstablishmentDetail;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;IJIDDDDDDDJ)V", "getActualTargetWeight", "()D", "setActualTargetWeight", "(D)V", "getAnimalQuantity", "()I", "setAnimalQuantity", "(I)V", "getArchiveDate", "()Ljava/lang/String;", "getCurrentWeight", "setCurrentWeight", "getCustomTargetWeight", "setCustomTargetWeight", "getDescription", "getEstablishment", "()Lcom/basculasmagris/visorremotomixer/model/entities/EstablishmentDetail;", "getFinalWeight", "setFinalWeight", "getId", "()J", "setId", "(J)V", "getInitialWeight", "setInitialWeight", "getName", "getOrder", "getPercentage", "getRemoteId", "setRemoteId", "getRfid", "getUpdatedDate", "setUpdatedDate", "(Ljava/lang/String;)V", "getWeight", "setWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_release"})
public final class CorralDetail implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull
    private final com.basculasmagris.visorremotomixer.model.entities.EstablishmentDetail establishment = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    private long remoteId;
    @org.jetbrains.annotations.NotNull
    private java.lang.String updatedDate;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String archiveDate = null;
    private int animalQuantity;
    private final long rfid = 0L;
    private final int order = 0;
    private double weight;
    private double actualTargetWeight;
    private double customTargetWeight;
    private double initialWeight;
    private double currentWeight;
    private double finalWeight;
    private final double percentage = 0.0;
    private long id;
    public static final android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.CorralDetail> CREATOR = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.CorralDetail copy(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.EstablishmentDetail establishment, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String description, long remoteId, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.Nullable
    java.lang.String archiveDate, int animalQuantity, long rfid, int order, double weight, double actualTargetWeight, double customTargetWeight, double initialWeight, double currentWeight, double finalWeight, double percentage, long id) {
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
    
    public CorralDetail(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.EstablishmentDetail establishment, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String description, long remoteId, @org.jetbrains.annotations.NotNull
    java.lang.String updatedDate, @org.jetbrains.annotations.Nullable
    java.lang.String archiveDate, int animalQuantity, long rfid, int order, double weight, double actualTargetWeight, double customTargetWeight, double initialWeight, double currentWeight, double finalWeight, double percentage, long id) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.EstablishmentDetail component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.entities.EstablishmentDetail getEstablishment() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final long getRemoteId() {
        return 0L;
    }
    
    public final void setRemoteId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUpdatedDate() {
        return null;
    }
    
    public final void setUpdatedDate(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getArchiveDate() {
        return null;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int getAnimalQuantity() {
        return 0;
    }
    
    public final void setAnimalQuantity(int p0) {
    }
    
    public final long component8() {
        return 0L;
    }
    
    public final long getRfid() {
        return 0L;
    }
    
    public final int component9() {
        return 0;
    }
    
    public final int getOrder() {
        return 0;
    }
    
    public final double component10() {
        return 0.0;
    }
    
    public final double getWeight() {
        return 0.0;
    }
    
    public final void setWeight(double p0) {
    }
    
    public final double component11() {
        return 0.0;
    }
    
    public final double getActualTargetWeight() {
        return 0.0;
    }
    
    public final void setActualTargetWeight(double p0) {
    }
    
    public final double component12() {
        return 0.0;
    }
    
    public final double getCustomTargetWeight() {
        return 0.0;
    }
    
    public final void setCustomTargetWeight(double p0) {
    }
    
    public final double component13() {
        return 0.0;
    }
    
    public final double getInitialWeight() {
        return 0.0;
    }
    
    public final void setInitialWeight(double p0) {
    }
    
    public final double component14() {
        return 0.0;
    }
    
    public final double getCurrentWeight() {
        return 0.0;
    }
    
    public final void setCurrentWeight(double p0) {
    }
    
    public final double component15() {
        return 0.0;
    }
    
    public final double getFinalWeight() {
        return 0.0;
    }
    
    public final void setFinalWeight(double p0) {
    }
    
    public final double component16() {
        return 0.0;
    }
    
    public final double getPercentage() {
        return 0.0;
    }
    
    public final long component17() {
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
    public static final class Creator implements android.os.Parcelable.Creator<com.basculasmagris.visorremotomixer.model.entities.CorralDetail> {
        
        public Creator() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.CorralDetail createFromParcel(@org.jetbrains.annotations.NotNull
        android.os.Parcel in) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public final com.basculasmagris.visorremotomixer.model.entities.CorralDetail[] newArray(int size) {
            return null;
        }
    }
}