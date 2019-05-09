/*
 * Akarin Forge
 */
package org.bukkit.help;

import java.util.Comparator;
import org.bukkit.help.HelpTopic;

public class HelpTopicComparator
implements Comparator<HelpTopic> {
    private static final TopicNameComparator tnc = new TopicNameComparator();
    private static final HelpTopicComparator htc = new HelpTopicComparator();

    public static TopicNameComparator topicNameComparatorInstance() {
        return tnc;
    }

    public static HelpTopicComparator helpTopicComparatorInstance() {
        return htc;
    }

    private HelpTopicComparator() {
    }

    @Override
    public int compare(HelpTopic lhs, HelpTopic rhs) {
        return tnc.compare(lhs.getName(), rhs.getName());
    }

    public static class TopicNameComparator
    implements Comparator<String> {
        private TopicNameComparator() {
        }

        @Override
        public int compare(String lhs, String rhs) {
            boolean lhsStartSlash = lhs.startsWith("/");
            boolean rhsStartSlash = rhs.startsWith("/");
            if (lhsStartSlash && !rhsStartSlash) {
                return 1;
            }
            if (!lhsStartSlash && rhsStartSlash) {
                return -1;
            }
            return lhs.compareToIgnoreCase(rhs);
        }
    }

}

