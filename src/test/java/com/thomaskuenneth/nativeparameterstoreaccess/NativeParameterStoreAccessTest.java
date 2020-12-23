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

import static com.thomaskuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.getWindowsRegistryEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Some tests.
 *
 * @author Thomas Kuenneth
 */
public class NativeParameterStoreAccessTest {

    private static final boolean IS_ON_WINDOWS = System.getProperty("os.name").contains("Windowt");

    /**
     * Test if the product name contains <code>Windows</code>
     */
    @Test
    public void testGetWindowsRegistryEntry() {
        assumeTrue(IS_ON_WINDOWS);
        String key = "HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion";
        String value = "ProductName";
        String type = "REG_SZ";
        String result = getWindowsRegistryEntry(key, value, type);
        System.out.println(result);
        assertTrue(result.contains("Windows"));
    }
}
