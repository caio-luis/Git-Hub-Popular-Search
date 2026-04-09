# ── Room entities ────────────────────────────────────────────────────────
# Room reflects over constructors and fields of @Entity-annotated classes.
-keep class com.caioluis.githubpopular.data.bridge.local.** { *; }

# ── Kotlinx Serialization models ────────────────────────────────────
# Keep @Serializable classes, their companions, and generated serializers
# so kotlinx.serialization can find them at runtime.

-keep class com.caioluis.githubpopular.data.bridge.remote.** { *; }

-keepclassmembers class com.caioluis.githubpopular.data.bridge.remote.** {
    *** Companion;
}

-keep,includedescriptorclasses class com.caioluis.githubpopular.data.bridge.remote.**$$serializer { *; }

-keepclasseswithmembers class com.caioluis.githubpopular.data.bridge.remote.** {
    kotlinx.serialization.KSerializer serializer(...);
}

