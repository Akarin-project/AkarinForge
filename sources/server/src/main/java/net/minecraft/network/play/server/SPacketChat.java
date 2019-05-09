package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketChat implements Packet<INetHandlerPlayClient>
{
    private ITextComponent chatComponent;
    public net.md_5.bungee.api.chat.BaseComponent[] components; // Spigot
    private ChatType type;

    public SPacketChat()
    {
    }

    public SPacketChat(ITextComponent componentIn)
    {
        this(componentIn, ChatType.SYSTEM);
    }

    public SPacketChat(ITextComponent message, ChatType type)
    {
        this.chatComponent = message;
        this.type = type;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chatComponent = buf.readTextComponent();
        this.type = ChatType.byId(buf.readByte());
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        // Spigot start
        if (components != null) {
            //packetdataserializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(components)); // Paper - comment, replaced with below
            // Paper start - don't nest if we don't need to so that we can preserve formatting
            if (this.components.length == 1) {
                buf.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(this.components[0]));
            } else {
                buf.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(this.components));
            }
            // Paper end
        } else {
            buf.writeTextComponent(this.chatComponent);
        }
        // Spigot end
        buf.writeByte(this.type.getId());
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChat(this);
    }

    @SideOnly(Side.CLIENT)
    public ITextComponent getChatComponent()
    {
        return this.chatComponent;
    }

    public boolean isSystem()
    {
        return this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO;
    }

    public ChatType getType()
    {
        return this.type;
    }
}