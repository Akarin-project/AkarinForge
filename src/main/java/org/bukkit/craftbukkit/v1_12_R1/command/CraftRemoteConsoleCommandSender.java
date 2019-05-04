/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.command;

import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.ServerCommandSender;

public class CraftRemoteConsoleCommandSender
extends ServerCommandSender
implements RemoteConsoleCommandSender {
    private final px listener;

    public CraftRemoteConsoleCommandSender(px listener) {
        this.listener = listener;
    }

    @Override
    public void sendMessage(String message) {
        this.listener.a(new ho(message + "\n"));
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return "Rcon";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}

