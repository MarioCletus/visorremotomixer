package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MixerRemote (
    val name: String,
    val description: String,
    val mac: String,
    val btBox: String,
    val tara: Double,
    val calibration: Float,
    val remoteId: Long,
    val updatedDate: String,
    val id: Long = 0,
    val appId: Long,
    val archiveDate: String,
    val capacity: Float,
    val created_date: String,
    val total_hours: Double,
    var mac_rfid: String? = "",
    var internal_divisions: Int = 1,
    var type: Int = 0
) : Parcelable