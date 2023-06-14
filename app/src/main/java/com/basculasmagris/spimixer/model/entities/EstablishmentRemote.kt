package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EstablishmentRemote (
    val name: String,
    val description: String,
    val id: Long,
    val appId: Long,
    val updatedDate: String,
    val archiveDate: String
) : Parcelable