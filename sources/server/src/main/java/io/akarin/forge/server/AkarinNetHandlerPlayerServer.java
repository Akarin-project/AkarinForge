package io.akarin.forge.server;

import java.util.Collections;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public abstract class AkarinNetHandlerPlayerServer {
    public CraftServer server;
    public volatile boolean processedDisconnect;

    // Get position of last block hit for BlockDamageLevel.STOPPED
    public double lastPosX = Double.MAX_VALUE;
    public double lastPosY = Double.MAX_VALUE;
    public double lastPosZ = Double.MAX_VALUE;
    public float lastPitch = Float.MAX_VALUE;
    public float lastYaw = Float.MAX_VALUE;
    public boolean justTeleported = false;
    
    public abstract CraftPlayer getPlayer();
    
    public void disconnect(ITextComponent ichatbasecomponent) {
        disconnect(CraftChatMessage.fromComponent(ichatbasecomponent, TextFormatting.WHITE));
    }
    
    public abstract void disconnect(String s);
	
	public abstract void teleport(Location dest);
	
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, TeleportCause cause) {
        this.setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet(), cause);
    }
    
    public abstract void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> relativeSet, TeleportCause cause);
}
