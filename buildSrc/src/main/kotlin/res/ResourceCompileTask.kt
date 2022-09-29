package io.github.landgrafhomyak.chatwars.wiki.preprocessors.res

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ResourceCompileTask : DefaultTask() {
    private lateinit var destDir: File

    fun destDir(file: File) {
        this.destDir = file
    }

    private lateinit var pkg: String


    fun pkg(value: String) {
        this.pkg = value
    }

    @Suppress("PropertyName")
    private lateinit var _name: String

    fun name(value: String) {
        this._name = value
    }

    private val textFiles = HashMap<String, File>()

    fun text(name: String, file: File) {
        this.textFiles[name] = file
    }

    private val binaryFiles = HashMap<String, File>()


    fun binary(name: String, file: File) {
        this.binaryFiles[name] = file
    }

    @TaskAction
    fun compile() {
        this.destDir.mkdirs()
        this.destDir.deleteRecursively()
        this.destDir.mkdir()
        this.destDir.resolve("${this._name}.kt").writeText(
            Generator.generate(
                this.pkg, this._name,
                this.textFiles.mapValues { (_, v) -> v.readText() },
                this.binaryFiles.mapValues { (_, v) -> v.readBytes() }
            )
        )
    }
}