package com.kotakotik.creategears.regitration;

import com.kotakotik.creategears.Gears;
import com.kotakotik.creategears.blocks.GearBlock;
import com.kotakotik.creategears.blocks.HalfShaftGearBlock;
import com.kotakotik.creategears.util.Registration;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class GearsBlocks extends Registration {
    public static BlockEntry<GearBlock> GEAR;
    public static BlockEntry<GearBlock> LARGE_GEAR;
    public static BlockEntry<HalfShaftGearBlock> HALF_SHAFT_GEAR;
    public static BlockEntry<HalfShaftGearBlock> LARGE_HALF_SHAFT_GEAR;

    public GearsBlocks(CreateRegistrate r) {
        super(r);
    }

    @Override
    public void register() {
        GEAR = r.block("gear", (p) -> new GearBlock(false, p))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate(BlockStateGen.axisBlockProvider(false))
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(CogwheelBlockItem::new)
                .model((c, p) -> {})
                .build()
                .recipe((ctx, prov) -> {
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get(), 8)
                            .pattern("www")
                            .pattern("w w")
                            .pattern("www")
                            .define('w', ItemTags.BUTTONS)
                            .unlockedBy("has_cogwheels", prov.has(AllBlocks.COGWHEEL.get()))
                            .save(prov);

                    ctx.get().toCogwheelRecipe(AllBlocks.COGWHEEL.get(), prov);
                    ctx.get().fromCogwheelRecipe(AllBlocks.COGWHEEL.get(), prov);
                })
                .register();

        LARGE_GEAR = r.block("large_gear", (p) -> new GearBlock(true, p))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate(BlockStateGen.axisBlockProvider(false))
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(CogwheelBlockItem::new)
                .model((c, p) -> {})
                .build()
                .recipe((ctx, prov) -> {
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get(), 2)
                            .pattern("bwb")
                            .pattern("w w")
                            .pattern("bwb")
                            .define('w', ItemTags.PLANKS)
                            .define('b', ItemTags.BUTTONS)
                            .unlockedBy("has_large_cogwheels", prov.has(AllBlocks.LARGE_COGWHEEL.get()))
                            .save(prov);

                    ctx.get().toCogwheelRecipe(AllBlocks.LARGE_COGWHEEL.get(), prov);
                    ctx.get().fromCogwheelRecipe(AllBlocks.LARGE_COGWHEEL.get(), prov);
                })
                .register();

        HALF_SHAFT_GEAR = r.block("half_shaft_gear", (p) -> new HalfShaftGearBlock(false, p))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate(GearsBlocks::halfShaftGearState)
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(CogwheelBlockItem::new)
                .model((c, p) -> {})
                .build()
                .recipe((ctx, prov) -> {
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get(), 8)
                            .pattern("www")
                            .pattern("waw")
                            .pattern("www")
                            .define('w', ItemTags.BUTTONS)
                            .define('a', Blocks.ANDESITE)
                            .unlockedBy("has_cogwheels", prov.has(AllBlocks.COGWHEEL.get()))
                            .save(prov);
                })
                .register();

        LARGE_HALF_SHAFT_GEAR = r.block("large_half_shaft_gear", (p) -> new HalfShaftGearBlock(true, p))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate(GearsBlocks::halfShaftGearState)
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(CogwheelBlockItem::new)
                .model((c, p) -> {})
                .build()
                .recipe((ctx, prov) -> {
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get(), 2)
                            .pattern("bwb")
                            .pattern("waw")
                            .pattern("bwb")
                            .define('w', ItemTags.PLANKS)
                            .define('b', ItemTags.BUTTONS)
                            .define('a', Blocks.ANDESITE)
                            .unlockedBy("has_large_cogwheels", prov.has(AllBlocks.LARGE_COGWHEEL.get()))
                            .save(prov);
                })
                .register();
    }

    private static ResourceLocation modLoc(String path) {
        return new ResourceLocation(Gears.MODID, path);
    }

    public static void halfShaftGearState(DataGenContext<Block, HalfShaftGearBlock> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.getEntry()).forAllStatesExcept((state) -> {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            Direction.AxisDirection dir = HalfShaftGearBlock.boolToAxisDirection(state.getValue(HalfShaftGearBlock.AXIS_DIRECTION));
            return ConfiguredModel.builder()
                    .modelFile(AssetLookup.standardModel(ctx, prov))
                    .rotationX((axis == Direction.Axis.Y ? 0 : 90) + (axis.isVertical() && dir == Direction.AxisDirection.NEGATIVE ? 180 : 0))
                    .rotationY((axis == Direction.Axis.X ? 90 : (axis == Direction.Axis.Z ? 180 : 0)) +
                            (axis.isHorizontal() && dir == Direction.AxisDirection.NEGATIVE ? 180 : 0)).build();
        }, BlockStateProperties.WATERLOGGED);
    }
}
