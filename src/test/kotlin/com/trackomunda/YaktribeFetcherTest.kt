package com.trackomunda

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.marcinziolo.kotlin.wiremock.any
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returns
import com.trackomunda.services.YakTribeFetchException
import com.trackomunda.services.YakTribeGangFetcher
import io.ktor.http.*
import org.junit.Rule
import java.net.ServerSocket
import kotlin.test.Test
import kotlin.test.assertEquals

class YaktribeFetcherTest {
    val port = findRandomPort()
    val url = "http://localhost:$port"

    @Rule
    @JvmField
    var wiremock: WireMockRule = WireMockRule(port)


    @Test
    fun fetchingAYakTribeGangSucceeds() {
        wiremock.get {
            url equalTo "/gang"
        } returns {
            statusCode = 200
            body = this::class.java.classLoader.getResource("yaktribe_test_gang.json")!!.readText()
        }
        val gang = YakTribeGangFetcher().fetchGang(Url("$url/gang"))
        assertEquals(gang.name, "Cawdor1000")
        assertEquals(gang.ganger.size, 10)
    }

    @Test(expected = YakTribeFetchException::class)
    fun fetchingAYakTribeGangShouldFailWhenTheResponseIsNoSuccess() {
        wiremock.any {
        } returns {
            statusCode = 400
        }
        val gang = YakTribeGangFetcher().fetchGang(Url("$url/gang"))
    }

    private fun findRandomPort(): Int {
        ServerSocket(0).use { socket -> return socket.localPort }
    }


}