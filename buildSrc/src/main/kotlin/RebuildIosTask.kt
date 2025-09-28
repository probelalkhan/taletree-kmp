package dev.belalkhan.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class RebuildIosTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputDirectory
    abstract val iosAppDir: DirectoryProperty

    init {
        group = "ios"
        description = "Run pod install for the iOS app"
        dependsOn("generateDummyFramework")
        iosAppDir.set(project.layout.projectDirectory.dir("../iosApp"))
    }

    @TaskAction
    fun execute() {
        logger.lifecycle("📦 Installing CocoaPods dependencies...")
        execOperations.exec {
            workingDir = iosAppDir.get().asFile
            commandLine("pod", "install")
        }
        logger.lifecycle("✅ iOS pod install complete.")
    }
}