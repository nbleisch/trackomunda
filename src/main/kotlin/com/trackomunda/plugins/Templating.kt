package com.trackomunda.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import kotlinx.css.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }

    routing {
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.white
                    margin(20.px)
                }

                rule("div.content") {
                  /*  top = LinearDimension("20%")
                    left = LinearDimension("50%")
                    transform = Transforms().apply {
                        translate(LinearDimension("-50%"), LinearDimension("-50%"))
                    }*/
                }
                rule("h1.page-title") {
                    color = Color.black
                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

