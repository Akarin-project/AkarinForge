/*
 * Akarin Forge
 */
package org.bukkit.help;

import java.util.Collection;
import java.util.List;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

public interface HelpMap {
    public HelpTopic getHelpTopic(String var1);

    public Collection<HelpTopic> getHelpTopics();

    public void addTopic(HelpTopic var1);

    public void clear();

    public void registerHelpTopicFactory(Class<?> var1, HelpTopicFactory<?> var2);

    public List<String> getIgnoredPlugins();
}

