# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.google.android.gms.location.**
-dontwarn com.google.android.gms.gcm.**
-dontwarn com.google.android.gms.iid.**

-keep class com.google.android.gms.gcm.** { *; }
-keep class com.google.android.gms.iid.** { *; }
-keep class com.google.android.gms.location.** { *; }

################################################################################

-keepattributes Exceptions,*InnerClasses*
-keep class com.girnarsoft.tracking.exception.TrackerException { *; }
-keep class com.girnarsoft.tracking.config.TrackerConfiguration { *; }
-keep class com.girnarsoft.tracking.config.TrackerConfiguration$Builder { *; }
-keep class com.girnarsoft.tracking.event.EventInfo { *; }
-keep class com.girnarsoft.tracking.event.UserAttributes { *; }
-keep class com.girnarsoft.tracking.event.UserAttributes$Builder { *; }
-keep class com.girnarsoft.tracking.event.EventInfo$Builder { *; }
-keep class com.girnarsoft.tracking.event.EventInfo$EventName { *; }
-keep class com.girnarsoft.tracking.event.EventInfo$EventAction { *; }
-keep class com.girnarsoft.tracking.event.EventInfo$EventCategory { *; }
-keep class com.girnarsoft.tracking.AnalyticsManager { *; }
-keep class com.girnarsoft.tracking.IAnalyticsManager { *; }
-keep class com.girnarsoft.tracking.callback.OnDataAvailableListener { *; }