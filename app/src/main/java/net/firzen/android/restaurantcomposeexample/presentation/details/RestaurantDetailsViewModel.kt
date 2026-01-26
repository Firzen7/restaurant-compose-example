package net.firzen.android.restaurantcomposeexample.presentation.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.firzen.android.restaurantcomposeexample.domain.Restaurant
import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    private val repository = RestaurantsRepository()
    private val _state = mutableStateOf<Restaurant?>(null)

    val state: State<Restaurant?>
        get() = _state

    init {
        // here we are getting the id of restaurant, which was set using navigation component
        // as defined in MainActivity
        val restaurantId = stateHandle.get<Int>("restaurant_id") ?: 0

        viewModelScope.launch {
            val remote = repository.getRemoteRestaurant(restaurantId)
            val restaurant = Restaurant(remote.id, remote.title, remote.description)
            _state.value = restaurant
        }
    }
}
