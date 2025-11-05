package com.qwaecd.wtmt.data;

import com.qwaecd.wtmt.api.TrainPermissionLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CarriageAuthData implements IAuthDataAccessor {
    private String ownerPlayerName = "";
    private final Set<String> authorizedPlayers = new HashSet<>();
    public static final String AUTH_PLAYER_KEY = "AuthorizedPlayers";
    private TrainPermissionLevel permissionLevel = TrainPermissionLevel.PUBLIC;

    // 无需同步, 0 是默认值 不使用
    private long generation = 1L;

    private boolean isDirty = false;

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.permissionLevel.getLevel());
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

    public void write(CompoundTag compound) {
        compound.putString("OwnerPlayerName", this.ownerPlayerName);
        compound.putInt("PermissionLevel", this.permissionLevel.getLevel());
        compound.putLong("Generation", this.generation);
        CompoundTag authPlayersTag = new CompoundTag();
        int index = 0;
        for (String playerName : this.authorizedPlayers) {
            if (playerName != null) {
                authPlayersTag.putString(String.valueOf(index), playerName);
                index++;
            }
        }
        compound.putInt("AuthorizedPlayerCount", index);
        compound.put(AUTH_PLAYER_KEY, authPlayersTag);
    }

    public void read(CompoundTag compound) {
        this.ownerPlayerName = compound.getString("OwnerPlayerName");
        this.permissionLevel = TrainPermissionLevel.fromLevel(compound.getInt("PermissionLevel"));
        this.generation = compound.getLong("Generation");
        CompoundTag authPlayersTag = compound.getCompound(AUTH_PLAYER_KEY);
        int size = compound.getInt("AuthorizedPlayerCount");
        this.authorizedPlayers.clear();
        for (int i = 0; i < size; i++) {
            String playerName = authPlayersTag.getString(String.valueOf(i));
            this.authorizedPlayers.add(playerName);
        }
        this.isDirty = true;
    }


    public void read(FriendlyByteBuf buffer) {
        this.permissionLevel = TrainPermissionLevel.fromLevel(buffer.readInt());
        this.ownerPlayerName = StringSyncHelper.readString(buffer);
        int size = buffer.readInt();
        this.authorizedPlayers.clear();
        for (int i = 0; i < size; i++) {
            String playerName = StringSyncHelper.readString(buffer);
            this.authorizedPlayers.add(playerName);
        }
//        System.out.println("Read CarriageAuthData: owner=" + this.ownerPlayerName + ", authorizedPlayers=" + this.authorizedPlayers + ", permissionLevel=" + this.permissionLevel);
    }

    public CarriageAuthData copy() {
        CarriageAuthData data = new CarriageAuthData();
        data.permissionLevel = this.permissionLevel;
        data.authorizedPlayers.addAll(this.authorizedPlayers);
        data.ownerPlayerName = this.ownerPlayerName;
        return data;
    }

    @Override
    public String getOwnerPlayerName() {
        return this.ownerPlayerName;
    }

    @Override
    public void setOwnerPlayerName(String ownerPlayerName) {
        this.generation++;
        if (ownerPlayerName == null) {
            this.ownerPlayerName = "";
            return;
        }
        this.ownerPlayerName = ownerPlayerName;
        this.permissionLevel = TrainPermissionLevel.PRIVATE;
        this.isDirty = true;
    }

    @Override
    public TrainPermissionLevel getPermissionLevel() {
        return this.permissionLevel;
    }

    @Override
    public void setPermissionLevel(TrainPermissionLevel level) {
        this.permissionLevel = level;
        this.isDirty = true;
    }

    @Override
    public boolean hasAuthorizedPlayer(String playerName) {
        return playerName.equals(this.ownerPlayerName) || this.authorizedPlayers.contains(playerName);
    }

    public void authorizePlayer(String playerName) {
        if (playerName == null) {
            return;
        }
        this.authorizedPlayers.add(playerName);
        this.isDirty = true;
    }

    public void deauthorizePlayer(String playerName) {
        this.authorizedPlayers.remove(playerName);
        this.isDirty = true;
    }

    public void setPublic() {
        this.ownerPlayerName = "";
        this.authorizedPlayers.clear();
        this.permissionLevel = TrainPermissionLevel.PUBLIC;
        this.isDirty = true;
    }

    public void apply(CarriageAuthData newData) {
        this.permissionLevel = newData.permissionLevel;
        this.ownerPlayerName = newData.ownerPlayerName;
        this.authorizedPlayers.clear();
        this.authorizedPlayers.addAll(newData.authorizedPlayers);
        this.isDirty = true;
    }

    @Override
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
        return Objects.equals(ownerPlayerName, that.ownerPlayerName)
                && Objects.equals(authorizedPlayers, that.authorizedPlayers)
                && permissionLevel.equals(that.permissionLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerPlayerName, authorizedPlayers, permissionLevel);
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean b) {
        this.isDirty = b;
    }
}
