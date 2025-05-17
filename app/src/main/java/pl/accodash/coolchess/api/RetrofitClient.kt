package pl.accodash.coolchess.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.accodash.coolchess.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BACKEND_URL

    private val retrofitCache = ConcurrentHashMap<String, Retrofit>()

    private fun createRetrofit(token: String): Retrofit {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>, token: String): T {
        val retrofit = retrofitCache.getOrPut(token) {
            createRetrofit(token)
        }
        return retrofit.create(serviceClass)
    }
}
