package net.firzen.android.restaurantcomposeexample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.firzen.android.restaurantcomposeexample.presentation.Description
import net.firzen.android.restaurantcomposeexample.presentation.list.RestaurantsScreen
import net.firzen.android.restaurantcomposeexample.presentation.list.RestaurantsScreenState
import net.firzen.android.restaurantcomposeexample.ui.theme.RestaurantComposeExampleTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class RestaurantScreenTests {
    @get:Rule
    val testRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("net.firzen.android.restaurantcomposeexample", appContext.packageName)
    }

    @Test
    fun initialState_isRendered() {
        testRule.mainClock.autoAdvance = false

        testRule.setContent {
            RestaurantComposeExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        RestaurantsScreen(
                            state = RestaurantsScreenState(
                                restaurants = emptyList(),
                                isLoading = true
                            ),
                            onFavouriteClick = { _: Int, _: Boolean -> },
                            onItemClick = { },
                            onLaunchCoroutineClick = {}
                        )
                    }
                }
            }
        }

        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING,
            useUnmergedTree = true
        ).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val restaurants = DummyContent.getDomainRestaurants()

        testRule.setContent {
            RestaurantComposeExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        RestaurantsScreen(
                            state = RestaurantsScreenState(
                                restaurants = restaurants,
                                isLoading = false
                            ),
                            onFavouriteClick = { _: Int, _: Boolean -> },
                            onItemClick = {},
                            onLaunchCoroutineClick = {}
                        )
                    }
                }
            }
        }

        // onNodeWithText can find Text using its text content
        // here it is used to check if restaurant item with first title from dummy restaurants
        // list is shown in the UI
        testRule.onNodeWithText(restaurants[0].title).assertIsDisplayed()
        testRule.onNodeWithText(restaurants[0].description).assertIsDisplayed()

        // ^^ These assertions only work if the given nodes are displayed on screen. That means
        // is there was a long list of restaurants, and some items were not shown on screen, then
        // the test would fail on smaller screens if we were checking for some of the bottom items.

        // check that CircularProgressIndicator is no longer visible (it should be hidden after
        // restaurants list is loaded)
        testRule.onNodeWithContentDescription(Description.RESTAURANTS_LOADING).assertDoesNotExist()
    }

    @Test
    fun stateWithError_isRendered() {
        val errorMessage = "Fatal error! Self-destruct in 10 seconds!!!"

        testRule.setContent {
            RestaurantComposeExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        RestaurantsScreen(
                            state = RestaurantsScreenState(
                                restaurants = emptyList(),
                                isLoading = false,
                                error = errorMessage
                            ),
                            onFavouriteClick = { _: Int, _: Boolean -> },
                            onItemClick = {},
                            onLaunchCoroutineClick = {}
                        )
                    }
                }
            }
        }

        testRule.onNodeWithText(errorMessage).assertIsDisplayed()
        testRule.onNodeWithContentDescription(Description.RESTAURANTS_LOADING).assertDoesNotExist()

        Thread.sleep(2000)
    }

    @Test
    fun stateWithContent_ClickOnItem_isRegistered() {
        val restaurants = DummyContent.getDomainRestaurants()
        val targetRestaurant = restaurants[0]

        testRule.setContent {
            RestaurantComposeExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        RestaurantsScreen(
                            state = RestaurantsScreenState(
                                restaurants = restaurants,
                                isLoading = false
                            ),
                            onFavouriteClick = { _: Int, _: Boolean -> },

                            // here we check if callback is propagating clicked
                            // restaurant id correctly
                            onItemClick = { id -> assert(id == targetRestaurant.id) },

                            onLaunchCoroutineClick = {}
                        )
                    }
                }
            }
        }

        // here we simulate click on first restaurant in the list
        testRule.onNodeWithText(targetRestaurant.title).performClick()
    }
}
