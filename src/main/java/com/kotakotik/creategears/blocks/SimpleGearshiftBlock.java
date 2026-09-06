package com.kotakotik.creategears.blocks;

import com.kotakotik.creategears.regitration.GearsTiles;
import com.kotakotik.creategears.util.GenericUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleGearshiftBlock extends GearshiftBlock implements GenericUtils {

    public SimpleGearshiftBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, true));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(POWERED, true);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        // ignore redstone stuff
    }

    @Override
    public BlockEntityType<? extends SplitShaftBlockEntity> getBlockEntityType() {
        return GearsTiles.SIMPLE_GEARSHIFT.get();
    }

    @Override
    public Class<SplitShaftBlockEntity> getBlockEntityClass() {
        return SplitShaftBlockEntity.class;
    }

    public ShapedRecipeBuilder recipe(RegistrateRecipeProvider prov) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, this)
                .define('c', AllBlocks.ANDESITE_CASING.get())
                .define('w', AllBlocks.COGWHEEL.get())
                .unlockedBy("has_cogwheel", prov.has(AllBlocks.COGWHEEL.get()));
    }

    public void recipe(ShapedRecipeBuilder builder, RegistrateRecipeProvider prov, String type) {
        builder.save(prov, modLoc("simple_gearshift_" + type));
    }
}
