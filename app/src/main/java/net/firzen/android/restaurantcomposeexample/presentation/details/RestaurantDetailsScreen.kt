package net.firzen.android.restaurantcomposeexample.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import net.firzen.android.restaurantcomposeexample.presentation.list.RestaurantDetails
import net.firzen.android.restaurantcomposeexample.presentation.list.RestaurantIcon

@Composable
fun RestaurantDetailsScreen() {
    val viewModel: RestaurantDetailsViewModel = hiltViewModel()
    val item = viewModel.state.value

    if (item != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)) {

            RestaurantIcon(
                Icons.Filled.Place,
                Modifier.padding(
                    top = 32.dp,
                    bottom = 32.dp
                )
            )

            RestaurantDetails(
                item.title,
                item.description,
                Modifier.padding(bottom = 32.dp),
                Alignment.CenterHorizontally
            )

            Text("More info coming soon!")
        }
    }
}
