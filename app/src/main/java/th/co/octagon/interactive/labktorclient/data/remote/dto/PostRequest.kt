package th.co.octagon.interactive.labktorclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)