package trackomunda.model

import kotlinx.serialization.Serializable

@Serializable
data class Ganger(
    val ganger_id: String,
    val label_id: String,
    val name: String,
    val type: String,
    val m: Int,
    val ws: Int,
    val bs: Int,
    val s: Int,
    val t: Int,
    val w: Int,
    val i: Int,
    val a: Int,
    val ld: Int,
    val cl: Int,
    val wil: Int,
    val int: Int,
    val cost: Int,
    val xp: Int,
    val kills: Int,
    val advance_count: Int,
    val equipment: List<Equipment>,
    val skills: List<String>,
    val isReady: Boolean?
)

