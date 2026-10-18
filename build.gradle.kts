// Archivo de build raíz: declaramos plugins del proyecto para sub-módulos
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // KSP: procesador de símbolo Kotlin, reemplaza a kapt en AGP 9.x
    alias(libs.plugins.ksp) apply false
    // Hilt: inyección de dependencias con Dagger
    alias(libs.plugins.hilt) apply false
}