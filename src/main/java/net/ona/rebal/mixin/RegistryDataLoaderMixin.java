package net.ona.rebal.mixin;

import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.InputStream;
import java.io.FileNotFoundException;

@Mixin(RegistryDataLoader.class)
public abstract class RegistryDataLoaderMixin {
    @ModifyArgs(method = "loadContentsFromManager",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/resources/RegistryDataLoader;loadElementFromResource(Lnet/minecraft/core/WritableRegistry;Lcom/mojang/serialization/Decoder;Lnet/minecraft/resources/RegistryOps;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/server/packs/resources/Resource;Lnet/minecraft/core/RegistrationInfo;)V"
            )
    )
    private static void rebal$replaceEnchantments(Args args) {

        ResourceKey<?> key = args.get(3);

        if (!key.registry().equals(Registries.ENCHANTMENT.location())) {
            return;
        }

        String replacement = switch (key.location().toString()) {
            case "minecraft:mending" -> "/data/minecraft/enchantment/mending.json";
            case "minecraft:unbreaking" -> "/data/minecraft/enchantment/unbreaking.json";

            default -> null;
        };

        if (replacement == null) {
            return;
        }

        Resource original = args.get(4);

        Resource newResource = new Resource(original.source(), () -> {
                    InputStream stream = RegistryDataLoaderMixin.class.getResourceAsStream(replacement);
                    if (stream == null) {
                        throw new FileNotFoundException(replacement);
                    }
                    return stream;
                }
        );
        args.set(4, newResource);
    }
}
