package com.caioluis.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteRepositoryOwner(
	val login: String,
	val id: Int,

	@SerializedName("avatar_url")
	val avatarUrl: String
)