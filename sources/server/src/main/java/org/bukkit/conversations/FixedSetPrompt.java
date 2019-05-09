/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 */
package org.bukkit.conversations;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ValidatingPrompt;

public abstract class FixedSetPrompt
extends ValidatingPrompt {
    protected List<String> fixedSet;

    public /* varargs */ FixedSetPrompt(String ... fixedSet) {
        this.fixedSet = Arrays.asList(fixedSet);
    }

    private FixedSetPrompt() {
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return this.fixedSet.contains(input);
    }

    protected String formatFixedSet() {
        return "[" + StringUtils.join(this.fixedSet, (String)", ") + "]";
    }
}

