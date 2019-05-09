/*
 * Akarin Forge
 */
package org.bukkit.permissions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum PermissionDefault {
    TRUE("true"),
    FALSE("false"),
    OP("op", "isop", "operator", "isoperator", "admin", "isadmin"),
    NOT_OP("!op", "notop", "!operator", "notoperator", "!admin", "notadmin");
    
    private final String[] names;
    private static final Map<String, PermissionDefault> lookup;

    private /* varargs */ PermissionDefault(String ... names) {
        this.names = names;
    }

    public boolean getValue(boolean op2) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
            case OP: {
                return op2;
            }
            case NOT_OP: {
                return !op2;
            }
        }
        return false;
    }

    public static PermissionDefault getByName(String name) {
        return lookup.get(name.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z!]", ""));
    }

    public String toString() {
        return this.names[0];
    }

    static {
        lookup = new HashMap<String, PermissionDefault>();
        for (PermissionDefault value : PermissionDefault.values()) {
            for (String name : value.names) {
                lookup.put(name, value);
            }
        }
    }

}

