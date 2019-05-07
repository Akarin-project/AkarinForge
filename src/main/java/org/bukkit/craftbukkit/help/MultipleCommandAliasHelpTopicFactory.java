/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.help;

import org.bukkit.command.Command;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.craftbukkit.help.MultipleCommandAliasHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

public class MultipleCommandAliasHelpTopicFactory
implements HelpTopicFactory<MultipleCommandAlias> {
    @Override
    public HelpTopic createTopic(MultipleCommandAlias multipleCommandAlias) {
        return new MultipleCommandAliasHelpTopic(multipleCommandAlias);
    }
}

