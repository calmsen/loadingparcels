plugins {
    id("java")
}

group = "calmsen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    implementation("org.mapstruct:mapstruct:1.6.2")
    implementation("org.projectlombok:lombok:1.18.34")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    //testImplementation("org.mockito:mockito-inline:4.11.0")
    //testImplementation("org.mockito:mockito-junit-jupiter:4.11.0")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
//    jvmArgs("--illegal-access=permit")
}