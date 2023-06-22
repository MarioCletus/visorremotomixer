package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoundCorralDetail (
    val corralId: Long,
    val remoteCorralId: Long,
    val corralName: String,
    val corralDescription: String,
    val roundId: Long,
    val remoteRoundId: Long,
    var percentage: Double,
    var weight: Double,
    var order: Int,
    var animalQuantity: Int
) : Parcelable