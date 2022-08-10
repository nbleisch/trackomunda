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

    val gamesUseCases: GameService by inject()
    val gangImporter: GangImporter by inject()

    //TODO: Would like to work with LocationClasses but couldnt get them working with Post

    get() {
        val games = gamesUseCases.findAllGames()
        call.respond(ThymeleafContent(template = "games", model = mapOf("games" to games)))
    }

    post() {
        val newGame = gamesUseCases.createANewGame(call.receive<Parameters>()["gameName"].orEmpty())
        call.respondRedirect(URLBuilder.createFromCall(call).appendPathSegments(newGame.id).build().encodedPath)
    }

    route("{id}") {
        get {
            gamesUseCases.findGame(call.parameters["id"]!!)?.let {
                call.respond(ThymeleafContent(template = "game", model = mapOf("game" to it)))
            } ?: call.response.status(HttpStatusCode.NotFound)
        }

        post("add-gang1") {
            val parameters = call.receive<Parameters>()
            gamesUseCases.addGang1ToGame(call.parameters["id"]!!, parameters["yakTribeGangUrl1"].orEmpty())?.let {
                call.respondRedirect(URLBuilder.createFromCall(call).encodedPath.removeSuffix("/add-gang1"))
            } ?: call.response.status(HttpStatusCode.NotFound)
        }

        post("add-gang2") {
            val parameters = call.receive<Parameters>()
            gamesUseCases.addGang2ToGame(call.parameters["id"]!!, parameters["yakTribeGangUrl2"].orEmpty())?.let {
                call.respondRedirect(URLBuilder.createFromCall(call).encodedPath.removeSuffix("/add-gang2"))
            } ?: call.response.status(HttpStatusCode.NotFound)
        }

    }
}