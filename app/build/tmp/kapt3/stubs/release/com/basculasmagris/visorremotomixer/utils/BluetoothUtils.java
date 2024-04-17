package com.basculasmagris.visorremotomixer.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/BluetoothUtils;", "", "()V", "Companion", "app_release"})
public final class BluetoothUtils {
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.utils.BluetoothUtils.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_DISCOVERY_STARTED = "ACTION_DISCOVERY_STARTED";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_DISCOVERY_STOPPED = "ACTION_DISCOVERY_STOPPED";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_DEVICE_FOUND = "ACTION_DEVICE_FOUND";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_DEVICE_DISCONNECTED = "ACTION_DEVICE_DISCONNECTED";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_MESSAGE_RECEIVED = "ACTION_MESSAGE_RECEIVED";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_MESSAGE_SENT = "ACTION_MESSAGE_SENT";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_CONNECTION_ERROR = "ACTION_CONNECTION_ERROR";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_BONDED_DEVICES = "ACTION_BONDED_DEVICES";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String EXTRA_DEVICE = "EXTRA_DEVICE";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String EXTRA_DEVICES = "EXTRA_DEVICES";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String EXTRA_COMMAND = "EXTRA_COMMAND";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_COMMAND_RECEIVED = "ACTION_COMMAND_RECEIVED";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_COMMAND_SENT = "ACTION_COMMAND_SENT";
    
    public BluetoothUtils() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u001f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0014\u0010\u000b\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0014\u0010\r\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0014\u0010\u000f\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0014\u0010\u0011\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0014\u0010\u0013\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0014\u0010\u0015\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006R\u0014\u0010\u0017\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0006R\u0014\u0010\u0019\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0006R\u0014\u0010\u001b\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0006R\u0014\u0010\u001d\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0006R\u0014\u0010\u001f\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0006R\u0014\u0010!\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0006\u00a8\u0006#"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/BluetoothUtils$Companion;", "", "()V", "ACTION_BONDED_DEVICES", "", "getACTION_BONDED_DEVICES", "()Ljava/lang/String;", "ACTION_COMMAND_RECEIVED", "getACTION_COMMAND_RECEIVED", "ACTION_COMMAND_SENT", "getACTION_COMMAND_SENT", "ACTION_CONNECTION_ERROR", "getACTION_CONNECTION_ERROR", "ACTION_DEVICE_CONNECTED", "getACTION_DEVICE_CONNECTED", "ACTION_DEVICE_DISCONNECTED", "getACTION_DEVICE_DISCONNECTED", "ACTION_DEVICE_FOUND", "getACTION_DEVICE_FOUND", "ACTION_DISCOVERY_STARTED", "getACTION_DISCOVERY_STARTED", "ACTION_DISCOVERY_STOPPED", "getACTION_DISCOVERY_STOPPED", "ACTION_MESSAGE_RECEIVED", "getACTION_MESSAGE_RECEIVED", "ACTION_MESSAGE_SENT", "getACTION_MESSAGE_SENT", "EXTRA_COMMAND", "getEXTRA_COMMAND", "EXTRA_DEVICE", "getEXTRA_DEVICE", "EXTRA_DEVICES", "getEXTRA_DEVICES", "EXTRA_MESSAGE", "getEXTRA_MESSAGE", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_DISCOVERY_STARTED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_DISCOVERY_STOPPED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_DEVICE_FOUND() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_DEVICE_CONNECTED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_DEVICE_DISCONNECTED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_MESSAGE_RECEIVED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_MESSAGE_SENT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_CONNECTION_ERROR() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_BONDED_DEVICES() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getEXTRA_DEVICE() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getEXTRA_MESSAGE() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getEXTRA_DEVICES() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getEXTRA_COMMAND() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_COMMAND_RECEIVED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getACTION_COMMAND_SENT() {
            return null;
        }
    }
}