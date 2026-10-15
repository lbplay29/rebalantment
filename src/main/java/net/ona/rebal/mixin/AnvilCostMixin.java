package net.ona.rebal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public class AnvilCostMixin {
    @ModifyConstant(
            method = "createResult",
            constant = @Constant(intValue = 40, ordinal = 2)
    )
    private int balancement$increaseAnvilCap(int original) {
        return 10000;
    }
}
