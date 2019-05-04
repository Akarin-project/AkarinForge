/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.command;

import org.bukkit.craftbukkit.v1_12_R1.command.ServerCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;

public class CraftFunctionCommandSender
extends ServerCommandSender {
    private final bn handle;

    public CraftFunctionCommandSender(bn handle) {
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        for (hh component : CraftChatMessage.fromString(message)) {
            this.handle.a(component);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return this.handle.h_();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server function sender");
    }

    public bn getHandle() {
        return this.handle;
    }
}

