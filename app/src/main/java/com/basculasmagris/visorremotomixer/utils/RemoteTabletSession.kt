package com.basculasmagris.visorremotomixer.utils

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer

object RemoteTabletSession {

    var selectedTablet: TabletMixer? = null
        private set

    var bluetoothDevice: BluetoothDevice? = null
        private set

    val tabletName: String
        get() = selectedTablet?.name ?: ""

    val mixerName: String
        get() = selectedTablet?.mixerName ?: ""

    val serial: String
        get() = selectedTablet?.serial ?: ""

    val mac: String
        get() = selectedTablet?.mac ?: ""

    val btName: String
        get() = selectedTablet?.btName ?: ""

    fun getBluetoothName(context: Context): String {
        return BluetoothUtils.getBluetoothName(context, bluetoothDevice)
    }

    fun getBluetoothAddress(context: Context): String {
        return BluetoothUtils.getAddress(context, bluetoothDevice)
    }

    fun setTablet(tabletMixer: TabletMixer?) {
        selectedTablet = tabletMixer
    }

    fun setBluetoothDevice(device: BluetoothDevice?) {
        bluetoothDevice = device
    }

    fun setConnection(
        tabletMixer: TabletMixer?,
        device: BluetoothDevice?
    ) {
        selectedTablet = tabletMixer
        bluetoothDevice = device
    }

    fun clear() {
        selectedTablet = null
        bluetoothDevice = null
    }
}