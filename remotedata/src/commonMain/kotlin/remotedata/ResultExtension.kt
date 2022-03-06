package remotedata

import remotedata.RemoteData.Companion.failure
import remotedata.RemoteData.Companion.success

fun <T> Result<T>.remote() =
    fold(
        onSuccess = { it.success() },
        onFailure = { it.failure() },
    )
