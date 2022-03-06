package remotedata

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import kotlin.test.Test
import kotlin.test.fail

class RemoteDataTest {
    @Test
    fun successFactory() {
        assertThat("test".success()).isInstanceOf(RemoteData.Success::class)
    }

    @Test
    fun failureFactory() {
        assertThat("test".failure()).isInstanceOf(RemoteData.Failure::class)
    }

    @Test
    fun map() {
        setOf(RemoteData.NotAsked, RemoteData.Loading, Unit.failure())
            .forEach(::assertMapHasNoEffect)

        val successData = "data"
        val successDataAfterMap = "test data"
        val rd = successData.success()

        val result = rd.map { successDataAfterMap }

        assertThat(result).isEqualTo(successDataAfterMap.success())
    }

    @Test
    fun mapFailure() {
        setOf(RemoteData.NotAsked, RemoteData.Loading, Unit.success())
            .forEach(::assertMapFailureHasNoEffect)

        val error = "data"
        val errorAfterMap = "test data"
        val rd = error.failure()

        val result = rd.mapFailure { errorAfterMap }

        assertThat(result).isEqualTo(errorAfterMap.failure())
    }

    @Test
    fun fold() {
        val mock = "mock"

        assertThat(RemoteData.NotAsked.foldOrFail(ifNotAsked = { mock }))
            .isEqualTo(mock)

        assertThat(RemoteData.Loading.foldOrFail(ifLoading = { mock }))
            .isEqualTo(mock)

        assertThat(Unit.failure().foldOrFail(ifFailure = { mock }))
            .isEqualTo(mock)

        assertThat(Unit.success().foldOrFail(ifSuccess = { mock }))
            .isEqualTo(mock)
    }

    private fun <E, D> assertMapHasNoEffect(rd: RemoteData<E, D>) =
        rd.map { throw AssertionError() }

    private fun <E, D> assertMapFailureHasNoEffect(rd: RemoteData<E, D>) =
        rd.mapFailure { throw AssertionError() }

    private fun <E, D, R> RemoteData<E, D>.foldOrFail(
        ifNotAsked: () -> R = { fail() },
        ifLoading: () -> R = { fail() },
        ifFailure: (RemoteData.Failure<E>) -> R = { fail() },
        ifSuccess: (RemoteData.Success<D>) -> R = { fail() },
    ) =
        fold(
            ifNotAsked = ifNotAsked,
            ifLoading = ifLoading,
            ifFailure = ifFailure,
            ifSuccess = ifSuccess
        )
}
