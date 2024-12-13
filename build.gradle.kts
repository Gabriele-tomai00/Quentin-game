plugins {
    id("java")
    id("application")
    id("com.diffplug.spotless") version "6.17.0"
    id("org.beryx.jlink") version "2.25.0"
}

application {
    mainClass.set("quentin.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
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

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat()
    }
}