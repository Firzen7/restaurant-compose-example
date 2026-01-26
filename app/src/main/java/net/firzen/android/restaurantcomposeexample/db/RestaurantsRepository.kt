package net.firzen.android.restaurantcomposeexample.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.firzen.android.restaurantcomposeexample.BASE_API_URL
import net.firzen.android.restaurantcomposeexample.Main
import net.firzen.android.restaurantcomposeexample.network.ApiService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException

// repository needs to only return domain model objects - in our case these are instances
// of Restaurant class
class RestaurantsRepository {

    /**
     * Retrofit initialization
     */
    private var apiService: ApiService = Retrofit.Builder()
        // here we add GSON support for Retrofit
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .baseUrl(
            // Base API endpoint URL
            BASE_API_URL
        )
        .build()
        .create(ApiService::class.java)

    private var restaurantsDao = RestaurantsDb.getDaoInstance(Main.getAppContext())


    /**
     * Tries to fetch remote restaurants and store them into DB. Throws exception if there was
     * an issue.
     */
    suspend fun loadRestaurants() {
        withContext(Dispatchers.IO) {
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
        }
    }

    suspend fun getRestaurants() : List<Restaurant> {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.getAll().map {
                Restaurant(it.id, it.title, it.description, it.isFavourite)
            }
        }
    }

    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) {
        Timber.i("toggleFavoriteRestaurant($id, $value)")

        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialLocalRestaurant(id = id, isFavorite = value)
            )
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
        restaurantsDao.addAll(remoteRestaurants.map {
            LocalRestaurant(it.id, it.title, it.description, false)
        })

        // sets all new restaurants which were previously favourited to be favourited again
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialLocalRestaurant(it.id, true)
            }
        )
    }

    suspend fun getRemoteRestaurant(id: Int) : RemoteRestaurant {
        return withContext(Dispatchers.IO) {
            apiService.getRestaurant(id).first()
        }
    }
}
