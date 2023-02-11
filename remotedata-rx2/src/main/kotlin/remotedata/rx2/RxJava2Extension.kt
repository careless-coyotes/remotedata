package remotedata.rx2

import io.reactivex.Observable
import io.reactivex.Single
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import remotedata.map
import remotedata.mapFailure


/**
 * Lifts receiver [Single] values to the context of [RemoteData].
 *
 * Using this function produces a stream that begins its emission with
 * [RemoteData.Loading], then emits its values wrapped in [RemoteData.Success],
 * while catching errors into [RemoteData.Failure].
 */
fun <T : Any> Single<T>.remotify(): Observable<RemoteData<Throwable, T>> = this
    .toObservable()
    .map<RemoteData<Throwable, T>> { it.success() }
    .startWith(RemoteData.Loading)
    .onErrorReturn { it.failure() }

/**
 * Applies given [f] to the underlying [RemoteData.Success] values.
 */
inline fun <E, A, B> Observable<RemoteData<E, A>>.mapSuccess(
    crossinline f: (A) -> B,
): Observable<RemoteData<E, B>> = this
    .map { it.map(f) }


/**
 * Applies given [f] to the underlying [RemoteData.Failure] values.
 */
inline fun <D, A, B> Observable<RemoteData<A, D>>.mapFailure(
    crossinline f: (A) -> B,
): Observable<RemoteData<B, D>> = this
    .map { it.mapFailure(f) }
