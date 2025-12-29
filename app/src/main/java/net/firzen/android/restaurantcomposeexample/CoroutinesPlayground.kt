package net.firzen.android.restaurantcomposeexample

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    Thread.sleep(5000)
    Timber.i("User stored!")
}

fun saveDetails(user: User) {
    Timber.d("Preparing to launch coroutine")

    // GlobalScope should be avoided since the work launched within this coroutine scope is only
    // canceled when the application has been destroyed. This is only used to explain the principle.
    GlobalScope.launch(Dispatchers.IO) {
        storeUser(user)
    }

    Timber.d("Continuing program execution")
}

/**
 * Improved version of saveDetails(), which uses variable CoroutineScope and demonstrates use of
 * different Dispatchers:
 *
 * Dispatchers.Main     --> Main thread of Android app, similar to runOnUiThread { ... }
 * Dispatchers.IO       --> Disk or network based operations
 * Dispatchers.Default  --> CPU-intensive tasks
 */
fun saveDetails2(context: Context, coroutineScope: CoroutineScope, user: User) {
    Timber.d("Preparing to launch coroutine")

    coroutineScope.launch(Dispatchers.Main) {
        Toast.makeText(context, "Storing user ...", Toast.LENGTH_SHORT).show()

        withContext(Dispatchers.IO) {
            storeUser(user)
        }

        Toast.makeText(context, "User stored!", Toast.LENGTH_SHORT).show()
    }

    Timber.d("Continuing program execution")
}

// *** Custom coroutine scope instead of pre-defined GlobalScope ***********************************

/**
 * A Job object: This represents a cancelable component that controls the lifecycle
 * of a coroutine launched in a specific scope. When a job is canceled, the job will
 * cancel the coroutine it manages. For example, if we have defined a job object
 * and a custom myScope object inside an Activity class, a good place to cancel
 * the coroutine would be in the onDestroy() callback by calling the cancel()
 * method on the job object:
 *
 * override fun onDestroy() {
 *     super.onDestroy()
 *     job.cancel()
 * }
 *
 * By doing this, we've ensured that our async work done within our coroutine, which
 * uses the myScope scope, will stop when the activity has been destroyed and will
 * not cause any memory leaks.
 */
private val job = Job()

/**
 * A Dispatcher object: Marking a method as suspended provides no details about
 * the thread pool it should run on.
 *
 * This means that all coroutines launched in myScope will run their work, by
 * default, in the Dispatchers.IO thread pool and will not block the UI.
 */
private val myScope = CoroutineScope(context = job + Dispatchers.IO)
