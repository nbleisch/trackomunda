package com.trackomunda

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.trackomunda.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureKoin()
        configureTemplating()
        configureSerialization()
        configureMonitoring()
        configureHTTP()
        configureRouting()
    }.start(wait = true)
}
