package remotedata.rx2

import io.reactivex.Observable
import io.reactivex.Single
import org.testng.annotations.Test
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

class RxJava2ExtensionTest {

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
            .assertNotTerminated()
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
            .assertComplete()
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
            .assertValues(
                RemoteData.NotAsked,
                RemoteData.Loading,
                errorAfterMap.failure(),
                Unit.success(),
            )
            .assertComplete()
    }
}
