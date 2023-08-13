import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  // id("org.graalvm.buildtools.native") version "0.9.23"
  id("org.springframework.boot") version "2.7.14"
  id("io.spring.dependency-management") version "1.0.15.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  // kotlin("kapt") version "1.6.21"
}

group = "cn.xdwanj"
version = "v1.0.2"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  maven("https://oss.sonatype.org/content/repositories/snapshots")
  mavenCentral()
  google()
}

dependencies {
  val hutoolVersion = "6.0.0-M5"
  val mybatisPlusVersion = "3.5.3.1"
  // val mybatisFlexVersion = "1.5.3"
  // val retrofitVersion = "2.9.0"
  val picocliVersion = "4.7.4"

  // util
  implementation("org.dromara.hutool:hutool-all:$hutoolVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.jsoup:jsoup:1.15.4")

  // cli
  implementation("info.picocli:picocli-spring-boot-starter:$picocliVersion")
  // kapt("info.picocli:picocli-codegen:$picocliVersion")
  annotationProcessor("info.picocli:picocli-codegen:$picocliVersion")

  // springboot
  // implementation(platform("org.springframework.boot:spring-boot-dependencies:$springbootVersion"))
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-aop")
  implementation("org.springframework.boot:spring-boot-starter-validation")

  // kotlin
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // data
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.xerial:sqlite-jdbc:3.42.0.0")
  implementation("org.xerial:sqlite-jdbc")
  // implementation("com.mybatis-flex:mybatis-flex-spring-boot-starter:$mybatisFlexVersion")
  // annotationProcessor("com.mybatis-flex:mybatis-flex-processor:$mybatisFlexVersion")
  implementation("com.baomidou:mybatis-plus-boot-starter:$mybatisPlusVersion")

  // http
  // implementation("com.github.lianjiatech:retrofit-spring-boot-starter:3.0.1")

  // test
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  // testImplementation("junit:junit:4.13.2")

}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "1.8"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

// kapt {
//   arguments {
//     arg("project", "${project.group}/${project.name}")
//   }
// }