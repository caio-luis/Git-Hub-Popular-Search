# Git-Hub-Popular-Search

App for studies that lists the most popular kotlin github repositories given the programming language.

## Used:

- Kotlin Coroutines (Flow)
- Clean Architecture with MVVM and modularization
- Dagger Hilt for dependency injection
- Retrofit and Moshi (with codegen) for API REST
- KSP
- Room for cache persistence
- Glide

## Gradle:
- dependency analyser plugin for optimizing build

## Explaining some uses:

- Moshi with codegen is faster than Gson. It uses generated code for it's adapters, while Gson uses
  reflection.
- Kotlin Symbol Processing (KSP) according to kotlin lang docs, is faster than kapt
- Kotlin coroutines is a lighter option instead of RxJava, and has a good integration with kotlin.
  For my use case on this app, it's the best option since I don't need all stuff RxJava provides
