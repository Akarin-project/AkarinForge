/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;

public final class CraftChatMessage {
    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('\u00a7') + " \\n]|$))))");
    private static final Map<Character, a> formatMap;

    public static a getColor(ChatColor color) {
        return formatMap.get(Character.valueOf(color.getChar()));
    }

    public static ChatColor getColor(a format) {
        return ChatColor.getByChar(format.z);
    }

    public static hh[] fromString(String message) {
        return CraftChatMessage.fromString(message, false);
    }

    public static hh[] fromString(String message, boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }

    public static String fromComponent(hh component) {
        return CraftChatMessage.fromComponent(component, a.a);
    }

    public static String fromComponent(hh component, a defaultColor) {
        if (component == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (hh c2 : component) {
            hn modi = c2.b();
            out.append((Object)(modi.a() == null ? defaultColor : modi.a()));
            if (modi.b()) {
                out.append((Object)a.r);
            }
            if (modi.c()) {
                out.append((Object)a.u);
            }
            if (modi.e()) {
                out.append((Object)a.t);
            }
            if (modi.d()) {
                out.append((Object)a.s);
            }
            if (modi.f()) {
                out.append((Object)a.q);
            }
            out.append(c2.e());
        }
        return out.toString().replaceFirst("^(" + (Object)((Object)defaultColor) + ")*", "");
    }

    public static hh fixComponent(hh component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return CraftChatMessage.fixComponent(component, matcher);
    }

    private static hh fixComponent(hh component, Matcher matcher) {
        String msg;
        ho text;
        if (component instanceof ho && matcher.reset(msg = (text = (ho)component).g()).find()) {
            matcher.reset();
            hn modifier = text.b() != null ? text.b() : new hn();
            ArrayList<hh> extras = new ArrayList<hh>();
            ArrayList<hh> extrasOld = new ArrayList<hh>(text.a());
            text = new ho("");
            component = text;
            int pos = 0;
            while (matcher.find()) {
                String match = matcher.group();
                if (!match.startsWith("http://") && !match.startsWith("https://")) {
                    match = "http://" + match;
                }
                ho prev = new ho(msg.substring(pos, matcher.start()));
                prev.a(modifier);
                extras.add(prev);
                ho link = new ho(matcher.group());
                hn linkModi = modifier.m();
                linkModi.a(new hg(hg.a.a, match));
                link.a(linkModi);
                extras.add(link);
                pos = matcher.end();
            }
            ho prev = new ho(msg.substring(pos));
            prev.a(modifier);
            extras.add(prev);
            extras.addAll(extrasOld);
            for (hh c2 : extras) {
                text.a(c2);
            }
        }
        List<hh> extras = component.a();
        for (int i2 = 0; i2 < extras.size(); ++i2) {
            hh comp = extras.get(i2);
            if (comp.b() == null || comp.b().h() != null) continue;
            extras.set(i2, CraftChatMessage.fixComponent(comp, matcher));
        }
        if (component instanceof hp) {
            Object[] subs = ((hp)component).j();
            for (int i3 = 0; i3 < subs.length; ++i3) {
                Object comp = subs[i3];
                if (comp instanceof hh) {
                    hh c3 = (hh)comp;
                    if (c3.b() == null || c3.b().h() != null) continue;
                    subs[i3] = CraftChatMessage.fixComponent(c3, matcher);
                    continue;
                }
                if (!(comp instanceof String) || !matcher.reset((String)comp).find()) continue;
                subs[i3] = CraftChatMessage.fixComponent(new ho((String)comp), matcher);
            }
        }
        return component;
    }

    private CraftChatMessage() {
    }

    static {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (a format : a.values()) {
            builder.put((Object)Character.valueOf(Character.toLowerCase(format.toString().charAt(1))), (Object)format);
        }
        formatMap = builder.build();
    }

    private static class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('\u00a7') + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('\u00a7') + " \\n]|$))))", 2);
        private final List<hh> list = new ArrayList<hh>();
        private hh currentChatComponent = new ho("");
        private hn modifier = new hn();
        private final hh[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                this.output = new hh[]{this.currentChatComponent};
                return;
            }
            this.list.add(this.currentChatComponent);
            Matcher matcher = INCREMENTAL_PATTERN.matcher(message);
            String match = null;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {
                }
                this.appendNewComponent(matcher.start(groupId));
                block0 : switch (groupId) {
                    case 1: {
                        a format = (a)((Object)formatMap.get(Character.valueOf(match.toLowerCase(Locale.ENGLISH).charAt(1))));
                        if (format == a.v) {
                            this.modifier = new hn();
                            break;
                        }
                        if (format.c()) {
                            switch (format) {
                                case r: {
                                    this.modifier.a(Boolean.TRUE);
                                    break block0;
                                }
                                case u: {
                                    this.modifier.b(Boolean.TRUE);
                                    break block0;
                                }
                                case s: {
                                    this.modifier.c(Boolean.TRUE);
                                    break block0;
                                }
                                case t: {
                                    this.modifier.d(Boolean.TRUE);
                                    break block0;
                                }
                                case q: {
                                    this.modifier.e(Boolean.TRUE);
                                    break block0;
                                }
                            }
                            throw new AssertionError((Object)"Unexpected message format");
                        }
                        this.modifier = new hn().a(format);
                        break;
                    }
                    case 2: {
                        if (keepNewlines) {
                            this.currentChatComponent.a(new ho("\n"));
                            break;
                        }
                        this.currentChatComponent = null;
                        break;
                    }
                    case 3: {
                        if (!match.startsWith("http://") && !match.startsWith("https://")) {
                            match = "http://" + match;
                        }
                        this.modifier.a(new hg(hg.a.a, match));
                        this.appendNewComponent(matcher.end(groupId));
                        this.modifier.a((hg)null);
                    }
                }
                this.currentIndex = matcher.end(groupId);
            }
            if (this.currentIndex < message.length()) {
                this.appendNewComponent(message.length());
            }
            this.output = this.list.toArray(new hh[this.list.size()]);
        }

        private void appendNewComponent(int index) {
            if (index <= this.currentIndex) {
                return;
            }
            hh addition = new ho(this.message.substring(this.currentIndex, index)).a(this.modifier);
            this.currentIndex = index;
            this.modifier = this.modifier.m();
            if (this.currentChatComponent == null) {
                this.currentChatComponent = new ho("");
                this.list.add(this.currentChatComponent);
            }
            this.currentChatComponent.a(addition);
        }

        private hh[] getOutput() {
            return this.output;
        }
    }

}

