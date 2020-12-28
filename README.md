## Welcome to Native Parameter Store Acess
### A small Java library to access the Windows Registry and the macOS Defaults database

Sometime you need to access the Windows Registry or the macOS Defaults database in your Java or Kotlin apps. While the Java standard class library includes the Preferences api to read and write app-specific values, you cannot use it to access system settings. That's what this tiny library is for.

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