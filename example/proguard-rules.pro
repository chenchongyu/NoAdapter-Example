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

-keep class com.baidu.cloudsdk.common.http.** {*;}

-dontwarn com.tencent.**
-dontwarn com.google.**
-dontwarn rx.internal.**
-dontwarn okio.**
-dontwarn retrofit2.**

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# pass网络库混淆配置
-keep class com.baidu.pass.http.** {*;}
# Pass 活体识别SDK配置
-dontwarn com.baidu.pass.biometrics.**
-keep class com.baidu.pass.biometrics.**{*;}
-dontwarn com.baidu.idl.**
-keep class com.baidu.idl.**{*;}
# 移动SDK混淆配置
-dontwarn com.cmic.sso.sdk.**
-keep public class com.cmic.sso.sdk.*{*;}

# eventbus
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex; long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

# Dagger2
-dontwarn com.google.errorprone.annotations.*

# Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# 配置Gson的解析对象及变量都不混淆。
-keep class com.baidu.adu.hmi.pilotpanel.model.vop.bean.** { *; }


# ReactiveNetwork
-dontwarn com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
-dontwarn io.reactivex.functions.Function
-dontwarn rx.internal.util.**
-dontwarn sun.misc.Unsafe

# MTJ移动统计
-keep class com.baidu.mobstat.** { *; }
-keep class com.baidu.bottom.** { *; }

# 百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.vi.** {*;}
-keep class com.baidu.mapsdkplatform.**{*;}
-keep class com.baidu.navisdk.**{*;}
-keep class org.apache.**{*;}

# BaiduNavi SDK
-dontoptimize
-ignorewarnings
-keeppackagenames com.baidu.**
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod

-dontwarn com.baidu.navisdk.**
-dontwarn com.baidu.navi.**

-keep interface com.baidu.** { *; }


-dontwarn com.google.android.support.v4.**
-keep class com.google.android.support.v4.** { *; }
-keep interface com.google.android.support.v4.app.** { *; }
-keep public class * extends com.google.android.support.v4.**
-keep public class * extends com.google.android.support.v4.app.Fragment

# 保持 native 的方法不去混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持枚举 enum 不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留在Activity中的方法参数是view的方法，
# 从而我们在layout里面编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保留自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class **.R$* { *; }

-keepclassmembers class * {
    void *(**On*Event);
}

# 保留了继承自Activity、Application这些类的子类
# 因为这些子类，都有可能被外部调用
# 比如说，第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

-dontwarn android.support.v4.**
-keep class android.support.v4.**  { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-keep class com.blankj.utilcode.constant.** {*;}
-keep class com.blankj.utilcode.util.** {*;}

# ProGuard configurations for XRAY SDK
-keep class com.baidu.xray.agent.** { *; }
-dontwarn com.baidu.xray.agent.**
-ignorewarnings
# End XRAY SDK configurations

-keep public class com.baidu.crabsdk.**
-keepclassmembers public class com.baidu.crabsdk.*{
    *;
}
# MTJ
-keep class com.baidu.bottom.** { *; }
-keep class com.baidu.mobstat.** { *; }
# MTJ END

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keep public class com.baidu.mapapi.**

# wx pay
-keep class com.tencent.mm.opensdk.** {

*;

}

-keep class com.tencent.wxop.** {

*;

}

-keep class com.tencent.mm.sdk.** {

*;

}

-keep class com.baidu.poly.** {*;}
#wx pay end

#passport
-keep class com.baidu.sapi2.** {*;}
-keepattributes JavascriptInterface
-keepattributes *Annotation*
 # 网络库混淆配置
-keep class com.baidu.pass.http.** {*;}
# 安全SDK
-keepclasseswithmembers class com.baidu.sofire.jni.Asc {*;}
-keep class com.baidu.sofire.ac.Callback {*;}
-keep class com.baidu.sofire.ac.FI {*;}
-keepclasseswithmembers class com.baidu.sofire.ac.F{*;}
-keep class com.baidu.sofire.ac.FH {*;}
-keep class com.baidu.sofire.ac.U {*;}
-keep class com.baidu.sofire.core.ApkInfo {*;}
-keep class com.baidu.sofire.rp.Report {
        <methods>;
}
-keep class com.baidu.cloudsdk.common.http.** {
        *;
}


-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**

-keep class okhttp3.**{ *; }
-keep class okio.**{ *; }
-keep class org.conscrypt.** { *; }

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keep class com.baidubce.** {*;}
-keep class * implements com.baidu.searchbox.NoProGuard {*;}