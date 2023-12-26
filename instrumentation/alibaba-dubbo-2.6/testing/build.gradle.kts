plugins {
  id("otel.java-conventions")
}

val alibabaDubboVersion = "2.6.7"

dependencies {
  api(project(":testing-common"))

  api("com.alibaba:dubbo:$alibabaDubboVersion")
  api("com.alibaba:dubbo-config-api:$alibabaDubboVersion")

  implementation("javax.annotation:javax.annotation-api:1.3.2")
  implementation("com.google.guava:guava")

  implementation("org.apache.groovy:groovy")
  implementation("io.opentelemetry:opentelemetry-api")
  implementation("org.spockframework:spock-core")
}
