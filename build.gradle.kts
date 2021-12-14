plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
}

group = "cz.tefek"
version = "350.10"

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.withType<Wrapper> {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}

sourceSets {
    java {
        main {
            java.srcDirs(
                "src/main/java",
                "src/uss/java",
                "src/framework/java",
                "src/framework-discord/java",
                "src/data/java",
                "src/gamelogic/java",
                "src/util/java")

            resources.srcDirs(
                "src/main/resources",
                "src/uss/resources",
                "src/framework/resources",
                "src/framework-discord/resources",
                "src/data/resources",
                "src/gamelogic/resources",
                "src/util/resources")
        }

        create("commands") {
            java.srcDirs(
                "assets/commands/administrative/java",
                "assets/commands/competitive/java",
                "assets/commands/economy/java",
                "assets/commands/fun/java",
                "assets/commands/gambling/java",
                "assets/commands/general/java",
                "assets/commands/income/java",
                "assets/commands/items/java",
                "assets/commands/superuser/java")

            val mainSet = sourceSets.main.get()
            compileClasspath += mainSet.compileClasspath + mainSet.output
        }

    }
}

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "m2dv8tion"
        url = uri("https://m2.dv8tion.net/releases")
    }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/493msi/plutoengine/")
        credentials {
            username = "493msi"
            password = System.getenv("GITHUB_PACKAGES_KEY")
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

application {
    mainClass.set("com.botdiril.BotMain")

    applicationDistribution.into("assets") {
        from("assets")
        exclude("enhancementProposals/")
    }
}

tasks.getByName<Tar>("distTar") {
    val baseName = archiveBaseName.get()

    archiveFileName.set("$baseName.tar")

    println("Archive file name: $baseName")
}


tasks.getByName<Zip>("distZip") {
    // Feel free to comment
    // In Docker, we only care about the .tar file
    enabled = false
}


dependencies {
    implementation("org.jetbrains", "annotations", "20.1.0")

    implementation("com.mchange", "c3p0", "0.9.5.5")
    implementation("mysql", "mysql-connector-java", "8.0.22")

    implementation("org.yaml", "snakeyaml", "1.28")

    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.12.3")
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.12.3")

    implementation("org.slf4j", "slf4j-api", "1.8.0-beta4")
    implementation("org.slf4j", "slf4j-simple", "1.8.0-beta4")
    implementation("org.apache.logging.log4j", "log4j-core", "2.15.0")
    implementation("org.apache.logging.log4j", "log4j-api", "2.15.0")
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", "2.15.0")

    implementation("org.apache.commons", "commons-lang3", "3.11")
    implementation("org.apache.commons", "commons-text", "1.9")
    implementation("org.apache.commons", "commons-math3", "3.6.1")
    implementation("commons-io", "commons-io", "2.8.0")

    implementation("net.dv8tion", "JDA", "4.4.0_351") {
        exclude(module = "opus-java")
    }

    implementation("cz.tefek", "plutolib", "20.2.0.0-alpha.2")
}
