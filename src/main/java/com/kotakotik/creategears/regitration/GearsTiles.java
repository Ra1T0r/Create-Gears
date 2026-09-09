package com.kotakotik.creategears.regitration;

import com.kotakotik.creategears.tiles.GearTile;
import com.kotakotik.creategears.util.Registration;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GearsTiles extends Registration {
    public static BlockEntityEntry<GearTile> GEAR;

    public GearsTiles(CreateRegistrate r) {
        super(r);
    }

    @Override
    public void register() {
        GEAR = r.blockEntity("gear", GearTile::new)
                .instance(() -> SingleRotatingInstance::new, false)
                .renderer(() -> KineticBlockEntityRenderer::new)
                .validBlocks(GearsBlocks.GEAR, GearsBlocks.LARGE_GEAR, GearsBlocks.HALF_SHAFT_GEAR, GearsBlocks.LARGE_HALF_SHAFT_GEAR)
                .register();
    }
}
