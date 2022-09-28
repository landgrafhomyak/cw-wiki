
plugins {
    kotlin("multiplatform") version "1.7.0"
}

group = "io.github.landgrafhomyak.chatwars"
version = "0"

repositories {
    mavenCentral()
}


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux"    -> linuxX64("native")
        isMingwX64           -> mingwX64("native")
        else                 -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("com.squareup:kotlinpoet:1.12.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
//        val jvmTest by getting
//        val jsMain by getting
//        val jsTest by getting
//        val nativeMain by getting
//        val nativeTest by getting
    }
}
