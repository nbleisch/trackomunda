package trackomunda.model

import kotlinx.serialization.Serializable


@Serializable
data class Gang(
    val gang: GangPayload
) {
}