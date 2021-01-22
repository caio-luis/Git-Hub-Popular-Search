package com.caioluis.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Caio Luis (caio-luis) on 10/10/20
 */
data class RemoteGitHubRepositories(
	@SerializedName("items")
	val repositories: List<RemoteGitHubRepository>
)