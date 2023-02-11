package remotedata.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import remotedata.map
import remotedata.mapFailure


/**
 * Lifts receiver [Flow] values into [RemoteData] context.
 *
 * Using this function produces a stream that begins its emission with
 * [RemoteData.Loading], then emits its values wrapped in [RemoteData.Success],
 * while catching errors into [RemoteData.Failure].
 */
fun <T> Flow<T>.remotify(): Flow<RemoteData<Throwable, T>> = this
    .map<T, RemoteData<Throwable, T>> { it.success() }
    .onStart { emit(RemoteData.Loading) }
    .catch { emit(it.failure()) }

/**
 * Applies given [f] to the underlying [RemoteData.Success] values.
 */
inline fun <E, A, B> Flow<RemoteData<E, A>>.mapSuccess(crossinline f: (A) -> B): Flow<RemoteData<E, B>> = this
    .map { it.map(f) }

/**
 * Applies given [f] to the underlying [RemoteData.Failure] values.
 */
inline fun <D, A, B> Flow<RemoteData<A, D>>.mapFailure(crossinline f: (A) -> B): Flow<RemoteData<B, D>> = this
    .map { it.mapFailure(f) }
