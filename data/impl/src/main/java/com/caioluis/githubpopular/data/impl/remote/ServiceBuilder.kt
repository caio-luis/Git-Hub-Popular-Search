package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.impl.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface ServiceBuilder {
    companion object {
        const val TIMEOUT_IN_SECONDS = 30L

        inline operator fun <reified S> invoke(baseUrl: String): S {
            val httpClientBuilder = OkHttpClient.Builder()
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .callTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addInterceptor(logging)
            }

            val httpClient = httpClientBuilder.build()

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()
                .create(S::class.java)
        }
    }
}
