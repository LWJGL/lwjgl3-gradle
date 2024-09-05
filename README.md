# lwjgl

The idea is to easier the lwjgl dependency management

Let's say you want to include everything, as on the website with the preset

This:

```kotlin

val lwjglVersion = "3.3.4-SNAPSHOT"
val lwjglNatives = "natives-linux"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-cuda")
    implementation("org.lwjgl", "lwjgl-egl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-jawt")
    implementation("org.lwjgl", "lwjgl-jemalloc")
    implementation("org.lwjgl", "lwjgl-libdivide")
    implementation("org.lwjgl", "lwjgl-llvm")
    implementation("org.lwjgl", "lwjgl-lmdb")
    implementation("org.lwjgl", "lwjgl-lz4")
    implementation("org.lwjgl", "lwjgl-meow")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-odbc")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opencl")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-opengles")
    implementation("org.lwjgl", "lwjgl-openvr")
    implementation("org.lwjgl", "lwjgl-opus")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-remotery")
    implementation("org.lwjgl", "lwjgl-rpmalloc")
    implementation("org.lwjgl", "lwjgl-shaderc")
    implementation("org.lwjgl", "lwjgl-sse")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-tinyexr")
    implementation("org.lwjgl", "lwjgl-tinyfd")
    implementation("org.lwjgl", "lwjgl-tootle")
    implementation("org.lwjgl", "lwjgl-vma")
    implementation("org.lwjgl", "lwjgl-vulkan")
    implementation("org.lwjgl", "lwjgl-xxhash")
    implementation("org.lwjgl", "lwjgl-yoga")
    implementation("org.lwjgl", "lwjgl-zstd")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-bgfx", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-jemalloc", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-libdivide", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-llvm", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-lmdb", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-lz4", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-meow", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengles", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openvr", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opus", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-remotery", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-rpmalloc", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-shaderc", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-sse", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-tinyexr", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-tinyfd", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-tootle", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-vma", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-xxhash", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-yoga", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-zstd", classifier = lwjglNatives)
}
```

becomes

```kotlin
plugins {
    id("org.lwjgl.plugin") version "0.0.35"
}
repositories {
    mavenCentral()
    sonatype() // plugin accessor
}
dependencies {
    lwjgl {
        version = Snapshot.`3․3․4` // default to `Release.latest`, that is Release.`3․3․4`
        implementation(Preset.everything) 
    }
}
```
The corresponding natives will be loaded under the hood for all the modules which need them.
By default, only the natives of the running platform will be included. If you want to include them all, set 
`nativesForEveryPlatform = true`

The default version is the latest stable, that is `3.3.4`, if you want to override this
```kotlin
lwjgl {
    version = Release.`3․3․0` // down to 3.1.0
}
```

You can of course pass the wished `Module`s directly
```kotlin
lwjgl { implementation(assimp, bgfx, glfw, nanovg, nuklear, openal, opengl, par, stb, vulkan) }
```
`core` may be omitted since it's included by default

You can also extend presets with specific modules:
```kotlin
lwjgl {
    implementation(Lwjgl.Preset.gettingStarted + jemalloc)
}
```

Accessor addons are available as well (they revert to simple `String`s, so you can use them also outside `lwjgl` lambda):
```kotlin
implementation(Lwjgl.Addons.`joml 1․10․5`)
```
