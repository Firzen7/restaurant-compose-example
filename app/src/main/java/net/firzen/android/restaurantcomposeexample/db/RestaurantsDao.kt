package net.firzen.android.restaurantcomposeexample.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RestaurantsDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll() : List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)

    // Update methods in Room can be used to update rows using different entities, as long as they
    // have matching @ColumnInfo declarations and (presumably) data-types
    @Update(entity = Restaurant::class)
    suspend fun update(partialRestaurant: PartialRestaurant)

    @Update(entity = Restaurant::class)
    suspend fun updateAll(partialRestaurants: List<PartialRestaurant>)

    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
    suspend fun getAllFavorited(): List<Restaurant>
}
