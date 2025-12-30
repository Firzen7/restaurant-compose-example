package net.firzen.android.restaurantcomposeexample.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import net.firzen.android.restaurantcomposeexample.Restaurant
import net.firzen.android.restaurantcomposeexample.User
import net.firzen.android.restaurantcomposeexample.saveDetails2
import net.firzen.android.restaurantcomposeexample.ui.theme.RestaurantComposeExampleTheme

// https://github.com/PacktPublishing/Kickstart-Modern-Android-Development-with-Jetpack-and-Kotlin/tree/main/Chapter_01/chapter_1_restaurants_app/app/src/main/java/com/codingtroops/restaurantsapp
// Pages 32 - 49
// Added ViewModel in chapter 2 (pages 52 - 84)

@Composable
fun RestaurantsScreen() {
    val viewModel: RestaurantsViewModel = viewModel()
//    viewModel.fetchRestaurants()

//    LaunchedEffect(key1 = "request_restaurants") {
//        viewModel.fetchRestaurants()
//    }

    // This is quite interesting part. We are calling viewModel.fetchRestaurants() which
    // is fetching restaurants asynchronously, so then how is it possible that
    // restaurants are correctly shown in the app? The restaurants list should be still empty
    // at the time of LazyColumn initialization.
    // Well, the answer as far as I understand is that the `state` is being changed by
    // fetchRestaurants(), and since we are getting restaurant items from there,
    // it triggers recomposition of this LazyColumn automatically later on.

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            onClick = { saveDetails2(context, viewModel.viewModelScope, User(5, "Frankie")) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Launch coroutine"
            )
        }
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
            items(viewModel.state.value) { restaurant ->
                RestaurantItem(restaurant) { clickedId ->
                    viewModel.toggleFavourite(clickedId)
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(item: Restaurant, onClick: (id: Int) -> Unit) {
    val icon = if(item.isFavourite) {
        Icons.Filled.Favorite
    }
    else {
        Icons.Filled.FavoriteBorder
    }

    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(icon = Icons.Filled.Place, modifier = Modifier.weight(0.15f))
            RestaurantDetails(item.title, item.description, Modifier.weight(0.70f))
            RestaurantIcon(icon, Modifier.weight(0.15f)) {
                onClick(item.id)
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
        RestaurantsScreen()
    }
}
