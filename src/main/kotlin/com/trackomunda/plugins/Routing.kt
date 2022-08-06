package com.trackomunda.plugins

import com.trackomunda.model.Game
import com.trackomunda.plugins.GamesLoc.GameLoc
import com.trackomunda.services.GameService
import com.trackomunda.services.YakTribeGangFetcher
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }

    }
    install(Locations)

    val gameService = GameService()
    val yakTribeGangFetcher = YakTribeGangFetcher()


    routing {
        get("/") {
            call.respondRedirect(application.locations.href(GamesLoc()))
        }
        get<GamesLoc> {
            //Fetch Games
            val games: List<Game> = gameService.getGames()
            call.respond(ThymeleafContent(template = "games", model = mapOf("games" to games)))
        }

        post("/games") {
            val newGame = gameService.createGame(call.receive<Parameters>()["gameName"].orEmpty())
            call.respondRedirect(application.locations.href(GameLoc(id = newGame.id, games = GamesLoc())))
        }

        post("/games/{id}/new-gang") {
            gameService.findGame(call.parameters["id"]!!)?.let { game ->
                val parameters = call.receive<Parameters>()
                val yakTribeUrl = parameters["yakTribeGangUrl"].orEmpty()
                val gang = yakTribeGangFetcher.fetchGang(Url(yakTribeUrl))
                gameService.update(game.copy(gang = gang))
                call.respondRedirect(application.locations.href(GameLoc(id = game.id, games = GamesLoc())))
            } ?: call.response.status(NotFound)
        }

        // Register nested routes
        get<GameLoc> {
            //Fetch Game
            gameService.findGame(it.id)?.let {
                call.respond(ThymeleafContent(template = "game", model = mapOf("game" to it)))
            } ?: call.response.status(NotFound)

        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

@Location("/games")
class GamesLoc {
    @Location("/{id}")
    data class GameLoc(val id: String, val games: GamesLoc)
}