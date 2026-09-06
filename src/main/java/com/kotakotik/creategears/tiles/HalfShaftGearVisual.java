package com.kotakotik.creategears.tiles;

import com.kotakotik.creategears.blocks.HalfShaftGearBlock;
import com.kotakotik.creategears.regitration.GearsBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import dev.engine_room.flywheel.lib.instance.FlatLit;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * Renders a half-shaft gear as a shaftless cogwheel plus a single shaft half sticking
 * out along the gear's configured axis direction.
 */
public class HalfShaftGearVisual extends KineticBlockEntityVisual<GearTile> {

    protected final RotatingInstance gear;
    protected final RotatingInstance halfShaft;

    public HalfShaftGearVisual(VisualizationContext context, GearTile blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        BlockState state = blockEntity.getBlockState();
        boolean large = state.getBlock() == GearsBlocks.LARGE_HALF_SHAFT_GEAR.get();
        PartialModel model = large ? AllPartialModels.SHAFTLESS_LARGE_COGWHEEL : AllPartialModels.SHAFTLESS_COGWHEEL;

        gear = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(model))
                .createInstance()
                .rotateToFace(Direction.UP, rotationAxis())
                .setup(blockEntity)
                .setPosition(getVisualPosition());

        Direction.Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);
        boolean positive = state.getValue(HalfShaftGearBlock.AXIS_DIRECTION);
        Direction shaftDirection = Direction.fromAxisAndDirection(axis, positive ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);

        halfShaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance()
                .rotateToFace(Direction.SOUTH, shaftDirection)
                .setup(blockEntity)
                .setPosition(getVisualPosition());

        update(partialTick);
        updateLight(partialTick);
    }

    @Override
    public void update(float pt) {
        gear.setup(blockEntity).setChanged();
        halfShaft.setup(blockEntity).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(gear, halfShaft);
    }

    @Override
    protected void _delete() {
        gear.delete();
        halfShaft.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(gear);
        consumer.accept(halfShaft);
    }
}
