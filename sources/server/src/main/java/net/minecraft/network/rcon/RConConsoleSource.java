package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RConConsoleSource implements ICommandSender
{
    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;

    public RConConsoleSource(MinecraftServer serverIn)
    {
        this.server = serverIn;
    }

    public String getName()
    {
        return "Rcon";
    }

    public void sendMessage(ITextComponent component)
    {
        this.buffer.append(component.getUnformattedText());
    }

    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    public World getEntityWorld()
    {
        return this.server.getEntityWorld();
    }

    public boolean sendCommandFeedback()
    {
        return true;
    }

    public MinecraftServer getServer()
    {
        return this.server;
    }

    @SideOnly(Side.SERVER)
    public void resetLog()
    {
        this.buffer.setLength(0);
    }

    @SideOnly(Side.SERVER)
    public String getLogContents()
    {
        return this.buffer.toString();
    }
}