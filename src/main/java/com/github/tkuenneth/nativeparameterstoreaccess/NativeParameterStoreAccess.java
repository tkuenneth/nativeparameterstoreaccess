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

/**
 * A couple of constants.
 *
 * @author Thomas Kuenneth
 */
public final class NativeParameterStoreAccess {

    static final String OS_NAME_LC = System.getProperty("os.name", "").toLowerCase();

    /**
     * If running on Windows, <code>true</code> otherwise <code>false</code>
     */
    public static final boolean IS_WINDOWS = OS_NAME_LC.contains("windows");

    /**
     * If running on macOS, <code>true</code> otherwise <code>false</code>
     */
    public static final boolean IS_MACOS = OS_NAME_LC.contains("mac os x");

    private NativeParameterStoreAccess() {
    }

    static boolean execute(StringBuilder stdin,
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
                stderr.append(e.toString());
            }
            while (is.available() > 0) {
                stdin.append((char) is.read());
            }
            while (es.available() > 0) {
                stderr.append((char) es.read());
            }
            success = true;
        } catch (IOException e) {
            stderr.append(e.toString());
        }
        return success;
    }
}
