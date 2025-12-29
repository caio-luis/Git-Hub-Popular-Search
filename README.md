# Git-Hub-Popular-Search

**GitHubPopular** is a modern Android application designed to demonstrate advanced development practices, architectural patterns, and the use of the latest Jetpack libraries. The app allows users to search for and list the most popular repositories on GitHub by programming language.

## 🏗 Architecture & Design Patterns

The project follows **Clean Architecture** principles combined with **MVVM (Model-View-ViewModel)**, ensuring separation of concerns, scalability, and testability.

- **Modularization**: The project is structured into multi-modules to enforce separation of concerns:
  - **App Module**: Presentation layer using **Jetpack Compose**.
  - **Domain Layer** (`domain:bridge`, `domain:impl`): Contains pure business logic, use cases, and repository interfaces.
  - **Data Layer** (`data:bridge`, `data:impl`): Handles data retrieval (Network & Database).
  - **Build Logic**: Custom Gradle Convention Plugins for reusable build configuration.
  
- **Dependency Inversion**: Utilizes a **Bridge & Implementation** pattern to decouple modules and avoid circular dependencies, allowing feature modules to depend only on abstractions.
- **Repository Pattern**: Mediates data access between remote sources (API) and local cache (Database).

## 🛠 Tech Stack

### Languages & Core
- **[Kotlin](https://kotlinlang.org/)**: 100% Kotlin codebase.
- **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)**: For asynchronous operations and reactive data stream handling.

### User Interface (UI)
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: Modern, declarative UI toolkit (Material 3).
- **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)**: For seamless navigation between screens.
- **[Coil](https://coil-kt.github.io/coil/)**: Lightweight image loading library backed by Kotlin Coroutines.

### Networking & Data
- **[Retrofit](https://square.github.io/retrofit/)**: Type-safe HTTP client.
- **[Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)**: Official Kotlin serialization library, offering efficient, type-safe, and reflection-free JSON parsing.
- **[OkHttp](https://square.github.io/okhttp/)**: HTTP client with logging interceptors.
- **[Room](https://developer.android.com/training/data-storage/room)**: Local persistence library (SQLite abstraction).
- **[Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3)**: Efficiently loads and displays pages of data.

### Dependency Injection
- **[Dagger Hilt](https://dagger.dev/hilt/)**: Standard for dependency injection in Android.

### Build, Quality & Tools
- **[Gradle Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html)**: Centralized dependency management via `libs.versions.toml`.
- **[Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)**: Custom plugins to share build logic across modules.
- **[KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html)**: Faster alternative to KAPT for code generation (used by Room).
- **[Spotless](https://github.com/diffplug/spotless)**: Enforces consistent code formatting.
- **[LeakCanary](https://square.github.io/leakcanary/)**: Memory leak detection during development.
- **[Dependency Analysis](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin)**: Tool to analyze and optimize project dependencies.
- **[Mise](https://mise.jdx.dev/)**: Runtime manager for setting up development environments (Java, Ruby, etc.).
- **[Fastlane](https://fastlane.tools/)**: Automation tool for building and testing Android apps.

### Testing
- **[Mockk](https://mockk.io/)**: Mocking library for Kotlin.
- **[Robolectric](https://robolectric.org/)**: Runs Android unit tests on the JVM.
- **[JUnit 4](https://junit.org/junit4/)**: Standard testing framework.

## 💡 Strategic Decisions & Best Practices

- **Kotlin Serialization**: Chosen for its seamless integration with the Kotlin ecosystem, type safety, and reflection-free JSON parsing, offering better performance and future-proofing compared to reflection-based libraries like Gson.
- **Coroutines vs RxJava**: Coroutines provide a lighter, more idiomatic way to handle concurrency in Kotlin compared to RxJava, perfectly fitting the project's scope.
- **Offline Support**: Room is used to cache data, improving user experience under poor network conditions.
- **Strict Dependency Management**: Usage of `dependency-analysis` plugin ensures no unused or misconfigured dependencies exist in the project.
