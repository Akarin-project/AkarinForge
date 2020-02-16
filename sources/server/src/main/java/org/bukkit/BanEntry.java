/*
 * Akarin Forge
 */
package org.bukkit;

import java.util.Date;

public interface BanEntry {
    public String getTarget();

    public Date getCreated();

    public void setCreated(Date var1);

    public String getSource();

    public void setSource(String var1);

    public Date getExpiration();

    public void setExpiration(Date var1);

    public String getReason();

    public void setReason(String var1);

    public void save();
}

