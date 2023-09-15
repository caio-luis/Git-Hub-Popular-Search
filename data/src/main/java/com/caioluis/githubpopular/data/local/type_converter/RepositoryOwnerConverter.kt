package com.caioluis.githubpopular.data.local.type_converter

import androidx.room.TypeConverter
import com.caioluis.githubpopular.data.local.model.LocalRepositoryOwner
import com.google.gson.Gson

class RepositoryOwnerConverter {

    @TypeConverter
    fun toJson(repoOwner: LocalRepositoryOwner): String {
        return Gson().toJson(repoOwner)
    }

    @TypeConverter
    fun fromJson(repoOwnerJson: String): LocalRepositoryOwner {
        return Gson().fromJson(repoOwnerJson, LocalRepositoryOwner::class.java)
    }
}
