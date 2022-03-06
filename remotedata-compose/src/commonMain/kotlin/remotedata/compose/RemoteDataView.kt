package remotedata.compose

import androidx.compose.runtime.Composable
import remotedata.RemoteData

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
