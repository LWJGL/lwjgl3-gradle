package org.lwjgl

import org.gradle.api.Plugin
import org.gradle.api.Project

//abstract class LwjglPluginExtension {
//    abstract val version: Property<String>
//    abstract val allNatives: Property<Boolean>
//
//    init {
//        version.convention("3.2.3")
//        allNatives.convention(false)
//    }
//}

class LwjglPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        //        println("apply($project)")

        // Add the 'greeting' extension object
//        project.extensions.create<LwjglPluginExtension>("lwjgl")
    }
}
