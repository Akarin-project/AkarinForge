/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.math.NumberUtils
 */
package org.bukkit.conversations;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public abstract class NumericPrompt
extends ValidatingPrompt {
    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return NumberUtils.isNumber((String)input) && this.isNumberValid(context, NumberUtils.createNumber((String)input));
    }

    protected boolean isNumberValid(ConversationContext context, Number input) {
        return true;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        try {
            return this.acceptValidatedInput(context, NumberUtils.createNumber((String)input));
        }
        catch (NumberFormatException e2) {
            return this.acceptValidatedInput(context, NumberUtils.INTEGER_ZERO);
        }
    }

    protected abstract Prompt acceptValidatedInput(ConversationContext var1, Number var2);

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        if (NumberUtils.isNumber((String)invalidInput)) {
            return this.getFailedValidationText(context, NumberUtils.createNumber((String)invalidInput));
        }
        return this.getInputNotNumericText(context, invalidInput);
    }

    protected String getInputNotNumericText(ConversationContext context, String invalidInput) {
        return null;
    }

    protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
        return null;
    }
}

