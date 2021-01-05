## Welcome to Native Parameter Store Acess

### A small Java library to access the Windows Registry, macOS Defaults database and dconf.

Sometime you need to access the Windows Registry, the macOS Defaults database or dconf in your Java or Kotlin apps.
While the Java standard class library includes the Preferences api to read and write app-specific values, you cannot use
it to access system settings. That's what this tiny library is for.

#### Include in your project

It is very easy to add the library to your project.

##### Maven

```xml

<dependency>
    <groupId>com.github.tkuenneth</groupId>
    <artifactId>nativeparameterstoreaccess</artifactId>
    <version>0.1.1</version>
</dependency>
```

##### Gradle

```
dependencies {
  implementation("com.github.tkuenneth:nativeparameterstoreaccess:0.1.1")
}
```

#### Examples

This Kotlin example shows how to detect if the system (macOS or Windows) is using a dark theme.

```kotlin
fun isSystemInDarkTheme(): Boolean {
    return when {
        IS_WINDOWS -> {
            val result = getWindowsRegistryEntry(
                    "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                    "AppsUseLightTheme")
            result == 0x0
        }
        IS_MACOS -> {
            val result = getDefaultsEntry("AppleInterfaceStyle")
            result == "Dark"
        }
        else -> false
    }
}
```