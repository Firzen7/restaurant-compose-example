package net.firzen.android.restaurantcomposeexample.usecases

import net.firzen.android.restaurantcomposeexample.db.Restaurant
import net.firzen.android.restaurantcomposeexample.db.RestaurantsRepository

class GetInitialRestaurantsUseCase {
    private val repository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase()

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
