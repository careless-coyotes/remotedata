package remotedata.androidlayout

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import remotedata.RemoteData
import remotedata.bind

/**
 * Binds receiver [RemoteData] to specified [ProgressBar], error [TextView] and
 * invokes [bindData] to populate the success view.
 */
fun <A> RemoteData<String, A>.bind(
    loadingIndicator: ProgressBar,
    errorTextView: TextView,
    bindData: (A?) -> Unit,
) =
    bind(
        loading = { loading ->
            loadingIndicator.visibleOrGone(visible = loading)
        },
        error = { maybeError ->
            errorTextView.visibleOrGone(visible = maybeError != null)
            errorTextView.text = maybeError
        },
        data = bindData,
    )


private fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
