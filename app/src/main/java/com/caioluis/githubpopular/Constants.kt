package com.caioluis.githubpopular

object Constants {
    val languages = sortedSetOf(
        "Kotlin",
        "Swift",
        "Javascript",
        "Typescript",
        "Dart",
        "Python",
        "Ruby",
        "Rust",
        "Go",
        "C",
        "C#",
        "C++",
        "Cobol",
        "Elixir",
        "FORTRAN",
        "Haskell",
        "Lua",
        "SQL",
        "R",
        "Objective-C",
        "PHP",
        "Assembly",
        "Bash",
        "Java",
    ).toList()

    const val REPOSITORIES_VIEW_TYPE = 100
    const val RETRY_BUTTON_VIEW_TYPE = 101
}