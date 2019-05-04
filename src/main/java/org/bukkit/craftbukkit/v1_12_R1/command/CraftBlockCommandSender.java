/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.command;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.command.ServerCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;

public class CraftBlockCommandSender
extends ServerCommandSender
implements BlockCommandSender {
    private final bn block;

    public CraftBlockCommandSender(bn commandBlockListenerAbstract) {
        this.block = commandBlockListenerAbstract;
    }

    @Override
    public Block getBlock() {
        return this.block.e().getWorld().getBlockAt(this.block.c().p(), this.block.c().q(), this.block.c().r());
    }

    @Override
    public void sendMessage(String message) {
        for (hh component : CraftChatMessage.fromString(message)) {
            this.block.a(component);
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
        return this.block.h_();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public bn getTileEntity() {
        return this.block;
    }
}

