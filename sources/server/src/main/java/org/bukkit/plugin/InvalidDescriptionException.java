/*
 * Akarin Forge
 */
package org.bukkit.plugin;

public class InvalidDescriptionException
extends Exception {
    private static final long serialVersionUID = 5721389122281775896L;

    public InvalidDescriptionException(Throwable cause, String message) {
        super(message, cause);
    }

    public InvalidDescriptionException(Throwable cause) {
        super("Invalid plugin.yml", cause);
    }

    public InvalidDescriptionException(String message) {
        super(message);
    }

    public InvalidDescriptionException() {
        super("Invalid plugin.yml");
    }
}

