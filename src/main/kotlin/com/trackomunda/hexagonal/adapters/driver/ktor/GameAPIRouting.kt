package com.trackomunda.hexagonal.adapters.driver.ktor

import com.trackomunda.hexagonal.core.GameUseCases
import com.trackomunda.hexagonal.ports.GangImporter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.koin.ktor.ext.inject


@OptIn(KtorExperimentalLocationsAPI::class)
fun Routing.gameAPI(entryPoint: String) = route(entryPoint) {

    val gamesUseCases: GameUseCases by inject()
    val gangImporter: GangImporter by inject()

    //TODO: Would like to work with LocationClasses but couldnt get them working with Post

    get() {
        val games = gamesUseCases.findAllGames()
        call.respond(ThymeleafContent(template = "games", model = mapOf("games" to games)))
    }

    post() {
        val newGame = gamesUseCases.createANewGame(call.receive<Parameters>()["gameName"].orEmpty())
        call.respondRedirect { this.appendPathSegments(newGame.id) }
    }

    route("{id}") {
        get {
            gamesUseCases.findGame(call.parameters["id"]!!)?.let {
                call.respond(ThymeleafContent(template = "game", model = mapOf("game" to it)))
            } ?: call.response.status(HttpStatusCode.NotFound)
        }

        post("new-gang") {
            gamesUseCases.findGame(call.parameters["id"]!!)?.let { game ->
                val parameters = call.receive<Parameters>()
                val yakTribeUrl = parameters["yakTribeGangUrl"].orEmpty()
                val gang = gangImporter.importGang(yakTribeUrl)
                gamesUseCases.updateGame(game.copy(gang = gang))
                call.respondRedirect {
                    this.path(this.encodedPath.removeSuffix("/new-gang"))
                }
            } ?: call.response.status(HttpStatusCode.NotFound)
        }
    }
}