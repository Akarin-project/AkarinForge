package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public abstract class Enchantment extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Enchantment>
{
    public static final RegistryNamespaced<ResourceLocation, Enchantment> REGISTRY = net.minecraftforge.registries.GameData.getWrapper(Enchantment.class);
    private final EntityEquipmentSlot[] applicableEquipmentTypes;
    private final Enchantment.Rarity rarity;
    @Nullable
    public EnumEnchantmentType type;
    protected String name;

    @Nullable
    public static Enchantment getEnchantmentByID(int id)
    {
        return REGISTRY.getObjectById(id);
    }

    public static int getEnchantmentID(Enchantment enchantmentIn)
    {
        return REGISTRY.getIDForObject(enchantmentIn);
    }

    @Nullable
    public static Enchantment getEnchantmentByLocation(String location)
    {
        return REGISTRY.getObject(new ResourceLocation(location));
    }

    protected Enchantment(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots)
    {
        this.rarity = rarityIn;
        this.type = typeIn;
        this.applicableEquipmentTypes = slots;
    }

    public List<ItemStack> getEntityEquipment(EntityLivingBase entityIn)
    {
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (EntityEquipmentSlot entityequipmentslot : this.applicableEquipmentTypes)
        {
            ItemStack itemstack = entityIn.getItemStackFromSlot(entityequipmentslot);

            if (!itemstack.isEmpty())
            {
                list.add(itemstack);
            }
        }

        return list;
    }

    public Enchantment.Rarity getRarity()
    {
        return this.rarity;
    }

    public int getMinLevel()
    {
        return 1;
    }

    public int getMaxLevel()
    {
        return 1;
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + enchantmentLevel * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 5;
    }

    public int calcModifierDamage(int level, DamageSource source)
    {
        return 0;
    }

    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
    {
        return 0.0F;
    }

    public final boolean isCompatibleWith(Enchantment p_191560_1_)
    {
        return this.canApplyTogether(p_191560_1_) && p_191560_1_.canApplyTogether(this);
    }

    protected boolean canApplyTogether(Enchantment ench)
    {
        return this != ench;
    }

    public Enchantment setName(String enchName)
    {
        this.name = enchName;
        return this;
    }

    public String getName()
    {
        return "enchantment." + this.name;
    }

    public String getTranslatedName(int level)
    {
        String s = I18n.translateToLocal(this.getName());

        if (this.isCurse())
        {
            s = TextFormatting.RED + s;
        }

        return level == 1 && this.getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + level);
    }

    public boolean canApply(ItemStack stack)
    {
        return canApplyAtEnchantingTable(stack);
    }

    public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
    {
    }

    public void onUserHurt(EntityLivingBase user, Entity attacker, int level)
    {
    }

    public boolean isTreasureEnchantment()
    {
        return false;
    }

    public boolean isCurse()
    {
        return false;
    }

    /**
     * This applies specifically to applying at the enchanting table. The other method {@link #canApply(ItemStack)}
     * applies for <i>all possible</i> enchantments.
     * @param stack
     * @return
     */
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem().canApplyAtEnchantingTable(stack, this);
    }

    /**
     * Is this enchantment allowed to be enchanted on books via Enchantment Table
     * @return false to disable the vanilla feature
     */
    public boolean isAllowedOnBooks()
    {
        return true;
    }

    public static void registerEnchantments()
    {
        EntityEquipmentSlot[] aentityequipmentslot = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
        REGISTRY.register(0, new ResourceLocation("protection"), new EnchantmentProtection(Enchantment.Rarity.COMMON, EnchantmentProtection.Type.ALL, aentityequipmentslot));
        REGISTRY.register(1, new ResourceLocation("fire_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FIRE, aentityequipmentslot));
        REGISTRY.register(2, new ResourceLocation("feather_falling"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FALL, aentityequipmentslot));
        REGISTRY.register(3, new ResourceLocation("blast_protection"), new EnchantmentProtection(Enchantment.Rarity.RARE, EnchantmentProtection.Type.EXPLOSION, aentityequipmentslot));
        REGISTRY.register(4, new ResourceLocation("projectile_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.PROJECTILE, aentityequipmentslot));
        REGISTRY.register(5, new ResourceLocation("respiration"), new EnchantmentOxygen(Enchantment.Rarity.RARE, aentityequipmentslot));
        REGISTRY.register(6, new ResourceLocation("aqua_affinity"), new EnchantmentWaterWorker(Enchantment.Rarity.RARE, aentityequipmentslot));
        REGISTRY.register(7, new ResourceLocation("thorns"), new EnchantmentThorns(Enchantment.Rarity.VERY_RARE, aentityequipmentslot));
        REGISTRY.register(8, new ResourceLocation("depth_strider"), new EnchantmentWaterWalker(Enchantment.Rarity.RARE, aentityequipmentslot));
        REGISTRY.register(9, new ResourceLocation("frost_walker"), new EnchantmentFrostWalker(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET}));
        REGISTRY.register(10, new ResourceLocation("binding_curse"), new EnchantmentBindingCurse(Enchantment.Rarity.VERY_RARE, aentityequipmentslot));
        REGISTRY.register(16, new ResourceLocation("sharpness"), new EnchantmentDamage(Enchantment.Rarity.COMMON, 0, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(17, new ResourceLocation("smite"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 1, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(18, new ResourceLocation("bane_of_arthropods"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 2, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(19, new ResourceLocation("knockback"), new EnchantmentKnockback(Enchantment.Rarity.UNCOMMON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(20, new ResourceLocation("fire_aspect"), new EnchantmentFireAspect(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(21, new ResourceLocation("looting"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(22, new ResourceLocation("sweeping"), new EnchantmentSweepingEdge(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(32, new ResourceLocation("efficiency"), new EnchantmentDigging(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(33, new ResourceLocation("silk_touch"), new EnchantmentUntouching(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(34, new ResourceLocation("unbreaking"), new EnchantmentDurability(Enchantment.Rarity.UNCOMMON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(35, new ResourceLocation("fortune"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(48, new ResourceLocation("power"), new EnchantmentArrowDamage(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(49, new ResourceLocation("punch"), new EnchantmentArrowKnockback(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(50, new ResourceLocation("flame"), new EnchantmentArrowFire(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(51, new ResourceLocation("infinity"), new EnchantmentArrowInfinite(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(61, new ResourceLocation("luck_of_the_sea"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(62, new ResourceLocation("lure"), new EnchantmentFishingSpeed(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND}));
        REGISTRY.register(70, new ResourceLocation("mending"), new EnchantmentMending(Enchantment.Rarity.RARE, EntityEquipmentSlot.values()));
        REGISTRY.register(71, new ResourceLocation("vanishing_curse"), new EnchantmentVanishingCurse(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.values()));
    }

    public static enum Rarity
    {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight)
        {
            this.weight = rarityWeight;
        }

        public int getWeight()
        {
            return this.weight;
        }
    }
}