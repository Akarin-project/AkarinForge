/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  javax.annotation.Nullable
 */
package io.akarin.forge.command;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftRemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;

public class BukkitCommandWrapper
implements bk {
    private final CommandSender bukkitSender;
    private final String name;
    private final Command command;

    public BukkitCommandWrapper(CommandSender bukkitSender, String name, Command command) {
        this.bukkitSender = bukkitSender;
        this.command = command;
        this.name = name;
    }

    @Override
    public int compareTo(bk o2) {
        return 0;
    }

    @Override
    public String c() {
        return this.command.getName();
    }

    @Override
    public String b(bn sender) {
        return this.command.getDescription();
    }

    @Override
    public List<String> b() {
        return this.command.getAliases();
    }

    @Override
    public void a(MinecraftServer server, bn sender, String[] args) throws ei {
        try {
            this.command.execute(this.bukkitSender, this.name, args);
        }
        catch (Exception e2) {
            throw new ei(e2.getMessage(), new Object[0]);
        }
    }

    @Override
    public boolean a(MinecraftServer server, bn sender) {
        return this.command.testPermission(this.bukkitSender);
    }

    @Override
    public List<String> a(MinecraftServer server, bn sender, String[] args, et targetPos) {
        try {
            return this.command.tabComplete(this.bukkitSender, this.name, args);
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return ImmutableList.of();
        }
    }

    @Override
    public boolean b(String[] args, int index) {
        return false;
    }

    @Nullable
    public static BukkitCommandWrapper toNMSCommand(bn sender, String name) {
        CommandSender bukkitSender;
        Command command = MinecraftServer.getServerInst().server.getCommandMap().getCommand(name);
        if (command != null && (bukkitSender = BukkitCommandWrapper.toBukkitSender(sender)) != null) {
            return new BukkitCommandWrapper(bukkitSender, name, command);
        }
        return null;
    }

    @Nullable
    public static CommandSender toBukkitSender(bn sender) {
        if (sender instanceof MinecraftServer) {
            return MinecraftServer.getServerInst().console;
        }
        if (sender instanceof px) {
            return new CraftRemoteConsoleCommandSender((px)sender);
        }
        if (sender instanceof amj) {
            return new CraftBlockCommandSender(sender);
        }
        if (sender instanceof vg) {
            return ((vg)sender).getBukkitEntity();
        }
        return null;
    }
}

