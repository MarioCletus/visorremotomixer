package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round_run_report",
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = Round::class,
                childColumns = ["round_id"],
                parentColumns = ["id"],
                onDelete = CASCADE
            ),
            androidx.room.ForeignKey(
                entity = Mixer::class,
                childColumns = ["mixer_id"],
                parentColumns = ["id"]
            )
    ]
)
data class RoundRunReport (
    @ColumnInfo(name = "round_run_id") val roundRunId: Long,
    @ColumnInfo(name = "start_date") var startDate: String,
    @ColumnInfo(name = "updated_date") val updatedDate: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "weight") val weight: Double,
    @ColumnInfo(name = "custom_percentage") var customPercentage: Double,
    @ColumnInfo(name = "custom_tara") var customTara: Double,

    @ColumnInfo(name = "mixer_id") val mixerId: Long,
    @ColumnInfo(name = "mixer_name") val mixerName: String,
    @ColumnInfo(name = "mixer_description") val mixerDescription: String,
    @ColumnInfo(name = "mixer_mac") val mixerMac: String,

    @ColumnInfo(name = "round_id") val roundId: Long,
    @ColumnInfo(name = "round_name") val roundName: String,
    @ColumnInfo(name = "round_description") val roundDescription: String,


    @ColumnInfo(name = "diet_id") val dietId: Long,
    @ColumnInfo(name = "diet_name") val dietName: String,
    @ColumnInfo(name = "diet_description") val dietDescription: String,



    @PrimaryKey(autoGenerate = true) var id: Long = 0
)  : Parcelable