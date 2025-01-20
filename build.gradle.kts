plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "calmsen"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val springShellVersion = "3.4.0"

dependencies {
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    implementation("org.mapstruct:mapstruct:1.6.2")
    implementation("org.projectlombok:lombok:1.18.34")
    implementation("commons-cli:commons-cli:1.3.1")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.telegram:telegrambots:6.9.7.1")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")


    implementation("org.springframework.shell:spring-shell-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.shell:spring-shell-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.shell:spring-shell-dependencies:$springShellVersion")
    }
}

tasks.test {
    useJUnitPlatform()
}