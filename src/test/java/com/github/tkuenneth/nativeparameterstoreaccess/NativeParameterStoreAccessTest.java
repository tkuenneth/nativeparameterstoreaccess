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

import static com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.*;
import com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry.REG_TYPE;
import static com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry.REG_TYPE.REG_SZ;
import static com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Some tests.
 *
 * @author Thomas Kuenneth
 */
public class NativeParameterStoreAccessTest {

    /**
     * Test if the product name contains <code>Windows</code>
     */
    @Test
    public void testGetWindowsRegistryEntry() {
        assumeTrue(IS_WINDOWS);
        String key = "HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion";
        String value = "ProductName";
        REG_TYPE type = REG_SZ;
        String result = getWindowsRegistryEntry(key, value, type);
        assertTrue(result.contains("Windows"));
    }

    /**
     * Test if toggling dark mode works
     */
    @Test
    public void testSetWindowsRegistryEntry() {
        assumeTrue(IS_WINDOWS);
        String key = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize";
        String value = "AppsUseLightTheme";
        int currentData = getWindowsRegistryEntry(key, value);
        int newData = currentData == 0 ? 1 : 0;
        assertTrue(setWindowsRegistryEntry(key, value, newData));
        assertTrue(newData == getWindowsRegistryEntry(key, value));
    }
}
