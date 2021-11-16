package org.lwjgl

import org.lwjgl.Lwjgl.Module.*
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.internal.os.OperatingSystem.*
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo

object Lwjgl {

    val release = Release
    val snapshot = Snapshot
    val group = "org.lwjgl"
    var version = "3.3.0"
    var allNatives = false

    operator fun invoke(block: Lwjgl.() -> Unit) = Lwjgl.block()

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
        var config = if (test) "testImplementation" else "implementation"
        add(config, "$group:${module.artifact}:$version")
        if (module.hasNative) {
            config = if (test) "testRuntimeOnly" else "runtimeOnly"
            if (allNatives)
                for (native in allNative)
                    addExternalModuleDependencyTo(this, config, group, module.artifact, version,
                                                  null, native, null, null)
            else
                addExternalModuleDependencyTo(this, config, group, module.artifact, version,
                                              null, native, null, null)
        }
    }

    enum class Module(val hasNative: Boolean = true) {
        /** This can be skipped */
        core,

        assimp,
        bgfx,
        bullet,
        cuda(false),
        egl(false),
        glfw,
        jawt(false),
        jemalloc,
        libdivide,
        llvm,
        lmdb,
        lz4,
        meow,
        meshoptimizer,
        nanovg,
        nfd,
        nuklear,
        odbc(false),
        openal,
        opencl(false),
        opengl,
        opengles,
        openvr,
        opus,
        par,
        remotery,
        rpmalloc,
        shaderc,
        spvc,
        sse,
        stb,
        tinyexr,
        tinyfd,
        tootle,
        vma,
        vulkan(false),
        xxhash,
        yoga,
        zstd;

        val artifact: String
            get() = when (this) {
                core -> "lwjgl"
                else -> "lwjgl-$name"
            }
    }

    enum class Preset(val modules: ArrayList<Module>) {
        none(arrayListOf<Module>()),
        everything(Module.values().toCollection(ArrayList())),
        gettingStarted(arrayListOf(core, assimp, bgfx, glfw, nanovg, nuklear, openal, opengl, par, stb, vulkan)),
        minimalOpenGL(arrayListOf(core, assimp, glfw, openal, opengl, stb)),
        minimalOpenGLES(arrayListOf(core, assimp, egl, glfw, openal, opengles, stb)),
        minimalVulkan(arrayListOf(core, assimp, glfw, openal, stb, vulkan))
    }

    val native by lazy {
        val os = System.getProperty("os.arch")
        val aarch64 = os.startsWith("aarch64")
        "natives-" + when (current()) {
            LINUX -> "linux" + when {
                os.startsWith("arm") || aarch64 -> '-' + if ("64" in os || os.startsWith("armv8")) "arm64" else "arm32"
                else -> ""
            }
            MAC_OS -> "macos" + if (aarch64) "-arm64" else ""
            WINDOWS -> "windows" + when {
                "64" in os -> if (aarch64) "-arm64" else ""
                else -> "-x86"
            }
            else -> error("Unrecognized or unsupported Operating system. Please set `lwjglNatives` manually")
        }
    }
    private val allNative = listOf("linux-arm64", "linux-arm32", "linux",
                                   "macos-arm64", "macos",
                                   "windows-arm64", "windows", "windows-x86")
}

object Release {
    val latest: Unit
        get() {
            `3_3_0`
        }
    val `3_3_0`: Unit
        get() {
            Lwjgl.version = "3.2.3"
        }
    val `3_2_3`: Unit
        get() {
            Lwjgl.version = "3.2.3"
        }
    val `3_2_2`: Unit
        get() {
            Lwjgl.version = "3.2.2"
        }
    val `3_2_1`: Unit
        get() {
            Lwjgl.version = "3.2.1"
        }
    val `3_2_0`: Unit
        get() {
            Lwjgl.version = "3.2.0"
        }
    val `3_1_6`: Unit
        get() {
            Lwjgl.version = "3.1.6"
        }
    val `3_1_5`: Unit
        get() {
            Lwjgl.version = "3.1.5"
        }
    val `3_1_4`: Unit
        get() {
            Lwjgl.version = "3.1.4"
        }
    val `3_1_3`: Unit
        get() {
            Lwjgl.version = "3.1.3"
        }
    val `3_1_2`: Unit
        get() {
            Lwjgl.version = "3.1.2"
        }
    val `3_1_1`: Unit
        get() {
            Lwjgl.version = "3.1.1"
        }
    val `3_1_0`: Unit
        get() {
            Lwjgl.version = "3.1.0"
        }
}

object Snapshot {
    val `3_3_0`: Unit
        get() {
            Lwjgl.version = "3.3.0-SNAPSHOT"
        }
}