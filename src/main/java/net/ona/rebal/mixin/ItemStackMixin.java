package net.ona.rebal.mixin;

import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.world.item.Item;
import net.ona.rebal.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getDamageValue",at = @At("RETURN"), cancellable = true)
    public void newDamageValue(CallbackInfoReturnable<Integer> cir) {
        boolean hasUnbreaking = Config.UNBREAK.get();
        if (hasUnbreaking) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "getMaxDamage",at = @At("RETURN"), cancellable = true)
    public void newMaxDamage(CallbackInfoReturnable<Integer> cir) {
        double multipl = Config.DURAB.get();
        double newDurab = cir.getReturnValue() * multipl;
        cir.setReturnValue((int) newDurab);
    }

    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"), cancellable = true
    )
    private void balancement$makeUnbreakable(int amount, ServerLevel world, @Nullable LivingEntity entity, Consumer<Item> onBreak, CallbackInfo ci) {
        //check if unbreakable items is on
        boolean hasUnbreaking = Config.UNBREAK.get();
        if (hasUnbreaking) {
            ci.cancel();
        }
    }
}
