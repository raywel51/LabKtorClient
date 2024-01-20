package th.co.octagon.interactive.labktorclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    @SerialName("message") val message: String,
    @SerialName("status") val status: Boolean,
    @SerialName("token") val token: String
)
