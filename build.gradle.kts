plugins {
    java
    kotlin("jvm") version "1.3.70"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}


group = "net.dankito.examples.coroutines"
version = "1.0.0-SNAPSHOT"


val kotlinVersion = "1.3.70"
val kotlinCoroutinesVersion = "1.3.4"

val junitVersion = "5.5.2"
val assertJVersion = "3.12.2"
val mockitoVersion = "2.22.0"
val logbackVersion = "1.2.3"


repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")


    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")

    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
}