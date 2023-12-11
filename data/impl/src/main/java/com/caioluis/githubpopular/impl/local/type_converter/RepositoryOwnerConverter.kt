package com.caioluis.githubpopular.impl.local.type_converter

import androidx.room.TypeConverter
import com.caioluis.githubpopular.data.pub.local.model.LocalRepositoryOwner
import com.caioluis.githubpopular.data.pub.local.model.LocalRepositoryOwnerJsonAdapter
import com.squareup.moshi.Moshi

class RepositoryOwnerConverter {

    private val moshi: Moshi = Moshi.Builder().build()
    private val jsonAdapter = LocalRepositoryOwnerJsonAdapter(moshi)

    @TypeConverter
    fun toJson(repoOwner: LocalRepositoryOwner): String {
        return jsonAdapter.toJson(repoOwner)
    }

    @TypeConverter
    fun fromJson(repoOwnerJson: String): LocalRepositoryOwner? {
        return jsonAdapter.fromJson(repoOwnerJson)
    }
}
