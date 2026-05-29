package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tablet_mixer")
data class TabletMixer(
    @ColumnInfo var name: String,
    @ColumnInfo var mixerName: String,
    @ColumnInfo var mac: String,
    @ColumnInfo(name = "serial") var serial: String,
    @ColumnInfo(name = "bt_name") var btName: String,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    /** Posición en la lista de Home. 0 = sin ordenar explícitamente (fallback a name). */
    @ColumnInfo(name = "sort_order") var sortOrder: Int = 0,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable