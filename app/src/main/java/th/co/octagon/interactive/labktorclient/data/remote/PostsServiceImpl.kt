package th.co.octagon.interactive.labktorclient.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import th.co.octagon.interactive.labktorclient.data.remote.dto.ErrResponse
import th.co.octagon.interactive.labktorclient.data.remote.dto.PostRequest
import th.co.octagon.interactive.labktorclient.data.remote.dto.PostResponse

class PostsServiceImpl(
    private val client: HttpClient
) : PostsService {

    override suspend fun getPosts(): List<PostResponse> {
        return try {
            client.get { url(HttpRoutes.POSTS) }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun createPost(postRequest: PostRequest): Either<PostResponse, ErrResponse> {
        return try {
            val response = client.post<PostResponse> {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                body = postRequest
            }
            Either.OnSuccess(response)
        } catch (e: RedirectResponseException) {
            handleErrorResponse(e.response)
        } catch (e: ClientRequestException) {
            handleErrorResponse(e.response)
        } catch (e: ServerResponseException) {
            handleErrorResponse(e.response)
        } catch (e: Exception) {
            Either.OnFails(ErrResponse("Internal Server Error", 0))
        }
    }

    private suspend fun handleErrorResponse(response: HttpResponse): Either<PostResponse, ErrResponse> {
        val errorResponse = ErrResponse(response.readText(), status = response.status.value)
        return Either.OnFails(errorResponse)
    }
}