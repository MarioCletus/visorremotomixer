package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteViewerRemote (
    val name: String,
    val description: String,
    val mac: String,
    val btBox: String,
    val remoteId: Long,
    val updatedDate: String,
    val id: Long = 0,
    val appId: Long,
    val archiveDate: String
) : Parcelable