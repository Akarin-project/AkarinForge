/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftBanner
extends CraftBlockEntityState<avf>
implements Banner {
    private DyeColor base;
    private List<Pattern> patterns;

    public CraftBanner(Block block) {
        super(block, avf.class);
    }

    public CraftBanner(Material material, avf te2) {
        super(material, te2);
    }

    @Override
    public void load(avf banner) {
        super.load(banner);
        this.base = DyeColor.getByDyeData((byte)banner.f.b());
        this.patterns = new ArrayList<Pattern>();
        if (banner.g != null) {
            for (int i2 = 0; i2 < banner.g.c(); ++i2) {
                fy p2 = (fy)banner.g.i(i2);
                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte)p2.h("Color")), PatternType.getByIdentifier(p2.l("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(this.patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i2) {
        return this.patterns.get(i2);
    }

    @Override
    public Pattern removePattern(int i2) {
        return this.patterns.remove(i2);
    }

    @Override
    public void setPattern(int i2, Pattern pattern) {
        this.patterns.set(i2, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return this.patterns.size();
    }

    @Override
    public void applyTo(avf banner) {
        super.applyTo(banner);
        banner.f = ahs.a(this.base.getDyeData());
        ge newPatterns = new ge();
        for (Pattern p2 : this.patterns) {
            fy compound = new fy();
            compound.a("Color", (int)p2.getColor().getDyeData());
            compound.a("Pattern", p2.getPattern().getIdentifier());
            newPatterns.a(compound);
        }
        banner.g = newPatterns;
    }
}

