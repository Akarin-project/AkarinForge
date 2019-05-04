/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.Logger
 */
package org.bukkit.craftbukkit.v1_12_R1.command;

import java.util.List;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftFunctionCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper
extends BukkitCommand {
    protected final bi vanillaCommand;
    public static CommandSender lastSender = null;

    public VanillaCommandWrapper(bi vanillaCommand, String usage) {
        super(vanillaCommand.c(), "A Mojang provided command.", usage, vanillaCommand.b());
        this.vanillaCommand = vanillaCommand;
        this.setPermission("minecraft.command." + vanillaCommand.c());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        bn icommandlistener = this.getListener(sender);
        try {
            this.dispatchVanillaCommand(sender, icommandlistener, args);
        }
        catch (ei commandexception) {
            hp chatmessage = new hp(commandexception.getMessage(), commandexception.a());
            chatmessage.b().a(a.m);
            icommandlistener.a(chatmessage);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull((Object)sender, (String)"Sender cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)args, (String)"Arguments cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)alias, (String)"Alias cannot be null", (Object[])new Object[0]);
        return this.vanillaCommand.a(MinecraftServer.getServerInst(), this.getListener(sender), args, location == null ? null : new et(location.getX(), location.getY(), location.getZ()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final int dispatchVanillaCommand(CommandSender bSender, bn icommandlistener, String[] as2) throws ei {
        int j2;
        block22 : {
            int i2 = this.getPlayerListSize(as2);
            j2 = 0;
            MinecraftServer server = MinecraftServer.getServerInst();
            try {
                if (this.vanillaCommand.a(server, icommandlistener)) {
                    if (i2 > -1) {
                        List<vg> list = bq.matchEntitiesDefault(icommandlistener, as2[i2], vg.class);
                        String s2 = as2[i2];
                        icommandlistener.a(bp.a.c, list.size());
                        for (vg entity : list) {
                            CommandSender oldSender = lastSender;
                            lastSender = bSender;
                            try {
                                as2[i2] = entity.bm().toString();
                                this.vanillaCommand.a(server, icommandlistener, as2);
                                ++j2;
                                continue;
                            }
                            catch (ep exceptionusage) {
                                hp chatmessage = new hp("commands.generic.usage", new hp(exceptionusage.getMessage(), exceptionusage.a()));
                                chatmessage.b().a(a.m);
                                icommandlistener.a(chatmessage);
                                continue;
                            }
                            catch (ei commandexception) {
                                bi.a(icommandlistener, (bk)this.vanillaCommand, 0, commandexception.getMessage(), commandexception.a());
                                continue;
                            }
                            finally {
                                lastSender = oldSender;
                                continue;
                            }
                        }
                        as2[i2] = s2;
                    } else {
                        icommandlistener.a(bp.a.c, 1);
                        this.vanillaCommand.a(server, icommandlistener, as2);
                        ++j2;
                    }
                    break block22;
                }
                hp chatmessage = new hp("commands.generic.permission", new Object[0]);
                chatmessage.b().a(a.m);
                icommandlistener.a(chatmessage);
            }
            catch (ep exceptionusage) {
                hp chatmessage1 = new hp("commands.generic.usage", new hp(exceptionusage.getMessage(), exceptionusage.a()));
                chatmessage1.b().a(a.m);
                icommandlistener.a(chatmessage1);
            }
            catch (ei commandexception) {
                bi.a(icommandlistener, (bk)this.vanillaCommand, 0, commandexception.getMessage(), commandexception.a());
            }
            catch (Throwable throwable) {
                hp chatmessage3 = new hp("commands.generic.exception", new Object[0]);
                chatmessage3.b().a(a.m);
                icommandlistener.a(chatmessage3);
                if (icommandlistener.f() instanceof afg) {
                    MinecraftServer.k.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.c().p(), icommandlistener.c().q(), icommandlistener.c().r()), throwable);
                    break block22;
                }
                if (icommandlistener instanceof amj) {
                    amj listener = (amj)icommandlistener;
                    MinecraftServer.k.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.c().p(), listener.c().q(), listener.c().r()), throwable);
                    break block22;
                }
                MinecraftServer.k.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command", new Object[0]), throwable);
            }
            finally {
                icommandlistener.a(bp.a.a, j2);
            }
        }
        return j2;
    }

    private bn getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer)sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender)sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((CraftMinecartCommand)sender).getHandle().j();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((nz)MinecraftServer.getServerInst()).o;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer)sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender)sender).getHandle();
        }
        if (sender instanceof CraftFunctionCommandSender) {
            return ((CraftFunctionCommandSender)sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    private int getPlayerListSize(String[] as2) throws ei {
        for (int i2 = 0; i2 < as2.length; ++i2) {
            if (!this.vanillaCommand.b(as2, i2) || !bq.matchesMultiplePlayersDefault(as2[i2])) continue;
            return i2;
        }
        return -1;
    }

    public static String[] dropFirstArgument(String[] as2) {
        String[] as1 = new String[as2.length - 1];
        for (int i2 = 1; i2 < as2.length; ++i2) {
            as1[i2 - 1] = as2[i2];
        }
        return as1;
    }
}

