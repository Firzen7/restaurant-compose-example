package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.firzen.android.restaurantcomposeexample.BASE_API_URL
import net.firzen.android.restaurantcomposeexample.Main
import net.firzen.android.restaurantcomposeexample.db.PartialRestaurant
import net.firzen.android.restaurantcomposeexample.db.Restaurant
import net.firzen.android.restaurantcomposeexample.db.RestaurantsDb
import net.firzen.android.restaurantcomposeexample.network.ApiService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantsViewModel() : ViewModel() {

    val state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true)
    )

    private val apiService: ApiService
    private var restaurantsDao = RestaurantsDb.getDaoInstance(Main.getAppContext())


    // convenient errors handler that is compatible with Coroutines
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Error fetching restaurants list from API!")

        state.value = state.value.copy(
            error = exception.message,
            isLoading = false
        )
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
                BASE_API_URL
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

        // we can launch this logic directly on Main (UI) thread, since
        // getRemoteRestaurants() handles IO thread by itself
        viewModelScope.launch(Dispatchers.Main + errorHandler) {
            val restaurants = getAllRestaurants()
            state.value = state.value.copy(restaurants = restaurants, isLoading = false)
        }
    }

    private suspend fun getAllRestaurants() : List<Restaurant> {
        // Here we are returning result of getRestaurants() which is always going to be ran
        // on IO Dispatcher! This was very difficult to do before coroutines..
        return withContext(Dispatchers.IO) {
            try {
                // here we try to fetch restaurants from API
                refreshCache()
            } catch (e: Exception) {
                // .. if it fails, we only care if there are no data in Room DB yet
                when (e) {
                    is UnknownHostException, is ConnectException, is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty()) {
                            throw Exception("Something went wrong. We have no data.")
                        }
                    }

                    // of course we also care if unforeseen exception type has happened
                    else -> throw e
                }
            }

            // unless there was a serious problem, we always return data stored in local Room DB,
            // which is our Single Source of Truth
            return@withContext restaurantsDao.getAll()
        }
    }

    /**
     * Gets fresh data from the API and stores them into the database while preserving favourites
     * which are stored locally using Room DB.
     */
    private suspend fun refreshCache() {
        // fetches fresh restaurants data from API
        val remoteRestaurants = apiService.getRestaurants()
        // loads all restaurants from local DB that are favourited
        val favoriteRestaurants = restaurantsDao.getAllFavorited()

        // saves new restaurants into the DB (while rewriting existing ones)
        restaurantsDao.addAll(remoteRestaurants)

        // sets all new restaurants which were previously favourited to be favourited again
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialRestaurant(it.id, true)
            }
        )
    }

    fun toggleFavourite(targetId: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleFavoriteRestaurant(targetId, oldValue)
            // Here we are actually rewriting the UI state to correspond with latest data stored in DB
            // if we skipped this, the app would still work, but there could in theory be
            // inconsistency between shown UI state and data stored in DB.
            // So this line ensures that favourite icons are always shown correctly in the UI.
            state.value = state.value.copy(restaurants = updatedRestaurants)
        }
    }

    private suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean) : List<Restaurant> {
        return withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialRestaurant(id = id, isFavorite = !oldValue)
            )
            restaurantsDao.getAll()
        }
    }
}
