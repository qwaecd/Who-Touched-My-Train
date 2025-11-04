package com.qwaecd.wtmt.api;

import net.minecraft.network.FriendlyByteBuf;

import java.util.HashSet;
import java.util.Set;

public class CarriageAuthData {
    private String ownerPlayerName = "";
    private final Set<String> authorizedPlayers = new HashSet<>();

    public void write(FriendlyByteBuf buffer) {
        if (this.ownerPlayerName == null) {
            this.ownerPlayerName = "";
        }
        StringSyncHelper.writeString(buffer, this.ownerPlayerName);
        int size = this.authorizedPlayers.size();
        buffer.writeInt(size);
        for (String playerName : this.authorizedPlayers) {
            StringSyncHelper.writeString(buffer, playerName);
        }
    }

    public void read(FriendlyByteBuf buffer) {
        this.ownerPlayerName = StringSyncHelper.readString(buffer);
        int size = buffer.readInt();
        this.authorizedPlayers.clear();
        for (int i = 0; i < size; i++) {
            String playerName = StringSyncHelper.readString(buffer);
            this.authorizedPlayers.add(playerName);
        }
    }

    public CarriageAuthData copy() {
        CarriageAuthData data = new CarriageAuthData();
        data.authorizedPlayers.addAll(this.authorizedPlayers);
        data.ownerPlayerName = this.ownerPlayerName;
        return data;
    }

    public String getOwnerPlayerName() {
        return this.ownerPlayerName;
    }

    public void setOwnerPlayerName(String ownerPlayerName) {
        if (ownerPlayerName == null) {
            this.ownerPlayerName = "";
            return;
        }
        this.ownerPlayerName = ownerPlayerName;
    }

    public boolean hasAuthorizedPlayer(String playerName) {
        return playerName.equals(this.ownerPlayerName) || this.authorizedPlayers.contains(playerName);
    }

    public void authorizePlayer(String playerName) {
        if (playerName == null) {
            return;
        }
        this.authorizedPlayers.add(playerName);
    }

    public void deauthorizePlayer(String playerName) {
        this.authorizedPlayers.remove(playerName);
    }

    public void setPublic() {
        this.ownerPlayerName = "";
        this.authorizedPlayers.clear();
    }

    public void apply(CarriageAuthData newData) {
        this.ownerPlayerName = newData.ownerPlayerName;
        this.authorizedPlayers.clear();
        this.authorizedPlayers.addAll(newData.authorizedPlayers);
    }

    public static class StringSyncHelper {
        public static String readString(FriendlyByteBuf buffer) {
            int length = buffer.readInt();
            return buffer.readUtf(length);
        }

        public static void writeString(FriendlyByteBuf buffer, String value) {
            int length = value.length();
            buffer.writeInt(length);
            buffer.writeUtf(value, length);
        }
    }
}
