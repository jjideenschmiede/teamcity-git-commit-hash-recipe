tasks.register("recipe") {
    doLast {
        val outputDir = file(".teamcity/pluginData/_Self/metaRunners")
        outputDir.mkdirs()

        val inputFile = file("src/recipe.yml")
		val fileName = "git-commit-hash"
        val outputFile = outputDir.resolve("$fileName.yml")

        if (inputFile.exists()) {
            val lines = inputFile.readLines()

            val expanded = lines.flatMap { line ->
                Regex("""^(\s*)([\w.\-"]+):\s*!include\s+(.+)$""").matchEntire(line)?.let { match ->
                    val (indent, key, path) = match.destructured
                    val includeFile = file("src").resolve(path.trim())
                    val content = if (includeFile.exists()) includeFile.readLines() else listOf("# ERROR: Missing $path")
                    listOf("$indent$key: |-") + content.map { "$indent  $it" }
                } ?: listOf(line)
            }

            outputFile.writeText(expanded.joinToString("\n"))
			println("✓ Built $fileName.yml")
        }
    }
}

defaultTasks("recipe")
