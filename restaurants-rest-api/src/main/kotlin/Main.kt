package net.firzen.rest

import com.google.gson.Gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun main() {
    listFields(Restaurant::class)

    startServer()
}

fun startServer() {
    embeddedServer(Netty, 8080) {
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                val code = HttpStatusCode.InternalServerError

                cause.printStackTrace()

                call.respondText(text = cause.message ?: "", status = code, contentType = ContentType.Application.Json)
            }
        }

        routing {
            get("/restaurants.json") {
                val orderBy = call.request.queryParameters["orderBy"]
                val equalTo = call.request.queryParameters["equalTo"]

                var outputRestaurants: List<Restaurant> = dummyRestaurants

                orderBy?.replace("\"", "").let { orderBy ->
                    if(orderBy?.isNotBlank() == true) {
                        println("Sorted by $orderBy")
                        outputRestaurants = sortedByField(outputRestaurants, orderBy)

                        equalTo?.replace("\"", "").let { equalTo ->
                            if(equalTo?.isNotBlank() == true) {
                                println("Filtered by $equalTo")
                                outputRestaurants = filteredByField(outputRestaurants, orderBy, equalTo)
                            }
                        }
                    }
                }

                val json = Gson().toJson(outputRestaurants)
                call.respondText(json.toString(), ContentType.Application.Json)
            }

            route("{...}") {
                handle {
                    call.respondText("<html><p>Welcome to...</p></html>", ContentType.Text.Html)
                }
            }
        }
    }.start(wait = true)
}

/**
 * Returns a set of field names from given Kotlin class.
 */
private fun listFields(kClass: KClass<*>) : Set<String> {
    return kClass.memberProperties.map { it.name }.toSet()
}

private fun <T : Any> sortedByField(list: List<T>, fieldName: String) : List<T> {
    val listClass = list.first()::class
    val fields = listFields(listClass)

//    println("listClass = $listClass, fields = $fields")

    val prop = listClass.memberProperties.first { it.name == fieldName }

    @Suppress("UNCHECKED_CAST")
    val p = prop as kotlin.reflect.KProperty1<T, Comparable<Any>>

    return list.sortedBy { p.get(it) }
}

private fun <T : Any> filteredByField(list: List<T>, fieldName: String, desiredValue: String?) : List<T> {
//    println("filteredByField($list, $fieldName, $desiredValue)")

    val listClass = list.first()::class
    val fields = listFields(listClass)

//    println("listClass = $listClass, fields = $fields")

    @Suppress("UNCHECKED_CAST")
    val prop = listClass.memberProperties.first { it.name == fieldName } as KProperty1<T, *>

    return list.filter { instance ->
        val value = prop.get(instance).toString()
        val result = (value == desiredValue)
//        println("$value == $desiredValue --> $result")

        result
    }
}
