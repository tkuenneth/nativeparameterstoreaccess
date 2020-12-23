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
package com.thomaskuenneth.nativeparameterstoreaccess;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeParameterStoreAccess {

    private static final Logger LOGGER = Logger.getLogger(NativeParameterStoreAccess.class.getPackageName());

    public static String getWindowsRegistryEntry(String key, String value, String type) {
        StringBuilder stdin = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        String result = null;
        String cmd = String.format("reg query \"%s\" /v %s", key, value);
        if (execute(stdin, stderr, cmd)) {
            String temp = stdin.toString();
            int pos = temp.indexOf(type);
            if (pos >= 0) {
                result = temp.substring(pos + type.length()).trim();
            }
        }
        return result;
    }

    private static boolean execute(StringBuilder sbIS, StringBuilder sbES, String cmd) {
        boolean success = false;
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
                    sbIS.append((char) isData);
                }
                if (esData != -1) {
                    sbES.append((char) esData);
                }
                hasData = isData != -1 || esData != -1;
            }
            success = true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        if (sbES.length() > 0) {
            LOGGER.log(Level.SEVERE, sbES.toString());
        }
        return success;
    }
}
