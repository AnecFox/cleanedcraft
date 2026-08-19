package com.anecfox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;

import static com.anecfox.GenderUtilities.TAG_FEMALE;
import static com.anecfox.GenderUtilities.TAG_MALE;

public class CleanedCraft implements ModInitializer {

    public static final String MOD_ID = "cleanedcraft";

    @Override
    public void onInitialize() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, _) -> {
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }

            if (player.getItemInHand(hand).is(Items.COMPASS) && entity instanceof AgeableMob) {
                Component message;

                if (entity.entityTags().contains(TAG_MALE)) {
                    message = Component.translatable("message.cleanedcraft.gender_male");
                } else if (entity.entityTags().contains(TAG_FEMALE)) {
                    message = Component.translatable("message.cleanedcraft.gender_female");
                } else {
                    message = Component.translatable("message.cleanedcraft.gender_tag_not_found");
                }
                player.sendOverlayMessage(message);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, level, hand, entity, _) -> {
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }

            if (!(player.getItemInHand(hand).is(Items.BUCKET) || player.getItemInHand(hand).is(Items.BOWL))) {
                return InteractionResult.PASS;
            }

            if (entity.getType() == EntityTypes.COW || entity.getType() == EntityTypes.GOAT || entity.getType() == EntityTypes.MOOSHROOM) {
                if (entity.entityTags().contains(TAG_MALE)) {
                    var message = Component.translatable("message.cleanedcraft.on_trying_get_milk_from_" + (
                            entity.getType() == EntityTypes.GOAT ?
                                    "male_goat" :
                                    "bull"));
                    player.sendOverlayMessage(message);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.containerMenu.broadcastChanges();

                        serverPlayer.getInventory().setChanged();
                        serverPlayer.containerMenu.sendAllDataToRemote();
                    }
                    return InteractionResult.FAIL;
                }
            }

            return InteractionResult.PASS;
        });

        BiomeModifications.create(Identifier.fromNamespaceAndPath(MOD_ID, "valley_fog_change"))
                .add(
                        ModificationPhase.ADDITIONS,
                        BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY),
                        this::updateFog
                );
    }

    @SuppressWarnings("deprecation")
    private void updateFog(BiomeModificationContext context) {
        context.getEffects().setFogColor(1312262);
    }
}
