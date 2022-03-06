package remotedata

import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success


sealed interface RemoteData<out Error, out Data> {
    object NotAsked : RemoteData<Nothing, Nothing>
    object Loading : RemoteData<Nothing, Nothing>
    data class Failure<T>(val error: T) : RemoteData<T, Nothing>
    data class Success<T>(val data: T) : RemoteData<Nothing, T>

    companion object {
        fun <T> T.failure(): RemoteData<T, Nothing> =
            Failure(this)

        fun <T> T.success(): RemoteData<Nothing, T> =
            Success(this)
    }
}


inline fun <E, D> RemoteData<E, D>.bind(
    crossinline loading: (Boolean) -> Unit,
    crossinline error: (E?) -> Unit,
    crossinline data: (D?) -> Unit,
) {
    loading(this is RemoteData.Loading)
    error((this as? RemoteData.Failure)?.error)
    data((this as? RemoteData.Success)?.data)
}


inline fun <E, D, R> RemoteData<E, D>.fold(
    ifNotAsked: () -> R,
    ifLoading: () -> R,
    ifFailure: (RemoteData.Failure<E>) -> R,
    ifSuccess: (RemoteData.Success<D>) -> R,
) = when (this) {
    RemoteData.NotAsked -> ifNotAsked()
    RemoteData.Loading -> ifLoading()
    is RemoteData.Failure -> ifFailure(this)
    is RemoteData.Success -> ifSuccess(this)
}


inline fun <E, A, B> RemoteData<E, A>.map(crossinline f: (A) -> B): RemoteData<E, B> =
    when (this) {
        RemoteData.NotAsked -> RemoteData.NotAsked
        RemoteData.Loading -> RemoteData.Loading
        is RemoteData.Failure -> this
        is RemoteData.Success -> f(data).success()
    }

inline fun <D, A, B> RemoteData<A, D>.mapFailure(crossinline f: (A) -> B): RemoteData<B, D> =
    when (this) {
        RemoteData.NotAsked -> RemoteData.NotAsked
        RemoteData.Loading -> RemoteData.Loading
        is RemoteData.Failure -> f(error).failure()
        is RemoteData.Success -> this
    }
