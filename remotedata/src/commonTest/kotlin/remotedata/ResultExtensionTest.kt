package remotedata

import assertk.assertThat
import assertk.assertions.isEqualTo
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success
import kotlin.test.Test

class ResultExtensionTest {
    @Test
    fun success() {
        assertThat(Result.success(Unit).remote())
            .isEqualTo(Unit.success())
    }

    @Test
    fun failure() {
        val exception = Throwable("mock exception")
        assertThat(Result.failure<Nothing>(exception).remote())
            .isEqualTo(exception.failure())
    }
}
