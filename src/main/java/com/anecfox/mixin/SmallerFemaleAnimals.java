package com.anecfox.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.anecfox.GenderUtilities.TAG_FEMALE;

@Mixin(AgeableMob.class)
public class SmallerFemaleAnimals {

    @Unique
    private static final double SCALE = 0.94D;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void scaleFemaleAnimals(CallbackInfo ci) {
        var ageableMob = (AgeableMob) (Object) this;
        AttributeInstance scaleAttribute = ageableMob.getAttribute(Attributes.SCALE);
        if (scaleAttribute != null && ageableMob.entityTags().contains(TAG_FEMALE)) {
            if (scaleAttribute.getBaseValue() != SCALE) {
                scaleAttribute.setBaseValue(SCALE);
            }
        }
    }
}
