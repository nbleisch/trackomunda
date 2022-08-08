package com.trackomunda.hexagonal.adapters

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.trackomunda.hexagonal.core.domain.Gang
import com.trackomunda.hexagonal.core.domain.Ganger
import com.trackomunda.hexagonal.ports.GangImporter
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

class YakTribeFetchException(message: String, origin: Throwable) : RuntimeException(message, origin)

class YakTribeGangImporter : GangImporter {

    private val objectMapper = jacksonObjectMapper()

    override fun importGang(gangUrl: String): Gang {
        return kotlin.runCatching {
            HttpClient {
                expectSuccess = true
                followRedirects = true
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.HEADERS
                }
            }.use { client ->
                runBlocking {
                    objectMapper.readValue<YaktribeGangContainer>(client.get(gangUrl).bodyAsText()).toGang()
                }
            }
        }.getOrElse {
            throw (when (it) {
                is ClientRequestException -> YakTribeFetchException("Couldn't fetch $gangUrl", it)
                else -> it
            })
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class YaktribeGangContainer(
        val gang: YaktribeGang
    ) {
        fun toGang() =
            gang.let { yaktribeGang ->
                Gang(
                    name = yaktribeGang.gang_name,
                    ganger = yaktribeGang.gangers.map { yakTribeGanger ->
                        Ganger(
                            name = yakTribeGanger.name,
                            movement = yakTribeGanger.m,
                            weaponSkill = yakTribeGanger.w,
                            ballisticSkill = yakTribeGanger.bs,
                            strength = yakTribeGanger.s,
                            toughness = yakTribeGanger.t,
                            wounds = yakTribeGanger.w,
                            initiative = yakTribeGanger.i,
                            attacks = yakTribeGanger.a,
                            leadership = yakTribeGanger.ld,
                            coolness = yakTribeGanger.cl,
                            willpower = yakTribeGanger.wil,
                            intelligence = yakTribeGanger.int,
                        )
                    }
                )
            }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private data class YaktribeGang(
            val gang_id: String,
            val gang_name: String,
            val gang_type: String,
            val gang_rating: String,
            val campaign: String,
            val url: String,
            val credits: String,
            val meat: String,
            val reputation: String,
            val wealth: String,
            val alignment: String,
            val allegiance: String,
            val territories: List<String>,
            val campaign_territories: List<String>,
            val campaign_rackets: List<String>,
            val stash: List<String>,
            val gang_notes: String,
            val gangers: List<YaktribeGanger>
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class YaktribeGanger(
                val ganger_id: String,
                val label_id: String,
                val name: String,
                val type: String,
                val m: String,
                val ws: String,
                val bs: String,
                val s: String,
                val t: String,
                val w: String,
                val i: String,
                val a: String,
                val ld: String,
                val cl: String,
                val wil: String,
                val int: String,
                val cost: String,
                val xp: String,
                val kills: String,
                val advance_count: String,
                val equipment: List<YaktribeEquipment>,
                val skills: List<String>,
                val injuries: List<String>,
                val status: String,
            ) {
                @JsonIgnoreProperties(ignoreUnknown = true)
                data class YaktribeEquipment(
                    val name: String,
                    val qty: Int,
                )
            }
        }
    }
}