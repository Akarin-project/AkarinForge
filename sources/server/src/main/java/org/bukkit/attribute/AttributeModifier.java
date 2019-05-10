/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class AttributeModifier
implements ConfigurationSerializable {
    private final UUID uuid;
    private final String name;
    private final double amount;
    private final Operation operation;

    public AttributeModifier(String name, double amount, Operation operation) {
        this(UUID.randomUUID(), name, amount, operation);
    }

    public AttributeModifier(UUID uuid, String name, double amount, Operation operation) {
        Validate.notNull((Object)uuid, (String)"uuid");
        Validate.notEmpty((String)name, (String)"Name cannot be empty");
        Validate.notNull((Object)((Object)operation), (String)"operation");
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
        this.operation = operation;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public double getAmount() {
        return this.amount;
    }

    public Operation getOperation() {
        return this.operation;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("uuid", this.uuid.toString());
        data.put("name", this.name);
        data.put("operation", this.operation.ordinal());
        data.put("amount", this.amount);
        return data;
    }

    public static AttributeModifier deserialize(Map<String, Object> args) {
        return new AttributeModifier(UUID.fromString((String)args.get("uuid")), (String)args.get("name"), NumberConversions.toDouble(args.get("amount")), Operation.values()[NumberConversions.toInt(args.get("operation"))]);
    }

    public static enum Operation {
        ADD_NUMBER,
        ADD_SCALAR,
        MULTIPLY_SCALAR_1;
        

        private Operation() {
        }
    }

}

