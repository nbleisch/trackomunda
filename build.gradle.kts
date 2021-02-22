import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

group = "trackomunda"
version = "0.6.7"
description = "Trackomunda"

val serializationVersion = "1.0.0-RC"
val ktorVersion = "1.4.0"
val kotlinVersion = "1.4.20"
val kotlinJsVersion = "pre.129-kotlin-$kotlinVersion"

plugins {
    kotlin("plugin.serialization") version "1.4.20"
    kotlin("js") version "1.4.20"
}

repositories {
    jcenter()
    mavenLocal()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-js-wrappers") }
}

dependencies {

    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation("org.jetbrains:kotlin-styled:1.0.0-pre.110-kotlin-1.4.0")
    implementation(npm("styled-components", "latest"))

    //React, React DOM + Wrappers
    implementation("org.jetbrains:kotlin-react:17.0.1-pre.144-kotlin-1.4.21")
    implementation("org.jetbrains:kotlin-react-dom:17.0.1-pre.144-kotlin-1.4.30")
    implementation(npm("react", "latest"))
    implementation(npm("react-dom", "latest"))

    //Material UI
    implementation("com.ccfraser.muirwik:muirwik-components:0.6.7")
    implementation("org.jetbrains", "kotlin-styled", "5.2.0-$kotlinJsVersion")
    implementation(npm("react-hot-loader", "^4.12.20"))
    implementation(devNpm("webpack-bundle-analyzer", "^3.8.0"))

}


kotlin {
    js(LEGACY) {
        useCommonJs()
        browser {
            binaries.executable()

            commonWebpackConfig {
                cssSupport.enabled = true
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
}