package com.trackomunda.plugins

import freemarker.cache.*
import io.ktor.server.freemarker.*
import io.ktor.server.routing.*
import io.ktor.server.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.darkBlue
                    margin(0.px)
                }
                rule("h1.page-title") {
                    color = Color.white
                }
            }
        }
    }
}
suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
   this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

