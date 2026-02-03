package net.firzen.android.restaurantcomposeexample.domain

import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository
import timber.log.Timber
import javax.inject.Inject

class ToggleRestaurantUseCase @Inject constructor(
    private val repository: RestaurantsRepository,
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase) {

    suspend operator fun invoke(id: Int, oldValue: Boolean) : List<Restaurant> {
        Timber.i("invoke($id, $oldValue)")

        repository.toggleFavoriteRestaurant(id, oldValue.not())
        return getSortedRestaurantsUseCase()
    }
}
