## Welcome to Native Parameter Store Acess
### A small Java library to access the Windows registry and the macOS Defaults database

This example shows you how to detect if the system is using a dark theme.

```kotlin
fun isSystemInDarkTheme(): Boolean {
  return when {
    IS_WINDOWS -> {
      val result = getWindowsRegistryEntry(
        "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
        "AppsUseLightTheme",
        "REG_DWORD")
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