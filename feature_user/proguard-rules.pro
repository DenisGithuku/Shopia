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
# Copyright (C) 2020 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# We supply these as stubs and are able to link to them at runtime
# because they are hidden public classes in Android. We don't want
# R8 to complain about them not being there during optimization.
-dontwarn android.view.RenderNode
-dontwarn android.view.DisplayListCanvas
-dontwarn android.view.HardwareCanvas

-keepclassmembers class androidx.compose.ui.platform.ViewLayerContainer {
    protected void dispatchGetDisplayList();
}

-keepclassmembers class androidx.compose.ui.platform.AndroidComposeView {
    android.view.View findViewByAccessibilityIdTraversal(int);
}

# Users can create Modifier.Node instances that implement multiple Modifier.Node interfaces,
# so we cannot tell whether two modifier.node instances are of the same type without using
# reflection to determine the class type. See b/265188224 for more context.
-keep,allowshrinking class * extends androidx.compose.ui.node.ModifierNodeElement

# Keep all the functions created to throw an exception. We don't want these functions to be
# inlined in any way, which R8 will do by default. The whole point of these functions is to
# reduce the amount of code generated at the call site.
-keep,allowshrinking,allowobfuscation class androidx.compose.**.* {
    static void throw*Exception(...);
    # For methods returning Nothing
    static java.lang.Void throw*Exception(...);
}

-keep class com.githukudenis.feature_user.ui.profile.ProfileUiState { *; }
-keep class com.githukudenis.feature_user.data.remote.model.UsersDTOItem
-keepclassmembers class com.githukudenis.feature_user.ui.profile.ProfileUiState {
    public *;
}
-keep class com.githukudenis.feature_user.ui.** { *; }
-keepclassmembers class com.githukudenis.feature_user.ui.profile.ProfileUiState {
    *;
}
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }
-keepattributes *Composable*

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn com.githukudenis.core_design.R$drawable
-printconfiguration proguard-configuration.txt
-printusage proguard-usage.txt

# When pointer input modifier nodes are added dynamically and have the same keys (common when
# developers `Unit` for their keys), we need a way to differentiate them and using a
# functional interface and comparing classes allows us to do that.
