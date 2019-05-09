/*
 * Akarin Forge
 */
package io.akarin.forge.command;

import java.io.IOException;
import java.util.Collections;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.akarin.forge.AkarinForge;

public class PermissionCommand
extends Command {
    public PermissionCommand(String name) {
        super(name);
        this.description = "Reload fake player permission file";
        this.usageMessage = "/fakefile reload";
        this.setPermission("catserver.command.fakefile");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        if (!args[0].equals("reload")) {
            return false;
        }
        AkarinForge.fakePlayerPermissions = AkarinForge.getOrWriteStringListConfig("fakePlayer.permissions", Collections.singletonList("essentials.build"));
        sender.sendMessage("SUCCESS");
        return true;
    }
}

