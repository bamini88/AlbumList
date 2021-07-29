package com.coding.albums.network

import android.util.Log
import com.coding.albums.MyApplication
import com.coding.albums.MyApplication.Companion.hasNetwork
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private const val TAG = "ServiceGenerator"
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_PRAGMA: String = "Pragma"
    var instance: ServiceGenerator? = null
        get() {
            if (field == null) {
                field = ServiceGenerator
            }
            return field
        }
        private set
    private const val cacheSize = (5 * 1024 * 1024 // 5 MB
            ).toLong()

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache())
            .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
            .addNetworkInterceptor(networkInterceptor()) // only used when network is on
            .addInterceptor(offlineInterceptor())
            .build()
    }

    private fun cache(): Cache {
        return Cache(File(MyApplication.instance!!.cacheDir, "albumIdentifier"), cacheSize)
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     * @return
     */
    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "offline interceptor: called.")
            var request = chain.request()

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!hasNetwork()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    /**
     * This interceptor will be called ONLY if the network is available
     * @return
     */
    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "network interceptor: called.")
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.SECONDS)
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Log.d(TAG, "log: http log: $message") }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    val api: Api
        get() = retrofit().create(Api::class.java)
}