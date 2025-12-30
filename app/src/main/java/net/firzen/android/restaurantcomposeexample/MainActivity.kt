package net.firzen.android.restaurantcomposeexample

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

//                        RestaurantDetailsScreen()
//                        RestaurantsScreen()
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
            arguments =
            listOf(navArgument("restaurant_id") {
                type = NavType.IntType
            })
        ) { navStackEntry ->
            val id = navStackEntry.arguments?.getInt("restaurant_id")
            RestaurantDetailsScreen()
        }
    }
}
