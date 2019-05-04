/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;

public class CraftSign
extends CraftBlockEntityState<awc>
implements Sign {
    private String[] lines;

    public CraftSign(Block block) {
        super(block, awc.class);
    }

    public CraftSign(Material material, awc te2) {
        super(material, te2);
    }

    @Override
    public void load(awc sign) {
        super.load(sign);
        this.lines = new String[sign.a.length];
        System.arraycopy(CraftSign.revertComponents(sign.a), 0, this.lines, 0, this.lines.length);
    }

    @Override
    public String[] getLines() {
        return this.lines;
    }

    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        return this.lines[index];
    }

    @Override
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        this.lines[index] = line;
    }

    @Override
    public void applyTo(awc sign) {
        super.applyTo(sign);
        hh[] newLines = CraftSign.sanitizeLines(this.lines);
        System.arraycopy(newLines, 0, sign.a, 0, 4);
    }

    public static hh[] sanitizeLines(String[] lines) {
        hh[] components = new hh[4];
        for (int i2 = 0; i2 < 4; ++i2) {
            components[i2] = i2 < lines.length && lines[i2] != null ? CraftChatMessage.fromString(lines[i2])[0] : new ho("");
        }
        return components;
    }

    public static String[] revertComponents(hh[] components) {
        String[] lines = new String[components.length];
        for (int i2 = 0; i2 < lines.length; ++i2) {
            lines[i2] = CraftSign.revertComponent(components[i2]);
        }
        return lines;
    }

    private static String revertComponent(hh component) {
        return CraftChatMessage.fromComponent(component);
    }
}

