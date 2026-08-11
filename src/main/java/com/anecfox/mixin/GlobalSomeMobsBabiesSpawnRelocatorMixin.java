package com.anecfox.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.anecfox.GenderUtilities.TAG_FEMALE;

@Mixin(ServerLevel.class)
public class GlobalSomeMobsBabiesSpawnRelocatorMixin {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void relocateBabiesToMotherOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ServerLevel level = (ServerLevel) (Object) this;

        if (entity instanceof AgeableMob baby && baby.isBaby()) {
            if (baby instanceof Villager || baby instanceof Camel || baby instanceof Hoglin) {
                AABB searchArea = baby.getBoundingBox().inflate(4.0d);

                switch (baby) {
                    case Villager villager -> {
                        List<Villager> parents = level.getEntitiesOfClass(Villager.class, searchArea);
                        for (Villager parent : parents) {
                            if (!parent.isBaby() && parent.entityTags().contains(TAG_FEMALE)) {
                                villager.setPos(parent.getX(), parent.getY(), parent.getZ());
                                villager.setYRot(parent.getYRot());
                                villager.setXRot(parent.getXRot());
                                break;
                            }
                        }
                    }
                    case Camel camel -> {
                        List<Camel> parents = level.getEntitiesOfClass(Camel.class, searchArea);
                        for (Camel parent : parents) {
                            if (!parent.isBaby() && parent.entityTags().contains(TAG_FEMALE)) {
                                camel.setPos(parent.getX(), parent.getY(), parent.getZ());
                                camel.setYRot(parent.getYRot());
                                camel.setXRot(parent.getXRot());
                                break;
                            }
                        }
                    }
                    case Hoglin hoglin -> {
                        List<Hoglin> parents = level.getEntitiesOfClass(Hoglin.class, searchArea);
                        for (Hoglin parent : parents) {
                            if (!parent.isBaby() && parent.entityTags().contains(TAG_FEMALE)) {
                                hoglin.setPos(parent.getX(), parent.getY(), parent.getZ());
                                hoglin.setYRot(parent.getYRot());
                                hoglin.setXRot(parent.getXRot());
                                break;
                            }
                        }
                    }
                    default -> {
                    }
                }
            }
        } else if (entity instanceof ItemEntity itemEntity) {
            if (itemEntity.getItem().is(Items.SNIFFER_EGG)) {
                AABB searchArea = itemEntity.getBoundingBox().inflate(4.0d);
                List<Sniffer> parents = level.getEntitiesOfClass(Sniffer.class, searchArea);

                for (Sniffer parent : parents) {
                    if (!parent.isBaby() && parent.entityTags().contains(TAG_FEMALE)) {
                        itemEntity.setPos(parent.getX(), parent.getY(), parent.getZ());
                        itemEntity.setDeltaMovement(0, itemEntity.getDeltaMovement().y, 0);
                        break;
                    }
                }
            }
        }
    }
}
