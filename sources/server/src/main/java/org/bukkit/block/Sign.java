/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.block.BlockState;

public interface Sign
extends BlockState {
    public String[] getLines();

    public String getLine(int var1) throws IndexOutOfBoundsException;

    public void setLine(int var1, String var2) throws IndexOutOfBoundsException;
}

