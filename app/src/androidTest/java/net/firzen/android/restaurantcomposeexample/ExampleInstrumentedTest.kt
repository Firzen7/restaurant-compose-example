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
class ExampleInstrumentedTest {
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
                            onFavouriteClick =
                                { _: Int, _: Boolean -> },
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


}
