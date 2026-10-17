package net.ona.rebal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;

@EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onEntityAttacked(LivingIncomingDamageEvent event) {
        Entity entity = event.getEntity();
        Entity sourceentity = event.getSource().getEntity();
        Level world = event.getEntity().level();
        float amount = event.getAmount();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity == null || sourceentity == null)
            return;
        if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
                .getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("rebal:extra_hit")))) != 0) {
            RebalantmentMod.queueServerWork(20, () -> {
                if (!(entity instanceof LivingEntity _livEnt2 && _livEnt2.isBlocking()) && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 0) {
                    entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.PLAYER_ATTACK)), ((amount * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
                            .getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("rebal:extra_hit"))))) / 8));
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 25, 1, 1, 1, 0.1);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.deactivate")), SoundSource.NEUTRAL, 1, 1);
                        } else {
                            _level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.deactivate")), SoundSource.NEUTRAL, 1, 1, false);
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onEntityCritical(CriticalHitEvent event) {
        Entity entity = event.getTarget();
        LivingEntity sourceentity = event.getEntity();
        Level world = event.getEntity().level();
        float amount = event.getDamageMultiplier();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
                .getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("minecraft:mending")))) != 0) {
            if (event.isCriticalHit()) {
                sourceentity.setHealth(sourceentity.getHealth() + (amount * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
                        .getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("minecraft:mending"))))));
                if (world instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.HEART, x, y, z, 9, 1, 1, 1, 0.1);
            }
        }
    }

    @SubscribeEvent
    public static void onGetEnchantmentLevel(GetEnchantmentLevelEvent event) {
        if (!Config.SPEC.isLoaded()) {
            return;
        }
        int setting = Config.ENCHLVL.get().intValue();
        if (setting == 0 && !Config.REMOVELVL.get()) {
            return;
        }
        ItemEnchantments.Mutable enchantments = event.getEnchantments();
        // Copy the keys so we aren't modifying a collection
        // while iterating over it.
        for (Holder<Enchantment> enchantment : new ArrayList<>(enchantments.keySet())) {
            int storedLevel = enchantments.getLevel(enchantment);
            if (event.getTargetEnchant() != null && !event.getTargetEnchant().equals(enchantment)) {
                continue;
            }
            if (storedLevel>enchantment.value().getMaxLevel() || Config.REMOVELVL.get()) {
                enchantments.set(enchantment, enchantment.value().getMaxLevel());
            }
        }
    }
}
