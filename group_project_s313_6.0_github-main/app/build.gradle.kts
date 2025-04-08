plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.group_project_s313"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.group_project_s313"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // ✅ 使用 Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // ✅ 设置 Java Toolchain
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // Google Maps
    implementation(libs.play.services.maps)
    implementation(libs.android.maps.utils)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // OkHttp
    implementation(libs.okhttp)

    // Places API（自动补全用）
    implementation("com.google.android.libraries.places:places:3.3.0")

    // Preference（如果你打算用 SharedPreferences）
    implementation("androidx.preference:preference:1.2.1")

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}