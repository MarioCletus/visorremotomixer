package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class RoundRunRemote (
    var remoteUserId: Long,
    var userDisplayName: String,
    val roundId: Long,
    var remoteRoundId: Long,
    val mixerId: Long,
    var remoteMixerId: Long,
    var startDate: String,
    val updatedDate: String,
    val endDate: String,
    var remoteId: Long,
    var customPercentage: Double,
    var customTara: Double,
    var id: Long,
    val appId: Long,
) : Parcelable

