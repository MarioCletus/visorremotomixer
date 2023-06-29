package com.basculasmagris.visorremotomixer.utils

object Constants {
    const val PRODUCT_TYPE = "PRODUCT_TYPE"
    const val ESTABLISHMENT_REF = "ESTABLISHMENT_REF"
    const val PRODUCT_REF = "PRODUCT_REF"
    const val DIET_REF = "DIET_REF"
    const val CORRAL_REF = "CORRAL_REF"
    const val USER_REF = "USER_REF"
    const val ROLE_REF = "ROLE_REF"
    const val DEVICE_REF = "DEVICE_REF"
    const val MIXER_REF = "MIXER_REF"

    const val PRODUCT_IMAGE_SOURCE_LOCAL = "Local"
    const val PRODUCT_IMAGE_SOURCE_ONLINE = "Online"

    const val EXTRA_PRODUCT_DETAILS: String = "ProductDetails"
    const val EXTRA_ESTABLISHMENT_DETAILS: String = "EstablishmentDetails"
    const val EXTRA_CORRAL_DETAILS: String = "CorralDetails"
    const val EXTRA_MIXER_DETAILS: String = "MixerDetails"
    const val EXTRA_REMOTE_VIEWER_DETAILS: String = "RemoteViewerDetails"
    const val EXTRA_DIET_DETAILS: String = "DietDetails"
    const val EXTRA_ROUND_DETAILS: String = "RoundDetails"
    const val EXTRA_ROUND_RUN_DETAILS: String = "RoundRunDetails"
    const val EXTRA_USER_DETAILS : String = "UserDetails"
    const val EXTRA_MIXER_MODE: String = "MixerMode"
    const val EXTRA_REMOTE_VIEWER_MODE: String = "RemoteViewerMode"

    const val BASE_URL: String = "http://sd-1810916-h00006.ferozo.net:3004/api/"
    const val API_PRODUCT_ENDPOINT: String = "products/"
    const val API_ESTABLISHMENT_ENDPOINT: String = "establishment/"
    const val API_CORRAL_ENDPOINT: String = "corral/"
    const val API_MIXER_ENDPOINT: String = "mixer/"
    const val API_DIET_ENDPOINT: String = "diet/"
    const val API_ROUND_ENDPOINT: String = "round/"
    const val API_USER_ENDPOINT: String = "users/"
    const val API_REPORT_ENDPOINT: String = "report/"
    const val API_KEY: String = ""

    const val APP_DB_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss"
    const val APP_XLS_FORMAT_DATE = "dd MMM yyyy HH:mm:ss"
    const val APP_SHOW_LARGE_FORMAT_DATE = "EEEE dd MMM HH:mm:ss"

    const val STATUS_FINISHED = "FINALIZADA"
    const val STATUS_INCOMPLETED = "INCOMPLETA"
    const val STATUS_NOT_RUN = "SIN EJECUCIONES PREVIAS"
    const val STATUS_LOAD_IN_PROGRESS = "CARGA INCOMPLETA"
    const val STATUS_LOAD_COMPLETED = "CARGA COMPLETA"
    const val STATUS_DOWNLOAD_IN_PROGRESS = "DESCARGA INCOMPLETA"
    const val STATUS_DOWNLOAD_COMPLETED = "DESCARGA COMPLETA"

    const val KEY_PREFIX_DEVICE = "TB"

    val COLUMNS_LOAD =  arrayOf(
        "Ronda",
        "Usuario",
        "Fecha",
        "Duración (')",
        "Planificado ronda (Kg)",
        "Real ronda (Kg)",
        "Desvío ronda (Kg)",
        "Dieta",
        "Producto",
        "Esperado (Kg)",
        "Real (Kg)",
        "Desvío producto (Kg)"
    )

    val COLUMNS_DOWNLOAD = arrayOf(
        "Ronda",
        "Usuario",
        "Fecha",
        "Duración (')",
        "Planificado ronda (Kg)",
        "Real ronda (Kg)",
        "Desvío ronda (Kg)",
        "Dieta",
        "Corral",
        "Esperado (Kg)",
        "Real (Kg)",
        "Desvío corral (Kg)",
    )

    val COLUMNS_LOAD_XLS =  arrayOf(
        "Ronda",
        "Usuario",
        "Fecha",
        "Duracion (')",
        "Planificado ronda (Kg)",
        "Real ronda (Kg)",
        "Desvio ronda (Kg)",
        "Dieta",
        "Producto",
        "Esperado (Kg)",
        "Real (Kg)",
        "Desvio producto (Kg)"
    )

    val COLUMNS_DOWNLOAD_XLS = arrayOf(
        "Ronda",
        "Usuario",
        "Fecha",
        "Duracion (')",
        "Planificado ronda (Kg)",
        "Real ronda (Kg)",
        "Desvio ronda (Kg)",
        "Dieta",
        "Corral",
        "Esperado (Kg)",
        "Real (Kg)",
        "Desvio corral (Kg)",
    )
    var FIELD_SEPARATOR = ";"

    //Login Preferences
    const val PREF_LOGIN: String = "PREF_LOGIN"
    const val PREF_LOGIN_KEY_ACCESS_TOKEN: String = "PREF_LOGIN_KEY_ACCESS_TOKEN"
    const val PREF_LOGIN_KEY_NAME: String = "PREF_LOGIN_KEY_NAME"
    const val PREF_LOGIN_KEY_MAIL: String = "PREF_LOGIN_KEY_MAIL"
    const val PREF_LOGIN_KEY_LASTNAME: String = "PREF_LOGIN_KEY_LASTNAME"
    const val PREF_LOGIN_KEY_ROLE: String = "PREF_LOGIN_KEY_ROLE"
    const val PREF_LOGIN_KEY_ROLE_DESC: String = "PREF_LOGIN_KEY_ROLE_DESC"
    const val PREF_LOGIN_KEY_USERNAME: String = "PREF_LOGIN_KEY_USERNAME"
    const val PREF_LOGIN_KEY_ENCRYPTED_PASSWORD: String = "PREF_LOGIN_KEY_ENCRYPTED_PASSWORD"
    const val PREF_LOGIN_KEY_CODE_CLIENT: String = "PREF_LOGIN_KEY_CODE_CLIENT"
    const val PREF_LOGIN_KEY_ID: String = "PREF_LOGIN_KEY_ID"
    const val PREF_IS_LOGGED: String = "PREF_IS_LOGGED"

    const val LOGIN_OFFLINE_NO_DATA = 1
    const val LOGIN_OFFLINE_INVALID_CREDENTIALS = 2
    const val LOGIN_OFFLINE_OK = 0

    const val BRODCAST_ROUND_UPDATED: String = "PREF_IS_LOGGED"

    //RoundDetail
    const val STATE_NONE = 0
    const val STATE_CONFIG = 1
    const val STATE_LOAD = 2
    const val STATE_DOWNLOAD = 3
    const val STATE_RESUME = 4
    const val STATE_FINISH = 10
    const val STATE_INTERRUPT = 11

    //Remote comunication protocol
    val CMD_INI  = "INI"
    val CMD_ROUNDDETAIL = "RRD"
    val CMD_NXTPRODUCT = "NXP"
    val CMD_END = "END"
    val CMD_UPDATE = "UPD"
    val CMD_ACK = "ACK"
    val CMD_USER = "USR"
    val CMD_TARA = "TAR"
    val CMD_PAUSE = "PAU"

}