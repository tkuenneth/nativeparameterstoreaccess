/*
 * MIT License
 * 
 * Copyright (c) 2020 - 2024 Thomas Kuenneth
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.tkuenneth.nativeparameterstoreaccess;

import static com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.IS_WINDOWS;
import static com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.execute;

/**
 * Provides access to the Windows Registry.
 *
 * @author Thomas Kuenneth
 */
public class WindowsRegistry {

    /**
     * Registry types
     */
    public enum REG_TYPE {
        REG_BINARY, REG_DWORD, REG_EXPAND_SZ, REG_MULTI_SZ, REG_SZ
    }

    private WindowsRegistry() {
    }

    /**
     * Gets an entry from the Windows registry.
     *
     * @param key the key, for example
     * <code>HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize</code>
     * @param value the value, for example <code>AppsUseLightTheme</code>
     * @param type the type, for example <code>REG_DWORD</code>
     * @return the result or an empty string
     */
    public static String getWindowsRegistryEntry(String key,
            String value,
            REG_TYPE type) {
        StringBuilder stderr = new StringBuilder();
        return getWindowsRegistryEntry(key, value, type, stderr);
    }

    /**
     * Gets an entry from the Windows registry.
     *
     * @param key the key, for example
     * <code>HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize</code>
     * @param value the value, for example <code>AppsUseLightTheme</code>
     * @param type the type, for example <code>REG_DWORD</code>
     * @param stderr may contain error messages
     * @return the result or an empty string
     */
    public static String getWindowsRegistryEntry(String key,
            String value,
            REG_TYPE type,
            StringBuilder stderr) {
        String result = "";
        if (IS_WINDOWS) {
            StringBuilder stdin = new StringBuilder();
            String cmd = String.format("reg query \"%s\" /v %s", key, value);
            if (execute(stdin, stderr, cmd)) {
                String temp = stdin.toString();
                String stringType = type.toString();
                int pos = temp.indexOf(stringType);
                if (pos >= 0) {
                    result = temp.substring(pos + stringType.length()).trim();
                }
            }
        }
        return result;
    }

    /**
     * Gets a <code>REG_DWORD</code> from the Windows registry.
     * <code>NumberFormatException</code> may be thrown if the result cannot be
     * obtained, or if it is not a number.
     *
     * @param key the key, for example
     * <code>HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize</code>
     * @param value the value, for example <code>AppsUseLightTheme</code>
     * @return the result
     */
    public static int getWindowsRegistryEntry(String key, String value) {
        var result = getWindowsRegistryEntry(key, value, REG_TYPE.REG_DWORD);
        return Integer.decode(result);
    }

    /**
     * Writes a <code>REG_DWORD</code> into the Windows registry.
     *
     * @param key the key, for example
     * <code>HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize</code>
     * @param value the value, for example <code>AppsUseLightTheme</code>
     * @param data the data to be written
     * @return if successful <code>true</code>, otherwise <code>false</code>
     */
    public static boolean setWindowsRegistryEntry(String key,
            String value,
            int data) {
        StringBuilder stderr = new StringBuilder();
        return setWindowsRegistryEntry(key, value,
                Integer.toHexString(data),
                REG_TYPE.REG_DWORD, stderr);
    }

    /**
     * Writes a <code>REG_DWORD</code> into the Windows registry.
     *
     * @param key the key, for example
     * <code>HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize</code>
     * @param value the value, for example <code>AppsUseLightTheme</code>
     * @param data the data to be written
     * @param type the type, for example <code>REG_DWORD</code>
     * @param stderr may contain error messages
     * @return if successful <code>true</code>, otherwise <code>false</code>
     */
    public static boolean setWindowsRegistryEntry(String key,
            String value,
            String data,
            REG_TYPE type,
            StringBuilder stderr) {
        boolean result = false;
        if (IS_WINDOWS) {
            StringBuilder stdin = new StringBuilder();
            String cmd = String.format("reg add \"%s\" /v %s /t %s /d %s /f",
                    key, value, type.toString(), data);
            if (execute(stdin, stderr, cmd)) {
                result = true;
            }
        }
        return result;
    }
}
