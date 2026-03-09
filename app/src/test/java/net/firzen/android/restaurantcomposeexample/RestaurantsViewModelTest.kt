package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import net.firzen.android.restaurantcomposeexample.data.RestaurantsRepository
import net.firzen.android.restaurantcomposeexample.domain.GetInitialRestaurantsUseCase
import net.firzen.android.restaurantcomposeexample.domain.GetSortedRestaurantsUseCase
import net.firzen.android.restaurantcomposeexample.domain.ToggleRestaurantUseCase
import net.firzen.android.restaurantcomposeexample.presentation.list.RestaurantsScreenState
import net.firzen.android.restaurantcomposeexample.presentation.list.RestaurantsViewModel
import org.junit.Test

/**
 * Test focused on functionality of RestaurantsViewModel. They cover these scenarios:
 *  - Initial loading state --> before any content is loaded
 *  - State with content    --> after data are successfully loaded - most common state in real-life
 *                              use of the app
 *  - Error state           --> something went wrong and CoroutineExceptionHandler was triggered
 */
class RestaurantsViewModelTest {
    // tests-compatible coroutines dispatcher
    private val dispatcher = StandardTestDispatcher()
    // test-compatible coroutine scope
    private val scope = TestScope(dispatcher)

    private fun getViewModel() : RestaurantsViewModel {
        val restaurantsRepository = RestaurantsRepository(FakeApiService(), FakeRoomDao())
        val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)

        val getInitialRestaurantsUseCase = GetInitialRestaurantsUseCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        val toggleRestaurantUseCase = ToggleRestaurantUseCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        return RestaurantsViewModel(getInitialRestaurantsUseCase, toggleRestaurantUseCase, dispatcher)
    }

    /**
     * Tests correctness of the initial state.
     */
    @Test
    fun initialState_isProduced() {
        scope.runTest {
            // here we initialize dummy ViewModel
            val viewModel = getViewModel()
            // we get its initial state
            val initialState = viewModel.state.value

            // and finally, we compare its state with initial (empty) state by initializing
            // RestaurantsScreenState with default parameters
            assert(
                initialState == RestaurantsScreenState(
                    restaurants = emptyList(),
                    isLoading = true,
                    error = null
                )
            )
        }
    }
}
