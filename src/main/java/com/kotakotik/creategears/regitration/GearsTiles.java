package com.kotakotik.creategears.regitration;

import com.kotakotik.creategears.tiles.GearTile;
import com.kotakotik.creategears.tiles.HalfShaftGearVisual;
import com.kotakotik.creategears.util.Registration;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.world.level.block.state.BlockState;

public class GearsTiles extends Registration {
    public static BlockEntityEntry<GearTile> GEAR;

    public GearsTiles(CreateRegistrate r) {
        super(r);
    }

    @Override
    public void register() {
        GEAR = r.blockEntity("gear", GearTile::new)
                .visual(() -> gearVisualFactory(), false)
                .renderer(() -> KineticBlockEntityRenderer::new)
                .validBlocks(GearsBlocks.GEAR, GearsBlocks.LARGE_GEAR, GearsBlocks.HALF_SHAFT_GEAR, GearsBlocks.LARGE_HALF_SHAFT_GEAR)
                .register();
    }

    /**
     * Regular gears render as a shaftless cogwheel. The half-shaft variants use our own
     * {@link HalfShaftGearVisual} which renders a shaftless cogwheel plus a single half-shaft.
     */
    private static SimpleBlockEntityVisualizer.Factory<GearTile> gearVisualFactory() {
        return (ctx, be, partialTick) -> {
            BlockState state = be.getBlockState();
            boolean halfShaft = state.getBlock() == GearsBlocks.HALF_SHAFT_GEAR.get()
                    || state.getBlock() == GearsBlocks.LARGE_HALF_SHAFT_GEAR.get();

            if (halfShaft) {
                return new HalfShaftGearVisual(ctx, be, partialTick);
            }

            boolean large = state.getBlock() == GearsBlocks.LARGE_GEAR.get();
            PartialModel model = large ? AllPartialModels.SHAFTLESS_LARGE_COGWHEEL : AllPartialModels.SHAFTLESS_COGWHEEL;
            return new SingleAxisRotatingVisual<>(ctx, be, partialTick, Models.partial(model));
        };
    }
}
