package remotedata.compose

import androidx.compose.runtime.Composable
import io.mockk.mockk
import io.mockk.verify
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import kotlin.test.fail

interface ComposeTestContext {
    fun setContent(composable: @Composable () -> Unit)
}

fun ComposeTestContext.testNotAsked() {
    val rd = RemoteData.NotAsked
    val notAsked: () -> Unit = mockk(relaxed = true)

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { notAsked() },
            loading = { fail() },
            failure = { fail() },
            success = { fail() },
        )
    }

    verify(exactly = 1) {
        notAsked()
    }
}

fun ComposeTestContext.testLoading() {
    val rd = RemoteData.Loading
    val loading: () -> Unit = mockk(relaxed = true)

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { fail() },
            loading = { loading() },
            failure = { fail() },
            success = { fail() },
        )
    }

    verify(exactly = 1) {
        loading()
    }
}

fun ComposeTestContext.testFailure() {
    val rd = "test error".failure()
    val failure: () -> Unit = mockk(relaxed = true)

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { fail() },
            loading = { fail() },
            failure = { failure() },
            success = { fail() },
        )
    }

    verify(exactly = 1) {
        failure()
    }
}

fun ComposeTestContext.testSuccess() {
    val rd = "test data".success()
    val success: () -> Unit = mockk(relaxed = true)

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { fail() },
            loading = { fail() },
            failure = { fail() },
            success = { success() },
        )
    }

    verify(exactly = 1) {
        success()
    }
}
