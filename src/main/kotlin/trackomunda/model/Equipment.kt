package trackomunda.model

import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    val name : String,
    val qty : Int,
)