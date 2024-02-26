package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoundRunBody (
    var roundRun: RoundRun,
    var roundRunProgressLoad: List<RoundRunProgressLoad>,
    val roundRunProgressDownload: List<RoundRunProgressDownload>,
) : Parcelable

