/*
 * Akarin Forge
 */
package org.bukkit.block.banner;

import java.util.HashMap;
import java.util.Map;

public enum PatternType {
    BASE("b"),
    SQUARE_BOTTOM_LEFT("bl"),
    SQUARE_BOTTOM_RIGHT("br"),
    SQUARE_TOP_LEFT("tl"),
    SQUARE_TOP_RIGHT("tr"),
    STRIPE_BOTTOM("bs"),
    STRIPE_TOP("ts"),
    STRIPE_LEFT("ls"),
    STRIPE_RIGHT("rs"),
    STRIPE_CENTER("cs"),
    STRIPE_MIDDLE("ms"),
    STRIPE_DOWNRIGHT("drs"),
    STRIPE_DOWNLEFT("dls"),
    STRIPE_SMALL("ss"),
    CROSS("cr"),
    STRAIGHT_CROSS("sc"),
    TRIANGLE_BOTTOM("bt"),
    TRIANGLE_TOP("tt"),
    TRIANGLES_BOTTOM("bts"),
    TRIANGLES_TOP("tts"),
    DIAGONAL_LEFT("ld"),
    DIAGONAL_RIGHT("rd"),
    DIAGONAL_LEFT_MIRROR("lud"),
    DIAGONAL_RIGHT_MIRROR("rud"),
    CIRCLE_MIDDLE("mc"),
    RHOMBUS_MIDDLE("mr"),
    HALF_VERTICAL("vh"),
    HALF_HORIZONTAL("hh"),
    HALF_VERTICAL_MIRROR("vhr"),
    HALF_HORIZONTAL_MIRROR("hhb"),
    BORDER("bo"),
    CURLY_BORDER("cbo"),
    CREEPER("cre"),
    GRADIENT("gra"),
    GRADIENT_UP("gru"),
    BRICKS("bri"),
    SKULL("sku"),
    FLOWER("flo"),
    MOJANG("moj");
    
    private final String identifier;
    private static final Map<String, PatternType> byString;

    private PatternType(String key) {
        this.identifier = key;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public static PatternType getByIdentifier(String identifier) {
        return byString.get(identifier);
    }

    static {
        byString = new HashMap<String, PatternType>();
        for (PatternType p2 : PatternType.values()) {
            byString.put(p2.identifier, p2);
        }
    }
}

