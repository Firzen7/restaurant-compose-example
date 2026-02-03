package net.firzen.android.restaurantcomposeexample.domain

import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository
import javax.inject.Inject

/**
 * Loads cached restaurants from DB and sorts them alphabetically by their title.
 */
class GetSortedRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository
) {

    suspend operator fun invoke() : List<Restaurant> {
        return repository.getRestaurants().sortedBy { it.title }
    }
}
