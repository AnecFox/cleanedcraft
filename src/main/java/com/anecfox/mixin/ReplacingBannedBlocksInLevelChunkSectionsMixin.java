package com.anecfox.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelChunkSection.class)
public class ReplacingBannedBlocksInLevelChunkSectionsMixin {

    @ModifyVariable(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), argsOnly = true, name = "state")
    private BlockState replaceBannedBlocks(BlockState state) {
        if (state != null) {
            if (state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL) || state.is(Blocks.DRIED_GHAST)) {
                return Blocks.NETHERRACK.defaultBlockState();
            } else if (state.is(Blocks.SOUL_TORCH)) {
                return Blocks.COPPER_TORCH.defaultBlockState();
            } else if (state.is(Blocks.SOUL_LANTERN)) {
                return Blocks.LANTERN.defaultBlockState();
            } else if (state.is(Blocks.SOUL_CAMPFIRE)) {
                return Blocks.CAMPFIRE.defaultBlockState();
            } else if (state.is(Blocks.SCULK_SHRIEKER)) {
                return Blocks.SCULK_SENSOR.defaultBlockState();
            } else if (state.is(Blocks.BREWING_STAND) || state.is(Blocks.ENCHANTING_TABLE)) {
                return Blocks.LECTERN.defaultBlockState();
            } else if (state.is(Blocks.COPPER_GOLEM_STATUE.waxed().unaffected()) ||
                    state.is(Blocks.COPPER_GOLEM_STATUE.waxed().exposed()) ||
                    state.is(Blocks.COPPER_GOLEM_STATUE.waxed().oxidized()) ||
                    state.is(Blocks.COPPER_GOLEM_STATUE.weathering().unaffected()) ||
                    state.is(Blocks.COPPER_GOLEM_STATUE.weathering().exposed()) ||
                    state.is(Blocks.COPPER_GOLEM_STATUE.weathering().oxidized())) {
                return Blocks.AIR.defaultBlockState();
            } else if (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN)) {
                return Blocks.PUMPKIN.defaultBlockState();
            }
        }
        return state;
    }
}
