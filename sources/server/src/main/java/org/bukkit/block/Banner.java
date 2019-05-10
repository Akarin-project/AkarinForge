/*
 * Akarin Forge
 */
package org.bukkit.block;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;

public interface Banner
extends BlockState {
    public DyeColor getBaseColor();

    public void setBaseColor(DyeColor var1);

    public List<Pattern> getPatterns();

    public void setPatterns(List<Pattern> var1);

    public void addPattern(Pattern var1);

    public Pattern getPattern(int var1);

    public Pattern removePattern(int var1);

    public void setPattern(int var1, Pattern var2);

    public int numberOfPatterns();
}

