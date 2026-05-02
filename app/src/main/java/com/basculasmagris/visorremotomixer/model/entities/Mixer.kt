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
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") var archiveDate: String?,
    @ColumnInfo var capacity: Float,
    @ColumnInfo var created_date: String,
    @ColumnInfo var total_hours: Double,
    @ColumnInfo var rfid_mac: String,
    @ColumnInfo var internal_divisions: Int = 1,
    @ColumnInfo var type: Int = 0,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable