package th.co.octagon.interactive.labktorclient.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import th.co.octagon.interactive.labktorclient.data.remote.dto.ErrResponse
import th.co.octagon.interactive.labktorclient.data.remote.dto.PostRequest
import th.co.octagon.interactive.labktorclient.data.remote.dto.PostResponse

interface PostsService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun createPost(postRequest: PostRequest): Either<PostResponse, ErrResponse>

    companion object {
        fun create(): PostsService {
            return PostsServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}