plugins {
	java
	id("org.springframework.boot") version "3.4.1-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.audio"
version = "1.0.0-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	implementation("net.bramp.ffmpeg:ffmpeg:0.6.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	enabled = true
	archiveFileName.set("speakbuddy-1.0.0.jar")
	destinationDirectory.set(layout.buildDirectory.dir("target"))
}

tasks.named<Jar>("jar") {
	enabled = false
}

tasks.register<Copy>("publishJar") {
	dependsOn("build")
	from("$buildDir/target")
	include("*.jar")
	into("$projectDir/publish")
}

