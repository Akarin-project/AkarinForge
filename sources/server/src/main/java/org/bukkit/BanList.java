/*
 * Akarin Forge
 */
package org.bukkit;

import java.util.Date;
import java.util.Set;
import org.bukkit.BanEntry;

public interface BanList {
    public BanEntry getBanEntry(String var1);

    public BanEntry addBan(String var1, String var2, Date var3, String var4);

    public Set<BanEntry> getBanEntries();

    public boolean isBanned(String var1);

    public void pardon(String var1);

    public static enum Type {
        NAME,
        IP;
        

        private Type() {
        }
    }

}

