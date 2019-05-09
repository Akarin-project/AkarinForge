/*
 * Akarin Forge
 */
package org.bukkit.help;

import org.bukkit.command.CommandSender;

public abstract class HelpTopic {
    protected String name;
    protected String shortText;
    protected String fullText;
    protected String amendedPermission;

    public abstract boolean canSee(CommandSender var1);

    public void amendCanSee(String amendedPermission) {
        this.amendedPermission = amendedPermission;
    }

    public String getName() {
        return this.name;
    }

    public String getShortText() {
        return this.shortText;
    }

    public String getFullText(CommandSender forWho) {
        return this.fullText;
    }

    public void amendTopic(String amendedShortText, String amendedFullText) {
        this.shortText = this.applyAmendment(this.shortText, amendedShortText);
        this.fullText = this.applyAmendment(this.fullText, amendedFullText);
    }

    protected String applyAmendment(String baseText, String amendment) {
        if (amendment == null) {
            return baseText;
        }
        return amendment.replaceAll("<text>", baseText);
    }
}

