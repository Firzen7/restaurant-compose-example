package net.firzen.android.restaurantcomposeexample.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import net.firzen.android.restaurantcomposeexample.domain.Restaurant
import net.firzen.android.restaurantcomposeexample.presentation.Description
import net.firzen.android.restaurantcomposeexample.ui.theme.RestaurantComposeExampleTheme

@Composable
fun RestaurantsScreen(
    state: RestaurantsScreenState,
    onItemClick: (id: Int) -> Unit = {},
    onFavouriteClick: (id: Int, oldValue: Boolean) -> Unit,
    onLaunchCoroutineClick: () -> Unit
) {
    // This is quite interesting part. We are calling viewModel.fetchRestaurants() which
    // is fetching restaurants asynchronously, so then how is it possible that
    // restaurants are correctly shown in the app? The restaurants list should be still empty
    // at the time of LazyColumn initialization.
    // Well, the answer as far as I understand is that the `state` is being changed by
    // fetchRestaurants(), and since we are getting restaurant items from there,
    // it triggers recomposition of this LazyColumn automatically later on.

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        // "Launch coroutine" test button
        Button(
            onClick = { onLaunchCoroutineClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Launch coroutine"
            )
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
                items(state.restaurants) { restaurant ->
                    RestaurantItem(restaurant, onFavouriteClick = { clickedId, oldValue ->
                        onFavouriteClick(clickedId, oldValue)
                    }, onItemClick = { id ->
                        onItemClick(id)
                    })
                }
            }

            if(state.isLoading) {
                RestaurantComposeExampleTheme {
                    CircularProgressIndicator(
                        color = Color.Black,
                        // semantics added so that this component can be identified in unit tests
                        modifier = Modifier.semantics {
                            this.contentDescription = Description.RESTAURANTS_LOADING
                        }
                    )
                }
            }

            if (state.error != null) {
                Text(state.error)
            }
        }
    }
}

@Composable
fun RestaurantItem(item: Restaurant, onFavouriteClick: (id: Int, oldValue: Boolean) -> Unit,
                   onItemClick: (id: Int) -> Unit) {

    val icon = if(item.isFavourite) {
        Icons.Filled.Favorite
    }
    else {
        Icons.Filled.FavoriteBorder
    }

    Card(modifier = Modifier
        .padding(8.dp)
        // on click listener for the whole restaurant item (used to go into RestaurantDetailsScreen)
        .clickable { onItemClick(item.id) }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(icon = Icons.Filled.Place, modifier = Modifier.weight(0.15f))
            RestaurantDetails(item.title, item.description, Modifier.weight(0.70f))
            RestaurantIcon(icon, Modifier.weight(0.15f)) {
                onFavouriteClick(item.id, item.isFavourite)
            }
        }
    }
}

@Composable
fun RestaurantIcon(icon: ImageVector, modifier: Modifier, onClick: () -> Unit = {}) {
    Image(
        imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
    )
}

@Composable
fun RestaurantDetails(title: String, description: String, modifier: Modifier,
                      horizontalAlignment: Alignment.Horizontal = Alignment.Start) {

    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        // title of this restaurant
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )

        // description of restaurant, which is a little faded using alpha property
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantsScreenPreview() {
    RestaurantComposeExampleTheme {
        RestaurantsScreen(RestaurantsScreenState(listOf(), true), {}, {_, _ ->}, {})
    }
}
