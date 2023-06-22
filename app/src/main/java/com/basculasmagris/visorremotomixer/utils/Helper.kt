package com.basculasmagris.visorremotomixer.utils

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.model.entities.*
import com.basculasmagris.visorremotomixer.utils.Constants.LOGIN_OFFLINE_INVALID_CREDENTIALS
import com.basculasmagris.visorremotomixer.utils.Constants.LOGIN_OFFLINE_NO_DATA
import com.basculasmagris.visorremotomixer.utils.Constants.LOGIN_OFFLINE_OK
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_IS_LOGGED
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ACCESS_TOKEN
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_CODE_CLIENT
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ENCRYPTED_PASSWORD
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ID
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_LASTNAME
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_MAIL
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_NAME
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ROLE
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_ROLE_DESC
import com.basculasmagris.visorremotomixer.utils.Constants.PREF_LOGIN_KEY_USERNAME
import com.basculasmagris.visorremotomixer.view.adapter.CustomListItem
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToLong
import java.math.BigInteger
import java.security.MessageDigest

class Helper {
    companion object {

        private const val MINUTES_PER_HOUR = 60
        private const val SECONDS_PER_MINUTE = 60
        private const val SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR

        fun logOut(context: Context){
            var sharedpreferences: SharedPreferences? = null
            sharedpreferences = context.getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE);
            sharedpreferences?.edit()
                //?.remove(PREF_LOGIN_KEY_ACCESS_TOKEN)
                //?.remove(PREF_LOGIN_KEY_NAME)
                //?.remove(PREF_LOGIN_KEY_LASTNAME)
                //?.remove(PREF_LOGIN_KEY_ROLE)
                ?.remove(PREF_IS_LOGGED)
                //?.remove(PREF_LOGIN_KEY_USERNAME)
                //?.remove(PREF_LOGIN_KEY_ENCRYPTED_PASSWORD)
                ?.apply();

        }
        fun getFormattedWeight(weight: Double, context: Context) : String {

            if  (weight > 0.0) {
                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.DOWN
                val formattedWeight = df.format(weight)
                return "$formattedWeight ${context.getString(R.string.lb_weight_unit)}"
            } else {
                return context.getString(R.string.lbl_empty)
            }
        }

        fun getWeightKg(readData: Double,mixer: Mixer) : Double {
            var weight:Double = 0.0
            weight = (readData-mixer.tara) * mixer.calibration
            return weight
        }

