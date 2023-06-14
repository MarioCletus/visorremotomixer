package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "mixer")
data class Mixer(
    @ColumnInfo var name: String,
    @ColumnInfo val description: String,
    @ColumnInfo var mac: String,
    @ColumnInfo(name = "bt_box") var btBox: String,
    @ColumnInfo var tara: Double,
    @ColumnInfo var calibration: Float,
    @ColumnInfo val rfid: Long,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @ColumnInfo var linked: Boolean?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable