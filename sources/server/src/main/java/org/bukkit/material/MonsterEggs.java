/*
 * Akarin Forge
 */
package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TexturedMaterial;

public class MonsterEggs
extends TexturedMaterial {
    private static final List<Material> textures = new ArrayList<Material>();

    public MonsterEggs() {
        super(Material.MONSTER_EGGS);
    }

    @Deprecated
    public MonsterEggs(int type) {
        super(type);
    }

    public MonsterEggs(Material type) {
        super(textures.contains((Object)type) ? Material.MONSTER_EGGS : type);
        if (textures.contains((Object)type)) {
            this.setMaterial(type);
        }
    }

    @Deprecated
    public MonsterEggs(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public MonsterEggs(Material type, byte data) {
        super(type, data);
    }

    @Override
    public List<Material> getTextures() {
        return textures;
    }

    @Override
    public MonsterEggs clone() {
        return (MonsterEggs)super.clone();
    }

    static {
        textures.add(Material.STONE);
        textures.add(Material.COBBLESTONE);
        textures.add(Material.SMOOTH_BRICK);
    }
}

