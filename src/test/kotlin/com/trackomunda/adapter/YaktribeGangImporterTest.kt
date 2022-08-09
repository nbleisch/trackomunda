package com.trackomunda.adapter

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.marcinziolo.kotlin.wiremock.any
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returns
import com.trackomunda.hexagonal.adapters.YakTribeFetchException
import com.trackomunda.hexagonal.adapters.YakTribeGangImporter
import org.junit.Rule
import java.net.ServerSocket
import kotlin.test.Test
import kotlin.test.assertEquals

class YaktribeGangImporterTest {
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
        val gang = YakTribeGangImporter().importGang("$url/gang")
        assertEquals(gang.name, "Cawdor1000")
        assertEquals(gang.ganger.size, 10)
    }

    @Test(expected = YakTribeFetchException::class)
    fun fetchingAYakTribeGangShouldFailWhenTheResponseIsNoSuccess() {
        wiremock.any {
        } returns {
            statusCode = 400
        }
        YakTribeGangImporter().importGang("$url/gang")
    }

    private fun findRandomPort(): Int {
        ServerSocket(0).use { socket -> return socket.localPort }
    }


}