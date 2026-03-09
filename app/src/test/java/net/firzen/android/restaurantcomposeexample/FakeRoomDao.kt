package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.delay
import net.firzen.android.restaurantcomposeexample.data.local.LocalRestaurant
import net.firzen.android.restaurantcomposeexample.data.local.PartialLocalRestaurant
import net.firzen.android.restaurantcomposeexample.data.local.RestaurantsDao

/**
 * Fake DAO mimicking functions of RestaurantsDao.
 */
class FakeRoomDao : RestaurantsDao {
    private var restaurants = HashMap<Int, LocalRestaurant>()

    override suspend fun getAll() : List<LocalRestaurant> {
        delay(1000)
        return restaurants.values.toList()
    }

    override suspend fun addAll(restaurants: List<LocalRestaurant>) {
        restaurants.forEach {
            this.restaurants[it.id] = it
        }
    }

    override suspend fun update(partialRestaurant: PartialLocalRestaurant) {
        delay(1000)
        updateRestaurant(partialRestaurant)
    }

    override suspend fun updateAll(partialRestaurants: List<PartialLocalRestaurant>) {
        delay(1000)
        partialRestaurants.forEach {
            updateRestaurant(it)
        }
    }

    override suspend fun getAllFavorited() : List<LocalRestaurant> {
        return restaurants.values.toList().filter { it.isFavourite }
    }

    /**
     * Updates restaurants (containing LocalRestaurants) based on given PartialLocalRestaurant.
     * (PartialLocalRestaurant it currently only adds one extra field compared to LocalRestaurant,
     * which is `isFavorite` flag)
     */
    private fun updateRestaurant(partialRestaurant: PartialLocalRestaurant) {
        val restaurant = restaurants[partialRestaurant.id]

        if (restaurant != null) {
            restaurants[partialRestaurant.id] = restaurant.copy(
                isFavourite = partialRestaurant.isFavorite
            )
        }
    }
}
