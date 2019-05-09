/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.util;

import java.util.Collection;
import org.apache.commons.lang.Validate;

public class StringUtil {
    public static <T extends Collection<? super String>> T copyPartialMatches(String token, Iterable<String> originals, T collection) throws UnsupportedOperationException, IllegalArgumentException {
        Validate.notNull((Object)token, (String)"Search token cannot be null");
        Validate.notNull(collection, (String)"Collection cannot be null");
        Validate.notNull(originals, (String)"Originals cannot be null");
        for (String string : originals) {
            if (!StringUtil.startsWithIgnoreCase(string, token)) continue;
            collection.add((String)string);
        }
        return collection;
    }

    public static boolean startsWithIgnoreCase(String string, String prefix) throws IllegalArgumentException, NullPointerException {
        Validate.notNull((Object)string, (String)"Cannot check a null string for a match");
        if (string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}

