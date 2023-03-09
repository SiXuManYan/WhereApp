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
#-keep class com.huantansheng.easyphotos.models.** { *; }
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# keep mob  share
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.bytedance.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**

# keep  融云
-keepattributes Exceptions,InnerClasses

-keepattributes Signature

-keep class io.rong.** {*;}
-keep class cn.rongcloud.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

# 下方混淆使用了Location包时才需要配置, 可参考高德官网的混淆方式:https://lbs.amap.com/api/android-sdk/guide/create-project/dev-attention
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}
-keep class com.autonavi.**{*;}


-ignorewarnings


# 友盟
# https://developer.umeng.com/docs/119267/detail/118584
-keep class com.umeng.** {*;}

-keep class org.repackage.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 友盟 SDK需要引用导入工程的资源文件，通过了反射机制得到资源引用文件R.java，
# 但是在通过proguard等混淆/优化工具处理apk时，proguard可能会将R.java删除，如果遇到这个问题，请添加如下配置：
-keep public class com.jcs.where.R$*{
    public static final int *;
}

# 友盟 U-APM 性能监控
#-keep class com.uc.** { *; }
#-keep class com.efs.** { *; }

# 极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }