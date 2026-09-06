package com.kotakotik.creategears.regitration;

import com.kotakotik.creategears.Gears;
import com.kotakotik.creategears.tiles.GearTile;
import com.kotakotik.creategears.util.Registration;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.simibubi.create.content.kinetics.transmission.SplitShaftVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class GearsTiles extends Registration {
    public static BlockEntityEntry<GearTile> GEAR;
    public static BlockEntityEntry<KineticBlockEntity> FULLY_ENCASED_BELT;
    public static BlockEntityEntry<GearshiftBlockEntity> SIMPLE_GEARSHIFT;

    public GearsTiles(CreateRegistrate r) {
        super(r);
    }

    @Override
    public void register() {
        GEAR = r.blockEntity("gear", GearTile::new)
                .renderer(() -> KineticBlockEntityRenderer::new)
                .validBlocks(GearsBlocks.GEAR, GearsBlocks.LARGE_GEAR, GearsBlocks.HALF_SHAFT_GEAR, GearsBlocks.LARGE_HALF_SHAFT_GEAR)
                .register();

        FULLY_ENCASED_BELT = r.blockEntity("fully_encased_shaft", KineticBlockEntity::new)
                .validBlock(GearsBlocks.FULLY_ENCASED_CHAIN_DRIVE)
                .register();

        SIMPLE_GEARSHIFT = r.blockEntity("simple_gearshift", GearshiftBlockEntity::new)
                .visual(() -> SplitShaftVisual::new)
                .validBlock(GearsBlocks.SIMPLE_GEARSHIFT)
                .renderer(() -> SplitShaftRenderer::new)
                .register();
    }
}
