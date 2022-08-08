package com.trackomunda.plugins

import com.trackomunda.hexagonal.adapters.driver.ktor.gameAPI
import io.ktor.http.*
import io.ktor.server.application.*
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

    routing {
        get("/") {
            call.respondRedirect("games")
        }
        gameAPI("games")

        get("/json/serialization") {
            call.respond(mapOf("hello" to "world"))
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
