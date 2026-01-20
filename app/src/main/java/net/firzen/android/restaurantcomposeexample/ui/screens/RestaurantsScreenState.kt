package net.firzen.android.restaurantcomposeexample.ui.screens

import net.firzen.android.restaurantcomposeexample.db.Restaurant

/**
 * This represents custom state of restaurants list with flag telling the UI if restaurants
 * are still being loaded. Thanks to this custom state, responsibility of the UI
 * (RestaurantsScreen in our case) is only to render data being provided by RestaurantsViewModel.
 * This way, the MVVM principle is being strictly followed and UI layer does not need to be
 * detecting whether restaurants are still being loaded, etc.
 * */
data class RestaurantsScreenState(val restaurants: List<Restaurant>,
                                  val isLoading: Boolean,
                                  val error: String? = null)
