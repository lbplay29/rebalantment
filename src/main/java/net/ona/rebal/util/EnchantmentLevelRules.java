package net.ona.rebal.util;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.ona.rebal.Config;

public class EnchantmentLevelRules {
    public static int getEffectiveLevel(Holder<Enchantment> enchantment, int storedLevel) {
        if (storedLevel <= 0 || !Config.SPEC.isLoaded()) {
            return storedLevel;
        }
        int setting = Config.ENCHLVL.get().intValue();
        // Default settings: preserve original levels.
        if (setting == 0 && !Config.REMOVELVL.get()) {
            return storedLevel;
        }
        // Otherwise, use the currently configured maximum.
        return enchantment.value().getMaxLevel();
    }
}
