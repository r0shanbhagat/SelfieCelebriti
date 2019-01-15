# copyright
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#-----------------------------------------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-verbose
-dontusemixedcaseclassnames

-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

# configuration file; if you are including this file, optimization
# file from your project's proguard.config path property.
#activity,application,service,broadcastReceiver,contentprovider....
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#layout :onclick: android:onclick="onClick"，
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}

-keepclassmembers class * {
    void *(*Event);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# natvie
-keepclasseswithmembernames class * {
    native <methods>;
}

# Parcelable
#Ref:http://parceler.org/#configuring_proguard
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class *$$Parcelable { *; }
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

#databinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
-keep class android.databinding.annotationprocessor.** { *; }
-keep class android.databinding.DataBindingUtil.** { *; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# picasso
-keep class com.squareup.picasso.** {*; }
-dontwarn com.squareup.picasso.**

#glide 4.x
#chenyongta*
#Github：https://github.com/yourtion
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#Banner Slider
-dontwarn ss.com.bannerslider.**
-keep class ss.com.bannerslider.adapters.**
-keep class ss.com.bannerslider.** { *; }

#Android SVG
-dontwarn com.caverock.androidsvg.**
-keep  class com.caverock.androidsvg.* { public *; }
-dontwarn com.caverock.androidsvg.**

#okhttp3.x
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}

## okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.{*;}
#retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn okhttp3.internal.platform.*
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepattributes *Annotation*
-keep public class * extends java.lang.Exception
-keep class io.fabric.sdk.android.** { *; }
-dontwarn io.fabric.sdk.android.**

-keep class com.selfie.star.** { *; }
-keep class com.base.commonframework.** { *; }
-keep class ja.burhanrashid52.photoeditor.** { *; }
-keep class com.girnarsoft.tracking.** { *; }

# copyright vahangyan
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#-----------------------------------------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-verbose
-dontusemixedcaseclassnames

-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

# configuration file; if you are including this file, optimization
# file from your project's proguard.config path property.
#activity,application,service,broadcastReceiver,contentprovider....
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#layout :onclick: android:onclick="onClick"，
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}

-keepclassmembers class * {
    void *(*Event);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# natvie
-keepclasseswithmembernames class * {
    native <methods>;
}

# Parcelable
#Ref:http://parceler.org/#configuring_proguard
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class *$$Parcelable { *; }
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

#databinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
-keep class android.databinding.annotationprocessor.** { *; }
-keep class android.databinding.DataBindingUtil.** { *; }


#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}


-keepclasseswithmembernames class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver

-dontwarn com.xiaomi.push.service.b

-keep class org.apache.http.**
-keep interface org.apache.http.**
-dontwarn org.apache.**

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote okhttp3.internal.Platform


### OKIO

# java.nio.file.* usage which cannot be used at runtime. Animal sniffer annotation.
-dontwarn okio.Okio
# JDK 7-only method which is @hide on Android. Animal sniffer annotation.
-dontwarn okio.DeflaterSink



#okhttp3.x
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}

## okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.{*;}
#retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**

#recyclerview-animators
-keep class jp.wasabeef.** {*;}
-dontwarn jp.wasabeef.*

#multistateview
-keep class com.kennyc.view.** { *; }
-dontwarn com.kennyc.view.*

# universal-image-loader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }


-keep public class com.girnarsoft.hdfc.R$*{
    public static final int *;
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#eventbus 3.0
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
public void xxxxxx(**);
}


################gson##################
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}


# support-v4
#https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }



# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

# support design
#@link http://stackoverflow.com/a/31028536
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#-------------------------------------------------------------------------

# picasso
-keep class com.squareup.picasso.** {*; }
-dontwarn com.squareup.picasso.**

#glide 4.x
#chenyongta*
#Github：https://github.com/yourtion
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keepattributes Exceptions,InnerClasses

-keep class io.rong.** {*;}

-keep class * implements io.rong.imlib.model.MessageContent{*;}

-keepattributes Signature

-keepattributes *Annotation*

-keep class sun.misc.Unsafe { *; }

-keep class com.google.gson.examples.android.model.** { *; }

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**

-keepclassmembers class * extends com.sea_monster.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**

-keep class com.ultrapower.** {*;}

#retrofit2.x
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okhttp3.internal.platform.*


#Rxjava RxAndroid
-dontwarn rx.*
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# Okio
-dontwarn com.squareup.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
# Retrolambda
-dontwarn java.lang.invoke.*

#push
-keepclasseswithmembernames class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver

#Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#Banner Slider
-dontwarn ss.com.bannerslider.**
-keep class ss.com.bannerslider.adapters.**
-keep class ss.com.bannerslider.** { *; }

#Android SVG
-dontwarn com.caverock.androidsvg.**
-keep  class com.caverock.androidsvg.* { public *; }
-dontwarn com.caverock.androidsvg.**

-dontwarn org.xmlpull.v1.**
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-keep class com.tozny.crypto.android.AesCbcWithIntegrity$PrngFixes$* { *; }


-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
