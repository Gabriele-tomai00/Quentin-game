plugins {
    id("java")
    id("application")
    id("com.diffplug.spotless") version "6.17.0"
}

application {
    mainClass.set("quentin.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

// Configurazione di Spotless
spotless {
    java {
        target("src/**/*.java")  // Seleziona i file Java da formattare
        googleJavaFormat()  // Usa il formato di Google Java Style (tabulazione, spazi, ecc.)
    }
}
