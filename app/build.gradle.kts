plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.dag.nexutils"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dag.nexutils"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}
configurations.all {
    exclude(group = "com.github.metaplex-foundation.kborsh", module = "kborsh-android")
    exclude(group = "com.github.metaplex-foundation.kborsh", module = "kborsh-jvm")
    exclude(group = "com.github.metaplex-foundation.SolanaKT", module = "solana")
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.gms:play-services-ads-lite:23.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.12.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    //Hilt
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-work:1.0.0")

    //Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.6.1")

    //Google Add
    implementation("com.google.android.gms:play-services-ads:23.6.0")
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")

    //Document Scanner
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

    //Text Scanner
    implementation("com.google.mlkit:text-recognition:16.0.1")

    //Solana SDK
    implementation("com.solanamobile:web3-solana:0.2.5")
    implementation("com.solanamobile:rpc-core:0.2.7")
    implementation("com.solanamobile:mobile-wallet-adapter-clientlib-ktx:2.0.3")

    //Ktor
    implementation("io.ktor:ktor-server-core:3.0.2")
    implementation("io.ktor:ktor-server-netty:3.0.2")
    implementation("io.ktor:ktor-client-android:2.3.4")
    implementation("io.ktor:ktor-serialization:3.0.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

}