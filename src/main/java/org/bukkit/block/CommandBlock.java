/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.block.BlockState;

public interface CommandBlock
extends BlockState {
    public String getCommand();

    public void setCommand(String var1);

    public String getName();

    public void setName(String var1);
}

