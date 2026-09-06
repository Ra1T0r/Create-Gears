package com.kotakotik.creategears.util;

import com.kotakotik.creategears.Gears;
import net.minecraft.resources.ResourceLocation;

public interface GenericUtils {
    default ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Gears.MODID, path);
    }
}
