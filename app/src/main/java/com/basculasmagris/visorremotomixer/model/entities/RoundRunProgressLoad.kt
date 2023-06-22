package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round_run_progress_load",
    primaryKeys = ["round_run_id", "diet_id", "product_id"],
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = RoundRun::class,
                childColumns = ["round_run_id"],
                parentColumns = ["id"],
                onDelete = ForeignKey.CASCADE
            ),
            androidx.room.ForeignKey(
                entity = Diet::class,
                childColumns = ["diet_id"],
                parentColumns = ["id"]
            ),
            androidx.room.ForeignKey(
                entity = Product::class,
                childColumns = ["product_id"],
                parentColumns = ["id"]
            )
    ]
)
data class RoundRunProgressLoad (
    @ColumnInfo(name = "round_run_id") val roundRunId: Long,
    @ColumnInfo(name = "remote_round_run_id") var remoteRoundRunId: Long,
    @ColumnInfo(name = "diet_id") val dietId: Long,
    @ColumnInfo(name = "remote_diet_id") var remoteDietId: Long,
    @ColumnInfo(name = "product_id") val productId: Long,
    @ColumnInfo(name = "remote_product_id") var remoteProductId: Long,
    @ColumnInfo(name = "initial_weight") var initialWeight: Double,
    @ColumnInfo(name = "current_weight") var currentWeight: Double,
    @ColumnInfo(name = "final_weight") var finalWeight: Double,
    @ColumnInfo(name = "target_weight") var targetWeight: Double,
    )  : Parcelable