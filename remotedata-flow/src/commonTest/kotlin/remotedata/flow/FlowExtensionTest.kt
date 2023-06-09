package remotedata.flow

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import kotlin.test.Test

class FlowExtensionTest {

    @Test
    fun remotifyUnit() = runBlocking {
        val result = flowOf(Unit)
            .remotify()

        assertThat(result.toList())
            .containsExactly(RemoteData.Loading, Unit.success())
    }

    @Test
    fun remotifyError() = runBlocking {
        val error = Throwable("test error")
        val result = flow<Nothing> { throw error }
            .remotify()

        assertThat(result.toList())
            .containsExactly(RemoteData.Loading, error.failure())
    }

    @Test
    fun mapSuccess() = runBlocking {
        val dataAfterMap = "test data"
        val error = Throwable()

        val subject = flowOf(
            RemoteData.NotAsked,
            RemoteData.Loading,
            error.failure(),
            Unit.success(),
        )

        val result = subject.mapSuccess { dataAfterMap }

        assertThat(result.toList())
            .containsExactly(RemoteData.NotAsked, RemoteData.Loading, error.failure(), dataAfterMap.success())
    }

    @Test
    fun mapFailure() = runBlocking {
        val errorAfterMap = "test error"

        val subject = flowOf(
            RemoteData.NotAsked,
            RemoteData.Loading,
            Unit.failure(),
            Unit.success(),
        )

        val result = subject.mapFailure { errorAfterMap }

        assertThat(result.toList())
            .containsExactly(RemoteData.NotAsked, RemoteData.Loading, errorAfterMap.failure(), Unit.success())
    }
}
