/*
 * Decompiled with CFR 0_132.
 */
package com.mms.util;

class IStringUtils {
    IStringUtils() {
    }

    public static boolean isEmpty(CharSequence str) {
        if (str != null && str.length() != 0) {
            return false;
        }
        return true;
    }
}
