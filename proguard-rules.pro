# Keep the main mod class and its methods
-keep public class com.finclient.finclientmc.Finclient_Main {
    public static void main(java.lang.String[]);
}

# Keep all classes annotated with @Mod
-keep @net.minecraftforge.fml.common.Mod class *

# Keep all methods annotated with @SubscribeEvent
-keepclassmembers class * {
    @net.minecraftforge.eventbus.api.SubscribeEvent *;
}

# Keep all fields and methods in classes annotated with @Mod.EventBusSubscriber
-keepclassmembers class * {
    @net.minecraftforge.fml.common.Mod$EventBusSubscriber *;
}

# Keep all annotations
-keepattributes *Annotation*

# Keep the Minecraft and Forge classes
-keep class net.minecraft.** { *; }
-keep class net.minecraftforge.** { *; }

# Obfuscate everything else
-obfuscationdictionary proguard-dictionaries/obfuscation.txt
-classobfuscationdictionary proguard-dictionaries/class_obfuscation.txt
-packageobfuscationdictionary proguard-dictionaries/package_obfuscation.txt