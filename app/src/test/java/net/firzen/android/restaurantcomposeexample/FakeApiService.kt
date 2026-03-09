package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.delay
import net.firzen.android.restaurantcomposeexample.data.remote.ApiService
import net.firzen.android.restaurantcomposeexample.data.remote.RemoteRestaurant

class FakeApiService : ApiService {
    override suspend fun getRestaurants() : List<RemoteRestaurant> {
        delay(1000)
        return DummyContent.getRemoteRestaurants()
    }

    override suspend fun getRestaurant(id: Int) : List<RemoteRestaurant> {
        TODO("Not yet implemented")
    }
}
