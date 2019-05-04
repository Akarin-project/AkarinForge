/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialData
implements Cloneable {
    private final int type;
    private byte data = 0;

    @Deprecated
    public MaterialData(int type) {
        this(type, 0);
    }

    public MaterialData(Material type) {
        this(type, 0);
    }

    @Deprecated
    public MaterialData(int type, byte data) {
        this.type = type;
        this.data = data;
    }

    @Deprecated
    public MaterialData(Material type, byte data) {
        this(type.getId(), data);
    }

    @Deprecated
    public byte getData() {
        return this.data;
    }

    @Deprecated
    public void setData(byte data) {
        this.data = data;
    }

    public Material getItemType() {
        return Material.getMaterial(this.type);
    }

    @Deprecated
    public int getItemTypeId() {
        return this.type;
    }

    @Deprecated
    public ItemStack toItemStack() {
        return new ItemStack(this.type, 0, this.data);
    }

    public ItemStack toItemStack(int amount) {
        return new ItemStack(this.type, amount, this.data);
    }

    public String toString() {
        return (Object)((Object)this.getItemType()) + "(" + this.getData() + ")";
    }

    public int hashCode() {
        return this.getItemTypeId() << 8 ^ this.getData();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MaterialData) {
            MaterialData md2 = (MaterialData)obj;
            return md2.getItemTypeId() == this.getItemTypeId() && md2.getData() == this.getData();
        }
        return false;
    }

    public MaterialData clone() {
        try {
            return (MaterialData)super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new Error(e2);
        }
    }
}

