package com.caioluis.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Caio Luis (caio-luis) on 10/10/20
 */
data class RemoteGitHubRepository(

	val id: Int?,
	val name: String?,

	@SerializedName("full_name")
	val fullName: String?,

	val owner: RemoteRepositoryOwner?,
	val description: String?,

	@SerializedName("pulls_url")
	val pullsUrl: String?,

	@SerializedName("stargazers_count")
	val stargazersCount: Int?,

	@SerializedName("forks_count")
	val forksCount: Int?
)