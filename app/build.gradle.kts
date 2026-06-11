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

fun requestedTasksContainFlavor(flavorName: String): Boolean {
    return gradle.startParameter.taskNames.any { taskName ->
        taskName.contains(flavorName, ignoreCase = true)
    }
}

fun kakaoNativeAppKey(
    flavorName: String,
    propertyName: String,
    environmentName: String
): String {
    val key = localProperties
        .getProperty(propertyName)
        .orEmpty()
        .ifBlank { System.getenv(environmentName).orEmpty() }

    if (key.isBlank() && requestedTasksContainFlavor(flavorName)) {
        error(
            "Missing Kakao native app key for $flavorName. " +
                "Set '$propertyName' in local.properties or $environmentName in the environment."
        )
    }

    return key
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
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationId = "com.lyrics.feelin.dev"
            val kakaoKey = kakaoNativeAppKey(
                flavorName = name,
                propertyName = "kakao.native.app.key.dev",
                environmentName = "KAKAO_NATIVE_APP_KEY_DEV"
            )
            buildConfigField("String", "BASE_URL", "\"http://dev.feelinapp.com/\"")
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoKey\"")
            manifestPlaceholders["kakaoNativeAppKey"] = kakaoKey
        }

        create("staging") {
            dimension = "environment"
            applicationId = "com.lyrics.feelin.qa"
            val kakaoKey = kakaoNativeAppKey(
                flavorName = name,
                propertyName = "kakao.native.app.key.staging",
                environmentName = "KAKAO_NATIVE_APP_KEY_STAGING"
            )
            buildConfigField("String", "BASE_URL", "\"http://dev.feelinapp.com/\"")
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoKey\"")
            manifestPlaceholders["kakaoNativeAppKey"] = kakaoKey
        }

        // MARK(@이대근): prod용 사이닝 키를 추후 생성 필요 2026.06.11.
        create("prod") {
            dimension = "environment"
            val kakaoKey = kakaoNativeAppKey(
                flavorName = name,
                propertyName = "kakao.native.app.key.prod",
                environmentName = "KAKAO_NATIVE_APP_KEY_PROD"
            )
            buildConfigField("String", "BASE_URL", "\"http://api.feelinapp.com/\"")
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoKey\"")
            manifestPlaceholders["kakaoNativeAppKey"] = kakaoKey
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
    implementation(libs.androidx.core.splashscreen)
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
