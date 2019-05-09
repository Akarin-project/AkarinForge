/*
 * Akarin Forge
 */
package org.bukkit.help;

import org.bukkit.command.Command;
import org.bukkit.help.HelpTopic;

public interface HelpTopicFactory<TCommand extends Command> {
    public HelpTopic createTopic(TCommand var1);
}

