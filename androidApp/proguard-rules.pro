# Keep Kotlin Serialization models
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keepclasseswithmembers class * {
    companion object;
}
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class **$$serializer {
    public static final **$$serializer INSTANCE;
}

# Keep SQLDelight database & driver
-keep class com.obsidian.shipathon.data.local.db.** { *; }
-keep class app.cash.sqldelight.** { *; }

# Keep RevenueCat Billing & Suppress internal preview warnings
-keep class com.revenuecat.purchases.** { *; }
-dontwarn com.emergetools.**
-dontwarn com.revenuecat.purchases.**

# Line numbers & debugging attributes for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile