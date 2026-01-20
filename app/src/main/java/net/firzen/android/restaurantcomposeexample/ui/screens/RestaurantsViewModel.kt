package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.firzen.android.restaurantcomposeexample.db.RestaurantsRepository
import timber.log.Timber

class RestaurantsViewModel() : ViewModel() {

    val state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true)
    )

    private val repository = RestaurantsRepository()

    // convenient errors handler that is compatible with Coroutines
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Error fetching restaurants list from API!")

        state.value = state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }


    init {
        fetchRestaurants()
    }

    /**
     * This method fetches list of Restaurants from the API
     */
    private fun fetchRestaurants() {
        Timber.i("fetchRestaurants()")

        // we can launch this logic directly on Main (UI) thread, since
        // getRemoteRestaurants() handles IO thread by itself
        viewModelScope.launch(Dispatchers.Main + errorHandler) {
            val restaurants = repository.getAllRestaurants()
            state.value = state.value.copy(restaurants = restaurants, isLoading = false)
        }
    }


    fun toggleFavourite(targetId: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = repository.toggleFavoriteRestaurant(targetId, oldValue)
            // Here we are actually rewriting the UI state to correspond with latest data stored in DB
            // if we skipped this, the app would still work, but there could in theory be
            // inconsistency between shown UI state and data stored in DB.
            // So this line ensures that favourite icons are always shown correctly in the UI.
            state.value = state.value.copy(restaurants = updatedRestaurants)
        }
    }

}
