/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.meta.ItemMeta;

public interface BannerMeta
extends ItemMeta {
    @Deprecated
    public DyeColor getBaseColor();

    @Deprecated
    public void setBaseColor(DyeColor var1);

    public List<Pattern> getPatterns();

    public void setPatterns(List<Pattern> var1);

    public void addPattern(Pattern var1);

    public Pattern getPattern(int var1);

    public Pattern removePattern(int var1);

    public void setPattern(int var1, Pattern var2);

    public int numberOfPatterns();
}

