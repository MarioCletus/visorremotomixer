package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TabletRemote (
    val id: Long,
    val name: String?,
    val serial: String?,
    val subscriptionActive: Boolean,
    val codeClient: String,
    val appVersion: String?,
    val bluetoothName: String?
): Parcelable