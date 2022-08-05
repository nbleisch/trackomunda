package com.trackomunda.yaktribe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.trackomunda.model.Gang
import com.trackomunda.model.Ganger

@JsonIgnoreProperties(ignoreUnknown = true)
data class YaktribeGangContainer(
    val gang: YaktribeGang
) {
    fun toGang() =
        gang.let { yaktribeGang ->
            Gang(
                name = yaktribeGang.gang_name,
                ganger = yaktribeGang.gangers.map { yakTribeGanger ->
                    Ganger(name = yakTribeGanger.name)
                }
            )
        }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class YaktribeGang(
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
        val campaign_territories:  List<String>,
        val campaign_rackets:  List<String>,
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

