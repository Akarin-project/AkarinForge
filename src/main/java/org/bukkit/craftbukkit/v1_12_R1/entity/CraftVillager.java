/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAgeable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchant;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.MerchantRecipe;

public class CraftVillager
extends CraftAgeable
implements Villager,
InventoryHolder {
    private static final Map<Villager.Career, Integer> careerIDMap = new HashMap<Villager.Career, Integer>();
    private CraftMerchant merchant;

    public CraftVillager(CraftServer server, ady entity) {
        super(server, entity);
    }

    @Override
    public ady getHandle() {
        return (ady)this.entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    @Override
    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    @Override
    public Villager.Profession getProfession() {
        return Villager.Profession.values()[this.getHandle().dl() + 1];
    }

    @Override
    public void setProfession(Villager.Profession profession) {
        Validate.notNull((Object)((Object)profession));
        Validate.isTrue((boolean)(!profession.isZombie()), (String)"Profession is reserved for Zombies: ", (Object[])new Object[]{profession});
        this.getHandle().g(profession.ordinal() - 1);
    }

    @Override
    public Villager.Career getCareer() {
        return CraftVillager.getCareer(this.getProfession(), this.getHandle().bK);
    }

    @Override
    public void setCareer(Villager.Career career) {
        this.setCareer(career, true);
    }

    @Override
    public void setCareer(Villager.Career career, boolean resetTrades) {
        if (career == null) {
            this.getHandle().bK = 0;
        } else {
            Validate.isTrue((boolean)(career.getProfession() == this.getProfession()), (String)("Career assignment mismatch. Found (" + (Object)((Object)this.getProfession()) + ") Required (" + (Object)((Object)career.getProfession()) + ")"), (Object[])new Object[0]);
            this.getHandle().bK = CraftVillager.getCareerID(career);
        }
        if (resetTrades) {
            this.getHandle().bE = null;
            this.getHandle().dx();
        }
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().bO);
    }

    private CraftMerchant getMerchant() {
        CraftMerchant craftMerchant = this.merchant == null ? (this.merchant = new CraftMerchant(this.getHandle())) : this.merchant;
        return craftMerchant;
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return this.getMerchant().getRecipes();
    }

    @Override
    public void setRecipes(List<MerchantRecipe> recipes) {
        this.getMerchant().setRecipes(recipes);
    }

    @Override
    public MerchantRecipe getRecipe(int i2) {
        return this.getMerchant().getRecipe(i2);
    }

    @Override
    public void setRecipe(int i2, MerchantRecipe merchantRecipe) {
        this.getMerchant().setRecipe(i2, merchantRecipe);
    }

    @Override
    public int getRecipeCount() {
        return this.getMerchant().getRecipeCount();
    }

    @Override
    public boolean isTrading() {
        return this.getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        return this.getMerchant().getTrader();
    }

    @Override
    public int getRiches() {
        return this.getHandle().bI;
    }

    @Override
    public void setRiches(int riches) {
        this.getHandle().bI = riches;
    }

    @Nullable
    private static Villager.Career getCareer(Villager.Profession profession, int id2) {
        Validate.isTrue((boolean)(id2 > 0), (String)"Career id must be greater than 0", (Object[])new Object[0]);
        List<Villager.Career> careers = profession.getCareers();
        for (Villager.Career c2 : careers) {
            if (!careerIDMap.containsKey((Object)c2) || careerIDMap.get((Object)c2) != id2) continue;
            return c2;
        }
        return null;
    }

    private static int getCareerID(Villager.Career career) {
        return careerIDMap.getOrDefault((Object)career, 0);
    }

    static {
        int id2 = 0;
        for (Villager.Profession prof : Villager.Profession.values()) {
            List<Villager.Career> careers = prof.getCareers();
            if (!careers.isEmpty()) {
                for (Villager.Career c2 : careers) {
                    careerIDMap.put(c2, ++id2);
                }
            }
            Validate.isTrue((boolean)(id2 == careers.size()), (String)"Career id registration mismatch", (Object[])new Object[0]);
            id2 = 0;
        }
    }
}

