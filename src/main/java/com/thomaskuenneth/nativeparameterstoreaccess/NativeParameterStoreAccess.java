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