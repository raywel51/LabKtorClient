package th.co.octagon.interactive.labktorclient.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrResponse(
    val message: String,
    val status: Int
)
