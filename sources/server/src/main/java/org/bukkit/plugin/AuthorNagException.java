/*
 * Akarin Forge
 */
package org.bukkit.plugin;

public class AuthorNagException
extends RuntimeException {
    private final String message;

    public AuthorNagException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

