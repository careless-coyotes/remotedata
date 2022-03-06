package remotedata.rx3

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.testng.annotations.Test

import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

class RxJava3ExtensionTest {

    @Test
    fun remotelifyJustUnit() {
        Single.just(Unit)
            .remotelify()
            .test()
            .assertValues(
                RemoteData.Loading,
                Unit.success(),
            )
            .assertComplete()
    }

    @Test
    fun remotelifyError() {
        val error = Throwable()

        Single.error<Nothing>(error)
            .remotelify()
            .test()
            .assertValues(
                RemoteData.Loading,
                error.failure(),
            )
            .assertComplete()
    }

    @Test
    fun remotelifyNever() {
        Single.never<Nothing>()
            .remotelify()
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
