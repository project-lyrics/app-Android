import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.hilt)
}

// local.properties 읽기
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

// 로컬은 local.properties를 우선 사용하고, CI는 env로 폴백하되 둘 다 없으면 즉시 실패한다.
val kakaoNativeAppKey = localProperties
    .getProperty("kakao.native.app.key.dev")
    .orEmpty()
    .ifBlank { System.getenv("KAKAO_NATIVE_APP_KEY_DEV").orEmpty() }
    .ifBlank {
        error(
            "Missing Kakao native app key. Set 'kakao.native.app.key.dev' in local.properties " +
                "or KAKAO_NATIVE_APP_KEY_DEV in the environment."
        )
    }

android {
    namespace = "com.lyrics.feelin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lyrics.feelin"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Application 클래스에서 사용할 BuildConfig 생성
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")

        // AndroidManifest.xml에서 사용할 placeholder
        manifestPlaceholders["kakaoNativeAppKey"] = kakaoNativeAppKey
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

detekt {
    buildUponDefaultConfig = true // 기본 설정을 기반으로 사용
    allRules = false // 안정적인 규칙만 활성화
    config.setFrom("$projectDir/detekt.yml") // 커스텀 설정 파일 (선택사항)
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.webkit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

//    Network (Retrofit3 + okhttp4)
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging.interceptor)

    // Serializer (kotlinx.serialization)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.kotlinx.serialization)

//    ui image (coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)

//    DI (hilt + dagger)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

//    time (kotlinx.datetime)
    implementation(libs.kotlinx.datetime)

//    DataStore
    implementation(libs.androidx.datastore.preferences)

    // kakao SDK
    implementation(libs.kakao.user) // 카카오 로그인 API 모듈

    // detekt plugins
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.compose)
}
