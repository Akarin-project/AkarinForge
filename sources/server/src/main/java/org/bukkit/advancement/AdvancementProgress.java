/*
 * Akarin Forge
 */
package org.bukkit.advancement;

import java.util.Collection;
import java.util.Date;
import org.bukkit.advancement.Advancement;

public interface AdvancementProgress {
    public Advancement getAdvancement();

    public boolean isDone();

    public boolean awardCriteria(String var1);

    public boolean revokeCriteria(String var1);

    public Date getDateAwarded(String var1);

    public Collection<String> getRemainingCriteria();

    public Collection<String> getAwardedCriteria();
}

