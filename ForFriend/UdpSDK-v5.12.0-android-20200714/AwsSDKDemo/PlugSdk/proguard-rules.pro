-ignorewarnings
-keepattributes SourceFile, LineNumberTable, *Annotation*, Signature, EnclosingMethod, InnerClasses, EnclosingMethod

-keep class com.naver.cafe.** { *; }
-keep interface com.naver.cafe.** { *; }
-keepclassmembers class com.naver.cafe.** {
    <fields>;
    <methods>;
}

-keep class com.naver.glink.** { *; }
-keep interface com.naver.glink.** { *; }

-keep class com.naver.plug.** { *; }
-keep interface com.naver.plug.** { *; }

-keep class com.nhn.** { *; }
-keep interface com.nhn.** { *; }

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.Gson
-keep class com.google.gson.stream.** { *; }

-keepclassmembers class com.google.gson.** {
    <fields>;
    <methods>;
}

-keep class com.bumptech.glide.** { *; }
-keep interface com.bumptech.glide.** { *; }

-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** {*;}

-keep class com.navercorp.volleyextensions.** { *; }
-keep interface com.navercorp.volleyextensions.** { *; }

-keep class com.squareup.** { *; }
-keep interface com.squareup.** { *; }

-dontwarn com.nhn.android.neoid.**
-dontwarn com.navercorp.volleyextensions.volleyer.response.parser.**



-keep public class com.games.plug.SdkPlugUtils {
    public <fields>;
    public <methods>;
}
