import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*
import java.io.*

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.9.25")
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

group = "com.github.shynixn"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(System.getenv("SHYNIXN_MCUTILS_REPOSITORY_2025")) // All MCUTILS libraries are private and not OpenSource.
}

dependencies {
    // Compile Only
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    // Library dependencies with legacy compatibility, we can use more up-to-date version in the plugin.yml
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-api:2.21.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-core:2.21.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // Custom dependencies
    implementation("com.github.shynixn.mcutils:common:2025.6")
    implementation("com.github.shynixn.mcutils:packet:2025.9")

    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    testImplementation("org.mockito:mockito-core:2.23.0")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

/**
 * Include all but exclude debugging classes.
 */
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependsOn("jar")
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-shadowjar.${archiveExtension.get()}")
    exclude("DebugProbesKt.bin")
    exclude("module-info.class")
}

/**
 * Create all plugin jar files.
 */
tasks.register("pluginJars") {
    dependsOn("pluginJarLatest")
    dependsOn("pluginJarPremium")
    dependsOn("pluginJarPremiumFolia")
    dependsOn("pluginJarLegacy")
}

/**
 * Relocate Plugin Jar.
 */
tasks.register("relocatePluginJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("shadowJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("shadowJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-relocate.${archiveExtension.get()}")
    relocate("com.github.shynixn.mcutils", "com.github.shynixn.shyscoreboard.lib.com.github.shynixn.mcutils")
    relocate("com.fasterxml", "com.github.shynixn.shyscoreboard.lib.com.fasterxml")
}

/**
 * Create latest plugin jar file.
 */
