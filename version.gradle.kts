val stableVersion = "1.32.0.1"
val alphaVersion = "1.32.0.1-alpha"

allprojects {
  if (findProperty("otel.stable") != "true") {
    version = alphaVersion
  } else {
    version = stableVersion
  }
}
