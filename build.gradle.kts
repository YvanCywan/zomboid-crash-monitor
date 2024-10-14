plugins {
    java
    application
    id("com.google.cloud.tools.jib") version "3.3.2"
}

group = "com.hebi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.oshi:oshi-core:6.4.2")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("net.dv8tion:JDA:5.1.2")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("org.mockito:mockito-inline:4.5.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.hebi.ZomboidCrashMonitor"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

application {
    mainClass = "com.hebi.ZomboidCrashMonitor"
}

jib {
    from {
        image = "openjdk:17-jdk-slim"
    }
    to {
        image = "hebirura682:zomboid-crash-monitor"
        tags = setOf("latest", "$version")
        auth {
            username = System.getenv("DOCKER_USERNAME")
            password = System.getenv("DOCKER_PASSWORD")
        }
    }
    container {
        jvmFlags = listOf("-Xms256m", "-Xmx512m")
        ports = listOf("8080")
        mainClass = "com.hebi.ZomboidMonitor"
        environment = mapOf(
            "DISCORD_BOT_TOKEN" to System.getenv("DISCORD_BOT_TOKEN"),
            "DISCORD_CHANNEL_ID" to System.getenv("DISCORD_CHANNEL_ID"),
            "ZOMBOID_SERVICE_NAME" to System.getenv("ZOMBOID_SERVICE_NAME"),
        )
    }
}
