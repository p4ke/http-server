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
}

configure<JavaApplication> {
    mainClass = "$group.http.HttpServer"
}