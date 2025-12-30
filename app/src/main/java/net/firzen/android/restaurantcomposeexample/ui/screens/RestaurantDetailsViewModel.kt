package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.firzen.android.restaurantcomposeexample.BASE_API_URL
import net.firzen.android.restaurantcomposeexample.Restaurant
import net.firzen.android.restaurantcomposeexample.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsViewModel() : ViewModel() {
    private var restInterface: ApiService
    val state = mutableStateOf<Restaurant?>(null)

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_API_URL)
            .build()

        restInterface = retrofit.create(ApiService::class.java)

        viewModelScope.launch {
            val restaurant = getRemoteRestaurant(2)
            state.value = restaurant
        }
    }

    private suspend fun getRemoteRestaurant(id: Int) : Restaurant {
        return withContext(Dispatchers.IO) {
            restInterface.getRestaurant(id).first()
        }
    }
}
