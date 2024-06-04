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

import static com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.*;

/**
 * Provides access to dconf.
 *
 * @author Thomas Kuenneth
 */
public class Dconf {

    /**
     * Is <code>true</code> if the operating system is Linux and dconf is
     * present, otherwise <code>false</code>
     */
    public static final boolean HAS_DCONF = hasDconf();

    private Dconf() {
    }

    /**
     * Gets an entry from dconf.
     *
     * @param key the key, for example
     * <code>/org/gnome/desktop/interface/gtk-theme</code>
     * @return the result or an empty string
     */
    public static String getDconfEntry(String key) {
        StringBuilder stderr = new StringBuilder();
        return getDconfEntry(key, stderr);
    }

    /**
     * Gets an entry from dconf.
     *
     * @param key the key, for example
     * <code>/org/gnome/desktop/interface/gtk-theme</code>
     * @param stderr may contain error messages
     * @return the result or an empty string
     */
    public static String getDconfEntry(String key,
            StringBuilder stderr) {
        String result = "";
        if (IS_LINUX) {
            StringBuilder stdin = new StringBuilder();
            String cmd = String.format("dconf read %s", key);
            if (execute(stdin, stderr, cmd)) {
                result = stdin.toString().trim();
            }
        }
        return result;
    }

    private static boolean hasDconf() {
        boolean result = false;
        if (IS_LINUX) {
            StringBuilder stdin = new StringBuilder();
            StringBuilder stderr = new StringBuilder();
            String cmd = "dconf list /";
            if (execute(stdin, stderr, cmd)) {
                result = stdin.length() > 0 && stderr.length() == 0;
            }
        }
        return result;
    }
}
