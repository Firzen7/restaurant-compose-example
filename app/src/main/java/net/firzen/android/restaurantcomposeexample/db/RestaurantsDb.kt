package net.firzen.android.restaurantcomposeexample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.firzen.android.restaurantcomposeexample.main.Main

@Database(
    entities = [LocalRestaurant::class],
    version = 3,
    exportSchema = false)
abstract class RestaurantsDb : RoomDatabase() {
    abstract val dao: RestaurantsDao

    companion object {
        @Volatile
        private var INSTANCE: RestaurantsDao? = null

        private fun buildDatabase(context: Context) : RestaurantsDb {
            return Room.databaseBuilder(
                Main.getAppContext(),
                RestaurantsDb::class.java,
                "restaurants_database")
                .fallbackToDestructiveMigration(true)
                .build()
        }

        // super ugly singleton here.. i think i have done it in better way in other projects,
        // but following the book here for now ...
        fun getDaoInstance(context: Context) : RestaurantsDao {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
