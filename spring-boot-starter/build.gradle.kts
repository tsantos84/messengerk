dependencies {
    api(project(":core"))
    implementation ("org.springframework.boot:spring-boot-starter:2.6.2")
    testImplementation ("org.springframework.boot:spring-boot-starter-test:2.6.2") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}