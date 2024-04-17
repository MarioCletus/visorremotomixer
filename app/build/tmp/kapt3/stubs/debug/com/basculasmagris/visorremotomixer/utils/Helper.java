package com.basculasmagris.visorremotomixer.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004\u00a8\u0006\t"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/Helper;", "", "()V", "getBluetoothService", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;", "setBluetoothService", "", "service", "Companion", "app_debug"})
public final class Helper {
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.utils.Helper.Companion Companion = null;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 3600;
    private static boolean isBluetoothEnabled = false;
    private static com.basculasmagris.visorremotomixer.services.BluetoothSDKService mService;
    private static com.basculasmagris.visorremotomixer.utils.Helper serviceInstance;
    
    public Helper() {
        super();
    }
    
    public final void setBluetoothService(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.services.BluetoothSDKService service) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.services.BluetoothSDKService getBluetoothService() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0015J\u001e\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0015J\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020\u0015J\u000e\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020\u0015J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010!\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010$\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010$\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\u0015J\u0016\u0010&\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020#2\u0006\u0010(\u001a\u00020\u0004J\u000e\u0010)\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u0015J\u0006\u0010*\u001a\u00020\u000fJ\u0016\u0010+\u001a\u00020#2\u0006\u0010,\u001a\u00020#2\u0006\u0010-\u001a\u00020.J6\u0010/\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00132\u0016\u00100\u001a\u0012\u0012\u0004\u0012\u00020201j\b\u0012\u0004\u0012\u000202`32\u0006\u00104\u001a\u00020\u00152\u0006\u00105\u001a\u00020\u0015J\u000e\u00106\u001a\u0002072\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u00108\u001a\u0002072\u0006\u00109\u001a\u00020:J\u000e\u0010;\u001a\u00020\u00152\u0006\u0010<\u001a\u00020\u0015J\u000e\u0010=\u001a\u0002072\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010>\u001a\u00020?2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010@\u001a\u00020\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\t\"\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f@BX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006A"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/Helper$Companion;", "", "()V", "MINUTES_PER_HOUR", "", "SECONDS_PER_HOUR", "SECONDS_PER_MINUTE", "isBluetoothEnabled", "", "()Z", "setBluetoothEnabled", "(Z)V", "mService", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;", "<set-?>", "Lcom/basculasmagris/visorremotomixer/utils/Helper;", "serviceInstance", "checkForInternet", "context", "Landroid/content/Context;", "diffDate", "", "startDate", "endDate", "originFormat", "formattedDate", "date", "targetFormat", "getApiDateFromString", "Ljava/time/LocalDateTime;", "getAppDateFromString", "getCurrentUser", "Lcom/basculasmagris/visorremotomixer/model/entities/UserRemote;", "getFormattedWeight", "weight", "", "getFormattedWeightKg", "defaultValue", "getNumberWithDecimals", "value", "decimals", "getRelativeDateTime", "getServiceInstance", "getWeightKg", "readData", "mixer", "Lcom/basculasmagris/visorremotomixer/model/entities/Mixer;", "isAllowedLoginOffline", "userList", "Ljava/util/ArrayList;", "Lcom/basculasmagris/visorremotomixer/model/entities/User;", "Lkotlin/collections/ArrayList;", "username", "password", "logOut", "", "logRoundRunComplete", "roundRunDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunDetail;", "md5", "input", "saveBluetoothState", "setProgressDialog", "Landroid/app/AlertDialog;", "message", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isBluetoothEnabled() {
            return false;
        }
        
        public final void setBluetoothEnabled(boolean p0) {
        }
        
        public final void saveBluetoothState(boolean isBluetoothEnabled) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.basculasmagris.visorremotomixer.utils.Helper getServiceInstance() {
            return null;
        }
        
        public final void logOut(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getFormattedWeight(double weight, @org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
        
        public final double getWeightKg(double readData, @org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.model.entities.Mixer mixer) {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getFormattedWeightKg(double weight, @org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getFormattedWeightKg(double weight, @org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        java.lang.String defaultValue) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getNumberWithDecimals(double value, int decimals) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.time.LocalDateTime getAppDateFromString(@org.jetbrains.annotations.NotNull
        java.lang.String date) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.time.LocalDateTime getApiDateFromString(@org.jetbrains.annotations.NotNull
        java.lang.String date) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String formattedDate(@org.jetbrains.annotations.NotNull
        java.lang.String date, @org.jetbrains.annotations.NotNull
        java.lang.String originFormat, @org.jetbrains.annotations.NotNull
        java.lang.String targetFormat) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String diffDate(@org.jetbrains.annotations.NotNull
        java.lang.String startDate, @org.jetbrains.annotations.NotNull
        java.lang.String endDate, @org.jetbrains.annotations.NotNull
        java.lang.String originFormat) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getRelativeDateTime(@org.jetbrains.annotations.NotNull
        java.lang.String date) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.app.AlertDialog setProgressDialog(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        java.lang.String message) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String md5(@org.jetbrains.annotations.NotNull
        java.lang.String input) {
            return null;
        }
        
        public final int isAllowedLoginOffline(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.User> userList, @org.jetbrains.annotations.NotNull
        java.lang.String username, @org.jetbrains.annotations.NotNull
        java.lang.String password) {
            return 0;
        }
        
        public final boolean checkForInternet(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.basculasmagris.visorremotomixer.model.entities.UserRemote getCurrentUser(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
        
        public final void logRoundRunComplete(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail roundRunDetail) {
        }
    }
}