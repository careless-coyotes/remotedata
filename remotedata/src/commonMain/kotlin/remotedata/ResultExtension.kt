package remotedata

import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

/**
 * Lifts receiver [Result] containing an object of type [T] into the context of
 * [RemoteData].
 *
 * If [Result] is success, the result will be [RemoteData.Success] with [T]
 * value.
 * Otherwise, it returns [RemoteData.Failure] with [Throwable] underneath.
 */
fun <T> Result<T>.remote() =
    fold(
        onSuccess = { it.success() },
        onFailure = { it.failure() },
    )
