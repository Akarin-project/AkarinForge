/*
 * Akarin Forge
 */
package org.bukkit.command;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FormattedCommandAlias
extends Command {
    private final String[] formatStrings;

    public FormattedCommandAlias(String alias, String[] formatStrings) {
        super(alias);
        this.formatStrings = formatStrings;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean result = false;
        ArrayList<String> commands = new ArrayList<String>();
        for (String formatString : this.formatStrings) {
            try {
                commands.add(this.buildCommand(formatString, args));
                continue;
            }
            catch (Throwable throwable) {
                if (throwable instanceof IllegalArgumentException) {
                    sender.sendMessage(throwable.getMessage());
                } else {
                    sender.sendMessage((Object)((Object)ChatColor.RED) + "An internal error occurred while attempting to perform this command");
                }
                return false;
            }
        }
        for (String command : commands) {
            result |= Bukkit.dispatchCommand(sender, command);
        }
        return result;
    }

    private String buildCommand(String formatString, String[] args) {
        int index = formatString.indexOf(36);
        while (index != -1) {
            int start = index;
            if (index > 0 && formatString.charAt(start - 1) == '\\') {
                formatString = formatString.substring(0, start - 1) + formatString.substring(start);
                index = formatString.indexOf(36, index);
                continue;
            }
            boolean required = false;
            if (formatString.charAt(index + 1) == '$') {
                required = true;
                ++index;
            }
            int argStart = ++index;
            while (index < formatString.length() && FormattedCommandAlias.inRange(formatString.charAt(index) - 48, 0, 9)) {
                ++index;
            }
            if (argStart == index) {
                throw new IllegalArgumentException("Invalid replacement token");
            }
            int position = Integer.parseInt(formatString.substring(argStart, index));
            if (position == 0) {
                throw new IllegalArgumentException("Invalid replacement token");
            }
            --position;
            boolean rest = false;
            if (index < formatString.length() && formatString.charAt(index) == '-') {
                rest = true;
                ++index;
            }
            int end = index;
            if (required && position >= args.length) {
                throw new IllegalArgumentException("Missing required argument " + (position + 1));
            }
            StringBuilder replacement = new StringBuilder();
            if (rest && position < args.length) {
                for (int i2 = position; i2 < args.length; ++i2) {
                    if (i2 != position) {
                        replacement.append(' ');
                    }
                    replacement.append(args[i2]);
                }
            } else if (position < args.length) {
                replacement.append(args[position]);
            }
            formatString = formatString.substring(0, start) + replacement.toString() + formatString.substring(end);
            index = start + replacement.length();
            index = formatString.indexOf(36, index);
        }
        return formatString;
    }

    private static boolean inRange(int i2, int j2, int k2) {
        return i2 >= j2 && i2 <= k2;
    }
}

