package net.firzen.android.restaurantcomposeexample.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Main : Application() {

    // no longer needed thanks to Hilt
//    override fun onCreate() {
//        super.onCreate()
//
//        Timber.plant(Timber.DebugTree())
//
//        Timber.i("*** App started ***")
//    }
//
//    init { app = this }
//    companion object {
//        private lateinit var app: Main
//        fun getAppContext(): Context = app.applicationContext
//    }
}
