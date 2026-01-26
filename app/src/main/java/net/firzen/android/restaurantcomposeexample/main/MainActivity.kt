package net.firzen.android.restaurantcomposeexample.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import net.firzen.android.restaurantcomposeexample.ui.screens.RestaurantDetailsScreen
import net.firzen.android.restaurantcomposeexample.ui.screens.RestaurantsScreen
import net.firzen.android.restaurantcomposeexample.ui.theme.RestaurantComposeExampleTheme

// https://github.com/PacktPublishing/Kickstart-Modern-Android-Development-with-Jetpack-and-Kotlin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RestaurantComposeExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        RestaurantsApp()
                    }
                }
            }
        }
    }
}

@Composable
private fun RestaurantsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "restaurants") {
        composable(route = "restaurants") {
            RestaurantsScreen() { clickedRestaurantId ->
                navController.navigate("restaurants/$clickedRestaurantId")
            }
        }
        composable(
            route = "restaurants/{restaurant_id}",
            // this is boilerplate needed to tell the navigation component what arguments
            // to extract from route and how (what datatype it is)
            arguments = listOf(navArgument("restaurant_id") {
                type = NavType.IntType
            }),
            // Deep links integration. Test using:
            // adb shell am start -W -a android.intent.action.VIEW -d "https://www.restaurantsapp.details.com/4" net.firzen.android.restaurantcomposeexample
            deepLinks = listOf(navDeepLink {
                uriPattern = "www.restaurantsapp.details.com/{restaurant_id}"
            })
        ) { navStackEntry ->
            // not passing any id directly from here to RestaurantDetailsScreen, because
            // the restaurant id is being set in RestaurantDetailsViewModel using SavedStateHandle
            RestaurantDetailsScreen()
        }
    }
}
