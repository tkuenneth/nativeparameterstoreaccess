/*
 * MIT License
 * 
 * Copyright (c) 2020 Thomas Kuenneth
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

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides access to native parameter stores (Windows registry and macOS
 * defaults).
 *
 * @author Thomas Kuenneth
 */
public final class NativeParameterStoreAccess {

    private static final Logger LOGGER = Logger.getLogger(NativeParameterStoreAccess.class.getPackageName());
    private static final String OS_NAME_LC = System.getProperty("os.name", "").toLowerCase();

    /**
     * If running on Windows, <code>true</code> otherwise <code>false</code>
     */
    public static final boolean IS_WINDOWS = OS_NAME_LC.contains("windows");

    /**
     * If running on macOS, <code>true</code> otherwise <code>false</code>
     */
    public static final boolean IS_MACOS = OS_NAME_LC.contains("mac os x");

    /**
     * Windows registry types
     */
    public enum REG_TYPE {
        REG_BINARY, REG_DWORD, REG_EXPAND_SZ, REG_MULTI_SZ, REG_SZ
    }

    private NativeParameterStoreAccess() {
    }

    /**
     * Gets an entry from the Windows registry.
     *
     * @param key the key, for example
     * <code>"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize"</code>
     * @param value the value, for example <code>"AppsUseLightTheme"</code>
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
     * <code>"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize"</code>
     * @param value the value, for example <code>"AppsUseLightTheme"</code>
     * @param type the type, for example <code>REG_DWORD</code>
     * @param stderr may contain error messages
     * @return the result or an empty string
     */
    public static String getWindowsRegistryEntry(String key,
            String value,
            REG_TYPE type,
            StringBuilder stderr) {
        String result = null;
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
     * Gets an entry from the Defaults database.
     *
     * @param key the key, for example <code>"AppleInterfaceStyle"</code>
     * @return the result or an empty string
     */
    public static String getDefaultsEntry(String key) {
        StringBuilder stderr = new StringBuilder();
        return getDefaultsEntry(key, stderr);
    }

    /**
     * Gets an entry from the Defaults database.
     *
     * @param key the key, for example <code>"AppleInterfaceStyle"</code>
     * @param stderr may contain error messages
     * @return the result or an empty string
     */
    public static String getDefaultsEntry(String key,
            StringBuilder stderr) {
        String result = null;
        if (IS_MACOS) {
            StringBuilder stdin = new StringBuilder();
            String cmd = String.format("defaults read -g %s", key);
            if (execute(stdin, stderr, cmd)) {
                result = stdin.toString().trim();
            }
        }
        return result;
    }

    private static boolean execute(StringBuilder stdin,
            StringBuilder stderr,
            String cmd) {
        boolean success = false;
        stdin.setLength(0);
        stderr.setLength(0);
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            InputStream is = p.getInputStream();
            InputStream es = p.getErrorStream();
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            boolean hasData = true;
            int isData;
            int esData;
            while (hasData) {
                if (is.available() > 0) {
                    isData = is.read();
                } else {
                    isData = -1;
                }
                if (es.available() > 0) {
                    esData = es.read();
                } else {
                    esData = -1;
                }
                if (isData != -1) {
                    stdin.append((char) isData);
                }
                if (esData != -1) {
                    stderr.append((char) esData);
                }
                hasData = isData != -1 || esData != -1;
            }
            success = true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return success;
    }
}
