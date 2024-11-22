plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.org.jetbrains.kotlin.android)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.dagger.hilt.plugin)
  id("kotlin-kapt")
}

android {
  buildFeatures {
    compose = true
  }
  compileSdk = libs.versions.compileSdk.get().toInt()
  // Should match package structure of project
  namespace = "playground.main"

/*
* KGP will throw errors if Kotlin's jvmTarget is different from the targetCompatibility.
* Whether or not the incompatibility fails the build can be configured by adding this to the gradle.properties for
* the project:
* kotlin.jvm.target.validation.mode=warning
* Alternatively, the jvmToolchain can be set to a version in a separate kotlin block instead of specifying
* compatibility in compileOptions & kotlinOptions to ensure compatibility
*/

// Must match jvmTarget under Kotlin options. Will default to 1.8 for java tasks triggered by AGP
compileOptions {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

// Must match compatibility versions in compileOptions. Defaults to whatever is set in AS System Preferences under
// Gradle JDK
kotlinOptions {
  jvmTarget = "21"
}

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    // For Google Playstore publishing
    // Should match namespace
    // applicationId = "playground.main"
    // versionCode = 1
    // versionName = "1.0"
    // vectorDrawables.useSupportLibrary = true
    // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

// For targeting the JVM and setting compatibilities if not done in android block
// kotlin {
//   jvmToolchain(21)
// }

dependencies {
  // Compose BOM
  implementation(platform(libs.compose.bom))
  // Compose Runtime
  implementation(libs.compose.runtime)
  // Activity Ktx
  implementation(libs.activity.ktx)
  // Compose Integration with Activities
  implementation(libs.activity.compose)
  // Compose Integration with ViewModels
  implementation(libs.viewmodel.compose)
  // Material Lib
  implementation(libs.material3)
  // Compose Preview
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.ui.tooling.preview)
  // Dagger Hilt
  implementation(libs.dagger.hilt.android)
  implementation(libs.graphics.shapes.android)
  implementation(libs.navigation.common.ktx)
  kapt(libs.dagger.hilt.compiler)
  implementation(libs.hilt.navigation)
  // Lifecycle
  implementation(libs.lifecycle.compose)
  // Hexgrid
  implementation(libs.hexgrid)
  // Testing
  androidTestImplementation(platform(libs.compose.bom))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
