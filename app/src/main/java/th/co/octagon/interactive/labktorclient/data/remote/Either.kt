package th.co.octagon.interactive.labktorclient.data.remote

sealed class Either <out A, out B> {
    data class OnSuccess<out A>(val value: A) : Either<A, Nothing>()
    data class OnFails<out B>(val value: B) : Either<Nothing, B>()
}
