package com.caioluis.githubpopular.data.base

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface ServiceBuilder {
    companion object {
        const val TIMEOUT_IN_SECONDS = 30L

        inline operator fun <reified S> invoke(baseUrl: String): S {

            val httpClient = OkHttpClient.Builder()
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .callTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()
                .create(S::class.java)
        }
    }
}
