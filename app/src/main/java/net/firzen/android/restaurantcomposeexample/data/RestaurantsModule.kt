package net.firzen.android.restaurantcomposeexample.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.firzen.android.restaurantcomposeexample.data.local.RestaurantsDao
import net.firzen.android.restaurantcomposeexample.data.local.RestaurantsDb
import net.firzen.android.restaurantcomposeexample.data.remote.ApiService
import net.firzen.android.restaurantcomposeexample.main.BASE_API_URL
import net.firzen.android.restaurantcomposeexample.main.DATABASE_NAME
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestaurantsModule {

    @Provides
    fun provideRoomDao(database: RestaurantsDb) : RestaurantsDao {
        return database.dao
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ) : RestaurantsDb {
        return Room.databaseBuilder(
            appContext,
            RestaurantsDb::class.java,
            DATABASE_NAME)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            // here we add GSON support for Retrofit
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                // Base API endpoint URL
                BASE_API_URL
            )
            .build()
    }

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
