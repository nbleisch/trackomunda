import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window
import trackomunda.model.GangPayload

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getShoppingList(): List<GangPayload> {
    return jsonClient.get(endpoint + GangPayload.path)
}

suspend fun addShoppingListItem(gangPayload: GangPayload) {
    jsonClient.post<Unit>(endpoint + GangPayload.path) {
        contentType(ContentType.Application.Json)
        body = gangPayload
    }
}

suspend fun deleteShoppingListItem(gangPayload: GangPayload) {
    jsonClient.delete<Unit>(endpoint + GangPayload.path + "/${gangPayload.gang_id}")
}