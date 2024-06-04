## Welcome to Native Parameter Store Acess

### A small Java library to access the Windows Registry, macOS Defaults database and dconf.

Sometimes you need to access the Windows Registry, the macOS Defaults database or dconf in your Java or Kotlin apps.
While the Java standard class library includes the Preferences api to read and write app-specific values, you cannot use
it to access system settings. That's what this tiny library is for.

#### Include in your project

It is very easy to add the library to your project.

##### Maven

```xml

<dependency>
    <groupId>com.github.tkuenneth</groupId>
    <artifactId>nativeparameterstoreaccess</artifactId>
    <version>0.1.3</version>
</dependency>
```

##### Gradle

```
dependencies {
  implementation("com.github.tkuenneth:nativeparameterstoreaccess:0.1.3")
}
```

#### Examples

This Kotlin example shows how to detect if the system (macOS, Linux with dconf or Windows) is using a dark theme.

```kotlin
fun isSystemInDarkTheme(): Boolean = when {
    IS_WINDOWS -> {
        val result = WindowsRegistry.getWindowsRegistryEntry(
                "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                "AppsUseLightTheme")
        result == 0x0
    }
    IS_MACOS -> {
        val result = MacOSDefaults.getDefaultsEntry("AppleInterfaceStyle")
        result == "Dark"
    }
    HAS_DCONF -> {
        val result = Dconf.getDconfEntry("/org/gnome/desktop/interface/gtk-theme")
        result.toLowerCase().contains("dark")
    }
    else -> false
}
```