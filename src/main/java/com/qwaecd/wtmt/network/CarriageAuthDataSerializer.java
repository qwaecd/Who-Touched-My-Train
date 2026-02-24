package com.qwaecd.wtmt.network;

import com.qwaecd.wtmt.data.CarriageAuthData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

import javax.annotation.Nonnull;

public class CarriageAuthDataSerializer implements EntityDataSerializer<CarriageAuthData> {
    public void write(FriendlyByteBuf buf, CarriageAuthData carriageAuthData) {
        carriageAuthData.write(buf);
    }

    public CarriageAuthData read(FriendlyByteBuf buf) {
        CarriageAuthData data = new CarriageAuthData();
        data.read(buf);
        return data;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CarriageAuthData> codec() {
        return StreamCodec.of(CarriageAuthData::encode, CarriageAuthData::decode);
//        return new StreamCodec<>() {
//            @Override
//            public void encode(RegistryFriendlyByteBuf buffer, CarriageAuthData value) {
//                value.write(buffer);
//            }
//
//            @Override
//            public CarriageAuthData decode(RegistryFriendlyByteBuf buffer) {
//                return CarriageAuthData.decode(buffer);
//            }
//        };
    }

    @Override
    @Nonnull
    public CarriageAuthData copy(CarriageAuthData data) {
        return data.copy();
    }
}
