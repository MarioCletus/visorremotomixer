package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round_run_progress_download",
    primaryKeys = ["round_run_id", "corral_id"],
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = RoundRun::class,
                childColumns = ["round_run_id"],
                parentColumns = ["id"],
                onDelete = ForeignKey.CASCADE
            ),
            androidx.room.ForeignKey(
                entity = Corral::class,
                childColumns = ["corral_id"],
                parentColumns = ["id"]
            )
    ]
)
data class RoundRunProgressDownload (
    @ColumnInfo(name = "round_run_id") val roundRunId: Long,
    @ColumnInfo(name = "remote_round_run_id") var remoteRoundRunId: Long,
    @ColumnInfo(name = "corral_id") val corralId: Long,
    @ColumnInfo(name = "remote_corral_id") var remoteCorralId: Long,
    @ColumnInfo(name = "initial_weight") var initialWeight: Double,
    @ColumnInfo(name = "current_weight") var currentWeight: Double,
    @ColumnInfo(name = "final_weight") var finalWeight: Double,
    @ColumnInfo(name = "custom_target_weight") var customTargetWeight: Double,
    @ColumnInfo(name = "actual_target_weight") var actualTargetWeight: Double,
    )  : Parcelable