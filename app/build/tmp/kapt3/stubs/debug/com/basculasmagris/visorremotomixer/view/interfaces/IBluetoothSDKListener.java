package com.basculasmagris.visorremotomixer.view.interfaces;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H&J\u001c\u0010\u0007\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\tH&J\u001c\u0010\n\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\tH&J\u0012\u0010\u000b\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0006H&J\b\u0010\f\u001a\u00020\u0003H&J\u0012\u0010\r\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0006H&J\b\u0010\u000e\u001a\u00020\u0003H&J\b\u0010\u000f\u001a\u00020\u0003H&J\u0012\u0010\u0010\u001a\u00020\u00032\b\u0010\b\u001a\u0004\u0018\u00010\u0011H&J\u001c\u0010\u0012\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\u0011H&J\u001c\u0010\u0013\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\u0011H&\u00a8\u0006\u0014"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "", "onBondedDevices", "", "device", "", "Landroid/bluetooth/BluetoothDevice;", "onCommandReceived", "message", "", "onCommandSent", "onDeviceConnected", "onDeviceDisconnected", "onDeviceDiscovered", "onDiscoveryStarted", "onDiscoveryStopped", "onError", "", "onMessageReceived", "onMessageSent", "app_debug"})
public abstract interface IBluetoothSDKListener {
    
    /**
     * from action BluetoothUtils.ACTION_DISCOVERY_STARTED
     */
    public abstract void onDiscoveryStarted();
    
    /**
     * from action BluetoothUtils.ACTION_DISCOVERY_STOPPED
     */
    public abstract void onDiscoveryStopped();
    
    /**
     * from action BluetoothUtils.ACTION_DEVICE_FOUND
     */
    public abstract void onDeviceDiscovered(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice device);
    
    /**
     * from action BluetoothUtils.ACTION_DEVICE_CONNECTED
     */
    public abstract void onDeviceConnected(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice device);
    
    /**
     * from action BluetoothUtils.ACTION_MESSAGE_RECEIVED
     */
    public abstract void onMessageReceived(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice device, @org.jetbrains.annotations.Nullable
    java.lang.String message);
    
    /**
     * from action BluetoothUtils.ACTION_COMMAND_RECEIVED
     */
    public abstract void onCommandReceived(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice device, @org.jetbrains.annotations.Nullable
    byte[] message);
    
    /**
     * from action BluetoothUtils.ACTION_MESSAGE_SENT
     */
    public abstract void onMessageSent(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice device, @org.jetbrains.annotations.Nullable
    java.lang.String message);
    
    /**
     * from action BluetoothUtils.ACTION_MESSAGE_SENT
     */
    public abstract void onCommandSent(@org.jetbrains.annotations.Nullable
    android.bluetooth.BluetoothDevice device, @org.jetbrains.annotations.Nullable
    byte[] message);
    
    /**
     * from action BluetoothUtils.ACTION_CONNECTION_ERROR
     */
    public abstract void onError(@org.jetbrains.annotations.Nullable
    java.lang.String message);
    
    /**
     * from action BluetoothUtils.ACTION_DEVICE_DISCONNECTED
     */
    public abstract void onDeviceDisconnected();
    
    /**
     * from action BluetoothUtils.ACTION_BONDED_DEVICES
     */
    public abstract void onBondedDevices(@org.jetbrains.annotations.Nullable
    java.util.List<android.bluetooth.BluetoothDevice> device);
}