tasks.register("pluginJarLatest", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("relocatePluginJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("relocatePluginJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-latest.${archiveExtension.get()}")
    // destinationDirectory.set(File("C:\\temp\\plugins"))

    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_8_R3/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_9_R2/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_17_R1/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_18_R1/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_18_R2/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_19_R1/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_19_R2/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_19_R3/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_20_R1/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_20_R2/**")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/packet/nms/v1_20_R3/**")
    exclude("com/github/shynixn/mcutils/**")
    exclude("com/github/shynixn/mccoroutine/**")
    exclude("kotlin/**")
    exclude("org/**")
    exclude("kotlinx/**")
    exclude("javax/**")
    exclude("com/google/**")
    exclude("com/fasterxml/**")
    exclude("com/zaxxer/**")
    exclude("plugin-folia.yml")
    exclude("plugin-legacy.yml")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/common/FoliaMarker.class")
}

/**
 * Create premium plugin jar file.
 */
tasks.register("pluginJarPremium", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("relocatePluginJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("relocatePluginJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-premium.${archiveExtension.get()}")
    // destinationDirectory.set(File("C:\\temp\\plugins"))

    exclude("com/github/shynixn/mcutils/**")
    exclude("com/github/shynixn/mccoroutine/**")
    exclude("kotlin/**")
    exclude("org/**")
    exclude("kotlinx/**")
    exclude("javax/**")
    exclude("com/zaxxer/**")
    exclude("com/google/**")
    exclude("com/fasterxml/**")
    exclude("plugin-folia.yml")
    exclude("plugin-legacy.yml")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/common/FoliaMarker.class")
}

/**
 * Relocate Plugin Folia Jar.
 */
tasks.register("relocateFoliaPluginJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("shadowJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("shadowJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-relocate-folia.${archiveExtension.get()}")
    relocate("com.github.shynixn.mcutils", "com.github.shynixn.shyscoreboard.lib.com.github.shynixn.mcutils")
    relocate("com.fasterxml", "com.github.shynixn.shyscoreboard.lib.com.fasterxml")
    exclude("plugin.yml")
    rename("plugin-folia.yml", "plugin.yml")
}

/**
 * Create premium folia plugin jar file.
 */
tasks.register("pluginJarPremiumFolia", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("relocateFoliaPluginJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("relocateFoliaPluginJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-premium-folia.${archiveExtension.get()}")
    // destinationDirectory.set(File("C:\\temp\\Folia\\plugins"))

    exclude("com/github/shynixn/mcutils/**")
    exclude("com/github/shynixn/mccoroutine/**")
    exclude("kotlin/**")
    exclude("org/**")
    exclude("kotlinx/**")
    exclude("javax/**")
    exclude("com/zaxxer/**")
    exclude("com/google/**")
    exclude("com/fasterxml/**")
    exclude("plugin-folia.yml")
    exclude("plugin-legacy.yml")
}

/**
 * Relocate legacy plugin jar file.
 */
tasks.register("relocateLegacyPluginJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("shadowJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("shadowJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-legacy-relocate.${archiveExtension.get()}")
    relocate("com.github.shynixn.mcutils", "com.github.shynixn.shyscoreboard.lib.com.github.shynixn.mcutils")
    relocate("kotlin", "com.github.shynixn.shyscoreboard.lib.kotlin")
    relocate("org.intellij", "com.github.shynixn.shyscoreboard.lib.org.intelli")
    relocate("org.aopalliance", "com.github.shynixn.shyscoreboard.lib.org.aopalliance")
    relocate("org.checkerframework", "com.github.shynixn.shyscoreboard.lib.org.checkerframework")
    relocate("org.jetbrains", "com.github.shynixn.shyscoreboard.lib.org.jetbrains")
    relocate("org.openjdk.nashorn", "com.github.shynixn.shyscoreboard.lib.org.openjdk.nashorn")
    relocate("org.slf4j", "com.github.shynixn.shyscoreboard.lib.org.slf4j")
    relocate("org.objectweb", "com.github.shynixn.shyscoreboard.lib.org.objectweb")
    relocate("javax.annotation", "com.github.shynixn.shyscoreboard.lib.javax.annotation")
    relocate("javax.inject", "com.github.shynixn.shyscoreboard.lib.javax.inject")
    relocate("kotlinx.coroutines", "com.github.shynixn.shyscoreboard.lib.kotlinx.coroutines")
    relocate("com.google", "com.github.shynixn.shyscoreboard.lib.com.google")
    relocate("com.fasterxml", "com.github.shynixn.shyscoreboard.lib.com.fasterxml")
    relocate("com.zaxxer", "com.github.shynixn.shyscoreboard.lib.com.zaxxer")
    relocate("com.github.shynixn.mccoroutine", "com.github.shynixn.shyscoreboard.lib.com.github.shynixn.mccoroutine")
    exclude("plugin.yml")
    rename("plugin-legacy.yml", "plugin.yml")
}

/**
 * Create legacy plugin jar file.
 */
tasks.register("pluginJarLegacy", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    dependsOn("relocateLegacyPluginJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("relocateLegacyPluginJar") as Jar).archiveFileName.get())))
    archiveFileName.set("${archiveBaseName.get()}-${archiveVersion.get()}-legacy.${archiveExtension.get()}")
    // destinationDirectory.set(File("C:\\temp\\plugins"))
    exclude("com/github/shynixn/mcutils/**")
    exclude("org/**")
    exclude("kotlin/**")
    exclude("kotlinx/**")
    exclude("javax/**")
    exclude("com/google/**")
    exclude("com/github/shynixn/mccoroutine/**")
    exclude("com/fasterxml/**")
    exclude("com/zaxxer/**")
    exclude("plugin-legacy.yml")
    exclude("plugin-folia.yml")
    exclude("com/github/shynixn/shyscoreboard/lib/com/github/shynixn/mcutils/common/FoliaMarker.class")
}

tasks.register("languageFile") {
    val kotlinSrcFolder = project.sourceSets.toList()[0].allJava.srcDirs.first { e -> e.endsWith("java") }
    val contractFile = kotlinSrcFolder.resolve("com/github/shynixn/shyscoreboard/contract/ShyScoreboardLanguage.kt")
    val resourceFile = kotlinSrcFolder.parentFile.resolve("resources").resolve("lang").resolve("en_us.yml")
    val lines = resourceFile.readLines()

    val contractContents = ArrayList<String>()
    contractContents.add("package com.github.shynixn.shyscoreboard.contract")
    contractContents.add("")
    contractContents.add("import com.github.shynixn.mcutils.common.language.LanguageItem")
    contractContents.add("import com.github.shynixn.mcutils.common.language.LanguageProvider")
    contractContents.add("")
    contractContents.add("interface ShyScoreboardLanguage : LanguageProvider {")
    for (key in lines) {
        if (key.toCharArray()[0].isLetter()) {
            contractContents.add("  var ${key} LanguageItem")
            contractContents.add("")
        }
    }
    contractContents.removeLast()
    contractContents.add("}")

    contractFile.printWriter().use { out ->
        for (line in contractContents) {
            out.println(line)
        }
    }

    val implFile = kotlinSrcFolder.resolve("com/github/shynixn/shyscoreboard/ShyScoreboardLanguageImpl.kt")
    val implContents = ArrayList<String>()
    implContents.add("package com.github.shynixn.shyscoreboard")
    implContents.add("")
    implContents.add("import com.github.shynixn.mcutils.common.language.LanguageItem")
    implContents.add("import com.github.shynixn.shyscoreboard.contract.ShyScoreboardLanguage")
    implContents.add("")
    implContents.add("class ShyScoreboardLanguageImpl : ShyScoreboardLanguage {")
    implContents.add(" override val names: List<String>\n" +
            "  get() = listOf(\"en_us\")")

    for (i in 0 until lines.size) {
        val key = lines[i]

        if (key.toCharArray()[0].isLetter()) {
            var text = ""
            println(">" + lines[i])

            println("_")
            var j = i
            while (true){
                if(lines[j].contains("text:")){
                    text = lines[j]
                    break
                }
                j++
            }

            implContents.add(" override var ${key.replace(":","")} = LanguageItem(${text.replace("  text: ","")})")
            implContents.add("")
        }
    }
    implContents.removeLast()
    implContents.add("}")

    implFile.printWriter().use { out ->
        for (line in implContents) {
            out.println(line)
        }
    }
}

tasks.register("printVersion") {
    println(version)
}
