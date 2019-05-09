/*
 * Akarin Forge
 */
package org.bukkit.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.io.Wrapper;

public class BukkitObjectInputStream
extends ObjectInputStream {
    protected BukkitObjectInputStream() throws IOException, SecurityException {
        super.enableResolveObject(true);
    }

    public BukkitObjectInputStream(InputStream in2) throws IOException {
        super(in2);
        super.enableResolveObject(true);
    }

    @Override
    protected Object resolveObject(Object obj) throws IOException {
        if (obj instanceof Wrapper) {
            try {
                obj = ConfigurationSerialization.deserializeObject(((Wrapper)obj).map);
                obj.getClass();
            }
            catch (Throwable ex2) {
                throw BukkitObjectInputStream.newIOException("Failed to deserialize object", ex2);
            }
        }
        return super.resolveObject(obj);
    }

    private static IOException newIOException(String string, Throwable cause) {
        IOException exception = new IOException(string);
        exception.initCause(cause);
        return exception;
    }
}

