package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round_run",
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = Round::class,
                childColumns = ["round_id"],
                parentColumns = ["id"],
                onDelete = ForeignKey.CASCADE
            ),
            androidx.room.ForeignKey(
                entity = Mixer::class,
                childColumns = ["mixer_id"],
                parentColumns = ["id"]
            )
    ]
)
data class RoundRun (
    @ColumnInfo(name = "remote_user_id") var remoteUserId: Long,
    @ColumnInfo(name = "user_display_name") var userDisplayName: String,
    @ColumnInfo(name = "round_id") val roundId: Long,
    @ColumnInfo(name = "remote_round_id") var remoteRoundId: Long,
    @ColumnInfo(name = "mixer_id") val mixerId: Long,
    @ColumnInfo(name = "remote_mixer_id") var remoteMixerId: Long,
    @ColumnInfo(name = "start_date") var startDate: String,
    @ColumnInfo(name = "updated_date") val updatedDate: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "custom_percentage") var customPercentage: Double,
    @ColumnInfo(name = "custom_tara") var customTara: Double,
    @ColumnInfo(name = "added_blend") var addedBlend: Double,
    @ColumnInfo(name = "state") var state: Int,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
)  : Parcelable