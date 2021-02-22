package trackomunda.model

import kotlinx.serialization.Serializable


@Serializable
data class GangPayload(
    val gangers: List<Ganger>,
    //TODO: Make it lowerCamelCase and use JsonProperty for the mapping
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
) {
    companion object {
        const val path = "/gang"
    }
}
