package com.qwaecd.wtmt.network;

import com.qwaecd.wtmt.data.CarriageAuthData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public class CarriageAuthDataSerializer implements EntityDataSerializer<CarriageAuthData> {
    @Override
    public void write(FriendlyByteBuf buf, CarriageAuthData carriageAuthData) {
        carriageAuthData.write(buf);
    }

    @Override
    public CarriageAuthData read(FriendlyByteBuf buf) {
        CarriageAuthData data = new CarriageAuthData();
        data.read(buf);
        return data;
    }

    @Override
    public CarriageAuthData copy(CarriageAuthData data) {
        return data.copy();
    }
}
