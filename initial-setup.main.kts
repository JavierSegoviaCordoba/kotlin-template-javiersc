@file:Suppress("ComplexCondition")

import java.io.File
import java.nio.file.Paths

val argsMap: Map<String, String> =
    args.toList().associate { arg ->
        arg.split("=").run {
            first().filterNot(Char::isWhitespace) to
                last().dropWhile(Char::isWhitespace).dropLastWhile(Char::isWhitespace)
        }
    }

val String.placeholder
    get() = "{{ $this }}"

println("ARGS: \n${argsMap.toList().joinToString("\n"){ (key, value) -> "ARG: $key = $value"}}")

val currentDir: File = Paths.get("").toAbsolutePath().toFile()

println("FILE: ${currentDir.path}")

currentDir.walkTopDown().forEach { file ->
    if (file.name != "initial-setup.main.kts" &&
            file.name != "initial-setup.yaml" &&
            file.isFile &&
            file.path.contains("${File.separator}.git${File.separator}").not() &&
            file.path.contains(".gradle").not() &&
            file.path.contains("build").not() &&
            file.path.contains(".idea").not()
    ) {
        println("CHECKING FILE: $file...")
        val newContent =
            file.readLines().joinToString("\n") { line ->
                val key = argsMap.keys.firstOrNull { key -> line.contains(key.placeholder) }
                when {
                    key != null -> {
                        println("KEY FOUND: $key")
                        line.replace("#TODO: ", "")
                        line.replace("# TODO: ", "")
                            .replace("//TODO: ", "")
                            .replace("// TODO: ", "")
                            .replace(key.placeholder, argsMap[key]!!)
                    }
                    listOf(
                        "#TODO: Uncomment{{",
                        "# TODO: Uncomment{{",
                        "//TODO: Uncomment{{",
                        "// TODO: Uncomment{{",
                    )
                        .any { line.contains(it) } -> {
                        println("UNCOMMENT FOUND: $line")
                        line.replace("#TODO: Uncomment{{", "")
                            .replace("# TODO: Uncomment{{", "")
                            .replace("//TODO: Uncomment{{", "")
                            .replace("// TODO: Uncomment{{", "")
                            .replace(" }}", "")
                    }
                    else -> line
                }
            }
        file.writeText(newContent)
    }
}

File(".github/workflows/initial-setup.yaml").delete()

File("initial-setup.main.kts").delete()
