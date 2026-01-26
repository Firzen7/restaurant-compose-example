package net.firzen.android.restaurantcomposeexample.usecases

import net.firzen.android.restaurantcomposeexample.db.Restaurant
import net.firzen.android.restaurantcomposeexample.db.RestaurantsRepository

/**
 * Loads cached restaurants from DB and sorts them alphabetically by their title.
 */
class GetSortedRestaurantsUseCase {
    private val repository = RestaurantsRepository()

    suspend operator fun invoke() : List<Restaurant> {
        return repository.getRestaurants().sortedBy { it.title }
    }
}
