package com.anecfox.mixin;

import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public abstract class EndNoDragonFightMixin {

    @Shadow
    private boolean dragonKilled;

    @Shadow
    private boolean hasPreviouslyKilledDragon;

    @Shadow
    protected abstract void spawnExitPortal(boolean activated);

    @Shadow
    protected abstract void spawnNewGateway();

    @Inject(method = "scanState", at = @At("HEAD"), cancellable = true)
    private void fakeDragonDeathOnFirstLoad(CallbackInfo ci) {
        this.dragonKilled = this.hasPreviouslyKilledDragon = true;

        spawnExitPortal(true);
        spawnNewGateway();
        ci.cancel();
    }
}
