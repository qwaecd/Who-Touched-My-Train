package com.qwaecd.wtmt.tools;

import net.minecraft.resources.ResourceLocation;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class ModRL {
    public static ResourceLocation of(String namespace, String location) {
        return ResourceLocation.fromNamespaceAndPath(namespace, location);
    }

    public static ResourceLocation InModSpace(String location) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }
}
