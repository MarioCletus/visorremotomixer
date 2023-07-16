package com.basculasmagris.visorremotomixer.utils

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener

class BluetoothSDKListenerHelper {
    companion object {

        private var mBluetoothSDKBroadcastReceiver: BluetoothSDKBroadcastReceiver? = null

        class BluetoothSDKBroadcastReceiver : BroadcastReceiver() {
            private var mGlobalListener: IBluetoothSDKListener? = null

            fun setBluetoothSDKListener(listener: IBluetoothSDKListener) {
                mGlobalListener = listener
            }

            fun removeBluetoothSDKListener(listener: IBluetoothSDKListener): Boolean {
                if (mGlobalListener == listener) {
                    mGlobalListener = null
                }

                return mGlobalListener == null
            }

            override fun onReceive(context: Context?, intent: Intent?) {
//                Log.i("BLUE", "Helper onReceive: ${intent?.action}")

                val devices =
                    intent!!.getParcelableArrayListExtra<BluetoothDevice>(BluetoothUtils.EXTRA_DEVICES)

                when (intent.action) {
                    BluetoothUtils.ACTION_DEVICE_FOUND -> {
                        devices?.let {
                            mGlobalListener!!.onDeviceDiscovered(it[0])
                        }
                    }
                    BluetoothUtils.ACTION_DISCOVERY_STARTED -> {
                        mGlobalListener!!.onDiscoveryStarted()
                    }
                    BluetoothUtils.ACTION_DISCOVERY_STOPPED -> {
                        Log.i("BLUE", "****** ACTION_DISCOVERY_STOPPED")
                        mGlobalListener!!.onDiscoveryStopped()
                    }
                    BluetoothUtils.ACTION_DEVICE_CONNECTED -> {
                        devices?.let {
                            mGlobalListener!!.onDeviceConnected(it[0])
                        }
                    }
                    BluetoothUtils.ACTION_MESSAGE_RECEIVED -> {
                        val message = intent.getStringExtra(BluetoothUtils.EXTRA_MESSAGE)
                        devices?.let {
                            mGlobalListener!!.onMessageReceived(it[0], message)
                        }
                    }
                    BluetoothUtils.ACTION_COMMAND_RECEIVED -> {
                        val command = intent.getByteArrayExtra(BluetoothUtils.EXTRA_COMMAND)
                        Log.i("DEBBTS","ACTION_COMMAND_RECEIVED")
                        devices?.let {
                            mGlobalListener!!.onCommandReceived(it[0], command)
                        }
                    }
                    BluetoothUtils.ACTION_MESSAGE_SENT -> {
                        val message = intent.getStringExtra(BluetoothUtils.EXTRA_MESSAGE)
                        devices?.let {
                            mGlobalListener!!.onMessageSent(it[0],message)
                        }
                    }
                    BluetoothUtils.ACTION_COMMAND_SENT -> {
                        val command = intent.getByteArrayExtra(BluetoothUtils.EXTRA_COMMAND)
                        devices?.let {
                            mGlobalListener!!.onCommandSent(it[0],command)
                        }
                    }
                    BluetoothUtils.ACTION_CONNECTION_ERROR -> {
                        val message = intent!!.getStringExtra(BluetoothUtils.EXTRA_MESSAGE)
                        mGlobalListener!!.onError(message)
                    }
                    BluetoothUtils.ACTION_DEVICE_DISCONNECTED -> {
                        mGlobalListener!!.onDeviceDisconnected()
                    }
                    BluetoothUtils.ACTION_BONDED_DEVICES -> {
                        mGlobalListener!!.onBondedDevices(devices)
                    }
                }
            }
        }

        fun registerBluetoothSDKListener(
            context: Context?,
            listener: IBluetoothSDKListener
        ) {
            Log.i("BLUE", "Helper registerBluetoothSDKListener")

            if (mBluetoothSDKBroadcastReceiver == null) {
                mBluetoothSDKBroadcastReceiver = BluetoothSDKBroadcastReceiver()
                Log.i("BLUE", "Helper mBluetoothSDKBroadcastReceiver")

                val intentFilter = IntentFilter().also {
                    it.addAction(BluetoothUtils.ACTION_DEVICE_FOUND)
                    it.addAction(BluetoothUtils.ACTION_DISCOVERY_STARTED)
                    it.addAction(BluetoothUtils.ACTION_DISCOVERY_STOPPED)
                    it.addAction(BluetoothUtils.ACTION_DEVICE_CONNECTED)
                    it.addAction(BluetoothUtils.ACTION_MESSAGE_RECEIVED)
                    it.addAction(BluetoothUtils.ACTION_COMMAND_RECEIVED)
                    it.addAction(BluetoothUtils.ACTION_MESSAGE_SENT)
                    it.addAction(BluetoothUtils.ACTION_CONNECTION_ERROR)
                    it.addAction(BluetoothUtils.ACTION_DEVICE_DISCONNECTED)
                    it.addAction(BluetoothUtils.ACTION_BONDED_DEVICES)
                }


                LocalBroadcastManager.getInstance(context!!).registerReceiver(
                    mBluetoothSDKBroadcastReceiver!!, intentFilter
                )
            }

            mBluetoothSDKBroadcastReceiver!!.setBluetoothSDKListener(listener)
        }

        fun unregisterBluetoothSDKListener(
            context: Context?,
            listener: IBluetoothSDKListener
        ) {

            if (mBluetoothSDKBroadcastReceiver != null) {
                val empty = mBluetoothSDKBroadcastReceiver!!.removeBluetoothSDKListener(listener)


                if (empty) {
                    LocalBroadcastManager.getInstance(context!!)
                        .unregisterReceiver(mBluetoothSDKBroadcastReceiver!!)
                    mBluetoothSDKBroadcastReceiver = null
                }
            }
        }
    }
}