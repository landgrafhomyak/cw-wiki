plugins {
    `kotlin-dsl`
}

group = "io.github.landgrafhomyak.chatwars"
version = "0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.31")
}