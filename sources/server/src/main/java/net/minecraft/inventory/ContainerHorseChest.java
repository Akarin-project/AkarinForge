package net.minecraft.inventory;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHorseChest extends InventoryBasic
{
    public ContainerHorseChest(String inventoryTitle, int slotCount)
    {
        super(inventoryTitle, false, slotCount);
    }
    // Akarin start
    public ContainerHorseChest(String s, int i, AbstractHorse owner) {
        super(s, false, i, (org.bukkit.entity.AbstractHorse) owner.getBukkitEntity());
    }
    // Akarin end

    @SideOnly(Side.CLIENT)
    public ContainerHorseChest(ITextComponent inventoryTitle, int slotCount)
    {
        super(inventoryTitle, slotCount);
    }
}