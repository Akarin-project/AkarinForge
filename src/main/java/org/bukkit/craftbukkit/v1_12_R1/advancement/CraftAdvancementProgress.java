/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package org.bukkit.craftbukkit.v1_12_R1.advancement;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.craftbukkit.v1_12_R1.advancement.CraftAdvancement;

public class CraftAdvancementProgress
implements AdvancementProgress {
    private final CraftAdvancement advancement;
    private final np playerData;
    private final k handle;

    public CraftAdvancementProgress(CraftAdvancement advancement, np player, k handle) {
        this.advancement = advancement;
        this.playerData = player;
        this.handle = handle;
    }

    @Override
    public Advancement getAdvancement() {
        return this.advancement;
    }

    @Override
    public boolean isDone() {
        return this.handle.a();
    }

    @Override
    public boolean awardCriteria(String criteria) {
        return this.playerData.a(this.advancement.getHandle(), criteria);
    }

    @Override
    public boolean revokeCriteria(String criteria) {
        return this.playerData.b(this.advancement.getHandle(), criteria);
    }

    @Override
    public Date getDateAwarded(String criteria) {
        o criterion = this.handle.c(criteria);
        return criterion == null ? null : criterion.d();
    }

    @Override
    public Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(this.handle.e()));
    }

    @Override
    public Collection<String> getAwardedCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(this.handle.f()));
    }
}

