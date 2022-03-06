package remotedata.androidlayout

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import io.mockk.mockk
import io.mockk.verify
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import remotedata.RemoteData
import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

class RemoteDataExtensionTest {
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var bindData: (Any?) -> Unit

    @BeforeMethod
    fun setUp() {
        loadingIndicator = mockk(relaxed = true)
        errorTextView = mockk(relaxed = true)
        bindData = mockk(relaxed = true)
    }

    @Test
    fun bindNotAsked() {
        val rd = RemoteData.NotAsked

        rd.bind(
            loadingIndicator = loadingIndicator,
            errorTextView = errorTextView,
            bindData = bindData,
        )

        verify(exactly = 1) {
            loadingIndicator.visibility = View.GONE
            errorTextView.visibility = View.GONE
            bindData(null)
        }
    }

    @Test
    fun bindLoading() {
        val rd = RemoteData.Loading

        rd.bind(
            loadingIndicator = loadingIndicator,
            errorTextView = errorTextView,
            bindData = bindData,
        )

        verify(exactly = 1) {
            loadingIndicator.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            bindData(null)
        }
    }

    @Test
    fun bindFailure() {
        val error = "test error"
        val rd: RemoteData<String, Nothing> = error.failure()

        rd.bind(
            loadingIndicator = loadingIndicator,
            errorTextView = errorTextView,
            bindData = bindData,
        )

        verify(exactly = 1) {
            loadingIndicator.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = error
            bindData(null)
        }
    }

    @Test
    fun bindSuccess() {
        val rd: RemoteData<String, Unit> = Unit.success()

        rd.bind(
            loadingIndicator = loadingIndicator,
            errorTextView = errorTextView,
            bindData = bindData,
        )

        verify(exactly = 1) {
            loadingIndicator.visibility = View.GONE
            errorTextView.visibility = View.GONE
            bindData(Unit)
        }
    }
}
