package net.firzen.android.restaurantcomposeexample

import com.google.gson.annotations.SerializedName


data class Restaurant(
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_title")
    val title: String,
    @SerializedName("r_description")
    val description: String,
    var isFavourite: Boolean = false
)
