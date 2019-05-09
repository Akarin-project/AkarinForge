/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ValidatingPrompt;

public abstract class RegexPrompt
extends ValidatingPrompt {
    private Pattern pattern;

    public RegexPrompt(String regex) {
        this(Pattern.compile(regex));
    }

    public RegexPrompt(Pattern pattern) {
        this.pattern = pattern;
    }

    private RegexPrompt() {
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return this.pattern.matcher(input).matches();
    }
}

