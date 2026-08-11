package com.anecfox.mixin;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class BrushItemMixin {

    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    private void onBrushShulker(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type, CallbackInfoReturnable<InteractionResult> cir) {
        if (itemStack.getItem() instanceof BrushItem && target instanceof Shulker shulker) {
            if (!player.level().isClientSide()) {
                var serverLevel = (ServerLevel) player.level();
                var shulkerShell = new ItemStack(Items.SHULKER_SHELL);
                var itemEntity = new ItemEntity(serverLevel, shulker.getX(), shulker.getY() + 0.5D, shulker.getZ(), shulkerShell);

                itemEntity.setDefaultPickUpDelay();
                serverLevel.addFreshEntity(itemEntity);
                itemStack.hurtAndBreak(32, player, player.getEquipmentSlotForItem(itemStack));

                for (int i = 0; i < 10; i++) {
                    serverLevel.sendParticles(
                            new ItemParticleOption(ParticleTypes.ITEM, shulkerShell.getItem()),
                            shulker.getRandomX(0.6D), shulker.getRandomY(), shulker.getRandomZ(0.6D),
                            1, 0.0D, 0.0D, 0.0D, 0.05D
                    );
                }
            }

            player.level().playSound(
                    null,
                    shulker.blockPosition(),
                    SoundEvents.ARMADILLO_BRUSH,
                    SoundSource.PLAYERS,
                    1.0F, 1.0F
            );
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
