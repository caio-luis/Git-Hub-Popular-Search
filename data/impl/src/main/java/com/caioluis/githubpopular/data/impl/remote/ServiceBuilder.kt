import com.caioluis.githubpopular.data.impl.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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

            val jsonConfig = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
                .client(httpClient)
                .build()
                .create(S::class.java)
        }
    }
}
