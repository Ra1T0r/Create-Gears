package com.kotakotik.creategears;

import com.kotakotik.creategears.regitration.GearsBlocks;
import com.kotakotik.creategears.regitration.GearsStressProvider;
import com.kotakotik.creategears.regitration.GearsTiles;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Gears.MODID)
public class Gears {

    public static final String MODID = "creategears";
    public static final Logger LOGGER = LoggerFactory.getLogger("Create Gears");

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public static final ResourceKey<CreativeModeTab> TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(MODID, "main"));

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final RegistryObject<CreativeModeTab> TAB =
            TAB_REGISTER.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.creategears"))
                    .icon(() -> new ItemStack(GearsBlocks.GEAR.get()))
                    .displayItems((params, output) -> {
                        output.accept(GearsBlocks.GEAR.get());
                        output.accept(GearsBlocks.LARGE_GEAR.get());
                        output.accept(GearsBlocks.HALF_SHAFT_GEAR.get());
                        output.accept(GearsBlocks.LARGE_HALF_SHAFT_GEAR.get());
                    })
                    .build());

    public Gears() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registrate must be wired to the mod event bus before any registration happens.
        TAB_REGISTER.register(modBus);
        REGISTRATE.registerEventListeners(modBus);

        // Register content.
        new GearsBlocks(REGISTRATE).register();
        new GearsTiles(REGISTRATE).register();

        // Register stress values for all kinetic blocks.
        GearsStressProvider.register();
    }
}
