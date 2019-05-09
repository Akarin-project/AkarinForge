/*
 * Akarin Forge
 */
package org.bukkit.material;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public abstract class TexturedMaterial
extends MaterialData {
    public TexturedMaterial(Material m2) {
        super(m2);
    }

    @Deprecated
    public TexturedMaterial(int type) {
        super(type);
    }

    @Deprecated
    public TexturedMaterial(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public TexturedMaterial(Material type, byte data) {
        super(type, data);
    }

    public abstract List<Material> getTextures();

    public Material getMaterial() {
        int n2 = this.getTextureIndex();
        if (n2 > this.getTextures().size() - 1) {
            n2 = 0;
        }
        return this.getTextures().get(n2);
    }

    public void setMaterial(Material material) {
        if (this.getTextures().contains((Object)((Object)material))) {
            this.setTextureIndex(this.getTextures().indexOf((Object)((Object)material)));
        } else {
            this.setTextureIndex(0);
        }
    }

    @Deprecated
    protected int getTextureIndex() {
        return this.getData();
    }

    @Deprecated
    protected void setTextureIndex(int idx) {
        this.setData((byte)idx);
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getMaterial()) + " " + super.toString();
    }

    @Override
    public TexturedMaterial clone() {
        return (TexturedMaterial)super.clone();
    }
}

