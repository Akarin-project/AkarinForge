/*
 * Akarin Forge
 */
package org.bukkit.util;

import java.util.Arrays;
import java.util.LinkedList;
import org.bukkit.ChatColor;

public class ChatPaginator {
    public static final int GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = 55;
    public static final int AVERAGE_CHAT_PAGE_WIDTH = 65;
    public static final int UNBOUNDED_PAGE_WIDTH = Integer.MAX_VALUE;
    public static final int OPEN_CHAT_PAGE_HEIGHT = 20;
    public static final int CLOSED_CHAT_PAGE_HEIGHT = 10;
    public static final int UNBOUNDED_PAGE_HEIGHT = Integer.MAX_VALUE;

    public static ChatPage paginate(String unpaginatedString, int pageNumber) {
        return ChatPaginator.paginate(unpaginatedString, pageNumber, 55, 10);
    }

    public static ChatPage paginate(String unpaginatedString, int pageNumber, int lineLength, int pageHeight) {
        String[] lines = wordWrap(unpaginatedString, lineLength);

        int totalPages = lines.length / pageHeight + (lines.length % pageHeight == 0 ? 0 : 1);
        int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;

        int from = (actualPageNumber - 1) * pageHeight;
        int to = from + pageHeight <= lines.length ? from + pageHeight : lines.length;
        String[] selectedLines = Arrays.copyOfRange(lines, from, to);

        return new ChatPage(selectedLines, actualPageNumber, totalPages);
    }

    public static String[] wordWrap(String rawString, int lineLength) {
        int i2;
        if (rawString == null) {
            return new String[]{""};
        }
        if (rawString.length() <= lineLength && !rawString.contains("\n")) {
            return new String[]{rawString};
        }
        char[] rawChars = (rawString + ' ').toCharArray();
        StringBuilder word = new StringBuilder();
        StringBuilder line = new StringBuilder();
        LinkedList<String> lines = new LinkedList<String>();
        int lineColorChars = 0;
        for (i2 = 0; i2 < rawChars.length; ++i2) {
            char c2 = rawChars[i2];
            if (c2 == '\u00a7') {
                word.append((Object)ChatColor.getByChar(rawChars[i2 + 1]));
                lineColorChars += 2;
                ++i2;
                continue;
            }
            if (c2 == ' ' || c2 == '\n') {
                if (line.length() == 0 && word.length() > lineLength) {
                    for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
                        lines.add(partialWord);
                    }
                } else if (line.length() + 1 + word.length() - lineColorChars == lineLength) {
                    if (line.length() > 0) {
                        line.append(' ');
                    }
                    line.append(word);
                    lines.add(line.toString());
                    line = new StringBuilder();
                    lineColorChars = 0;
                } else if (line.length() + 1 + word.length() - lineColorChars > lineLength) {
                    for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
                        lines.add(line.toString());
                        line = new StringBuilder(partialWord);
                    }
                    lineColorChars = 0;
                } else {
                    if (line.length() > 0) {
                        line.append(' ');
                    }
                    line.append(word);
                }
                word = new StringBuilder();
                if (c2 != '\n') continue;
                lines.add(line.toString());
                line = new StringBuilder();
                continue;
            }
            word.append(c2);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        if (((String)lines.get(0)).length() == 0 || ((String)lines.get(0)).charAt(0) != '\u00a7') {
            lines.set(0, (Object)((Object)ChatColor.WHITE) + (String)lines.get(0));
        }
        for (i2 = 1; i2 < lines.size(); ++i2) {
            String pLine = (String)lines.get(i2 - 1);
            String subLine = (String)lines.get(i2);
            char color = pLine.charAt(pLine.lastIndexOf(167) + 1);
            if (subLine.length() != 0 && subLine.charAt(0) == '\u00a7') continue;
            lines.set(i2, (Object)((Object)ChatColor.getByChar(color)) + subLine);
        }
        return lines.toArray(new String[lines.size()]);
    }

    public static class ChatPage {
        private String[] lines;
        private int pageNumber;
        private int totalPages;

        public ChatPage(String[] lines, int pageNumber, int totalPages) {
            this.lines = lines;
            this.pageNumber = pageNumber;
            this.totalPages = totalPages;
        }

        public int getPageNumber() {
            return this.pageNumber;
        }

        public int getTotalPages() {
            return this.totalPages;
        }

        public String[] getLines() {
            return this.lines;
        }
    }

}

