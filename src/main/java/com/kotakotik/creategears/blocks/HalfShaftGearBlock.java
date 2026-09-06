package com.kotakotik.creategears.blocks;

import com.kotakotik.creategears.util.ShapeUtils;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HalfShaftGearBlock extends GearBlock {

    public VoxelShape shape = cuboid(2.0D, 6.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    public VoxelShaper shaper = shape(shape).add(6.0D, 8, 6.0D, 10.0D, 16, 10.0D).forDirectional();

    public VoxelShape shapeLarge = cuboid(0.0D, 6.0D, 0.0D, 16.0D, 10.0D, 16.0D);
    public VoxelShaper shaperLarge = shape(shapeLarge).add(6.0D, 8, 6.0D, 10.0D, 16.0D, 10.0D).forDirectional();

    public static final BooleanProperty AXIS_DIRECTION = BooleanProperty.create("axis_direction");

    public HalfShaftGearBlock(boolean large, Properties properties) {
        super(large, properties);
        registerDefaultState(this.defaultBlockState().setValue(AXIS_DIRECTION, axisDirectionToBool(Direction.AxisDirection.POSITIVE)));
    }

    public static boolean axisDirectionToBool(Direction.AxisDirection dir) {
        return dir == Direction.AxisDirection.POSITIVE;
    }

    public static Direction.AxisDirection boolToAxisDirection(boolean bool) {
        return bool ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_DIRECTION);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS) && face.getAxisDirection() == boolToAxisDirection(state.getValue(AXIS_DIRECTION));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction dir = Direction.fromAxisAndDirection(state.getValue(AXIS), boolToAxisDirection(state.getValue(AXIS_DIRECTION)));
        return isLargeCog() ? shaperLarge.get(dir) : shaper.get(dir);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // shut up
        Direction dir = context.getClickedFace().getOpposite();
        boolean b = axisDirectionToBool(dir.getAxisDirection());
        Direction.Axis a = dir.getAxis();
        return super.getStateForPlacement(context).setValue(AXIS_DIRECTION, context.getPlayer() != null && context.getPlayer().isShiftKeyDown() != b).setValue(AXIS, a);
    }
}
