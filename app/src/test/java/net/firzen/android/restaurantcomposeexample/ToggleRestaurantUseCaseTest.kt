package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository
import net.firzen.android.restaurantcomposeexample.domain.GetSortedRestaurantsUseCase
import net.firzen.android.restaurantcomposeexample.domain.ToggleRestaurantUseCase
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ToggleRestaurantUseCaseTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun toggleRestaurant_IsUpdatingFavoriteField() {
        scope.runTest {
            val restaurantsRepository = RestaurantsRepository(
                FakeApiService(),
                FakeRoomDao(),
                dispatcher
            )

            val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)

            val useCase = ToggleRestaurantUseCase(restaurantsRepository, getSortedRestaurantsUseCase)

            // preload data
            restaurantsRepository.loadRestaurants()
            advanceUntilIdle()

            // execute useCase
            val restaurants = DummyContent.getDomainRestaurants()
            val targetItem = restaurants[0]
            val isFavorite = targetItem.isFavourite
            val updatedRestaurants = useCase(targetItem.id, isFavorite)
            advanceUntilIdle()

            // check if isFavourite state has really been negated
            restaurants[0] = targetItem.copy(isFavourite = !isFavorite)
            assert(updatedRestaurants == restaurants)
        }
    }
}
