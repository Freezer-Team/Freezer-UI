import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

configure<ApplicationExtension> {
    namespace = "nep.timeline.freezerUI"
    compileSdk = 37

    val versionNameString = "3.1"
    val versionCodeInt = 1

    defaultConfig {
        minSdk = 28
        targetSdk = 37
        versionCode = versionCodeInt
        versionName = versionNameString
    }

    val freezerType = "CI"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val dateFormat = SimpleDateFormat("MMddHHmm", Locale.getDefault())
            val buildTime = dateFormat.format(Date())
            buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
            buildConfigField("String", "FREEZER_TYPE", "\"$freezerType\"")
        }
        getByName("debug") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val dateFormat = SimpleDateFormat("MMddHHmm", Locale.getDefault())
            val buildTime = dateFormat.format(Date())
            buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
            buildConfigField("String", "FREEZER_TYPE", "\"$freezerType\"")
        }
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    buildFeatures {
        aidl = true
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    packaging {
        resources {
            excludes += "**"
        }

        buildOutputs.all {
            val dateFormat = SimpleDateFormat("MMddHHmm", Locale.CHINA)
            val buildTime = dateFormat.format(Date())
            (this as BaseVariantOutputImpl).outputFileName = "FreezerUI-${versionNameString}(${versionCodeInt}-${buildTime}).apk"
        }
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.14.0")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
    implementation("commons-io:commons-io:2.22.0")
    implementation("dev.rikka.rikkax.parcelablelist:parcelablelist:2.0.1")
    implementation("androidx.navigation3:navigation3-runtime:1.1.1")
    implementation("androidx.navigation3:navigation3-runtime-android:1.1.1")
    implementation("io.coil-kt.coil3:coil-compose:3.4.0")

    implementation("top.yukonga.miuix.kmp:miuix-blur:0.9.0")
    implementation("top.yukonga.miuix.kmp:miuix-preference:0.9.0")
    implementation("top.yukonga.miuix.kmp:miuix-navigation3-ui:0.9.0")
    implementation("top.yukonga.miuix.kmp:miuix-icons:0.9.0")
    implementation("top.yukonga.miuix.kmp:miuix-ui:0.9.0")

    implementation("io.github.kyant0:backdrop:1.0.6")

    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.37.3")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.kongzue.dialogx:DialogX:0.0.49")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    val libsuVersion = "6.0.0"
    implementation("com.github.topjohnwu.libsu:core:$libsuVersion")
    implementation("com.github.topjohnwu.libsu:service:$libsuVersion")
    implementation("com.github.topjohnwu.libsu:io:$libsuVersion")
}
