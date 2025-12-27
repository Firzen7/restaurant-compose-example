package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import net.firzen.android.restaurantcomposeexample.dummyRestaurants

class RestaurantsViewModel() : ViewModel() {
    val state = mutableStateOf(dummyRestaurants)

    fun toggleFavourite(targetId: Int) {
        val restaurants = state.value.toMutableList()
        val targetIndex = restaurants.indexOfFirst { it.id == targetId }
        val targetRestaurant = restaurants[targetIndex]
        restaurants[targetIndex] = targetRestaurant.copy(
            isFavourite = !targetRestaurant.isFavourite
        )
        state.value = restaurants
    }
}