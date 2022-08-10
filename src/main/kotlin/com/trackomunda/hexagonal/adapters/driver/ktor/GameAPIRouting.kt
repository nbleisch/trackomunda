package com.trackomunda.hexagonal.adapters.driver.ktor

import com.trackomunda.hexagonal.core.services.GameService
import com.trackomunda.hexagonal.ports.GangImporter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject


@OptIn(KtorExperimentalLocationsAPI::class)
fun Routing.gameAPI(entryPoint: String) = route(entryPoint) {

    val gamesService: GameService by inject()
    val gangImporter: GangImporter by inject()

    //TODO: Would like to work with LocationClasses but couldnt get them working with Post

    get() {
        val games = gamesService.findAllGames()
        call.respond(ThymeleafContent(template = "games", model = mapOf("games" to games)))
    }

    post() {
        val newGame = gamesService.createANewGame(call.receive<Parameters>()["gameName"].orEmpty())
        call.respondRedirect(URLBuilder.createFromCall(call).appendPathSegments(newGame.id).build().encodedPath)
    }

    route("{id}") {
        get {
            gamesService.findGame(call.parameters["id"]!!)?.let {
                call.respond(ThymeleafContent(template = "game", model = mapOf("game" to it)))
            } ?: call.response.status(HttpStatusCode.NotFound)
        }

        post("add-gang") {
            val parameters = call.receive<Parameters>()
            val gameId = call.parameters["id"]
            val yakTribeGangUrl = parameters["yakTribeGangUrl"]
            val gangIndex = parameters["gangIndex"]?.toInt()
            requireNotNull(gameId) { "gameId parameter is missing" }
            requireNotNull(yakTribeGangUrl) { "yakTribeGangUrl parameter is missing" }
            requireNotNull(gangIndex) { "gangIndex must be set" }
            require(gangIndex in 1..2) { "gangIndex can only be 1 or 2" }
            when (gangIndex) {
                1 -> gamesService.addGang1ToGame(gameId, yakTribeGangUrl)
                2 -> gamesService.addGang2ToGame(gameId, yakTribeGangUrl)
                else -> null
            }?.let {
                call.respondRedirect(URLBuilder.createFromCall(call).encodedPath.removeSuffix("/add-gang"))
            } ?: call.response.status(HttpStatusCode.NotFound)
        }
    }
}