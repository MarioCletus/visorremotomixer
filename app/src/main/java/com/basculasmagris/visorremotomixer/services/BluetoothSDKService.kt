package com.basculasmagris.visorremotomixer.services

import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.*
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.basculasmagris.visorremotomixer.utils.BluetoothUtils
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.activities.MixerConfigActivity
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class BluetoothSDKService : Service() {

    // Service Binder
    private val binder = LocalBinder()

    // Bluetooth stuff
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var pairedDevices: MutableSet<BluetoothDevice>
    private var connectedDevice: BluetoothDevice? = null
    private val MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    private val RESULT_INTENT = 15

    // Bluetooth connections
    private var connectThread: ConnectThread? = null
    private var connectThreadWithTransfer: ConnectThreadWithTransfer? = null

    private var connectedThread: ConnectedThread? = null
    private var mAcceptThread: AcceptThread? = null

    // Invoked only first time
    override fun onCreate() {
        super.onCreate()
        Log.i("BLUE", "Service onCreate")
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Log.i("BLUE", "No soportadoo ********")
        }

    }

    fun isConnected(): Boolean {
        return binder?.isConnected() ?: false
    }


    // Invoked every service star
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("BLUE", "Service onStartCommand")
        return START_STICKY
    }

    /**
     * Class used for the client Binder.
     */
    inner class LocalBinder : Binder() {
        /**
         * Enable the discovery, registering a broadcastreceiver {@link discoveryBroadcastReceiver}
         * The discovery filter by LABELER_SERVER_TOKEN_NAME
         */
        public fun startDiscovery(context: Context) {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            registerReceiver(discoveryBroadcastReceiver, filter)
            bluetoothAdapter.startDiscovery()
            pushBroadcastMessage(BluetoothUtils.ACTION_DISCOVERY_STARTED, null, null)
        }


        fun getBondedDevices() : ArrayList<BluetoothDevice>{
            val devices = ArrayList<BluetoothDevice>()
            try{
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
                //filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    devices.add(device)
                    Log.i("BLUE", "[getBondedDevices] deviceName: $deviceName | $deviceHardwareAddress")
                }
                pushBroadcastMessage(BluetoothUtils.ACTION_BONDED_DEVICES, devices, null)
            } catch (ex: java.lang.Exception) {
                Log.i("BLUE", "[getBondedDevices] ${ex.message}")
            }
            return devices
        }

        fun connectKnowDevice(activity: Activity, bluetoothDevice: BluetoothDevice) {
            Log.i("BLUE", "***Se CONECTA el servicio")
            connectThread = ConnectThread(activity, bluetoothDevice)
            connectThread?.start()
        }

        fun connectKnowDeviceWithTransfer(bluetoothDevice: BluetoothDevice) {
            Log.i("BLUE", "***Se CONECTA el servicio TR ${bluetoothDevice.name}")
            connectThreadWithTransfer = ConnectThreadWithTransfer(bluetoothDevice)
            connectThreadWithTransfer?.start()
        }

        fun disconnectKnowDevice() {
            Log.i("BLUE", "***Se desconecta el servicio")
            connectThreadWithTransfer?.cancel()
        }

        fun disconnectKnowDeviceWithTransfer() {
            Log.i("BLUE", "***Se desconecta el servicio TR")

            connectedThread?.cancel()
            connectThreadWithTransfer?.cancel()
            connectedThread?.interrupt()
            connectThreadWithTransfer?.interrupt()
            connectThreadWithTransfer = null
            connectedThread = null

        }

        fun startDataTrasfer(){
            Log.i("BLUE", "*** connectedThread: ${connectedThread}")
            connectedThread?.start()
        }

        fun stopDataTrasfer(){
            connectedThread?.cancel()
        }

        /**
         * stop discovery
         */
        public fun stopDiscovery() {
            bluetoothAdapter.cancelDiscovery()
            pushBroadcastMessage(BluetoothUtils.ACTION_DISCOVERY_STOPPED, null, null)
        }

        fun getService(): BluetoothSDKService = this@BluetoothSDKService
        fun isConnected(): Boolean {
            return connectThreadWithTransfer?.isConnected() ?: false
        }

        fun write(msg : String) : Int{
            return connectThreadWithTransfer?.write(msg) ?: -1
        }

        fun write(command : ByteArray) : Int{
            try {
                val retornoDelWrite : Int = connectThreadWithTransfer?.write(command) ?: -1
                Log.i("BLE","write command ${String(command)} $retornoDelWrite $connectThreadWithTransfer")
                return retornoDelWrite
            }catch (e :Exception){
                Log.i("BLE","write command Exception $e")
                return -1
            }
        }
        // other stuff
    }


    /**
     * Broadcast Receiver for catching ACTION_FOUND aka new device discovered
     */
    private val discoveryBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.i("BLUE", "[discoveryBroadcastReceiver] onReceive | ACTION: ${intent.action}")
            when(intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    pushBroadcastMessage(BluetoothUtils.ACTION_DISCOVERY_STOPPED, null , null)
                }
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        Log.i("BLUE", "ACTION_FOUND: ${device.name}")
                        pushBroadcastMessage(BluetoothUtils.ACTION_DEVICE_FOUND, arrayListOf(device) , null)
                    }
                }
            }
        }
    }

    private inner class AcceptThread : Thread() {

    }


    private inner class ConnectThread(activity: Activity, device: BluetoothDevice) : Thread() {

        private val activity = activity
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
        }

        override fun run() {
            Log.i("BLUE", "**** uniqueID: $MY_UUID")
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                try
                {
                    socket.connect()
                    connectedThread = ConnectedThread(socket)
                    Log.i("BLUEFAT", "conexión exitosa")
                    if(activity is MainActivity){
                        activity.bluetoothConnectionSuccess()
                    }else if(activity is MixerConfigActivity){
                        activity.bluetoothConnectionSuccess()
                    }

                } catch (e: IOException) {
                    Log.i("BLUEFAT", e.message.toString())
                    if(activity is MainActivity){
                        activity.bluetoothConnectionError()
                    }else if(activity is MixerConfigActivity){
                        activity.bluetoothConnectionError()
                    }

                }

            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("BLUE", "Could not close the client socket", e)
            }
        }

    }

    private inner class ConnectThreadWithTransfer(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
        }

        override fun run() {

            try {
                // Cancel discovery because it otherwise slows down the connection.
                bluetoothAdapter.cancelDiscovery()

                mmSocket?.let { socket ->
                    Log.i("BLUE", "**** uniqueID: ${MY_UUID} | socket: ${socket.toString()}")
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()

                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    connectedThread = ConnectedThread(socket)
                    //mAcceptThread = AcceptThread()
                    //mAcceptThread?.start()
                    connectedThread?.start()
                    Log.i("BLUE", "***Transfer connectedThread: $connectedThread")
                }
            } catch (e: IOException) {
                Log.e("BLUE", "Error in socket.connect ${mmSocket}", e)
            }

        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                if (mmSocket?.isConnected == true) {
                    mmSocket?.close()
                }
            } catch (e: IOException) {
                Log.e("BLUE", "Could not close the client socket", e)
            }
        }

        fun isConnected(): Boolean {
            return connectedThread?.isConnected() ?: false
        }

        fun write(msg : ByteArray) : Int{
            return connectedThread?.write(msg) ?: -1
        }

        fun write(msg : String) : Int {
            return connectedThread?.write(msg) ?: -1
        }
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(4096) // mmBuffer store for the stream
        private var isConnected : Boolean = false

        override fun run() {
            Log.i("BLUE", "BEGIN ConnectedThread ${mmSocket}");
            var numBytes: Int // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs.
            pushBroadcastMessage(BluetoothUtils.ACTION_DEVICE_CONNECTED,arrayListOf(mmSocket.remoteDevice),null)
            isConnected = true
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.i("BLUE", "Errno Bytes: ${e.message}")
                    isConnected = false
                    pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR,null,"Input stream was disconnected")
                    break
                }
                if(numBytes<3){
                    Log.i("PING","Ping: ${String(mmBuffer,0,numBytes)}")
                    continue
                }
                isConnected = true
                val arrayCommand : ByteArray = mmBuffer.copyOfRange(3,numBytes)
                if(numBytes >3 && mmBuffer.copyOfRange(0,3).contentEquals("CMD".toByteArray())){
                    Log.i("DEBBTS","CMD "+ numBytes)
                    pushBroadcastCommand(BluetoothUtils.ACTION_COMMAND_RECEIVED, arrayListOf(mmSocket.remoteDevice), arrayCommand)
                }else{
                    // Send to broadcast the message
                    val message = String(mmBuffer, 0, numBytes)
                    pushBroadcastMessage(BluetoothUtils.ACTION_MESSAGE_RECEIVED, arrayListOf(mmSocket.remoteDevice), message)
                }
            }
        }

        fun write(msg: String) : Int{
            return write(msg.toByteArray())
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) : Int{
            return try {
                mmOutStream.write(bytes)
                // Send to broadcast the message
                pushBroadcastMessage(BluetoothUtils.ACTION_MESSAGE_SENT, arrayListOf(mmSocket.remoteDevice), null)
                bytes.size
            } catch (e: IOException) {
                pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR, null, "Error occurred when sending data")
                -1
            }
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            Log.i("BLUE", "CANCEL ConnectedThread");
            try {

                if (mmSocket.isConnected) {
                    mmSocket.close()
                }
            } catch (e: IOException) {
                pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR, null, "Could not close the connect socket")
            }
        }

        fun isConnected(): Boolean {
            return isConnected
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(discoveryBroadcastReceiver)
        } catch (e: Exception) {
            // already unregistered
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun pushBroadcastMessage(action: String, devices: ArrayList<BluetoothDevice>?, message: String?) {
        val intent = Intent(action)

        devices?.let {
            if (it.size > 0) {
                intent.putParcelableArrayListExtra(BluetoothUtils.EXTRA_DEVICES, devices)
            }
        }

        if (message != null) {
            intent.putExtra(BluetoothUtils.EXTRA_MESSAGE, message)
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun pushBroadcastCommand(action: String, devices: ArrayList<BluetoothDevice>?, command: ByteArray?) {
        val intent = Intent(action)

        devices?.let {
            if (it.size > 0) {
                intent.putParcelableArrayListExtra(BluetoothUtils.EXTRA_DEVICES, devices)
            }
        }

        if (command != null) {
            intent.putExtra(BluetoothUtils.EXTRA_COMMAND, command)
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private var uniqueID: String? = null
    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

    @Synchronized
    fun id(context: Context): String? {
        if (uniqueID == null) {
            val sharedPrefs = context.getSharedPreferences(
                PREF_UNIQUE_ID, MODE_PRIVATE
            )
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor: SharedPreferences.Editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueID)
                editor.commit()
            }
        }
        return uniqueID
    }

}
