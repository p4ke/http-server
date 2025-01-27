plugins {
    id("java-library")
    id("application")
}

group = "dev.booky"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jspecify:jspecify:1.0.0")
    compileOnlyApi("org.jetbrains:annotations:26.0.2")
}

configure<JavaApplication> {
    mainClass = "$group.http.HttpServerMain"
}

configure<JavaPluginExtension> {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}