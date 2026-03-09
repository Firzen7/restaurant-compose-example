package net.firzen.android.restaurantcomposeexample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

// @Qualifier annotation allows us to provide different dispatchers
// to the CoroutineDispatcher dependencies
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher


// this tells hilt how to initialize `dispatcher` parameter in RestaurantsViewModel.
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    
    @MainDispatcher
    @Provides
    fun providesMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

    @IoDispatcher
    @Provides
    fun providesIoDispatcher() : CoroutineDispatcher = Dispatchers.IO
}
