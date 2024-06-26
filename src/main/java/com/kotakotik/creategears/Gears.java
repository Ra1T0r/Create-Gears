package com.kotakotik.creategears;

import com.kotakotik.creategears.regitration.GearsBlocks;
import com.kotakotik.creategears.regitration.GearsPonder;
import com.kotakotik.creategears.regitration.GearsStressProvider;
import com.kotakotik.creategears.regitration.GearsTiles;
import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.OneTimeEventReceiver;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gears.modid)
public class Gears {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String modid = "creategears";
    public static IEventBus MOD_EVENT_BUS;

    public final CreateRegistrate REGISTRATE = CreateRegistrate.lazy(modid).get();

    public static ItemGroup itemGroup = new ItemGroup(modid) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GearsBlocks.GEAR.get());
        }
    };

    public Gears() {
        BlockStressValues.registerProvider(modid, new GearsStressProvider());

        // events
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        // registration

//        GearsConfig.register();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GearsConfig.CONFIG, "gears.toml");

        REGISTRATE.itemGroup(() -> itemGroup, "Create Gears");
        new GearsBlocks(REGISTRATE).register();
        new GearsTiles(REGISTRATE).register();

        OneTimeEventReceiver.addListener(MOD_EVENT_BUS, FMLClientSetupEvent.class, (event) -> {
            event.enqueueWork(GearsPonder::register);
        });
    }
}

