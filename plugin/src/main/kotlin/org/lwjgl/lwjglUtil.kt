package org.lwjgl

import org.gradle.api.Action
import org.lwjgl.Lwjgl.Module.*
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.internal.os.OperatingSystem.*
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo
import org.gradle.kotlin.dsl.maven
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


fun lwjgl(action: Action<Lwjgl>) = action.execute(Lwjgl)

object Lwjgl {

    val group = "org.lwjgl"
    var version: Version = Release.latest
    var nativesForEveryPlatform = false

    operator fun invoke(block: Lwjgl.() -> Unit) = Lwjgl.block()

    fun DependencyHandler.implementation(modules: List<Module>) = implementation(false, modules)
    fun DependencyHandler.testImplementation(modules: List<Module>) = implementation(true, modules)
    fun DependencyHandler.implementation(vararg modules: Module) = implementation(false, modules)
    fun DependencyHandler.testImplementation(vararg modules: Module) = implementation(true, modules)

    fun DependencyHandler.implementation(preset: Preset) = implementation(false, preset.modules)
    fun DependencyHandler.testImplementation(preset: Preset) = implementation(true, preset.modules)

    private fun DependencyHandler.implementation(test: Boolean, modules: Array<out Module>) {
        // core
        if (core !in modules)
            implementation(test, core)
        for (module in modules)
            implementation(test, module)
    }

    private fun DependencyHandler.implementation(test: Boolean, modules: Collection<Module>) {
        // core
        if (core !in modules)
            implementation(test, core)
        for (module in modules)
            implementation(test, module)
    }

    private fun DependencyHandler.implementation(test: Boolean, module: Module) {
        if (version.uint < module.since.uint)
            return
        var config = if (test) "testImplementation" else "implementation"
        add(config, "$group:${module.artifact}:${version.string}")
        if (module.hasNative) {
            config = if (test) "testRuntimeOnly" else "runtimeOnly"
            if (nativesForEveryPlatform)
                for (platform in platforms)
                    addExternalModuleDependencyTo(this, config, group, module.artifact, version.string,
                                                  null, "natives-$platform", null, null)
            else
                addExternalModuleDependencyTo(this, config, group, module.artifact, version.string,
                                              null, "natives-$runningPlatform", null, null)
        }
    }

    enum class Module(val hasNative: Boolean = true, val since: Version = Release.`3․1․0`) {
        /** This can be skipped */
        core,

        assimp(since = Release.`3․1․1`),
        bgfx,
        cuda(false, Release.`3․2․1`),
        egl(false),
        fmod(false, Snapshot.`3․3․2`),
        freetype(since = Snapshot.`3․3․2`),
        glfw,
        harfbuzz(since = Snapshot.`3․3․2`),
        hwloc(since = Snapshot.`3․3․2`),
        jawt(false),
        jemalloc,
        ktx(since = Snapshot.`3․3․2`),
        libdivide(since = Release.`3․2․1`),
        llvm(since = Release.`3․2․1`),
        lmdb,
        lz4(since = Release.`3․1․4`),
        meow(since = Release.`3․2․1`),
        meshoptimizer(since = Release.`3․3․0`),
        nanovg,
        nfd,
        nuklear,
        odbc(false, Release.`3․1․4`),
        openal,
        opencl(false),
        opengl,
        opengles,
        openvr(since = Release.`3․1․2`),
        openxr(since = Release.`3․3․1`),
        opus(since = Release.`3․2․1`),
        par,
        remotery(since = Release.`3․1․4`),
        rpmalloc(since = Release.`3․1․3`),
        shaderc(since = Release.`3․2․3`),
        spvc(since = Release.`3․3․0`),
        sse,
        stb,
        tinyexr(since = Release.`3․1․2`),
        tinyfd,
        tootle(since = Release.`3․1․5`),
        vma(since = Release.`3․2․0`),
        vulkan(false),
        xxhash,
        yoga(since = Release.`3․1․2`),
        zstd(since = Release.`3․1․4`);

        val artifact: String
            get() = when (this) {
                core -> "lwjgl"
                else -> "lwjgl-$name"
            }
    }

    enum class Preset(val modules: List<Module>) {
        none(emptyList()),
        everything(Module.values().toList()),
        gettingStarted(listOf(core, assimp, bgfx, glfw, nanovg, nuklear, openal, opengl, par, stb, vulkan)),
        minimalOpenGL(listOf(core, assimp, glfw, openal, opengl, stb)),
        minimalOpenGLES(listOf(core, assimp, egl, glfw, openal, opengles, stb)),
        minimalVulkan(listOf(core, assimp, glfw, openal, stb, vulkan));

