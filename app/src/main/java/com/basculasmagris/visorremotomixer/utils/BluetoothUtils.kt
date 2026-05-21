package com.basculasmagris.visorremotomixer.utils

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class BluetoothUtils {
    companion object {
        val ACTION_DISCOVERY_STARTED = "ACTION_DISCOVERY_STARTED"
        val ACTION_DISCOVERY_STOPPED = "ACTION_DISCOVERY_STOPPED"
        val ACTION_DEVICE_FOUND = "ACTION_DEVICE_FOUND"
        val ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED"
        val ACTION_DEVICE_DISCONNECTED = "ACTION_DEVICE_DISCONNECTED"
        val ACTION_MESSAGE_RECEIVED = "ACTION_MESSAGE_RECEIVED"
        val ACTION_MESSAGE_SENT = "ACTION_MESSAGE_SENT"
        val ACTION_CONNECTION_ERROR = "ACTION_CONNECTION_ERROR"
        val ACTION_BONDED_DEVICES = "ACTION_BONDED_DEVICES"
        val EXTRA_DEVICE = "EXTRA_DEVICE"
        val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        val EXTRA_DEVICES = "EXTRA_DEVICES"
        val EXTRA_COMMAND = "EXTRA_COMMAND"
        val ACTION_COMMAND_RECEIVED = "ACTION_COMMAND_RECEIVED"
        val ACTION_COMMAND_SENT = "ACTION_COMMAND_SENT"


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