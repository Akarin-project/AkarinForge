package net.minecraft.util.text;

import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TextComponentScore extends TextComponentBase
{
    private final String name;
    private final String objective;
    private String value = "";

    public TextComponentScore(String nameIn, String objectiveIn)
    {
        this.name = nameIn;
        this.objective = objectiveIn;
    }

    public String getName()
    {
        return this.name;
    }

    public String getObjective()
    {
        return this.objective;
    }

    public void setValue(String valueIn)
    {
        this.value = valueIn;
    }

    public String getUnformattedComponentText()
    {
        return this.value;
    }

    public void resolve(ICommandSender sender)
    {
        MinecraftServer minecraftserver = sender.getServer();

        if (minecraftserver != null && minecraftserver.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value))
        {
            Scoreboard scoreboard = minecraftserver.getWorld(0).getScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjective(this.objective);

            if (scoreboard.entityHasObjective(this.name, scoreobjective))
            {
                Score score = scoreboard.getOrCreateScore(this.name, scoreobjective);
                this.setValue(String.format("%d", score.getScorePoints()));
            }
            else
            {
                this.value = "";
            }
        }
    }

    public TextComponentScore createCopy()
    {
        TextComponentScore textcomponentscore = new TextComponentScore(this.name, this.objective);
        textcomponentscore.setValue(this.value);
        textcomponentscore.setStyle(this.getStyle().createShallowCopy());

        for (ITextComponent itextcomponent : this.getSiblings())
        {
            textcomponentscore.appendSibling(itextcomponent.createCopy());
        }

        return textcomponentscore;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof TextComponentScore))
        {
            return false;
        }
        else
        {
            TextComponentScore textcomponentscore = (TextComponentScore)p_equals_1_;
            return this.name.equals(textcomponentscore.name) && this.objective.equals(textcomponentscore.objective) && super.equals(p_equals_1_);
        }
    }

    public String toString()
    {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}