        operator fun plus(module: Module) = modules + module
    }

    val runningPlatform: String by lazy {
        val arch = System.getProperty("os.arch")
        val aarch64 = arch.startsWith("aarch64")
        val os = current()
        when {
            os.isLinux -> "linux" + when {
                arch.startsWith("arm") || aarch64 -> '-' + if ("64" in arch || arch.startsWith("armv8")) "arm64" else "arm32"
                else -> ""
            }

            os.isMacOsX -> "macos" + if (aarch64) "-arm64" else ""
            os.isWindows -> "windows" + when {
                "64" in arch -> if (aarch64) "-arm64" else ""
                else -> "-x86"
            }

            else -> error("Unrecognized or unsupported Operating system. Please set `lwjglNatives` manually")
        }
    }
    val platforms: List<String> = listOf(
        "linux-arm64", "linux-arm32", "linux",
        "macos-arm64", "macos",
        "windows-arm64", "windows", "windows-x86"
    )

    object Addons {
        val `joml 1․10․7` = "org.joml:joml:1.10.7"
        val `joml-primitives 1․10․0` = "org.joml:joml-primitives:1.10.0"
        val `awt 0․1․8` = "org.lwjglx:lwjgl3-awt:0.1.8"
        val `steamworks4j 1․9․0` = "com.code-disaster.steamworks4j:steamworks4j:1.9.0"
        val `steamworks4j-server 1․9․0` = "com.code-disaster.steamworks4j:steamworks4j-server:1.9.0"
    }
}

//fun KotlinDependencyHandler.lwjglImplementation(vararg modules: lwjgl.Module) = lwjglImplementation(modules)
fun KotlinDependencyHandler.lwjglImplementation(preset: Lwjgl.Preset) = impl(preset.modules)

fun KotlinDependencyHandler.lwjglImplementation(vararg modules: Lwjgl.Module) {
    // core
    if (core !in modules)
        impl(core)
    for (module in modules)
        impl(module)
}

private fun KotlinDependencyHandler.impl(modules: Collection<Lwjgl.Module>) {
    // core
    if (core !in modules)
        implementation(core)
    for (module in modules)
        implementation(module)
}

private fun KotlinDependencyHandler.impl(module: Lwjgl.Module) {
    implementation("${Lwjgl.group}:${module.artifact}:${Lwjgl.version.string}")
    if (module.hasNative)
        if (Lwjgl.nativesForEveryPlatform)
            for (platform in Lwjgl.platforms)
                runtimeOnly("${Lwjgl.group}:${module.artifact}:${Lwjgl.version.string}:natives-$platform")
        else
            runtimeOnly("${Lwjgl.group}:${module.artifact}:${Lwjgl.version.string}:natives-${Lwjgl.runningPlatform}")
}

interface Version {
    val string: String
    val uint: UInt
        get() = string[0].digitToInt().toUInt() * 100u + string[2].digitToInt().toUInt() * 10u + string[4].digitToInt().toUInt()
}

enum class Release : Version {
    `3․3․4`,
    `3․3․3`,
    `3․3․2`,
    `3․3․1`,
    `3․3․0`,
    `3․2․3`,
    `3․2․2`,
    `3․2․1`,
    `3․2․0`,
    `3․1․6`,
    `3․1․5`,
    `3․1․4`,
    `3․1․3`,
    `3․1․2`,
    `3․1․1`,
    `3․1․0`;

    override val string: String
        get() = name.replace('_', '.')

    companion object {
        val latest = values().first()
    }
}

enum class Snapshot : Version {
    `3․3․4`,
    `3․3․3`,
    `3․3․2`,
    `3․3․1`,
    `3․3․0`,
    `3․2․3`,
    `3․2․2`,
    `3․2․1`,
    `3․2․0`,
    `3․1․6`,
    `3․1․5`,
    `3․1․4`,
    `3․1․3`,
    `3․1․2`,
    `3․1․1`,
    `3․1․0`;

    override val string: String
        get() = name.replace('_', '.') + "-SNAPSHOT"

    companion object {
        val latest = Release.values().first()
    }
}

fun RepositoryHandler.sonatype() {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}
