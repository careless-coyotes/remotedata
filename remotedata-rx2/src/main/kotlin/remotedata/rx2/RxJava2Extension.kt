package remotedata.rx2

import io.reactivex.Observable
import io.reactivex.Single
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import remotedata.map
import remotedata.mapFailure


/**
 * Lifts a receiver [Single] to the context of [RemoteData].
 */
fun <T : Any> Single<T>.remotelify(): Observable<RemoteData<Throwable, T>> = this
    .toObservable()
    .map<RemoteData<Throwable, T>> { it.success() }
    .startWith(RemoteData.Loading)
    .onErrorReturn { it.failure() }


inline fun <E, A, B> Observable<RemoteData<E, A>>.mapSuccess(
    crossinline f: (A) -> B,
): Observable<RemoteData<E, B>> = this
    .map { it.map(f) }


inline fun <D, A, B> Observable<RemoteData<A, D>>.mapFailure(
    crossinline f: (A) -> B,
): Observable<RemoteData<B, D>> = this
    .map { it.mapFailure(f) }
