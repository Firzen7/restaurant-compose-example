package net.firzen.android.restaurantcomposeexample.domain

import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository
import javax.inject.Inject

// constructor fields here are being initialized by Hilt
class GetInitialRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository,
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
) {

    /**
     * `operator` keyword and `invoke()` name allows calling this method without its name like this:
     *
     * val useCase = GetRestaurantsUseCase()
     * val result = useCase()
     */
    suspend operator fun invoke() : List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}
