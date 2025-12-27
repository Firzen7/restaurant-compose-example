package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import net.firzen.android.restaurantcomposeexample.Restaurant
import net.firzen.android.restaurantcomposeexample.RestaurantViewModel
import net.firzen.android.restaurantcomposeexample.ui.theme.RestaurantComposeExampleTheme

// https://github.com/PacktPublishing/Kickstart-Modern-Android-Development-with-Jetpack-and-Kotlin/tree/main/Chapter_01/chapter_1_restaurants_app/app/src/main/java/com/codingtroops/restaurantsapp
// Pages 32 - 49
// Added ViewModel in chapter 2 (pages 52 - 84)

@Composable
fun RestaurantsScreen() {
    val viewModel: RestaurantViewModel = viewModel()

    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
        items(viewModel.getRestaurants()) { restaurant ->
            RestaurantItem(restaurant)
        }
    }
}

@Composable
fun RestaurantItem(item: Restaurant) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(icon = Icons.Filled.Place, modifier = Modifier.weight(0.15f))
            RestaurantDetails(item.title, item.description, Modifier.weight(0.85f))
        }
    }
}

@Composable
private fun RestaurantDetails(title: String, description: String, modifier: Modifier) {
    Column(modifier = modifier) {
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

@Composable
fun RestaurantIcon(icon: ImageVector, modifier: Modifier) {
    Image(imageVector = icon, contentDescription = null, modifier = modifier.padding(8.dp))
}

@Preview(showBackground = true)
@Composable
fun RestaurantsScreenPreview() {
    RestaurantComposeExampleTheme {
        RestaurantsScreen()
    }
}
