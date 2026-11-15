plugins {
    alias(libs.plugins.android.application)
    // KSP: processa as anotações do Room (@Entity, @Dao, @Database) e gera o código do banco
    alias(libs.plugins.ksp)
}

android {
    namespace = "br.edu.ifsp.pratodavila"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "br.edu.ifsp.pratodavila"
        // Versão mínima usada nas aulas (Android 13 - API 33)
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Aula "View Binding": gera uma classe de binding para cada layout XML
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // RF01 / RNF01: listagem com RecyclerView
    implementation(libs.androidx.recyclerview)

    // RF04: persistência local com Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
