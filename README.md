# CoroutinesPermission
<p>
<img src="https://img.shields.io/badge/version-1.0.2-blue"/>
<img src="https://img.shields.io/badge/license-Apache License--2-red"/>
</p>

Android library Android Library for easy permission management

## Install

### Gradle

1.  Add the JitPack repository to your project level build.gradle file

```kotlin
allprojects {
    repositories {
            ...
        maven { url 'https://jitpack.io' }
    }
}
```

2.  Add the dependency to your app level build.gradle file

    ```
    implementation 'com.github.wooooooak:CoroutinesPermission:version'
    ```

**Since this library uses coroutine, it is recommended to add the [following library](<[https://developer.android.com/topic/libraries/architecture/coroutines](https://developer.android.com/topic/libraries/architecture/coroutines)>) for convenience.**

```groovy
implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"
```

The latest version is 1.0.2. Checkout [here](https://github.com/wooooooak/DynamicTree/CoroutinesPermission)

## Usage

We will use kotlin DSL for request permission.

```kotlin
yourCoroutineScope.launch {
    val result = CoroutinesPermissionManager.requestPermission(activity) {
        permissionList = listOf(Manifest.permission.CAMERA, or something..)
        Rationale {
            title = "Alert"
            message = "In order to use the app, camera permission is required."
        }
                            ...
    }
    when (result) {
        is PermissionResult.Granted -> {}
        is PermissionResult.Denied -> {}
    }
}
```

You can set the following items.

- permissionLIst (**required**) : List of permission to acquire.

- rationaleTitle (option) : Title for Rationale dialog.

- rationaleMessage (option) : Message for Rationale dialog.

- rationaleConfirmText (option) : Confirm button Text for Rationale dialog.

- rationaleCancelText (option) : Cancel button Text for Rationale dialog.

**Rationale example**

<img src="https://user-images.githubusercontent.com/18481078/82140539-36b4df00-986a-11ea-8a66-ed5aafcfdb4f.png" width="250" height="500">

## Result

Result is an Seald Class.

```kotlin
sealed class PermissionResult {

    object Granted : PermissionResult()

    class Denied(
        val deniedList: List,
        val permanentlyDeniedList: List
    ) : PermissionResult()

    override fun toString(): String {
        return when (this) {
            is Granted -> "All Permission granted"
            is Denied -> "Some Permission denied : \ndenied - $deniedList, \npermanentlyDeniedList-$permanentlyDeniedList"
        }
    }
}
```

Therefore, if the permission acquisition fails, it is possible to know which permission was not obtained and which permission was permanently denied.

## License

```
Copyright 2020 wooooooak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
