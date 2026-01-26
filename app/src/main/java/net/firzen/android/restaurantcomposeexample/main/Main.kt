package net.firzen.android.restaurantcomposeexample.main

import android.app.Application
import android.content.Context
import timber.log.Timber

class Main : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        Timber.i("*** App started ***")
    }

    init { app = this }
    companion object {
        private lateinit var app: Main
        fun getAppContext(): Context = app.applicationContext
    }
}
