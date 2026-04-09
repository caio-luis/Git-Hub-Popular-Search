# ── Retrofit service interfaces ──────────────────────────────────────────
# Retrofit creates dynamic proxies at runtime; method signatures must survive.
-keep,allowobfuscation,allowshrinking interface com.caioluis.githubpopular.data.impl.remote.**.service.** {
    <methods>;
}

# ── Room Database & DAOs ────────────────────────────────────────────────
# Room-generated _Impl classes reference the abstract Database and DAO
# interfaces by name; keep them so the generated code can find them.
-keep class com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase { *; }
-keep class com.caioluis.githubpopular.data.impl.local.**.dao.** { *; }
-keep class com.caioluis.githubpopular.data.impl.local.**Dao { *; }

