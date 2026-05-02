package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Diet::class,
            childColumns = ["diet_id"],
            parentColumns = ["id"]
        )
    ]
)
data class Round (
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") var archiveDate: String?,
    @ColumnInfo(name = "weight") val weight: Double,
    @ColumnInfo(name = "use_percentage") val usePercentage: Boolean,
    @ColumnInfo(name = "custom_percentage") val customPercentage: Double,
    @ColumnInfo(name = "diet_id") var dietId: Long,
    @ColumnInfo(name = "remote_diet_id") var remoteDietId: Long,
    @ColumnInfo(name = "round_type") var round_type: Int,
    @ColumnInfo(name = "round_status") var round_status: Int,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
)  : Parcelable