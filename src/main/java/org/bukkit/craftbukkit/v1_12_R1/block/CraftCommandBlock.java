/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftCommandBlock
extends CraftBlockEntityState<avm>
implements CommandBlock {
    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block, avm.class);
    }

    public CraftCommandBlock(Material material, avm te2) {
        super(material, te2);
    }

    @Override
    public void load(avm commandBlock) {
        super.load(commandBlock);
        this.command = commandBlock.a().m();
        this.name = commandBlock.a().h_();
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    @Override
    public void applyTo(avm commandBlock) {
        super.applyTo(commandBlock);
        commandBlock.a().a(this.command);
        commandBlock.a().b(this.name);
    }
}

