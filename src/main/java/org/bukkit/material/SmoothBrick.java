/*
 * Akarin Forge
 */
package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TexturedMaterial;

public class SmoothBrick
extends TexturedMaterial {
    private static final List<Material> textures = new ArrayList<Material>();

    public SmoothBrick() {
        super(Material.SMOOTH_BRICK);
    }

    @Deprecated
    public SmoothBrick(int type) {
        super(type);
    }

    public SmoothBrick(Material type) {
        super(textures.contains((Object)type) ? Material.SMOOTH_BRICK : type);
        if (textures.contains((Object)type)) {
            this.setMaterial(type);
        }
    }

    @Deprecated
    public SmoothBrick(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public SmoothBrick(Material type, byte data) {
        super(type, data);
    }

    @Override
    public List<Material> getTextures() {
        return textures;
    }

    @Override
    public SmoothBrick clone() {
        return (SmoothBrick)super.clone();
    }

    static {
        textures.add(Material.STONE);
        textures.add(Material.MOSSY_COBBLESTONE);
        textures.add(Material.COBBLESTONE);
        textures.add(Material.SMOOTH_BRICK);
    }
}

