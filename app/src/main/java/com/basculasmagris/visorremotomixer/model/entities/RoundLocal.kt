package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class RoundLocal (
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val startDate: String,
    @ColumnInfo val endDate: String,
    @ColumnInfo val progress: Int,
    @ColumnInfo val status: Int,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "tablet_mixer_id") var tabletMixerId: Long,
    @ColumnInfo(name = "tablet_mixer_mac") var tabletMixerMac: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
)  : Parcelable
