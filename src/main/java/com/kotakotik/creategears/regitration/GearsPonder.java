package com.kotakotik.creategears.regitration;

import com.kotakotik.creategears.Gears;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.ChainDriveScenes;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class GearsPonder implements PonderPlugin {

    @Override
    public String getModId() {
        return Gears.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> h = helper.withKeyFunction(RegistryEntry::getId);

        h.forComponents(GearsBlocks.GEAR, GearsBlocks.LARGE_GEAR)
                .addStoryBoard("cog/small", KineticsScenes::cogAsRelay, AllCreatePonderTags.KINETIC_RELAYS)
                .addStoryBoard("cog/large", KineticsScenes::largeCogAsRelay, AllCreatePonderTags.KINETIC_RELAYS);

        h.forComponents(GearsBlocks.FULLY_ENCASED_CHAIN_DRIVE)
                .addStoryBoard("chain_drive/relay", ChainDriveScenes::chainDriveAsRelay, AllCreatePonderTags.KINETIC_RELAYS);

        h.forComponents(GearsBlocks.SIMPLE_GEARSHIFT)
                .addStoryBoard("gearshift", KineticsScenes::gearshift, AllCreatePonderTags.KINETIC_RELAYS);
    }
}
