package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.basculasmagris.visorremotomixer.view.activities.RoundRunProgressDownloadData
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class RoundRunBody (
    var roundRun: RoundRun,
    var roundRunProgressLoad: List<RoundRunProgressLoad>,
    val roundRunProgressDownload: List<RoundRunProgressDownload>,
) : Parcelable

