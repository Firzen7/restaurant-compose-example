package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
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

    private fun getViewModel(triggerError: Boolean = false,
                             errorMessage: String? = null) : RestaurantsViewModel {

        val restaurantsRepository = RestaurantsRepository(
            FakeApiService(triggerError, errorMessage),
            FakeRoomDao(),
            dispatcher
        )

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

    /**
     * Tests correctness of the state with content.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stateWithContent_isProduced() {
        scope.runTest {
            val viewModel = getViewModel()
            // ensures to wait until all methods called by viewModel have returned their values
            // (important when calling `delay()` in dummy methods inside of `FakeApiService`
            // and `FakeRoomDao`, etc.)
            advanceUntilIdle()

            val currentState = viewModel.state.value

            assert(
                currentState == RestaurantsScreenState(
                    restaurants = DummyContent.getDomainRestaurants(),
                    isLoading = false,
                    error = null
                )
            )
        }
    }

    /**
     * Tests correctness of the error state.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun errorState_isProduced() {
        scope.runTest {
            val message = "Could not retrieve remote restaurants!"

            val viewModel = getViewModel(triggerError = true, errorMessage = message)
            advanceUntilIdle()
            val currentState = viewModel.state.value

            // here we check if error message got all the way into the currentState by triggering
            // exception in viewModel
            assert(
                currentState == RestaurantsScreenState(
                    restaurants = emptyList(),
                    isLoading = false,
                    error = message
                )
            )
        }
    }
}
