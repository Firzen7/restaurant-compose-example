About
-----

This is a playground Android app for learning Jetpack Compose, Kotlin Coroutines, Retrofit, and other topics covered in [Kickstart Modern Android Development with Jetpack and Kotlin](https://github.com/PacktPublishing/Kickstart-Modern-Android-Development-with-Jetpack-and-Kotlin) from [Catalin Ghita](https://www.linkedin.com/in/catalin-ghita-590504127/).

While this app was largely inspired by the mentioned book, it does not exactly match example code. Main differences are:
 - Custom, Ktor based, REST API is used instead of Firebase.
 - "Launch coroutine" button is linked to CoroutinesPlayground.kt file, which contains detailed explanation of Kotlin coroutines basics.
 - Adjustments have been made to makde the original examples compatible with present Compose logic.

Building and running the app
----------------------------

1. Launch the REST API provided in `restaurants-api-v1.jar`. You can do so by running `./launch_rest_api.sh`.
2. Sources of REST API are in `restaurants-rest-api` subdfolder. You can build the .jar file with `build.sh`.
3. When the REST API is running, you can build and launch the app in Android Studio. Make sure Android emulator has network connjection enabled.

Features / what have I learned
------------------------------

1. Offline-first approach: Once restaurants list is initally downloaded into local `Room DB`, the app can be used offline forever. Thanks to this, **Single Source of Truth** principle is honoured, and the app is overall much less prone to bugs.
   - However, for accessing restaurant details, network connection is still needed. Not sure if this was forgotten by the book author or if it comes later. Maybe I will add it myself if it is not in the book. :-)
2. [LazyColumn](https://developer.android.com/develop/ui/compose/lists) is used for smooth andd efficient rendering of restaurants list.
3. `ViewModels` and `CoroutineScopes` are used for proper handling of configuration changes and [system-initiated process death](https://developer.android.com/topic/libraries/architecture/saving-states#ui-dismissal-system).
4. Deep links are supported in elegant way using Jetpack Navigation (`NavHost`).
5. `Clean Architecture` principles and `Repository pattern` are followed. Packages are organized to mostly fit with `MVVM` architecture.

<img width="267" height="513" alt="Screenshot_20260131_172140" src="https://github.com/user-attachments/assets/4d600654-4818-4dc6-9ffc-17a124fc4fa2" />

 

