package net.firzen.android.restaurantcomposeexample.domain

import net.firzen.android.restaurantcomposeexample.domain.Restaurant
import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository

/**
 * Loads cached restaurants from DB and sorts them alphabetically by their title.
 */
class GetSortedRestaurantsUseCase {
    private val repository = RestaurantsRepository()

    suspend operator fun invoke() : List<Restaurant> {
        return repository.getRestaurants().sortedBy { it.title }
    }
}
