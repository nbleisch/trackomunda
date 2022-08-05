package com.trackomunda.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.trackomunda.model.Gang
import com.trackomunda.yaktribe.YaktribeGangContainer
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class YakTribeFetchException(message: String, origin: Throwable) : RuntimeException(message, origin)

class YakTribeGangFetcher {

    private val objectMapper = jacksonObjectMapper()

    fun fetchGang(yakTribeGangJsonUrl: Url): Gang = kotlin.runCatching {
        HttpClient {
            expectSuccess = true
            followRedirects = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }.use { client ->
            runBlocking {
                objectMapper.readValue<YaktribeGangContainer>(client.get(yakTribeGangJsonUrl).bodyAsText()).toGang()
            }
        }
    }.getOrElse {
        throw (when (it) {
            is ClientRequestException -> YakTribeFetchException("Couldn't fetch $yakTribeGangJsonUrl", it)
            else -> it
        })
    }
}