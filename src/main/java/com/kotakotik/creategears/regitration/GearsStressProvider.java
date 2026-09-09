package com.kotakotik.creategears.regitration;

import com.kotakotik.creategears.Gears;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

/**
 * Registers stress values (impact/capacity) for the addon's kinetic blocks.
 *
 * <p>In Create 0.5.1 stress values are registered per-modid through
 * {@link BlockStressValues#registerProvider(String, BlockStressValues.IStressValueProvider)}.
 * All gears are pure relays (no impact, no capacity), so the provider simply reports 0/0.</p>
 */
public final class GearsStressProvider {

    private GearsStressProvider() {}

    public static void register() {
        BlockStressValues.registerProvider(Gears.MODID, new BlockStressValues.IStressValueProvider() {
            @Override
            public double getImpact(Block block) {
                return 0;
            }

            @Override
            public double getCapacity(Block block) {
                return 0;
            }

            @Override
            public boolean hasImpact(Block block) {
                return false;
            }

            @Override
            public boolean hasCapacity(Block block) {
                return false;
            }

            @Nullable
            @Override
            public Couple<Integer> getGeneratedRPM(Block block) {
                return null;
            }
        });
    }
}
