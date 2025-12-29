package net.firzen.android.restaurantcomposeexample

import android.app.Application
import timber.log.Timber

class Main : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        Timber.i("*** App started ***")
    }
}
