/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  jline.console.completer.Completer
 */
package org.bukkit.craftbukkit.v1_12_R1.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.console.completer.Completer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.util.Waitable;
import org.bukkit.event.Event;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.PluginManager;

public class ConsoleCommandCompleter
implements Completer {
    private final CraftServer server;

    public ConsoleCommandCompleter(CraftServer server) {
        this.server = server;
    }

    public int complete(final String buffer, int cursor, List<CharSequence> candidates) {
        Waitable<List<String>> waitable = new Waitable<List<String>>(){

            @Override
            protected List<String> evaluate() {
                List<String> offers;
                TabCompleteEvent tabEvent = new TabCompleteEvent(ConsoleCommandCompleter.this.server.getConsoleSender(), buffer, (offers = ConsoleCommandCompleter.this.server.getCommandMap().tabComplete(ConsoleCommandCompleter.this.server.getConsoleSender(), buffer)) == null ? Collections.EMPTY_LIST : offers);
                ConsoleCommandCompleter.this.server.getPluginManager().callEvent(tabEvent);
                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        this.server.getServer().processQueue.add(()waitable);
        try {
            List offers = (List)waitable.get();
            if (offers == null) {
                return cursor;
            }
            candidates.addAll(offers);
            int lastSpace = buffer.lastIndexOf(32);
            if (lastSpace == -1) {
                return cursor - buffer.length();
            }
            return cursor - (buffer.length() - lastSpace - 1);
        }
        catch (ExecutionException e2) {
            this.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e2);
        }
        catch (InterruptedException e3) {
            Thread.currentThread().interrupt();
        }
        return cursor;
    }

}

