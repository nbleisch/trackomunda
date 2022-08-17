package com.trackomunda.adapter

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.marcinziolo.kotlin.wiremock.any
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returns
import com.trackomunda.hexagonal.adapters.YakTribeFetchException
import com.trackomunda.hexagonal.adapters.YakTribeGangImporter
import com.trackomunda.hexagonal.adapters.adaptersModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.net.ServerSocket
import kotlin.test.Test
import kotlin.test.assertEquals

class YaktribeGangImporterTest : KoinTest {
    val port = findRandomPort()
    val url = "http://localhost:$port"
    val yakTribeGangImporter: YakTribeGangImporter by inject()

    @Before
    fun setup() {
        startKoin {
            modules(adaptersModule)
        }
    }

    @After
    fun tearDown(){
        stopKoin()
    }

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
        val gang = yakTribeGangImporter.importGang("$url/gang")
        assertEquals(gang.name, "Cawdor1000")
        assertEquals(gang.ganger.size, 10)
    }

    @Test(expected = YakTribeFetchException::class)
    fun fetchingAYakTribeGangShouldFailWhenTheResponseIsNoSuccess() {
        wiremock.any {
        } returns {
            statusCode = 400
        }
        yakTribeGangImporter.importGang("$url/gang")
    }

    private fun findRandomPort(): Int {
        ServerSocket(0).use { socket -> return socket.localPort }
    }


}