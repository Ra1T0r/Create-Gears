package com.kotakotik.creategears.blocks;

import com.kotakotik.creategears.regitration.GearsTiles;
import com.kotakotik.creategears.util.GenericUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FullyEncasedBeltBlock extends ChainDriveBlock implements GenericUtils {

    public FullyEncasedBeltBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return GearsTiles.FULLY_ENCASED_BELT.get();
    }

    public String getSuffix(String suffix) {
        if (suffix.contains("horizontal"))
            return "horizontal";
        if (suffix.contains("vertical"))
            return "vertical";
        if (suffix.contains("item"))
            return "item";
        return "horizontal";
    }

    public ShapedRecipeBuilder fullyEncasedChainDriveRecipe(RegistrateRecipeProvider prov) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, this, 2)
                .define('c', AllBlocks.ANDESITE_CASING.get())
                .define('s', AllBlocks.SHAFT.get())
                .unlockedBy("has_shaft", prov.has(AllBlocks.SHAFT.get()));
    }

    public void fullyEncasedChainDriveRecipe(ShapedRecipeBuilder builder, RegistrateRecipeProvider prov, String type) {
        builder.save(prov, modLoc("fully_encased_chain_drive_" + type));
    }
}
