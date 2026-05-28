package com.basculasmagris.visorremotomixer.utils

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class BluetoothUtils {
    companion object {
        const val ACTION_DISCOVERY_STARTED = "ACTION_DISCOVERY_STARTED"
        const val ACTION_DISCOVERY_STOPPED = "ACTION_DISCOVERY_STOPPED"
        const val ACTION_DEVICE_FOUND = "ACTION_DEVICE_FOUND"
        const val ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED"
        const val ACTION_DEVICE_DISCONNECTED = "ACTION_DEVICE_DISCONNECTED"
        const val ACTION_MESSAGE_RECEIVED = "ACTION_MESSAGE_RECEIVED"
        const val ACTION_MESSAGE_SENT = "ACTION_MESSAGE_SENT"
        const val ACTION_CONNECTION_ERROR = "ACTION_CONNECTION_ERROR"
        const val ACTION_BONDED_DEVICES = "ACTION_BONDED_DEVICES"
        const val EXTRA_DEVICE = "EXTRA_DEVICE"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        const val EXTRA_DEVICES = "EXTRA_DEVICES"
        const val EXTRA_COMMAND = "EXTRA_COMMAND"
        const val ACTION_COMMAND_RECEIVED = "ACTION_COMMAND_RECEIVED"
        const val ACTION_COMMAND_SENT = "ACTION_COMMAND_SENT"


        fun getAddress(
            context: Context,
            btDevice: BluetoothDevice?
        ): String {

            btDevice ?: return ""

            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return ""
            }

            return btDevice.address
        }

        fun getBluetoothName(
            context: Context,
            btDevice: BluetoothDevice?
        ): String {

            btDevice ?: return ""

            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return ""
            }

            return btDevice.name ?: ""
        }
    }


}