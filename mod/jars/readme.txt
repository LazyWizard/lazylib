LazyLib.jar: contains the main libraries. This is the only dependency most
modders will need from this library.

LazyLib-Kotlin.jar: contains Kotlin extension methods for LazyLib methods,
as well as the kotlinx.coroutines library. Include this as a dependency if
your mod is written using Kotlin. Otherwise, it can safely be ignored.

The internal folder contains the Kotlin runtime and functionality only used
by LazyLib itself such as tests. Modders shouldn't need to touch these files.