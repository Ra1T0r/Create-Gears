package com.kotakotik.creategears.regitration;

import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;

/**
 * Registers stress values (impact/capacity) for the addon's kinetic blocks.
 *
 * <p>In Create 6 the old IStressValueProvider mechanism is gone; stress values are
 * registered straight into {@link BlockStressValues#IMPACTS} and
 * {@link BlockStressValues#CAPACITIES}. Because the blocks are only bound during the
 * registration event, we register them through a Registrate {@code .transform(...)}
 * operator whose callback runs once the block instance exists.</p>
 */
public final class GearsStressProvider {

    private GearsStressProvider() {}

    /**
     * Registers fixed stress values for a block.
     *
     * @param impact   stress units consumed at 1 RPM (0 for pure relays)
     * @param capacity stress units that can be generated (0 if not a source)
     */
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> fixed(double impact, double capacity) {
        return builder -> {
            builder.onRegister(block -> {
                BlockStressValues.IMPACTS.register(block, () -> impact);
                BlockStressValues.CAPACITIES.register(block, () -> capacity);
            });
            return builder;
        };
    }
}
