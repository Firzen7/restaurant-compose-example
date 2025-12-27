package net.firzen.android.restaurantcomposeexample

import androidx.lifecycle.ViewModel

class RestaurantViewModel() : ViewModel() {
    fun getRestaurants() : List<Restaurant> {
        return dummyRestaurants
    }
}
