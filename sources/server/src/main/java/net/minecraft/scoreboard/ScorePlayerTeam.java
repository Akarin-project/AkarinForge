package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ScorePlayerTeam extends Team
{
    private final Scoreboard scoreboard;
    private final String name;
    private final Set<String> membershipSet = Sets.<String>newHashSet();
    private String displayName;
    private String prefix = "";
    private String suffix = "";
    private boolean allowFriendlyFire = true;
    private boolean canSeeFriendlyInvisibles = true;
    private Team.EnumVisible nameTagVisibility = Team.EnumVisible.ALWAYS;
    private Team.EnumVisible deathMessageVisibility = Team.EnumVisible.ALWAYS;
    private TextFormatting color = TextFormatting.RESET;
    private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;

    public ScorePlayerTeam(Scoreboard scoreboardIn, String name)
    {
        this.scoreboard = scoreboardIn;
        this.name = name;
        this.displayName = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Name cannot be null");
        }
        else
        {
            this.displayName = name;
            this.scoreboard.broadcastTeamInfoUpdate(this);
        }
    }

    public Collection<String> getMembershipCollection()
    {
        return this.membershipSet;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public void setPrefix(String prefix)
    {
        if (prefix == null)
        {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        else
        {
            this.prefix = prefix;
            this.scoreboard.broadcastTeamInfoUpdate(this);
        }
    }

    public String getSuffix()
    {
        return this.suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public String formatString(String input)
    {
        return this.getPrefix() + input + this.getSuffix();
    }

    public static String formatPlayerName(@Nullable Team teamIn, String string)
    {
        return teamIn == null ? string : teamIn.formatString(string);
    }

    public boolean getAllowFriendlyFire()
    {
        return this.allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean friendlyFire)
    {
        this.allowFriendlyFire = friendlyFire;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public boolean getSeeFriendlyInvisiblesEnabled()
    {
        return this.canSeeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles)
    {
        this.canSeeFriendlyInvisibles = friendlyInvisibles;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public Team.EnumVisible getNameTagVisibility()
    {
        return this.nameTagVisibility;
    }

    public Team.EnumVisible getDeathMessageVisibility()
    {
        return this.deathMessageVisibility;
    }

    public void setNameTagVisibility(Team.EnumVisible visibility)
    {
        this.nameTagVisibility = visibility;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public void setDeathMessageVisibility(Team.EnumVisible visibility)
    {
        this.deathMessageVisibility = visibility;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public Team.CollisionRule getCollisionRule()
    {
        return this.collisionRule;
    }

    public void setCollisionRule(Team.CollisionRule rule)
    {
        this.collisionRule = rule;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public int getFriendlyFlags()
    {
        int i = 0;

        if (this.getAllowFriendlyFire())
        {
            i |= 1;
        }

        if (this.getSeeFriendlyInvisiblesEnabled())
        {
            i |= 2;
        }

        return i;
    }

    @SideOnly(Side.CLIENT)
    public void setFriendlyFlags(int flags)
    {
        this.setAllowFriendlyFire((flags & 1) > 0);
        this.setSeeFriendlyInvisiblesEnabled((flags & 2) > 0);
    }

    public void setColor(TextFormatting color)
    {
        this.color = color;
    }

    public TextFormatting getColor()
    {
        return this.color;
    }
}