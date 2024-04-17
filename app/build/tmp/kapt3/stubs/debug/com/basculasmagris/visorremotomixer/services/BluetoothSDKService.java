package com.basculasmagris.visorremotomixer.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u00046789B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nH\u0002J\u0010\u0010\u0017\u001a\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001a\u001a\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\u001fJ\u0014\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u0015H\u0016J\b\u0010%\u001a\u00020\u0015H\u0016J\"\u0010&\u001a\u00020\'2\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010(\u001a\u00020\'2\u0006\u0010)\u001a\u00020\'H\u0016J*\u0010*\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u00042\u000e\u0010,\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010-2\b\u0010.\u001a\u0004\u0018\u00010/H\u0002J*\u00100\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u00042\u000e\u0010,\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010-2\b\u00101\u001a\u0004\u0018\u00010\u0004H\u0002J\u0010\u00102\u001a\u00020\u00152\u0006\u00103\u001a\u000204H\u0002J\u000e\u00105\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00060\bR\u00020\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0018\u00010\fR\u00020\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0018\u00010\u000eR\u00020\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0018\u00010\u0010R\u00020\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2 = {"Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;", "Landroid/app/Service;", "()V", "MY_UUID", "", "PREF_UNIQUE_ID", "TAG", "binder", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$LocalBinder;", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "connectThread", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$ConnectThread;", "connectThreadWithTransfer", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$ConnectThreadWithTransfer;", "connectedThread", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$ConnectedThread;", "discoveryBroadcastReceiver", "Landroid/content/BroadcastReceiver;", "uniqueID", "cancelDiscovery", "", "btAdapter", "getBluetoothAddress", "btDevice", "Landroid/bluetooth/BluetoothDevice;", "getBluetoothName", "id", "context", "Landroid/content/Context;", "isConnected", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "pushBroadcastCommand", "action", "devices", "Ljava/util/ArrayList;", "command", "", "pushBroadcastMessage", "message", "socketConcetWithPermission", "socket", "Landroid/bluetooth/BluetoothSocket;", "startDiscovery", "ConnectThread", "ConnectThreadWithTransfer", "ConnectedThread", "LocalBinder", "app_debug"})
public final class BluetoothSDKService extends android.app.Service {
    private final com.basculasmagris.visorremotomixer.services.BluetoothSDKService.LocalBinder binder = null;
    private final java.lang.String TAG = "DEBServ";
    private android.bluetooth.BluetoothAdapter bluetoothAdapter;
    private final java.lang.String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private com.basculasmagris.visorremotomixer.services.BluetoothSDKService.ConnectThread connectThread;
    private com.basculasmagris.visorremotomixer.services.BluetoothSDKService.ConnectThreadWithTransfer connectThreadWithTransfer;
    private com.basculasmagris.visorremotomixer.services.BluetoothSDKService.ConnectedThread connectedThread;
    
