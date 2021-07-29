package com.coding.albums

import android.app.Application
import android.net.ConnectivityManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = this
        }
    }

    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null &&
                    activeNetwork.isConnected
        }

    companion object {
        @JvmStatic
        var instance: MyApplication? = null
            private set

        @JvmStatic
        fun hasNetwork(): Boolean {
            return instance!!.isNetworkConnected
        }
    }
}