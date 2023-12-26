plugins {
  id("otel.javaagent-instrumentation")
}

muzzle {
  pass {
    group.set("com.alibaba")
    module.set("dubbo")
    versions.set("[2.6,2.7.0)")
    assertInverse.set(true)
  }
}

dependencies {
  implementation(project(":instrumentation:alibaba-dubbo-2.6:library-autoconfigure"))

  library("com.alibaba:dubbo:2.6.7")
}

val latestDepTest = findProperty("testLatestDeps") as Boolean

testing {
  suites {
    // using a test suite to ensure that project(":instrumentation:apache-dubbo-2.7:library-autoconfigure")
    // is not available on test runtime class path, otherwise instrumentation from library-autoconfigure
    // module would be used instead of the javaagent instrumentation that we want to test
    val testDubbo by registering(JvmTestSuite::class) {
      dependencies {
        implementation(project(":instrumentation:alibaba-dubbo-2.6:testing"))
        if (latestDepTest) {
          implementation("com.alibaba:dubbo:+")
          implementation("com.alibaba:dubbo-config-api:+")
        } else {
          implementation("com.alibaba:dubbo:2.6.7")
          implementation("com.alibaba:dubbo-config-api:2.6.7")
        }
      }
    }
  }
}

tasks.withType<Test>().configureEach {
  jvmArgs("-XX:+IgnoreUnrecognizedVMOptions")
  // to suppress non-fatal errors on jdk17
  jvmArgs("--add-opens=java.base/java.math=ALL-UNNAMED")
  // required on jdk17
  jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

tasks {
  check {
    dependsOn(testing.suites)
  }
}
