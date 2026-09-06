package com.kotakotik.creategears.blocks;

import com.kotakotik.creategears.regitration.GearsTiles;
import com.kotakotik.creategears.tiles.GearTile;
import com.kotakotik.creategears.util.GenericUtils;
import com.kotakotik.creategears.util.ShapeUtils;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.resources.ResourceLocation;

public class GearBlock extends CogWheelBlock implements ShapeUtils, GenericUtils {

    public VoxelShape shape = cuboid(2.0D, 6.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    public VoxelShaper shaper = shape(shape).forAxis();

    public VoxelShape shapeLarge = cuboid(0.0D, 6.0D, 0.0D, 16.0D, 10.0D, 16.0D);
    public VoxelShaper shaperLarge = shape(shapeLarge).forAxis();

    public GearBlock(boolean large, Properties properties) {
        super(large, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (isLargeCog() ? shaperLarge : shaper).get(state.getValue(AXIS));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false; // well that was easy
    }

    @Override
    public BlockEntityType<? extends GearTile> getBlockEntityType() {
        return GearsTiles.GEAR.get();
    }

    public void toCogwheelRecipe(CogWheelBlock cogwheel, RegistrateRecipeProvider prov) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, cogwheel)
                .requires(this).requires(AllItems.ANDESITE_ALLOY.get())
                .unlockedBy("has_gear", RegistrateRecipeProvider.has(this))
                .save(prov, modLoc((cogwheel.isLargeCog() ? "large_" : "") + "gear_to_cogwheel"));
    }

    public void fromCogwheelRecipe(CogWheelBlock cogwheel, RegistrateRecipeProvider prov) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, this)
                .requires(cogwheel)
                .unlockedBy("has_cogwheel", RegistrateRecipeProvider.has(cogwheel))
                .save(prov, modLoc((cogwheel.isLargeCog() ? "large_" : "") + "gear_from_cogwheel"));
    }
}
