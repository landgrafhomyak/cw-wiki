package io.github.landgrafhomyak.chatwars.wiki.preprocessors.html

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class HtmlCompileTask : DefaultTask() {
    private lateinit var destDir: File
    private val requests = ArrayList<Request>()

    private data class Request(val pkg: String, val name: String, val file: File)

    fun destDir(file: File) {
        this.destDir = file
    }

    fun html(pkg: String, name: String, file: File) {
        this.requests.add(Request(pkg, name, file))
    }

    @TaskAction
    fun compile() {
        this.destDir.mkdirs()
        this.destDir.deleteRecursively()
        this.destDir.mkdir()
        for (r in this.requests)
            this.destDir
                .resolve("${r.name}.kt")
                .apply { createNewFile() }
                .writeText(Generator.generate(r.pkg, r.name, Parser.parse(r.file.readText())))
    }
}