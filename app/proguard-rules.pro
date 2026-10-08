# Reglas de R8 para la versión de release del visor remoto.
#
# Lo importante: Gson arma y lee el JSON que se intercambia con la tablet principal (por Bluetooth)
# y con el servidor usando los NOMBRES de los campos de las clases. Si R8 los renombra, los
# mensajes salen con claves como "a", "b"... y se rompe la comunicación con tablets ya instaladas
# (sin error al compilar). Por eso todo el paquete model se conserva tal cual.

# Números de línea para leer los errores de Play Console con el código original.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes Signature,InnerClasses,EnclosingMethod,Exceptions,*Annotation*

# ---- Modelo de la app: entidades (Room, Gson, Parcelize, safe-args) y APIs de Retrofit ----
-keep class com.basculasmagris.visorremotomixer.model.** { *; }

# ---- Gson ----
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn sun.misc.**

# ---- Retrofit 2.9 (no trae reglas para el modo completo de R8) ----
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking class io.reactivex.rxjava3.core.Single
-keep,allowobfuscation,allowshrinking class io.reactivex.rxjava3.core.Observable
-keep,allowobfuscation,allowshrinking class io.reactivex.rxjava3.core.Completable
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# ---- OkHttp ----
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
