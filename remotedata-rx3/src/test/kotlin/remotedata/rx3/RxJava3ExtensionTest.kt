package remotedata.rx3

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import kotlin.test.Test

class RxJava3ExtensionTest {

    @Test
    fun remotifyJustUnit() {
        Single.just(Unit)
            .remotify()
            .test()
            .assertValues(
                RemoteData.Loading,
                Unit.success(),
            )
            .assertComplete()
    }

    @Test
    fun remotifyError() {
        val error = Throwable()

        Single.error<Nothing>(error)
            .remotify()
            .test()
            .assertValues(
                RemoteData.Loading,
                error.failure(),
            )
            .assertComplete()
    }

    @Test
    fun remotifyNever() {
        Single.never<Nothing>()
            .remotify()
            .test()
            .assertValues(
                RemoteData.Loading,
            )
            .assertNoErrors()
            .assertNotComplete()
    }

    @Test
    fun mapSuccess() {
        val dataAfterMap = "test"
        val error = Throwable()

        Observable.just(
            RemoteData.NotAsked,
            RemoteData.Loading,
            error.failure(),
            Unit.success(),
        )
            .mapSuccess { dataAfterMap }
            .test()
            .assertValues(
                RemoteData.NotAsked,
                RemoteData.Loading,
                error.failure(),
                dataAfterMap.success(),
            )
    }

    @Test
    fun mapFailure() {
        val errorAfterMap = "test error"
        val rd = Unit.failure()

        Observable.just(
            RemoteData.NotAsked,
            RemoteData.Loading,
            rd,
            Unit.success(),
        )
            .mapFailure { errorAfterMap }
            .test()
            .await()
            .assertValues(
                RemoteData.NotAsked,
                RemoteData.Loading,
                errorAfterMap.failure(),
                Unit.success(),
            )
    }
}
