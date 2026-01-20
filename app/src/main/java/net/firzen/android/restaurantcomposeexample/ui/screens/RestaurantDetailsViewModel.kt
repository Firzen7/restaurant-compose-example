package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.firzen.android.restaurantcomposeexample.db.Restaurant
import net.firzen.android.restaurantcomposeexample.db.RestaurantsRepository

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    private val repository = RestaurantsRepository()
    val state = mutableStateOf<Restaurant?>(null)

    init {
        // here we are getting the id of restaurant, which was set using navigation component
        // as defined in MainActivity
        val restaurantId = stateHandle.get<Int>("restaurant_id") ?: 0

        viewModelScope.launch {
            val restaurant = repository.getRemoteRestaurant(restaurantId)
            state.value = restaurant
        }
    }
}
