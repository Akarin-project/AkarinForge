/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchant;

public class CraftMerchantCustom
extends CraftMerchant {
    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
    }

    public String toString() {
        return "CraftMerchantCustom";
    }

    private static class MinecraftMerchant
    implements amf {
        private final String title;
        private final amh trades = new amh();
        private aed tradingPlayer;

        public MinecraftMerchant(String title) {
            this.title = title;
        }

        @Override
        public void a_(aed entityhuman) {
            this.tradingPlayer = entityhuman;
        }

        @Override
        public aed t_() {
            return this.tradingPlayer;
        }

        @Override
        public amh b_(aed entityhuman) {
            return this.trades;
        }

        @Override
        public void a(amg merchantrecipe) {
            merchantrecipe.g();
        }

        @Override
        public void a(aip itemstack) {
        }

        @Override
        public hh i_() {
            return new ho(this.title);
        }

        @Override
        public amu u_() {
            return null;
        }

        @Override
        public et v_() {
            return null;
        }
    }

}

