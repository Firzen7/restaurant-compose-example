package net.firzen.android.restaurantcomposeexample.ui.screens

import net.firzen.android.restaurantcomposeexample.db.Restaurant

data class RestaurantsScreenState(val restaurants: List<Restaurant>, val isLoading: Boolean)
