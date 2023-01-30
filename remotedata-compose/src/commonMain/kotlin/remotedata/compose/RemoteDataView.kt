package remotedata.compose

import androidx.compose.runtime.Composable
import remotedata.RemoteData

/**
 * A template for consuming [RemoteData] in Compose.
 *
 * Animating view changes depending on [remoteData] value isn't currently
 * supported.
 *
 * ```
 * val rd = ...
 * RemoteDataView(
 *     remoteData = rd,
 *     loading = { LoadingView() },
 *     failure = { error -> FailureView(error) },
 *     success = { data -> DataView(data) },
 * )
 * ```
 */
@Composable
fun <Error, Data> RemoteDataView(
    remoteData: RemoteData<Error, Data>,
    notAsked: @Composable () -> Unit,
    loading: @Composable () -> Unit,
    failure: @Composable (Error) -> Unit,
    success: @Composable (Data) -> Unit,
) = when (remoteData) {
    RemoteData.NotAsked -> notAsked()
    RemoteData.Loading -> loading()
    is RemoteData.Failure -> failure(remoteData.error)
    is RemoteData.Success -> success(remoteData.data)
}
