package com.anecfox.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.anecfox.BannedContent.BANNED_ITEMS;

@Mixin(Inventory.class)
public class PlayerInventoryFilterMixin {

    @Shadow
    @Final
    public Player player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onInventoryTick(CallbackInfo ci) {
        if (this.player.level().isClientSide()) {
            return;
        }
        var inventory = (Inventory) (Object) this;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (BANNED_ITEMS.contains(stack.getItem())) {
                    player.sendOverlayMessage(Component.translatable("message.cleanedcraft.it_is_banned_item"));
                    inventory.setItem(i, ItemStack.EMPTY);
                } else if (stack.isEnchanted()) {
                    stack.remove(DataComponents.ENCHANTMENTS);
                } else if (stack.is(Items.POTION)) {
                    stack.remove(DataComponents.POTION_CONTENTS);
                }
            }
        }
    }
}
