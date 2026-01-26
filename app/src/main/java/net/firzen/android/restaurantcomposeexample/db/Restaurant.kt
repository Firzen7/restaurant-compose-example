package net.firzen.android.restaurantcomposeexample.db

// this is our domain model class
data class Restaurant(
    val id: Int,
    val title: String,
    val description: String,
    val isFavourite: Boolean = false
)
