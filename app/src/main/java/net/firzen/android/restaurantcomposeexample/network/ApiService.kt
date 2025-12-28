package net.firzen.android.restaurantcomposeexample.network

import net.firzen.android.restaurantcomposeexample.Restaurant
import retrofit2.Call
import retrofit2.http.GET

// API endpoint: http://localhost:8080/restaurants.json
// http://10.0.2.2:8080/restaurants.json in emulator

/**
 * Interface linking HTTP operations with API endpoints.
 */
interface ApiService {
    // GET request for /restaurants.json endpoint
    @GET("restaurants.json")
    fun getRestaurants() : Call<List<Restaurant>>
}