    /**
     * Broadcast Receiver for catching ACTION_FOUND aka new device discovered
     */
    private final android.content.BroadcastReceiver discoveryBroadcastReceiver = null;
    private java.lang.String uniqueID;
    private final java.lang.String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    
    public BluetoothSDKService() {
        super();
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    public final boolean isConnected() {
        return false;
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    private final void pushBroadcastMessage(java.lang.String action, java.util.ArrayList<android.bluetooth.BluetoothDevice> devices, java.lang.String message) {
    }
    
    private final void pushBroadcastCommand(java.lang.String action, java.util.ArrayList<android.bluetooth.BluetoothDevice> devices, byte[] command) {
    }
    
    @org.jetbrains.annotations.Nullable
    @kotlin.jvm.Synchronized
    public final synchronized java.lang.String id(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getBluetoothName(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice btDevice) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getBluetoothAddress(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice btDevice) {
        return null;
    }
    
    public final void startDiscovery(@org.jetbrains.annotations.NotNull
    android.bluetooth.BluetoothAdapter btAdapter) {
    }
    
    private final void cancelDiscovery(android.bluetooth.BluetoothAdapter btAdapter) {
    }
    
    private final void socketConcetWithPermission(android.bluetooth.BluetoothSocket socket) {
    }
    
    /**
     * Class used for the client Binder.
     */
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\u0004J\u0006\u0010\u000b\u001a\u00020\u0004J\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\rJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0004J\u0006\u0010\u0017\u001a\u00020\u0004J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001d\u00a8\u0006\u001e"}, d2 = {"Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$LocalBinder;", "Landroid/os/Binder;", "(Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;)V", "connectKnowDevice", "", "activity", "Landroid/app/Activity;", "bluetoothDevice", "Landroid/bluetooth/BluetoothDevice;", "connectKnowDeviceWithTransfer", "disconnectKnowDevice", "disconnectKnowDeviceWithTransfer", "getBondedDevices", "Ljava/util/ArrayList;", "getService", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;", "isConnected", "", "startDataTrasfer", "startDiscovery", "context", "Landroid/content/Context;", "stopDataTrasfer", "stopDiscovery", "write", "", "command", "", "msg", "", "app_debug"})
    public final class LocalBinder extends android.os.Binder {
        
        public LocalBinder() {
            super();
        }
        
        /**
         * Enable the discovery, registering a broadcastreceiver {@link discoveryBroadcastReceiver}
         * The discovery filter by LABELER_SERVER_TOKEN_NAME
         */
        public final void startDiscovery(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.ArrayList<android.bluetooth.BluetoothDevice> getBondedDevices() {
            return null;
        }
        
        public final void connectKnowDevice(@org.jetbrains.annotations.NotNull
        android.app.Activity activity, @org.jetbrains.annotations.NotNull
        android.bluetooth.BluetoothDevice bluetoothDevice) {
        }
        
        public final void connectKnowDeviceWithTransfer(@org.jetbrains.annotations.NotNull
        android.bluetooth.BluetoothDevice bluetoothDevice) {
        }
        
        public final void disconnectKnowDevice() {
        }
        
        public final void disconnectKnowDeviceWithTransfer() {
        }
        
        public final void startDataTrasfer() {
        }
        
        public final void stopDataTrasfer() {
        }
        
        /**
         * stop discovery
         */
        public final void stopDiscovery() {
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.basculasmagris.visorremotomixer.services.BluetoothSDKService getService() {
            return null;
        }
        
        public final boolean isConnected() {
            return false;
        }
        
        public final int write(@org.jetbrains.annotations.NotNull
        java.lang.String msg) {
            return 0;
        }
        
        public final int write(@org.jetbrains.annotations.NotNull
        byte[] command) {
            return 0;
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\r\u001a\u00020\u000eJ\b\u0010\u000f\u001a\u00020\u000eH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u0004\u0018\u00010\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0010"}, d2 = {"Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$ConnectThread;", "Ljava/lang/Thread;", "activity", "Landroid/app/Activity;", "device", "Landroid/bluetooth/BluetoothDevice;", "(Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;Landroid/app/Activity;Landroid/bluetooth/BluetoothDevice;)V", "mmSocket", "Landroid/bluetooth/BluetoothSocket;", "getMmSocket", "()Landroid/bluetooth/BluetoothSocket;", "mmSocket$delegate", "Lkotlin/Lazy;", "cancel", "", "run", "app_debug"})
    final class ConnectThread extends java.lang.Thread {
        private final android.app.Activity activity = null;
        private final kotlin.Lazy mmSocket$delegate = null;
        
        public ConnectThread(@org.jetbrains.annotations.NotNull
        android.app.Activity activity, @org.jetbrains.annotations.NotNull
        android.bluetooth.BluetoothDevice device) {
            super();
        }
        
        private final android.bluetooth.BluetoothSocket getMmSocket() {
            return null;
        }
        
        @java.lang.Override
        public void run() {
        }
        
        public final void cancel() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u000eJ\b\u0010\u000f\u001a\u00020\fH\u0016J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0014R\u001d\u0010\u0005\u001a\u0004\u0018\u00010\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$ConnectThreadWithTransfer;", "Ljava/lang/Thread;", "device", "Landroid/bluetooth/BluetoothDevice;", "(Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;Landroid/bluetooth/BluetoothDevice;)V", "mmSocket", "Landroid/bluetooth/BluetoothSocket;", "getMmSocket", "()Landroid/bluetooth/BluetoothSocket;", "mmSocket$delegate", "Lkotlin/Lazy;", "cancel", "", "isConnected", "", "run", "write", "", "msg", "", "", "app_debug"})
    final class ConnectThreadWithTransfer extends java.lang.Thread {
        private final kotlin.Lazy mmSocket$delegate = null;
        
        public ConnectThreadWithTransfer(@org.jetbrains.annotations.NotNull
        android.bluetooth.BluetoothDevice device) {
            super();
        }
        
        private final android.bluetooth.BluetoothSocket getMmSocket() {
            return null;
        }
        
        @java.lang.Override
        public void run() {
        }
        
        public final void cancel() {
        }
        
        public final boolean isConnected() {
            return false;
        }
        
        public final int write(@org.jetbrains.annotations.NotNull
        byte[] msg) {
            return 0;
        }
        
        public final int write(@org.jetbrains.annotations.NotNull
        java.lang.String msg) {
            return 0;
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u0005\u001a\u00020\u0006J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService$ConnectedThread;", "Ljava/lang/Thread;", "mmSocket", "Landroid/bluetooth/BluetoothSocket;", "(Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;Landroid/bluetooth/BluetoothSocket;)V", "isConnected", "", "mmBuffer", "", "mmInStream", "Ljava/io/InputStream;", "mmOutStream", "Ljava/io/OutputStream;", "cancel", "", "run", "write", "", "bytes", "msg", "", "app_debug"})
    final class ConnectedThread extends java.lang.Thread {
        private final android.bluetooth.BluetoothSocket mmSocket = null;
        private final java.io.InputStream mmInStream = null;
        private final java.io.OutputStream mmOutStream = null;
        private final byte[] mmBuffer = null;
        private boolean isConnected = false;
        
        public ConnectedThread(@org.jetbrains.annotations.NotNull
        android.bluetooth.BluetoothSocket mmSocket) {
            super();
        }
        
        @java.lang.Override
        public void run() {
        }
        
        public final int write(@org.jetbrains.annotations.NotNull
        java.lang.String msg) {
            return 0;
        }
        
        public final int write(@org.jetbrains.annotations.NotNull
        byte[] bytes) {
            return 0;
        }
        
        public final void cancel() {
        }
        
        public final boolean isConnected() {
            return false;
        }
    }
}