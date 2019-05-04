/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  jline.Terminal
 *  jline.console.ConsoleReader
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import jline.Terminal;
import jline.console.ConsoleReader;
import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread
extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            this.server.u();
        }
        catch (amv ex2) {
            ex2.printStackTrace();
        }
        finally {
            try {
                this.server.reader.getTerminal().restore();
            }
            catch (Exception ex2) {}
        }
    }
}

