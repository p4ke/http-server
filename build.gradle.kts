plugins {
    // Installiert die Gradle-Plugins, um hier eine Java-Applikation zu starten
    id("java-library")
    id("application")
}

group = "dev.booky"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Mithilfe dieser Annotations-Bibliotheken können Laufzeitfehler vorgebeugt werden,
    // da mit sogenannten "Nullabilitäts-Annotationen" markiert wird, ob eine
    // Methode/Feld/Parameter/etc. einen "null"-Wert haben darf oder nicht;
    // dies ist in Java nicht durch die Sprache selbst garantiert, sondern muss
    // durch externe Bibliotheken gelöst werden
    compileOnlyApi("org.jspecify:jspecify:1.0.0")
    compileOnlyApi("org.jetbrains:annotations:26.0.2")
}

configure<JavaApplication> {
    // Beschreibt den Klassennamen der Starter-Klasse
    mainClass = "$group.http.ServerMain"
}

configure<JavaPluginExtension> {
    // Stellt ein, welche "Java-Toolchain" von welchem Hersteller benötigt wird,
    // damit die Ausführung dieses Projekts auch überall gleich läuft
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}
