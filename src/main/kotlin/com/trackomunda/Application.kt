package com.trackomunda

import com.trackomunda.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, host = "0.0.0.0") {
        configureKoin()
        configureTemplating()
        configureSerialization()
        configureMonitoring()
        configureHTTP()
        configureRouting()
    }.start(wait = true)
}
