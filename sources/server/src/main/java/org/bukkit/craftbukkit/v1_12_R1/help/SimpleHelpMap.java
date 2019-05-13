/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Collections2
 */
package org.bukkit.craftbukkit.v1_12_R1.help;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_12_R1.help.CommandAliasHelpTopic;
import org.bukkit.craftbukkit.v1_12_R1.help.HelpTopicAmendment;
import org.bukkit.craftbukkit.v1_12_R1.help.HelpYamlReader;
import org.bukkit.craftbukkit.v1_12_R1.help.MultipleCommandAliasHelpTopicFactory;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.HelpTopicFactory;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.Plugin;

public class SimpleHelpMap
implements HelpMap {
    private HelpTopic defaultTopic;
    private final Map<String, HelpTopic> helpTopics = new TreeMap<String, HelpTopic>(HelpTopicComparator.topicNameComparatorInstance());
    private final Map<Class, HelpTopicFactory<Command>> topicFactoryMap = new HashMap<Class, HelpTopicFactory<Command>>();
    private final CraftServer server;
    private HelpYamlReader yaml;

    public SimpleHelpMap(CraftServer server) {
        this.server = server;
        this.yaml = new HelpYamlReader(server);
        Predicate indexFilter = Predicates.not((Predicate)Predicates.instanceOf(CommandAliasHelpTopic.class));
        if (!this.yaml.commandTopicsInMasterIndex()) {
            indexFilter = Predicates.and((Predicate)indexFilter, (Predicate)Predicates.not((Predicate)new IsCommandTopicPredicate()));
        }
        this.defaultTopic = new IndexHelpTopic("Index", null, null, Collections2.filter(this.helpTopics.values(), (Predicate)indexFilter), "Use /help [n] to get page n of help.");
        this.registerHelpTopicFactory(MultipleCommandAlias.class, new MultipleCommandAliasHelpTopicFactory());
    }

    @Override
    public synchronized HelpTopic getHelpTopic(String topicName) {
        if (topicName.equals("")) {
            return this.defaultTopic;
        }
        if (this.helpTopics.containsKey(topicName)) {
            return this.helpTopics.get(topicName);
        }
        return null;
    }

    @Override
    public Collection<HelpTopic> getHelpTopics() {
        return this.helpTopics.values();
    }

    @Override
    public synchronized void addTopic(HelpTopic topic) {
        if (!this.helpTopics.containsKey(topic.getName())) {
            this.helpTopics.put(topic.getName(), topic);
        }
    }

    @Override
    public synchronized void clear() {
        this.helpTopics.clear();
    }

    @Override
    public List<String> getIgnoredPlugins() {
        return this.yaml.getIgnoredPlugins();
    }

    public synchronized void initializeGeneralTopics() {
        this.yaml = new HelpYamlReader(this.server);
        for (HelpTopic topic : this.yaml.getGeneralTopics()) {
            this.addTopic(topic);
        }
        for (HelpTopic topic : this.yaml.getIndexTopics()) {
            if (topic.getName().equals("Default")) {
                this.defaultTopic = topic;
                continue;
            }
            this.addTopic(topic);
        }
    }

    public synchronized void initializeCommands() {
        HashSet<String> ignoredPlugins = new HashSet<String>(this.yaml.getIgnoredPlugins());
        if (ignoredPlugins.contains("All")) {
            return;
        }
        block0 : for (Command command : this.server.getCommandMap().getCommands()) {
            if (this.commandInIgnoredPlugin(command, ignoredPlugins)) continue;
            for (Class c2 : this.topicFactoryMap.keySet()) {
                if (c2.isAssignableFrom(command.getClass())) {
                    HelpTopic t2 = this.topicFactoryMap.get(c2).createTopic(command);
                    if (t2 == null) continue block0;
                    this.addTopic(t2);
                    continue block0;
                }
                if (!(command instanceof PluginCommand) || !c2.isAssignableFrom(((PluginCommand)command).getExecutor().getClass())) continue;
                HelpTopic t2 = this.topicFactoryMap.get(c2).createTopic(command);
                if (t2 == null) continue block0;
                this.addTopic(t2);
                continue block0;
            }
            this.addTopic(new GenericCommandHelpTopic(command));
        }
        for (Command command : this.server.getCommandMap().getCommands()) {
            if (this.commandInIgnoredPlugin(command, ignoredPlugins)) continue;
            for (String alias : command.getAliases()) {
                if (this.server.getCommandMap().getCommand(alias) != command) continue;
                this.addTopic(new CommandAliasHelpTopic("/" + alias, "/" + command.getLabel(), this));
            }
        }
        Collection filteredTopics = Collections2.filter(this.helpTopics.values(), (Predicate)Predicates.instanceOf(CommandAliasHelpTopic.class));
        if (!filteredTopics.isEmpty()) {
            this.addTopic(new IndexHelpTopic("Aliases", "Lists command aliases", null, filteredTopics));
        }
        HashMap<String, Set<HelpTopic>> pluginIndexes = new HashMap<String, Set<HelpTopic>>();
        this.fillPluginIndexes(pluginIndexes, this.server.getCommandMap().getCommands());
        for (Map.Entry entry : pluginIndexes.entrySet()) {
            this.addTopic(new IndexHelpTopic((String)entry.getKey(), "All commands for " + (String)entry.getKey(), null, (Collection)entry.getValue(), "Below is a list of all " + (String)entry.getKey() + " commands:"));
        }
        for (HelpTopicAmendment amendment : this.yaml.getTopicAmendments()) {
            if (!this.helpTopics.containsKey(amendment.getTopicName())) continue;
            this.helpTopics.get(amendment.getTopicName()).amendTopic(amendment.getShortText(), amendment.getFullText());
            if (amendment.getPermission() == null) continue;
            this.helpTopics.get(amendment.getTopicName()).amendCanSee(amendment.getPermission());
        }
    }

    private void fillPluginIndexes(Map<String, Set<HelpTopic>> pluginIndexes, Collection<? extends Command> commands) {
        for (Command command : commands) {
            HelpTopic topic;
            String pluginName = this.getCommandPluginName(command);
            if (pluginName == null || (topic = this.getHelpTopic("/" + command.getLabel())) == null) continue;
            if (!pluginIndexes.containsKey(pluginName)) {
                pluginIndexes.put(pluginName, new TreeSet(HelpTopicComparator.helpTopicComparatorInstance()));
            }
            pluginIndexes.get(pluginName).add(topic);
        }
    }

    private String getCommandPluginName(Command command) {
        if (command instanceof VanillaCommandWrapper) {
            return "Minecraft";
        }
        if (command instanceof BukkitCommand) {
            return "Bukkit";
        }
        if (command instanceof PluginIdentifiableCommand) {
            return ((PluginIdentifiableCommand)((Object)command)).getPlugin().getName();
        }
        return null;
    }

    private boolean commandInIgnoredPlugin(Command command, Set<String> ignoredPlugins) {
        if (command instanceof BukkitCommand && ignoredPlugins.contains("Bukkit")) {
            return true;
        }
        if (command instanceof PluginIdentifiableCommand && ignoredPlugins.contains(((PluginIdentifiableCommand)((Object)command)).getPlugin().getName())) {
            return true;
        }
        return false;
    }

    public void registerHelpTopicFactory(Class commandClass, HelpTopicFactory factory) {
        if (!Command.class.isAssignableFrom(commandClass) && !CommandExecutor.class.isAssignableFrom(commandClass)) {
            throw new IllegalArgumentException("commandClass must implement either Command or CommandExecutor!");
        }
        this.topicFactoryMap.put(commandClass, factory);
    }

    private class IsCommandTopicPredicate
    implements Predicate<HelpTopic> {
        private IsCommandTopicPredicate() {
        }

        public boolean apply(HelpTopic topic) {
            return topic.getName().charAt(0) == '/';
        }
    }

}

