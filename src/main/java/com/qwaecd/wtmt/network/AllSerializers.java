package com.qwaecd.wtmt.network;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class AllSerializers {
    public static final CarriageAuthDataSerializer AUTH_DATA = new CarriageAuthDataSerializer();

    private static final DeferredRegister<EntityDataSerializer<?>> REGISTER = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, CarriageAuthDataSerializer> CARRIAGE_AUTH_DATA = REGISTER.register("carriage_auth_data", () -> AUTH_DATA);

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
