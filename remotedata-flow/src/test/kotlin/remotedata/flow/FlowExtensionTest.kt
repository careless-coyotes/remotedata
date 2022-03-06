package remotedata.flow

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

class FlowExtensionTest {

    @Test
    fun remotelifyUnit() = runBlocking {
        val result = flowOf(Unit)
            .remotelify()

        assertThat(result.toList())
            .containsExactly(RemoteData.Loading, Unit.success())
            .inOrder()
    }

    @Test
    fun remotelifyError() = runBlocking {
        val error = Throwable("test error")
        val result = flow<Nothing> { throw error }
            .remotelify()

        assertThat(result.toList())
            .containsExactly(RemoteData.Loading, error.failure())
            .inOrder()
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
            .inOrder()
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
            .inOrder()
    }
}
