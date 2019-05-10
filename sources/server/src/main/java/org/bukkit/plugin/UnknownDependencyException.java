/*
 * Akarin Forge
 */
package org.bukkit.plugin;

public class UnknownDependencyException
extends RuntimeException {
    private static final long serialVersionUID = 5721389371901775895L;

    public UnknownDependencyException(Throwable throwable) {
        super(throwable);
    }

    public UnknownDependencyException(String message) {
        super(message + " \u4f60\u7684\u63d2\u4ef6\u6ca1\u88c5\u4f9d\u8d56,\u518d\u95ee\u81ea\u6740! ");
    }

    public UnknownDependencyException(Throwable throwable, String message) {
        super(message, throwable);
    }

    public UnknownDependencyException() {
    }
}

