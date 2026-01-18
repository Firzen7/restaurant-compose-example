package net.firzen.android.restaurantcomposeexample.network

import net.firzen.android.restaurantcomposeexample.db.Restaurant
import retrofit2.http.GET
import retrofit2.http.Query

// API endpoint: http://localhost:8080/restaurants.json
// http://10.0.2.2:8080/restaurants.json in emulator

/**
 * Interface linking HTTP operations with API endpoints.
 */
interface ApiService {
    // GET request for /restaurants.json endpoint
    @GET("restaurants.json")
    suspend fun getRestaurants() : List<Restaurant>

    @GET("restaurants.json?orderBy=\"id\"")
    suspend fun getRestaurant(@Query("equalTo") id: Int) : List<Restaurant>
}
