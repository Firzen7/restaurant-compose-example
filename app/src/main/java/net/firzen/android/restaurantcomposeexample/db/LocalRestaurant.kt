package net.firzen.android.restaurantcomposeexample.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class LocalRestaurant(
    @PrimaryKey
    @ColumnInfo(name = "r_id")
    val id: Int,

    @ColumnInfo(name = "r_title")
    val title: String,

    @ColumnInfo(name = "r_description")
    val description: String,

    // val (immutability) is important here, since it allows correct Compose recompositions
    @ColumnInfo(name = "is_favorite")
    val isFavourite: Boolean = false
)
