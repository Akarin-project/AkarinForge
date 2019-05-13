/*
 * Akarin Forge
 */
package org.bukkit;

@Deprecated
public interface BlockChangeDelegate {
    @Deprecated
    public boolean setRawTypeId(int var1, int var2, int var3, int var4);

    @Deprecated
    public boolean setRawTypeIdAndData(int var1, int var2, int var3, int var4, int var5);

    @Deprecated
    public boolean setTypeId(int var1, int var2, int var3, int var4);

    @Deprecated
    public boolean setTypeIdAndData(int var1, int var2, int var3, int var4, int var5);

    @Deprecated
    public int getTypeId(int var1, int var2, int var3);

    public int getHeight();

    public boolean isEmpty(int var1, int var2, int var3);
}

