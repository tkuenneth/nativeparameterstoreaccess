## Welcome to Native Parameter Store Acess
### A small Java library to access the Windows registry and the macOS Defaults database

Sometime you need to access the Windows registry or the macOS Defaults database in your Java or Kotlin apps. While the Java standard class library includes the Preferecnes api to read and write app-specific values, you cannot use it to access system settings. That's what this tiny library is for. Currently you can only read values, but I plan to add write access in future versions.

#### Examples

This Kotlin example shows you how to detect if the system is using a dark theme.

```kotlin
fun isSystemInDarkTheme(): Boolean {
  return when {
    IS_WINDOWS -> {
      val result = getWindowsRegistryEntry(
          "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
          "AppsUseLightTheme",
          REG_TYPE.REG_DWORD)
      result == "0x0"
    }
    IS_MACOS -> {
      val result = getDefaultsEntry("AppleInterfaceStyle")
      result == "Dark"
    }
    else -> false
  }
}
```