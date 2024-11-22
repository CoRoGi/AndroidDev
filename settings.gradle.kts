pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
  }
}

// This should match the root dir of the project
rootProject.name = "AndroidDev"

// Any directories with a build.gradle
include("app")
