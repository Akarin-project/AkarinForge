/*
 * Akarin Forge
 */
package org.spigotmc;

public class SneakyThrow {
    public static void sneaky(Throwable t2) {
        throw (RuntimeException)SneakyThrow.superSneaky(t2);
    }

    private static <T extends Throwable> T superSneaky(Throwable t2) throws Throwable {
        throw t2;
    }
}

