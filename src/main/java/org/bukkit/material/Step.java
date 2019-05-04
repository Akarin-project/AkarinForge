/*
 * Akarin Forge
 */
package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TexturedMaterial;

public class Step
extends TexturedMaterial {
    private static final List<Material> textures = new ArrayList<Material>();

    public Step() {
        super(Material.STEP);
    }

    @Deprecated
    public Step(int type) {
        super(type);
    }

    public Step(Material type) {
        super(textures.contains((Object)type) ? Material.STEP : type);
        if (textures.contains((Object)type)) {
            this.setMaterial(type);
        }
    }

    @Deprecated
    public Step(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Step(Material type, byte data) {
        super(type, data);
    }

    @Override
    public List<Material> getTextures() {
        return textures;
    }

    public boolean isInverted() {
        return (this.getData() & 8) != 0;
    }

    public void setInverted(boolean inv) {
        int dat = this.getData() & 7;
        if (inv) {
            dat |= 8;
        }
        this.setData((byte)dat);
    }

    @Deprecated
    @Override
    protected int getTextureIndex() {
        return this.getData() & 7;
    }

    @Deprecated
    @Override
    protected void setTextureIndex(int idx) {
        this.setData((byte)(this.getData() & 8 | idx));
    }

    @Override
    public Step clone() {
        return (Step)super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + (this.isInverted() ? "inverted" : "");
    }

    static {
        textures.add(Material.STONE);
        textures.add(Material.SANDSTONE);
        textures.add(Material.WOOD);
        textures.add(Material.COBBLESTONE);
        textures.add(Material.BRICK);
        textures.add(Material.SMOOTH_BRICK);
        textures.add(Material.NETHER_BRICK);
        textures.add(Material.QUARTZ_BLOCK);
    }
}

