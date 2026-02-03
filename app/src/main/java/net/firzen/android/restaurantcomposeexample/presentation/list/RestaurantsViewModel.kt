package net.firzen.android.restaurantcomposeexample.presentation.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.firzen.android.restaurantcomposeexample.domain.GetInitialRestaurantsUseCase
import net.firzen.android.restaurantcomposeexample.domain.ToggleRestaurantUseCase
import timber.log.Timber
import javax.inject.Inject

// constructor fields here are being initialized by Hilt
// annotated as @HiltViewModel so that Hilt know when to use this when hiltViewModel() is called
@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getInitialRestaurantsUseCase: GetInitialRestaurantsUseCase,
    private val toggleRestaurantUseCase: ToggleRestaurantUseCase
    ) : ViewModel() {

    // actual state is kept in private field
    private val _state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true)
    )

    // this ensures that UI layer cannot change the state
    val state: State<RestaurantsScreenState>
        get() = _state


    // convenient errors handler that is compatible with Coroutines
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Error fetching restaurants list from API!")

        _state.value = _state.value.copy(
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
            val restaurants = getInitialRestaurantsUseCase()
            _state.value = _state.value.copy(restaurants = restaurants, isLoading = false)
        }
    }


    fun toggleFavourite(targetId: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleRestaurantUseCase(targetId, oldValue)
            // Here we are actually rewriting the UI state to correspond with latest data stored in DB
            // if we skipped this, the app would still work, but there could in theory be
            // inconsistency between shown UI state and data stored in DB.
            // So this line ensures that favourite icons are always shown correctly in the UI.
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }

}
