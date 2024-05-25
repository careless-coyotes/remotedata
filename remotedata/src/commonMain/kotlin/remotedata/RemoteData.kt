package remotedata

import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

/**
 * [RemoteData] class represents a model of a simple remote call resulting in
 * receiving data of type [Data], or fails with an error of type [Error].
 *
 * It can be one of the four concrete types: [NotAsked] when a call haven't
 * been started yet, [Loading] when it's in progress, [Failure] containing
 * error that occurred, and [Success] with the expected data.
 *
 * You can use [success] and [failure] to instantiate objects.
 * ```
 * val failure = "Error".failure()
 * val success = "Data".success()
 * ```
 */
sealed interface RemoteData<out Error, out Data> {
    data object NotAsked : RemoteData<Nothing, Nothing>
    data object Loading : RemoteData<Nothing, Nothing>
    data class Failure<T>(val error: T) : RemoteData<T, Nothing>
    data class Success<T>(val data: T) : RemoteData<Nothing, T>

    companion object {
        fun <T> T.failure(): RemoteData<T, Nothing> =
            Failure(this)

        fun <T> T.success(): RemoteData<Nothing, T> =
            Success(this)
    }
}

/**
 * Invokes [loading] parameter with a boolean indicating if the receiver is
 * [RemoteData.Loading],
 * [error] with an underlying error of type [E] if the receiver is
 * [RemoteData.Failure],
 * and [data] with an underlying value of type [D] if the receiver is
 * [RemoteData.Success].
 */
inline fun <E, D> RemoteData<E, D>.bind(
    crossinline loading: (Boolean) -> Unit,
    crossinline error: (E?) -> Unit,
    crossinline data: (D?) -> Unit,
) {
    loading(this is RemoteData.Loading)
    error((this as? RemoteData.Failure)?.error)
    data((this as? RemoteData.Success)?.data)
}

/**
 * Returns the result of [ifNotAsked], [ifLoading], [ifFailure], [ifSuccess]
 * depending on the corresponding type of the receiver [RemoteData].
 */
inline fun <E, D, R> RemoteData<E, D>.fold(
    ifNotAsked: () -> R,
    ifLoading: () -> R,
    ifFailure: (E) -> R,
    ifSuccess: (D) -> R,
) = when (this) {
    RemoteData.NotAsked -> ifNotAsked()
    RemoteData.Loading -> ifLoading()
    is RemoteData.Failure -> ifFailure(error)
    is RemoteData.Success -> ifSuccess(data)
}

/**
 * Returns [RemoteData.Success] containing the result of invoking [f] on the
 * underlying value if the receiver is [RemoteData.Success], or the receiver
 * otherwise.
 */
inline fun <E, A, B> RemoteData<E, A>.map(crossinline f: (A) -> B): RemoteData<E, B> =
    when (this) {
        RemoteData.NotAsked -> RemoteData.NotAsked
        RemoteData.Loading -> RemoteData.Loading
        is RemoteData.Failure -> this
        is RemoteData.Success -> f(data).success()
    }

/**
 * Returns [RemoteData.Failure] containing the result of invoking [f] on the
 * underlying value if the receiver is [RemoteData.Failure], or the receiver
 * otherwise.
 */
inline fun <D, A, B> RemoteData<A, D>.mapFailure(crossinline f: (A) -> B): RemoteData<B, D> =
    when (this) {
        RemoteData.NotAsked -> RemoteData.NotAsked
        RemoteData.Loading -> RemoteData.Loading
        is RemoteData.Failure -> f(error).failure()
        is RemoteData.Success -> this
    }
