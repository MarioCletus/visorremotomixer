package com.basculasmagris.visorremotomixer.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/BluetoothSDKListenerHelper;", "", "()V", "Companion", "app_debug"})
public final class BluetoothSDKListenerHelper {
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper.Companion Companion = null;
    private static com.basculasmagris.visorremotomixer.utils.BluetoothSDKListenerHelper.Companion.BluetoothSDKBroadcastReceiver mBluetoothSDKBroadcastReceiver;
    
    public BluetoothSDKListenerHelper() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nJ\u0018\u0010\u000b\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/BluetoothSDKListenerHelper$Companion;", "", "()V", "mBluetoothSDKBroadcastReceiver", "Lcom/basculasmagris/visorremotomixer/utils/BluetoothSDKListenerHelper$Companion$BluetoothSDKBroadcastReceiver;", "registerBluetoothSDKListener", "", "context", "Landroid/content/Context;", "listener", "Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "unregisterBluetoothSDKListener", "BluetoothSDKBroadcastReceiver", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void registerBluetoothSDKListener(@org.jetbrains.annotations.Nullable
        android.content.Context context, @org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener listener) {
        }
        
        public final void unregisterBluetoothSDKListener(@org.jetbrains.annotations.Nullable
        android.content.Context context, @org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener listener) {
        }
        
        @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004J\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/basculasmagris/visorremotomixer/utils/BluetoothSDKListenerHelper$Companion$BluetoothSDKBroadcastReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "mGlobalListener", "Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "removeBluetoothSDKListener", "", "listener", "setBluetoothSDKListener", "app_debug"})
        public static final class BluetoothSDKBroadcastReceiver extends android.content.BroadcastReceiver {
            private com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener mGlobalListener;
            
            public BluetoothSDKBroadcastReceiver() {
                super();
            }
            
            public final void setBluetoothSDKListener(@org.jetbrains.annotations.NotNull
            com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener listener) {
            }
            
            public final boolean removeBluetoothSDKListener(@org.jetbrains.annotations.NotNull
            com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener listener) {
                return false;
            }
            
            @java.lang.Override
            public void onReceive(@org.jetbrains.annotations.Nullable
            android.content.Context context, @org.jetbrains.annotations.Nullable
            android.content.Intent intent) {
            }
        }
    }
}