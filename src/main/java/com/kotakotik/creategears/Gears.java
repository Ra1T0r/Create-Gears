package com.kotakotik.creategears;

import com.kotakotik.creategears.regitration.GearsBlocks;
import com.kotakotik.creategears.regitration.GearsPonder;
import com.kotakotik.creategears.regitration.GearsTiles;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Gears.MODID)
public class Gears {

    public static final String MODID = "creategears";
    public static final Logger LOGGER = LoggerFactory.getLogger("Create Gears");

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public static final ResourceKey<CreativeModeTab> TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MODID, "main"));

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
            TAB_REGISTER.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.creategears"))
                    .icon(() -> new ItemStack(GearsBlocks.GEAR.get()))
                    .build());

    public Gears(IEventBus modBus, ModContainer container) {
        // Registrate must be wired to the mod event bus before any registration happens.
        TAB_REGISTER.register(modBus);
        REGISTRATE.registerEventListeners(modBus);

        // All items registered by Registrate go into our custom tab.
        REGISTRATE.setCreativeTab(TAB);
        REGISTRATE.addRawLang("itemGroup." + MODID, "Create Gears");

        // Register content.
        new GearsBlocks(REGISTRATE).register();
        new GearsTiles(REGISTRATE).register();

        // Register the Ponder plugin on client setup.
        modBus.addListener((FMLClientSetupEvent event) -> {
            event.enqueueWork(() -> PonderIndex.addPlugin(new GearsPonder()));
        });
    }
}
