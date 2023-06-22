package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User (
    @ColumnInfo val username: String,
    @ColumnInfo val name: String,
    @ColumnInfo val lastname: String,
    @ColumnInfo val mail: String,
    @ColumnInfo val password: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @ColumnInfo(name = "code_role") val codeRole: Int,
    @ColumnInfo(name = "code_client") val codeClient: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable