package com.trackomunda.integration

import com.trackomunda.plugins.configureKoin
import com.trackomunda.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testApplication() = testApplication {
        application {
            configureKoin()
            configureRouting()
        }
        client.get("/").apply {
            //assertEquals(HttpStatusCode.OK, status)
            //assertEquals("Hello World!", bodyAsText())
        }
    }
}