package com.trackomunda.plugins

import com.trackomunda.model.Game
import com.trackomunda.plugins.GamesLoc.GameLoc
import com.trackomunda.services.GameService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

    val gameService: GameService = GameService()


    routing {
        get("/") {
            call.respondRedirect(application.locations.href(GamesLoc()))
        }
        get<GamesLoc> {
            //Fetch Games
            val games: List<Game> = gameService.getGames()
            call.respond(FreeMarkerContent(template = "games.ftl", model = mapOf("games" to games)))
        }

        post("/games") {
            val postPayload = call.parameters
            val newGame = gameService.createGame()
            call.respondRedirect(application.locations.href(GameLoc(id = newGame.id, games = GamesLoc())))
        }

        // Register nested routes
        get<GameLoc> {
            //Fetch Game
            val game = gameService.findGame(it.id)
            call.respond(FreeMarkerContent(template = "game.ftl", model = mapOf("game" to game)))
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