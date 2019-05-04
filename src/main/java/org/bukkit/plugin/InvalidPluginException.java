/*
 * Akarin Forge
 */
package org.bukkit.plugin;

public class InvalidPluginException
extends Exception {
    private static final long serialVersionUID = -8242141640709409544L;

    public InvalidPluginException(Throwable cause) {
        super(cause);
    }

    public InvalidPluginException() {
    }

    public InvalidPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPluginException(String message) {
        super(message);
    }
}

