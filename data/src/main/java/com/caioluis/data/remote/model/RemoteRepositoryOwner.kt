package com.caioluis.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteRepositoryOwner(
    @SerializedName("login")
    val login: String?,

    @SerializedName("id")
    val id: Int?,

    @SerializedName("avatar_url")
    val avatarUrl: String?,
)
