package net.firzen.android.restaurantcomposeexample

import kotlinx.coroutines.delay
import net.firzen.android.restaurantcomposeexample.data.remote.ApiService
import net.firzen.android.restaurantcomposeexample.data.remote.RemoteRestaurant

class FakeApiService(val triggerError: Boolean = false,
                     val errorMessage: String? = null) : ApiService {

    override suspend fun getRestaurants() : List<RemoteRestaurant> {
        delay(1000)

        if(triggerError) {
            throw IllegalStateException(errorMessage)
        }

        return DummyContent.getRemoteRestaurants()
    }

    override suspend fun getRestaurant(id: Int) : List<RemoteRestaurant> {
        TODO("Not yet implemented")
    }
}
