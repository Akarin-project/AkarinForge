/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 */
package org.bukkit.craftbukkit.v1_12_R1.help;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.craftbukkit.v1_12_R1.help.CustomHelpTopic;
import org.bukkit.craftbukkit.v1_12_R1.help.CustomIndexHelpTopic;
import org.bukkit.craftbukkit.v1_12_R1.help.HelpTopicAmendment;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

public class HelpYamlReader {
    private YamlConfiguration helpYaml;
    private final char ALT_COLOR_CODE = '&';
    private final Server server;

    public HelpYamlReader(Server server) {
        this.server = server;
        File helpYamlFile = new File("help.yml");
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/help.yml"), Charsets.UTF_8));
        try {
            this.helpYaml = YamlConfiguration.loadConfiguration(helpYamlFile);
            this.helpYaml.options().copyDefaults(true);
            this.helpYaml.setDefaults(defaultConfig);
            try {
                if (!helpYamlFile.exists()) {
                    this.helpYaml.save(helpYamlFile);
                }
            }
            catch (IOException ex2) {
                server.getLogger().log(Level.SEVERE, "Could not save " + helpYamlFile, ex2);
            }
        }
        catch (Exception ex3) {
            server.getLogger().severe("Failed to load help.yml. Verify the yaml indentation is correct. Reverting to default help.yml.");
            this.helpYaml = defaultConfig;
        }
    }

    public List<HelpTopic> getGeneralTopics() {
        LinkedList<HelpTopic> topics = new LinkedList<HelpTopic>();
        ConfigurationSection generalTopics = this.helpYaml.getConfigurationSection("general-topics");
        if (generalTopics != null) {
            for (String topicName : generalTopics.getKeys(false)) {
                ConfigurationSection section = generalTopics.getConfigurationSection(topicName);
                String shortText = ChatColor.translateAlternateColorCodes('&', section.getString("shortText", ""));
                String fullText = ChatColor.translateAlternateColorCodes('&', section.getString("fullText", ""));
                String permission = section.getString("permission", "");
                topics.add(new CustomHelpTopic(topicName, shortText, fullText, permission));
            }
        }
        return topics;
    }

    public List<HelpTopic> getIndexTopics() {
        LinkedList<HelpTopic> topics = new LinkedList<HelpTopic>();
        ConfigurationSection indexTopics = this.helpYaml.getConfigurationSection("index-topics");
        if (indexTopics != null) {
            for (String topicName : indexTopics.getKeys(false)) {
                ConfigurationSection section = indexTopics.getConfigurationSection(topicName);
                String shortText = ChatColor.translateAlternateColorCodes('&', section.getString("shortText", ""));
                String preamble = ChatColor.translateAlternateColorCodes('&', section.getString("preamble", ""));
                String permission = ChatColor.translateAlternateColorCodes('&', section.getString("permission", ""));
                List<String> commands = section.getStringList("commands");
                topics.add(new CustomIndexHelpTopic(this.server.getHelpMap(), topicName, shortText, permission, commands, preamble));
            }
        }
        return topics;
    }

    public List<HelpTopicAmendment> getTopicAmendments() {
        LinkedList<HelpTopicAmendment> amendments = new LinkedList<HelpTopicAmendment>();
        ConfigurationSection commandTopics = this.helpYaml.getConfigurationSection("amended-topics");
        if (commandTopics != null) {
            for (String topicName : commandTopics.getKeys(false)) {
                ConfigurationSection section = commandTopics.getConfigurationSection(topicName);
                String description = ChatColor.translateAlternateColorCodes('&', section.getString("shortText", ""));
                String usage = ChatColor.translateAlternateColorCodes('&', section.getString("fullText", ""));
                String permission = section.getString("permission", "");
                amendments.add(new HelpTopicAmendment(topicName, description, usage, permission));
            }
        }
        return amendments;
    }

    public List<String> getIgnoredPlugins() {
        return this.helpYaml.getStringList("ignore-plugins");
    }

    public boolean commandTopicsInMasterIndex() {
        return this.helpYaml.getBoolean("command-topics-in-master-index", true);
    }
}

