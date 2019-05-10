/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 */
package org.bukkit.command;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Server;
import org.bukkit.permissions.Permissible;

public interface CommandSender
extends Permissible {
    public void sendMessage(String var1);

    public void sendMessage(String[] var1);

    public Server getServer();

    public String getName();

    public Spigot spigot();

    public static class Spigot {
        public void sendMessage(BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public /* varargs */ void sendMessage(BaseComponent ... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}

