package com.basculasmagris.visorremotomixer.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRemote (
    var id: Long,
    var username: String,
    var displayName: String,
    val name: String,
    val lastname: String,
    val mail: String,
    val password: String,
    val codeRole: Int,
    val role: RoleRemote,
    val codeClient: String,
    val accessToken: String,
    val updatedDate: String,
    val appId: Long
) : Parcelable

@Parcelize
data class RoleRemote (
    var code: Int,
    var description: String
) : Parcelable

