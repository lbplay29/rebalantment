package net.ona.rebal.mixin;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import net.ona.rebal.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
    private int balancement$adjustMaxLevel(int original) {
        if (Config.SPEC.isLoaded()) {
            if (original <= 1) return 1;
            if (Config.ENCHLVL.get() <= -1) return (int) Math.floor(original / 2.0);
            if (Config.ENCHLVL.get() == 0) return original;
            return Config.ENCHLVL.get().intValue();
        }
        return original;
    }

    //min into the new max
    @ModifyReturnValue(method = "getMinLevel", at = @At("RETURN"))
    private int balancement$adjustMinLevel(int original) {
        Enchantment self = (Enchantment) (Object) this;
        if (Config.REMOVELVL.get()) return self.getMaxLevel();
        return original;
    }

    @Redirect(
        method = "getFullname",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"
        )
    )
    private static MutableComponent balancement$removeLevelAppend(MutableComponent self, Component other) {
        if (!Config.REMOVELVL.get()) {
            self.append(other);
        }
        return self;
    }
}