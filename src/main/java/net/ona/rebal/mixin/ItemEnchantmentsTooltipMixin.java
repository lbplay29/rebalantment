package net.ona.rebal.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.ona.rebal.Config;
import net.ona.rebal.util.EnchantmentLevelRules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsTooltipMixin {
    @ModifyArgs(method = "addToTooltip", at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;"))
    private void rebal$changeTooltipLevel(Args args) {
        Holder<Enchantment> enchantment = args.get(0);
        int storedLevel = args.get(1);
        int effectiveLevel = EnchantmentLevelRules.getEffectiveLevel(enchantment, storedLevel);
        if (storedLevel>effectiveLevel|| Config.REMOVELVL.get()) {
            args.set(1, effectiveLevel);
        }
    }
}
