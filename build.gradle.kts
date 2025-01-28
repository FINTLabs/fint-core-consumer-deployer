plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("io.fabric8.java-generator") version "7.0.1"
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "no.fintlabs"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

sourceSets {
	main {
		java {
			srcDirs(layout.buildDirectory.dir("generated/source/kubernetes/main"))
		}
	}
}

repositories {
	mavenLocal()
	maven("https://repo.fintlabs.no/releases")
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("no.fintlabs:fint-core-consumer-state-library:unspecified")
	implementation("no.fintlabs:fint-core-webhook:1.0.0")

	implementation("io.fabric8:kubernetes-client:6.6.2")
	implementation("io.fabric8:generator-annotations:6.6.2")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}


tasks {
	javaGen {
		source = file(layout.projectDirectory.dir("src/main/resources/kubernetes"))
		target = file(layout.buildDirectory.dir("generated/source/kubernetes/main"))
	}
	compileKotlin {
		dependsOn(crd2java)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
