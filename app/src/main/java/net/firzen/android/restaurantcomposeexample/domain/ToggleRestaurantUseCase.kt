package net.firzen.android.restaurantcomposeexample.domain

import net.firzen.android.restaurantcomposeexample.domain.Restaurant
import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository
import timber.log.Timber

class ToggleRestaurantUseCase {
    private val repository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase()

    suspend operator fun invoke(id: Int, oldValue: Boolean) : List<Restaurant> {
        Timber.i("invoke($id, $oldValue)")

        repository.toggleFavoriteRestaurant(id, oldValue.not())
        return getSortedRestaurantsUseCase()
    }
}
