package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoundRunReportRemote (
    var roundRunId: Long,
    var startDate: String,
    var updatedDate: String,
    var endDate: String,
    var weight: Double,
    var customPercentage: Double,
    var customTara: Double,
    var mixerId: Long,
    var mixerName: String,
    var mixerDescription: String,
    var mixerMac: String,

    var roundId: Long,
    var roundName: String,
    var roundDescription: String,


    var dietId: Long,
    var dietName: String,
    var dietDescription: String,

    var id: Long = 0
) : Parcelable

