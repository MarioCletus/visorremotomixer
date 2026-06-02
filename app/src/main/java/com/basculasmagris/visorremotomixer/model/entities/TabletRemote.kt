package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TabletRemote (
    val id: Long = 0,
    val name: String? = null,
    val serial: String? = null,
    val subscriptionActive: Int = 0,
    val codeClient: String? = null,
    val appVersion: String? = null,
    val bluetoothName: String? = null,
    val updatedDate: String? = null
): Parcelable