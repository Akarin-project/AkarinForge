/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.help;

import org.bukkit.command.Command;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.craftbukkit.v1_12_R1.help.MultipleCommandAliasHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

public class MultipleCommandAliasHelpTopicFactory
implements HelpTopicFactory<MultipleCommandAlias> {
    @Override
    public HelpTopic createTopic(MultipleCommandAlias multipleCommandAlias) {
        return new MultipleCommandAliasHelpTopic(multipleCommandAlias);
    }
}

