package remotedata.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

internal fun ComposeContentTestRule.testContext() =
    object : ComposeTestContext {
        override fun setContent(composable: @Composable () -> Unit) {
            this@testContext.setContent(composable = composable)
        }
    }

class RemoteDataViewDesktopTest {

    @get:Rule
    val rule = createComposeRule()
    private val context by lazy { rule.testContext() }

    @Test
    fun notAsked() =
        context.testNotAsked()

    @Test
    fun loading() =
        context.testLoading()

    @Test
    fun failure() =
        context.testFailure()

    @Test
    fun success() =
        context.testSuccess()
}
