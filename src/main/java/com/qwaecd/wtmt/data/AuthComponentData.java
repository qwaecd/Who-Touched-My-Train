package com.qwaecd.wtmt.data;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class AuthComponentData {
    public static final String COMPONENT_NAME = "auth_data";

    private UUID carriageUUID;
    private String ownerName;
    public static final String OWNER_NAME = "OwnerName";
    private long generation = -1;
    public static final String GENERATION = "Generation";

    public AuthComponentData(String ownerName, ITrainInfoProvider infoProvider) {
        this(
                ownerName,
                infoProvider.getEntityUUID$who_touched_my_train(),
                infoProvider.getAuthData$who_touched_my_train().getGeneration()
        );
    }

    private AuthComponentData(String ownerName, UUID uuid, long generation) {
        this.ownerName = ownerName;
        this.carriageUUID = uuid;
        this.generation = generation;
    }

    public static AuthComponentData read(CompoundTag rootTag) {
        CompoundTag authComponent = rootTag.getCompound(COMPONENT_NAME);
        UUID uuid = authComponent.getUUID("CarriageUUID");
        String ownerName = authComponent.getString(OWNER_NAME);
        long generation = authComponent.getLong(GENERATION);
        return new AuthComponentData(ownerName, uuid, generation);
    }

    public CompoundTag toComponentTag() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("CarriageUUID", this.carriageUUID);
        tag.putString(OWNER_NAME, this.ownerName);
        tag.putLong(GENERATION, this.generation);
        return tag;
    }

    public void write(CompoundTag tag) {
        tag.putUUID("CarriageUUID", this.carriageUUID);
        tag.putString(OWNER_NAME, this.ownerName);
        tag.putLong(GENERATION, this.generation);
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public UUID getCarriageUUID() {
        return this.carriageUUID;
    }

    public void setCarriageUUID(UUID carriageUUID) {
        this.carriageUUID = carriageUUID;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getGeneration() {
        return this.generation;
    }

    public boolean isOverdue(long otherGeneration) {
        return this.generation != otherGeneration;
    }
}
