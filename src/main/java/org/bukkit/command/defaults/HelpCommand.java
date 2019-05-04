/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang.ArrayUtils
 *  org.apache.commons.lang.StringUtils
 *  org.apache.commons.lang.Validate
 *  org.apache.commons.lang.math.NumberUtils
 */
package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.util.ChatPaginator;

public class HelpCommand
extends BukkitCommand {
    public HelpCommand() {
        super("help");
        this.description = "Shows the help menu";
        this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";
        this.setAliases(Arrays.asList("?"));
        this.setPermission("bukkit.command.help");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        String command;
        int pageHeight;
        int pageWidth;
        int pageNumber;
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            command = "";
            pageNumber = 1;
        } else if (NumberUtils.isDigits((String)args[args.length - 1])) {
            command = StringUtils.join((Object[])ArrayUtils.subarray((Object[])args, (int)0, (int)(args.length - 1)), (String)" ");
            try {
                pageNumber = NumberUtils.createInteger((String)args[args.length - 1]);
            }
            catch (NumberFormatException exception) {
                pageNumber = 1;
            }
            if (pageNumber <= 0) {
                pageNumber = 1;
            }
        } else {
            command = StringUtils.join((Object[])args, (String)" ");
            pageNumber = 1;
        }
        if (sender instanceof ConsoleCommandSender) {
            pageHeight = Integer.MAX_VALUE;
            pageWidth = Integer.MAX_VALUE;
        } else {
            pageHeight = 9;
            pageWidth = 55;
        }
        HelpMap helpMap = Bukkit.getServer().getHelpMap();
        HelpTopic topic = helpMap.getHelpTopic(command);
        if (topic == null) {
            topic = helpMap.getHelpTopic("/" + command);
        }
        if (topic == null) {
            topic = this.findPossibleMatches(command);
        }
        if (topic == null || !topic.canSee(sender)) {
            sender.sendMessage((Object)((Object)ChatColor.RED) + "No help for " + command);
            return true;
        }
        ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);
        StringBuilder header = new StringBuilder();
        header.append((Object)ChatColor.YELLOW);
        header.append("--------- ");
        header.append((Object)ChatColor.WHITE);
        header.append("Help: ");
        header.append(topic.getName());
        header.append(" ");
        if (page.getTotalPages() > 1) {
            header.append("(");
            header.append(page.getPageNumber());
            header.append("/");
            header.append(page.getTotalPages());
            header.append(") ");
        }
        header.append((Object)ChatColor.YELLOW);
        for (int i2 = header.length(); i2 < 55; ++i2) {
            header.append("-");
        }
        sender.sendMessage(header.toString());
        sender.sendMessage(page.getLines());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull((Object)sender, (String)"Sender cannot be null");
        Validate.notNull((Object)args, (String)"Arguments cannot be null");
        Validate.notNull((Object)alias, (String)"Alias cannot be null");
        if (args.length == 1) {
            ArrayList<String> matchedTopics = new ArrayList<String>();
            String searchString = args[0];
            for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
                String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();
                if (!trimmedTopic.startsWith(searchString)) continue;
                matchedTopics.add(trimmedTopic);
            }
            return matchedTopics;
        }
        return ImmutableList.of();
    }

    protected HelpTopic findPossibleMatches(String searchString) {
        int maxDistance = searchString.length() / 5 + 3;
        TreeSet<HelpTopic> possibleMatches = new TreeSet<HelpTopic>(HelpTopicComparator.helpTopicComparatorInstance());
        if (searchString.startsWith("/")) {
            searchString = searchString.substring(1);
        }
        for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
            String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();
            if (trimmedTopic.length() < searchString.length() || Character.toLowerCase(trimmedTopic.charAt(0)) != Character.toLowerCase(searchString.charAt(0)) || HelpCommand.damerauLevenshteinDistance(searchString, trimmedTopic.substring(0, searchString.length())) >= maxDistance) continue;
            possibleMatches.add(topic);
        }
        if (possibleMatches.size() > 0) {
            return new IndexHelpTopic("Search", null, null, possibleMatches, "Search for: " + searchString);
        }
        return null;
    }

    protected static int damerauLevenshteinDistance(String s1, String s2) {
        int INF;
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 != null && s2 == null) {
            return s1.length();
        }
        if (s1 == null && s2 != null) {
            return s2.length();
        }
        int s1Len = s1.length();
        int s2Len = s2.length();
        int[][] H = new int[s1Len + 2][s2Len + 2];
        H[0][0] = INF = s1Len + s2Len;
        int i2 = 0;
        while (i2 <= s1Len) {
            H[i2 + 1][1] = i2++;
            H[i2 + 1][0] = INF;
        }
        int j2 = 0;
        while (j2 <= s2Len) {
            H[1][j2 + 1] = j2++;
            H[0][j2 + 1] = INF;
        }
        HashMap<Character, Integer> sd2 = new HashMap<Character, Integer>();
        for (char Letter : (s1 + s2).toCharArray()) {
            if (sd2.containsKey(Character.valueOf(Letter))) continue;
            sd2.put(Character.valueOf(Letter), 0);
        }
        for (int i3 = 1; i3 <= s1Len; ++i3) {
            int DB = 0;
            for (int j3 = 1; j3 <= s2Len; ++j3) {
                int i1 = (Integer)sd2.get(Character.valueOf(s2.charAt(j3 - 1)));
                int j1 = DB;
                if (s1.charAt(i3 - 1) == s2.charAt(j3 - 1)) {
                    H[i3 + 1][j3 + 1] = H[i3][j3];
                    DB = j3;
                } else {
                    H[i3 + 1][j3 + 1] = Math.min(H[i3][j3], Math.min(H[i3 + 1][j3], H[i3][j3 + 1])) + 1;
                }
                H[i3 + 1][j3 + 1] = Math.min(H[i3 + 1][j3 + 1], H[i1][j1] + (i3 - i1 - 1) + 1 + (j3 - j1 - 1));
            }
            sd2.put(Character.valueOf(s1.charAt(i3 - 1)), i3);
        }
        return H[s1Len + 1][s2Len + 1];
    }
}

