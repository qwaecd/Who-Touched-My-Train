package com.qwaecd.wtmt.data;

import net.minecraft.network.FriendlyByteBuf;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CarriageAuthData {
    private String ownerPlayerName = "";
    private final Set<String> authorizedPlayers = new HashSet<>();

    // 无需同步, 0 是默认值 不使用
    private long generation = 1L;

    public void write(FriendlyByteBuf buffer) {
        if (this.ownerPlayerName == null) {
            this.ownerPlayerName = "";
        }
        StringSyncHelper.writeString(buffer, this.ownerPlayerName);
        int size = this.authorizedPlayers.size();
        buffer.writeInt(size);
        for (String playerName : this.authorizedPlayers) {
            if (playerName != null) {
                StringSyncHelper.writeString(buffer, playerName);
            }
        }
//        System.out.println("Written CarriageAuthData: owner=" + this.ownerPlayerName + ", authorizedPlayers=" + this.authorizedPlayers);
    }

    public void read(FriendlyByteBuf buffer) {
        this.ownerPlayerName = StringSyncHelper.readString(buffer);
        int size = buffer.readInt();
        this.authorizedPlayers.clear();
        for (int i = 0; i < size; i++) {
            String playerName = StringSyncHelper.readString(buffer);
            this.authorizedPlayers.add(playerName);
        }
//        System.out.println("Read CarriageAuthData: owner=" + this.ownerPlayerName + ", authorizedPlayers=" + this.authorizedPlayers);
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
        this.generation++;
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

    public long getGeneration() {
        return this.generation;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarriageAuthData that = (CarriageAuthData) o;
        return Objects.equals(ownerPlayerName, that.ownerPlayerName) && Objects.equals(authorizedPlayers, that.authorizedPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerPlayerName, authorizedPlayers);
    }
}
