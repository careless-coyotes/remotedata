RemoteData library for Kotlin
=============================

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/careless-coyotes/remotedata/gradle-check.yml?logo=github)
![Codecov](https://img.shields.io/codecov/c/github/careless-coyotes/remotedata?logo=codecov)
![Maven Central](https://img.shields.io/maven-central/v/com.carelesscoyotes.remotedata/remotedata-bom?logo=maven)

Handling remote requests in applications with UI is about displaying progress
indicators, handling errors and populating data.
This library provides you with a model of such request that help you to avoid
common errors and streamlining state consumption.

Ported from Elm.
See [the original blog post][elm-remotedata] for the motivation behind it.


RemoteData model
----------

The structure in Kotlin is pretty straightforward. It's a `sealed interface`
hierarchy parametrized by `Error` and `Data` type variables and consists of four
states:

```kotlin
sealed interface RemoteData<out Error, out Data> {
    object NotAsked : RemoteData<Nothing, Nothing>
    object Loading : RemoteData<Nothing, Nothing>
    data class Failure<T>(val error: T) : RemoteData<T, Nothing>
    data class Success<T>(val data: T) : RemoteData<Nothing, T>
}
```

You can use factory extension functions to instantiate `Success` and `Failure`
objects.

```kotlin
val success: RemoteData<Nothing, String> = "stuff".success()
val failure: RemoteData<String, Nothing> = "error".failure()
```

Modify `RemoteData` content with mapping functions:

```kotlin
"RemoteData"
    .success()                      // Success("RemoteData")
    .map { "Hello $it!" }           // Success("Hello RemoteData!")
```
```kotlin
Throwable("What a Terrible Failure")
    .failure()                      // Failure(Throwable("What a Terrible Failure"))
    .mapFailure { it.description }  // Failure("What a Terrible Failure")
```

Folding `RemoteData` is also possible:

```kotlin
fun toString(rd: RemoteData<Throwable, List<String>>) {
    rd.fold(
        ifNotAsked = { "not asked" },
        ifLoading = { "loading" },
        ifFailure = { it.description },
        ifSuccess = { it.joinToString() },
    )
}
```


Rendering UI
------------

You can utilize `bind()` extension to render your UI.
It accepts setters for your UI as parameters.

```kotlin
fun updateUi(data: RemoteData<Throwable, Stuff>) {
    data.bind(
        loading = { loadingIndicator.visible = it },
        error = { errorView.error = it },
        data = { stuffView.stuff = it },
    )
}
```


### Jetpack Compose

There is a `RemoteDataView()` composable for easy consuming of `RemoteData` from
Compose. It invokes corresponding parameters depending on its concrete type
(similarly to `fold()` method).

```kotlin
@Composable
fun RemoteStuffView(rd: RemoteData<Throwable, Stuff>) {
    RemoteDataView(
        remoteData = rd,
        notAsked = { Text("Press the button to load stuff.") },
        loading = { ProgressIndicator() },
        failure = { error -> FailureView(error) },
        success = { stuff -> StuffView(stuff) },
    )
}
```


### Android Layout

This is more of an example than a useful extension, because in real world you'll
have different types for your views.

In order to use `RemoteData<Throwable, *>` with it, you should first use
`mapFailure()` to have a `RemoteData<String, *>`.

```kotlin
fun updateUi(rd: RemoteData<String, Stuff>) {
    rd.bind(
        progressIndicator = progressBar,
        errorTextView = errorTextView,
        bindData = { stuff -> updateStuffView(stuff) },
    )
}
```


Streams support
---------------

Often times you have a ready to use API returning a stream – be it a RxJava
Single, or Kotlin Flow. Consuming such stream isn't trivial. Most of the time
you want to set loading state on stream start and update it when it terminates.
You should also handle errors and data when request finishes successfully.

```kotlin
api.requestStuff()
    .remotify()
    .collect { data: RemoteData<Throwable, Stuff> ->
        updateUi(data)
    }

```

There are also `mapSuccess()` and `mapFailure()` functions.
Kotlin Flow, RxJava2, and RxJava3 are supported in `remotedata-flow`,
`remotedata-rx2`, and `remotedata-rx3` artifacts respectively.


Usage
-----

This project uses [Bill of Materials](https://docs.gradle.org/current/userguide/platforms.html#sub:bom_import).
You should specify a BOM dependency with a version, and then add required
artifacts omitting version.

```kotlin
// Specify BOM version
implementation(platform("com.carelesscoyotes.remotedata:remotedata-bom:$version"))

// Core artifact
implementation("com.carelesscoyotes.remotedata:remotedata")

// Compose RemoteDataView() support
implementation("com.carelesscoyotes.remotedata:remotedata-compose")

// Streams support
implementation("com.carelesscoyotes.remotedata:remotedata-flow")
implementation("com.carelesscoyotes.remotedata:remotedata-rx2")
implementation("com.carelesscoyotes.remotedata:remotedata-rx3")
```

You can also use version catalog adding the following to the `libs.versions.toml` file:

```toml
remotedata-bom = "com.carelesscoyotes.remotedata:remotedata-bom:0.5"
remotedata = { module = "com.carelesscoyotes.remotedata:remotedata" }
remotedata-compose = { module = "com.carelesscoyotes.remotedata:remotedata-compose" }
remotedata-flow = { module = "com.carelesscoyotes.remotedata:remotedata-flow" }
remotedata-rx2 = { module = "com.carelesscoyotes.remotedata:remotedata-rx2" }
remotedata-rx3 = { module = "com.carelesscoyotes.remotedata:remotedata-rx3" }
```

And then adding dependencies in `build.gradle`:

```kotlin
implementation(platform(libs.remotedata.bom))
implementation(libs.remotedata)
implementation(libs.remotedata.flow)
// ...
```

[elm-remotedata]: http://blog.jenkster.com/2016/06/how-elm-slays-a-ui-antipattern.html
