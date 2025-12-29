package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.firzen.android.restaurantcomposeexample.Restaurant
import net.firzen.android.restaurantcomposeexample.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

private const val FAVOURITES = "favourites"

// SavedStateHandle survives system-initiated process death (e.g. deallocation of the app
// after it's been backgrounded for long time
class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    // we are setting list of restaurants with favourite statuses correctly assigned
    // using SavedStateHandle via calling restoreSelections() extension function
    val state = mutableStateOf(emptyList<Restaurant>())
    private val apiService: ApiService

    // convenient errors handler that is compatible with Coroutines
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Error fetching restaurants list from API!")
    }


    /**
     * Retrofit initialization
     */
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            // here we add GSON support for Retrofit
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                // Base API endpoint URL
                "http://10.0.2.2:8080/"
            )
            .build()

        apiService = retrofit.create(ApiService::class.java)

        fetchRestaurants()
    }

    /**
     * This method fetches list of Restaurants from the API
     */
    private fun fetchRestaurants() {
        Timber.i("fetchRestaurants()")

        // launches network thread (using Dispatchers.IO
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val restaurants = apiService.getRestaurants()

            // Here we launch UI (Main) thread. This is needed, because we are here changing
            // composable state, which directly affects the UI.
            withContext(Dispatchers.Main) {
                state.value = restaurants.restoreSelections()
            }
        }
    }

    fun toggleFavourite(targetId: Int) {
        val restaurants = state.value.toMutableList()
        val targetIndex = restaurants.indexOfFirst { it.id == targetId }
        val targetRestaurant = restaurants[targetIndex]
        restaurants[targetIndex] = targetRestaurant.copy(
            isFavourite = !targetRestaurant.isFavourite
        )
        storeSelection(restaurants[targetIndex])
        state.value = restaurants
    }

    fun storeSelection(item: Restaurant) {
        // SavedStateHandle is a key-value storage structure, so first we get all favourite
        // restaurants' ids
        val favouriteIds = stateHandle.get<List<Int>?>(FAVOURITES).orEmpty().toMutableList()

        // here we either add given items' id if it is set as favourite or remove it from the
        // list if not
        if(item.isFavourite) (
            favouriteIds.add(item.id)
        )
        else {
            favouriteIds.remove(item.id)
        }

        // here we set new list of favourite ids into the SavedStateHandle
        stateHandle[FAVOURITES] = favouriteIds
    }

    fun List<Restaurant>.restoreSelections() : List<Restaurant> {
        // here we get all the stored favourite ids
        stateHandle.get<List<Int>?>(FAVOURITES)?.let { favouriteIds ->
            val restaurantsMap = this.associateBy { it.id }
            favouriteIds.forEach { id ->
                // we mark each restaurant whose id was in favourites list as favourite
                restaurantsMap[id]?.isFavourite = true
            }

            // here we return new altered list of restaurants with correct favourite statuses
            return restaurantsMap.values.toList()
        }

        // in case there are no stored favourite ids, we will return untouched list of restaurants
        return this
    }
}
