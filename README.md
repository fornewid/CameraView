# CameraView
CameraX View + CameraX Analyzer

<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href='https://developer.android.com'><img height="20px" src='http://img.shields.io/badge/platform-android-green.svg'/></a>
[![](https://jitpack.io/v/fornewid/CameraView.svg)](https://jitpack.io/#fornewid/CameraView)

## Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.fornewid:CameraView:1.0.0-alpha10'
}
```

## Features
- All features in [AndroidX Camera-View](https://developer.android.com/jetpack/androidx/releases/camera#camera-extensions_and_camera-view_version_100_2).
- And [image analysis](https://developer.android.com/training/camerax/analyze). 

## Usage
There is a [sample](https://github.com/fornewid/CameraView/tree/master/sample) provided which shows how to use the library in a simple way:
```xml
<androidx.camera.view.CameraView
    android:id="@+id/cameraView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:captureMode="image" />
```
```kt
val cameraView: CameraView = findViewById(R.id.cameraView)
photoView.setImageResource(R.drawable.image);
cameraView.bindToLifecycle(this)
cameraView.setAnalyzer { proxy ->
    ...
}
```