        fun getFormattedWeightKg(weight: Double, context: Context) : String {

            if  (weight > 0.0) {
                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.DOWN
                val formattedWeight = df.format(weight)
                return "$formattedWeight ${context.getString(R.string.lb_weight_unit_kg)}"
            } else {
                return context.getString(R.string.lbl_empty)
            }
        }
        fun getFormattedWeightKg(weight: Double, context: Context, defaultValue: String) : String {

            if  (weight > 0.0) {
                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.DOWN
                val formattedWeight = df.format(weight)
                return "$formattedWeight ${context.getString(R.string.lb_weight_unit_kg)}"
            } else {
                return if (defaultValue.isNullOrEmpty()) "0 ${context.getString(R.string.lb_weight_unit_kg)}" else defaultValue
            }
        }
        fun getNumberWithDecimals(value: Double, decimals: Int) : String {
            if(value==null || value.isNaN()){
                return "0.0"
            }

            var pattern = "#"
            if (decimals > 0){
                pattern += "."
            } else {
                return  value.roundToLong().toString()
            }

            for (index in 1..decimals){
                pattern += "#"
            }

            return if  (value > 0.0) {
                val otherSymbols = DecimalFormatSymbols(Locale.getDefault())
                val df = DecimalFormat(pattern, otherSymbols)
                df.roundingMode = RoundingMode.DOWN
                df.format(value).replace(",",".")
            } else {
                "0.00"
            }
        }
        fun getAppDateFromString(date: String) : LocalDateTime {
            //var localDateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
            var localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return LocalDateTime.parse(date, localDateFormat)
        }
        fun getApiDateFromString(date: String) : LocalDateTime{
            var remoteDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'")
            //var remoteDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")

            return LocalDateTime.parse(date, remoteDateFormat)
        }
        fun formattedDate(date: String, originFormat: String, targetFormat: String) : String {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(originFormat)).format(DateTimeFormatter.ofPattern(targetFormat))
        }
        fun diffDate(startDate: String, endDate: String, originFormat: String) : String {
             var startDateDt = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern(originFormat))
             var endDateDt = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern(originFormat))
             val duration = Duration.between(startDateDt, endDateDt)
            val seconds = duration.seconds
            val hours: Long = seconds / SECONDS_PER_HOUR
            val minutes: Long = seconds % SECONDS_PER_HOUR / SECONDS_PER_MINUTE
            val secs: Long = seconds % SECONDS_PER_MINUTE

            return "${minutes}"
        }
        fun getRelativeDateTime(date:String): String {
            return DateUtils.getRelativeTimeSpanString(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toEpochSecond(
                ZoneOffset.MAX), Instant.EPOCH.toEpochMilli(), 0L, DateUtils.FORMAT_ABBREV_RELATIVE).toString()

            return  "Iniciada el día $date"
        }
        fun setProgressDialog(context:Context, message:String): AlertDialog {
            val llPadding = 30
            val ll = LinearLayout(context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.setPadding(llPadding, llPadding, llPadding, llPadding)
            ll.gravity = Gravity.CENTER
            var llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            llParam.gravity = Gravity.CENTER
            ll.layoutParams = llParam

            val progressBar = ProgressBar(context)
            progressBar.isIndeterminate = true
            progressBar.setPadding(0, 0, llPadding, 0)
            progressBar.layoutParams = llParam

            llParam = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            llParam.gravity = Gravity.CENTER
            val tvText = TextView(context)
            tvText.text = message
            tvText.setTextColor(Color.parseColor("#000000"))
            tvText.textSize = 20.toFloat()
            tvText.layoutParams = llParam

            ll.addView(progressBar)
            ll.addView(tvText)

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setView(ll)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            val window = dialog.window
            if (window != null) {
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams
            }
            return dialog
        }
        fun md5(input:String): String {
            //val md = MessageDigest.getInstance("MD5")
            //return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
            val md = MessageDigest.getInstance("MD5")
            val bigInt = BigInteger(1, md.digest(input.toByteArray(Charsets.UTF_8)))
            return String.format("%032x", bigInt)
        }
        fun isAllowedLoginOffline(context: Context, userList: ArrayList<User>, username: String, password: String): Int {
            //var sharedpreferences: SharedPreferences? = null
            //sharedpreferences = context.getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE);
            //val localUserName = sharedpreferences.getString(PREF_LOGIN_KEY_USERNAME, "")
            //val localEncryptedPass = sharedpreferences.getString(PREF_LOGIN_KEY_ENCRYPTED_PASSWORD, "")

            val userExist = userList.firstOrNull { user -> user.username == username}
            val isValidUser = userList.firstOrNull { user -> user.username == username && (user.password == Helper.md5(password) || user.password == password)}

            userList.forEach { user ->
                Log.i("LOGIN", "Username: ${user.username} | pass: ${user.password} | ${Helper.md5(password)}")

            }
            Log.i("LOGIN", "userExist: $userExist | isValidUser: $isValidUser | username: $username | password: $password")
            return if (isValidUser != null) {
                LOGIN_OFFLINE_OK
            } else if (userExist != null) {
                LOGIN_OFFLINE_INVALID_CREDENTIALS
            } else {
                LOGIN_OFFLINE_NO_DATA
            }
        }

        fun checkForInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            Log.i("Internet", "NetworkCapabilities: ${connectivityManager}")

            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                Log.i("Internet", "NetworkCapabilities: ${capabilities}")

                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
        fun getCurrentUser(context: Context) : UserRemote {
            var sharedpreferences: SharedPreferences? = null
            sharedpreferences = context.getSharedPreferences(Constants.PREF_LOGIN, Context.MODE_PRIVATE);

            val username = sharedpreferences.getString(PREF_LOGIN_KEY_USERNAME, "").toString()
            val name = sharedpreferences.getString(PREF_LOGIN_KEY_NAME, "").toString()
            val mail = sharedpreferences.getString(PREF_LOGIN_KEY_MAIL, "").toString()
            val lastName = sharedpreferences.getString(PREF_LOGIN_KEY_LASTNAME, "").toString()
            val codeRole = sharedpreferences.getInt(PREF_LOGIN_KEY_ROLE, 0)
            val id = sharedpreferences.getLong(PREF_LOGIN_KEY_ID, 0)
            val roleDescription = sharedpreferences.getString(PREF_LOGIN_KEY_ROLE_DESC, "").toString()
            val codeClient = sharedpreferences.getString(PREF_LOGIN_KEY_CODE_CLIENT, "").toString()
            val accessToken = sharedpreferences.getString(PREF_LOGIN_KEY_ACCESS_TOKEN, "").toString()
            val password = sharedpreferences.getString(PREF_LOGIN_KEY_ENCRYPTED_PASSWORD, "").toString()

            return UserRemote(
                id,
                username,
                "$name $lastName",
                name,
                lastName,
                mail,
                password,
                codeRole,
                RoleRemote(codeRole,""),
                codeClient,
                roleDescription,
                accessToken,
                id
            )
        }

        fun logRoundRunComplete(roundRunDetail: RoundRunDetail){
            val TAG_DEBUG = "DataRoundRunDetail"
            Log.i(TAG_DEBUG, "******************************")
            Log.i(TAG_DEBUG, "id: ${roundRunDetail.id}")
            Log.i(TAG_DEBUG, "userId: ${roundRunDetail.userId}")
            Log.i(TAG_DEBUG, "userDisplayName: ${roundRunDetail.userDisplayName}")
            Log.i(TAG_DEBUG, "mixer.id: ${roundRunDetail.mixer?.id}")
            Log.i(TAG_DEBUG, "mixer.name: ${roundRunDetail.mixer?.name}")
            Log.i(TAG_DEBUG, "mixer.bluetooth: ${roundRunDetail.mixerBluetoothDevice?.toString()}")
            Log.i(TAG_DEBUG, "customTara: ${roundRunDetail.customTara}")
            Log.i(TAG_DEBUG, "customPercentage: ${roundRunDetail.customPercentage}")
            Log.i(TAG_DEBUG, "startDate: ${roundRunDetail.startDate}")
            Log.i(TAG_DEBUG, "endDate: ${roundRunDetail.endDate}")
            Log.i(TAG_DEBUG, "round.name: ${roundRunDetail.round.id} | ${roundRunDetail.round.name}")
            roundRunDetail.round.corrals.forEach { corralDetail ->
                Log.i(TAG_DEBUG, "    Corral Name: ${corralDetail.id} | ${corralDetail.name}")
                Log.i(TAG_DEBUG, "    Weights :${corralDetail.weight} | currentWeight: ${corralDetail.currentWeight} | actualTargetWeight: ${corralDetail.actualTargetWeight} | customTargetWeight: ${corralDetail.customTargetWeight}")
            }
            Log.i(TAG_DEBUG, "round.diet: ${roundRunDetail.round.diet.id} | ${roundRunDetail.round.diet.name}")
            roundRunDetail.round.diet.products.forEach { productDetail ->
                Log.i(TAG_DEBUG, "    Product Name: ${productDetail.id} | ${productDetail.name}")
                Log.i(TAG_DEBUG, "    Weights : currentWeight: ${productDetail.currentWeight} | actualTargetWeight: ${productDetail.targetWeight}}")
            }
            Log.i(TAG_DEBUG, "******************************")
        }
    }
}

class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceSize
            }
            left = spaceSize
            right = spaceSize
            bottom = spaceSize
        }
    }
}