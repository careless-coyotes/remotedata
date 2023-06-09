package remotedata.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import kotlin.test.fail

interface ComposeTestContext {
    fun setContent(composable: @Composable () -> Unit)
    fun assertExistsSingle(testTag: String)
}

fun ComposeTestContext.testNotAsked() {
    val rd = RemoteData.NotAsked
    val tag = "not asked"

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { Box(modifier = Modifier.testTag(tag)) },
            loading = { fail() },
            failure = { fail() },
            success = { fail() },
        )
    }

    assertExistsSingle(testTag = tag)
}

fun ComposeTestContext.testLoading() {
    val rd = RemoteData.Loading
    val tag = "loading"

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { fail() },
            loading = { Box(modifier = Modifier.testTag(tag)) },
            failure = { fail() },
            success = { fail() },
        )
    }

    assertExistsSingle(testTag = tag)
}

fun ComposeTestContext.testFailure() {
    val rd = "test error".failure()
    val tag = "failure"

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { fail() },
            loading = { fail() },
            failure = { Box(modifier = Modifier.testTag(tag = tag)) },
            success = { fail() },
        )
    }

    assertExistsSingle(testTag = tag)
}

fun ComposeTestContext.testSuccess() {
    val rd = "test data".success()
    val tag = "success"

    setContent {
        RemoteDataView(
            remoteData = rd,
            notAsked = { fail() },
            loading = { fail() },
            failure = { fail() },
            success = { Box(modifier = Modifier.testTag(tag = tag)) },
        )
    }

    assertExistsSingle(testTag = tag)
}
