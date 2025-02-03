plugins {
    kotlin("jvm") version "2.0.21"
}


repositories {
    mavenCentral()
}

val log4jVersion = "2.17.2"

dependencies {
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
}


kotlin {
    jvmToolchain(11)
}