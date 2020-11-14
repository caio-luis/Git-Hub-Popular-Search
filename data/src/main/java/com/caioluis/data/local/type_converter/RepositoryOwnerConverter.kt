package com.caioluis.data.local.type_converter

import androidx.room.TypeConverter
import com.caioluis.data.local.model.LocalRepositoryOwner
import com.google.gson.Gson

/**
 * Created by Caio Luis (@caio.luis) on 31/10/20
 */

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