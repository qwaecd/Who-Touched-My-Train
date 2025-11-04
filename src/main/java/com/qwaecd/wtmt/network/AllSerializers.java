package com.qwaecd.wtmt.network;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class AllSerializers {
    public static final CarriageAuthDataSerializer AUTH_DATA = new CarriageAuthDataSerializer();

    private static final DeferredRegister<EntityDataSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MOD_ID);

    public static final RegistryObject<CarriageAuthDataSerializer> CARRIAGE_AUTH_DATA = REGISTER.register("carriage_auth_data", () -> AUTH_DATA);

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
