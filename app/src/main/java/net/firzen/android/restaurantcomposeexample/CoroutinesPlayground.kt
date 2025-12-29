package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

data class User(val id: Int, val name: String)

/**
 * A suspending function is a special function that can be paused (suspended) and resumed
 * at some later point in time.
 *
 * In Kotlin, suspending function is a regular function that is marked with the suspend keyword:
 */
suspend fun storeUser(user: User) {
    Timber.i("storeUser($user)")
    Thread.sleep(2000)
    Timber.i("User stored!")
}

fun saveDetails(user: User) {
    Timber.d("Preparing to launch coroutine")
    GlobalScope.launch(Dispatchers.IO) {
        storeUser(user)
    }
    Timber.d("Continuing program execution")
}
