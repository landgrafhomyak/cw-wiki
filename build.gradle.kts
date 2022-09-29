import io.github.landgrafhomyak.chatwars.wiki.preprocessors.HtmlCompile
import io.github.landgrafhomyak.chatwars.wiki.preprocessors.ResourceCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.0"
}

group = "io.github.landgrafhomyak.chatwars"
version = "0"

repositories {
    mavenCentral()
}

val htmlKotlinDir = buildDir.resolve("generated").resolve("html")
val htmlCompile = tasks.create<HtmlCompile>("htmlCompile") {
    destDir(htmlKotlinDir)
    val sourceRoot = rootDir.resolve("src").resolve("commonMain").resolve("html")
    val pkg = "io.github.landgrafhomyak.chatwars.wiki.templates"
    html(pkg, "Root", sourceRoot.resolve("root.html"))
}

val sqlKotlinDir = buildDir.resolve("generated").resolve("sql")
val sqlCompile = tasks.create<ResourceCompile>("sqlCompile") {
    destDir(sqlKotlinDir)
    pkg("io.github.landgrafhomyak.chatwars.wiki")
    name("SQLiteRequests")
    val sourceRoot = rootDir.resolve("src").resolve("commonMain").resolve("sql")
    text("_createTables", sourceRoot.resolve("create_tables.sql"))
    text("getUserBySession", sourceRoot.resolve("get_user_by_session.sql"))
}

val resKotlinDir = buildDir.resolve("generated").resolve("res")
val resCompile = tasks.create<ResourceCompile>("resCompile") {
    destDir(resKotlinDir)
    pkg("io.github.landgrafhomyak.chatwars.wiki")
    name("Resources")
    val sourceRoot = rootDir.resolve("src").resolve("commonMain").resolve("resources")
    text("cssCommon", sourceRoot.resolve("common.css"))
}

tasks.withType<KotlinCompile>().all {
    dependsOn(htmlCompile, sqlCompile, resCompile)
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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
            }
            this.kotlin.srcDir(htmlKotlinDir)
            this.kotlin.srcDir(sqlKotlinDir)
            this.kotlin.srcDir(resKotlinDir)
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.xerial:sqlite-jdbc:3.8.11.2")
            }
        }
//        val jvmTest by getting
//        val jsMain by getting
//        val jsTest by getting
//        val nativeMain by getting
//        val nativeTest by getting
    }
}
