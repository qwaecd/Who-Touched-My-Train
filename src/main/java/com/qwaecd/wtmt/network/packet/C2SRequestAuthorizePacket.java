package com.qwaecd.wtmt.network.packet;

import com.qwaecd.wtmt.data.CarriageAuthData;
import com.qwaecd.wtmt.server.AuthReq;
import com.qwaecd.wtmt.server.TrainEntityAuthRequest;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SRequestAuthorizePacket {
    private final String playerName;
    private final UUID trainUUID;

    public C2SRequestAuthorizePacket(String playerName, UUID trainUUID) {
        this.playerName = playerName;
        this.trainUUID = trainUUID;
    }

    public C2SRequestAuthorizePacket(FriendlyByteBuf buf) {
        this.playerName = CarriageAuthData.StringSyncHelper.readString(buf);
        this.trainUUID = buf.readUUID();
    }

    public void write(FriendlyByteBuf buf) {
        CarriageAuthData.StringSyncHelper.writeString(buf, this.playerName);
        buf.writeUUID(this.trainUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            TrainEntityAuthRequest.offerRequest(this.trainUUID, new AuthReq(trainUUID, playerName));
        });
        context.setPacketHandled(true);
        return true;
    }

    public static C2SRequestAuthorizePacket create(String playerName, UUID trainUUID) {
        return new C2SRequestAuthorizePacket(playerName, trainUUID);
    }
}
