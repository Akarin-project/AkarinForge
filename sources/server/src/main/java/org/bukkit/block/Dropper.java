/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.block.Container;

public interface Dropper
extends Container,
Nameable {
    public void drop();
}

