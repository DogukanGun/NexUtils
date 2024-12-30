// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply true
}

buildscript{
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.52")
    }
}