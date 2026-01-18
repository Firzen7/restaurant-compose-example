package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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

private const val FAVOURITES = "favourites"

// SavedStateHandle survives system-initiated process death (e.g. deallocation of the app
// after it's been backgrounded for long time
class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    // we are setting list of restaurants with favourite statuses correctly assigned
    // using SavedStateHandle via calling restoreSelections() extension function
    val state = mutableStateOf(emptyList<Restaurant>())
    private val apiService: ApiService
    private var restaurantsDao = RestaurantsDb.getDaoInstance(Main.getAppContext())


    // convenient errors handler that is compatible with Coroutines
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Error fetching restaurants list from API!")

        // TODO figure out how to correctly handle errors here (might be something to do with
        //  MutableSharedFlow and emit())
//        viewModelScope.launch(Dispatchers.Main) {
//            Toast.makeText(, "sdf", Toast.LENGTH_SHORT)
//        }
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
            state.value = restaurants.restoreSelections()
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

    fun toggleFavourite(targetId: Int) {
        val restaurants = state.value.toMutableList()
        val targetIndex = restaurants.indexOfFirst { it.id == targetId }
        val targetRestaurant = restaurants[targetIndex]

        restaurants[targetIndex] = targetRestaurant.copy(
            isFavourite = !targetRestaurant.isFavourite
        )

        storeSelection(restaurants[targetIndex])
        state.value = restaurants

        viewModelScope.launch {
            val updatedRestaurants = toggleFavoriteRestaurant(targetId, targetRestaurant.isFavourite)
            // Here we are actually rewriting the UI state to correspond with latest data stored in DB
            // if we skipped this, the app would still work, but there could in theory be
            // inconsistency between shown UI state and data stored in DB.
            // So this line ensures that favourite icons are always shown correctly in the UI.
            state.value = updatedRestaurants
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
            val restaurantsMap = this.associateBy { it.id }.toMutableMap()
            favouriteIds.forEach { id ->
                // we mark each restaurant whose id was in favourites list as favourite
                val restaurant = restaurantsMap[id] ?: return@forEach
                restaurantsMap[id] = restaurant.copy(isFavourite = true)
            }

            // here we return new altered list of restaurants with correct favourite statuses
            return restaurantsMap.values.toList()
        }

        // in case there are no stored favourite ids, we will return untouched list of restaurants
        return this
    }
}
