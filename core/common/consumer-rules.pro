# ── ServiceBuilder ───────────────────────────────────────────────────────
# Uses reified inline + Retrofit.create(S::class.java) at runtime.
-keep class com.caioluis.githubpopular.core.common.ServiceBuilder { *; }

# ── Exception classes ───────────────────────────────────────────────────
# Keep exception hierarchy so stack traces remain readable.
-keep class com.caioluis.githubpopular.core.common.exception.** { *; }

