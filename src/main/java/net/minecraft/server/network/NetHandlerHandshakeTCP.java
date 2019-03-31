package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer
{
    private final MinecraftServer server;
    private final NetworkManager networkManager;

    public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager)
    {
        this.server = serverIn;
        this.networkManager = netManager;
    }

    public void processHandshake(C00Handshake packetIn)
    {
        if (!net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerHandshake(packetIn, this.networkManager)) return;

        switch (packetIn.getRequestedState())
        {
            case LOGIN:
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);

                if (packetIn.getProtocolVersion() > 340)
                {
                    ITextComponent itextcomponent = new TextComponentTranslation("multiplayer.disconnect.outdated_server", new Object[] {"1.12.2"});
                    this.networkManager.sendPacket(new SPacketDisconnect(itextcomponent));
                    this.networkManager.closeChannel(itextcomponent);
                }
                else if (packetIn.getProtocolVersion() < 340)
                {
                    ITextComponent itextcomponent1 = new TextComponentTranslation("multiplayer.disconnect.outdated_client", new Object[] {"1.12.2"});
                    this.networkManager.sendPacket(new SPacketDisconnect(itextcomponent1));
                    this.networkManager.closeChannel(itextcomponent1);
                }
                else
                {
                    this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
                }

                break;
            case STATUS:
                this.networkManager.setConnectionState(EnumConnectionState.STATUS);
                this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
                break;
            default:
                throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
        }
    }

    public void onDisconnect(ITextComponent reason)
    {
    }
}