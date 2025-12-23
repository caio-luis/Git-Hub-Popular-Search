package com.caioluis.githubpopular.data.impl.local.typeconverter

import androidx.room.TypeConverter
import com.caioluis.githubpopular.data.bridge.local.model.LocalRepositoryOwner
import com.caioluis.githubpopular.data.bridge.local.model.LocalRepositoryOwnerJsonAdapter
import com.squareup.moshi.Moshi

class RepositoryOwnerConverter {
    private val moshi: Moshi = Moshi.Builder().build()
    private val jsonAdapter = LocalRepositoryOwnerJsonAdapter(moshi)

    @TypeConverter
    fun toJson(repoOwner: LocalRepositoryOwner): String = jsonAdapter.toJson(repoOwner)

    @TypeConverter
    fun fromJson(repoOwnerJson: String): LocalRepositoryOwner? = jsonAdapter.fromJson(repoOwnerJson)
}
