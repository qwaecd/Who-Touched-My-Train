package com.qwaecd.wtmt.mixin;


import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.qwaecd.wtmt.api.TrainPermissionLevel;
import com.qwaecd.wtmt.data.CarriageAuthData;
import com.qwaecd.wtmt.network.AllSerializers;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.UUID;


@SuppressWarnings("FieldMayBeFinal")
@Mixin(value = CarriageContraptionEntity.class)
public abstract class CarriageContraptionEntityMixin extends OrientedContraptionEntity implements ITrainInfoProvider {
    @SuppressWarnings("WrongEntityDataParameterClass")
    @Unique
    private static final EntityDataAccessor<CarriageAuthData> AUTH_DATA$who_touched_my_train =
            SynchedEntityData.defineId(CarriageContraptionEntity.class, AllSerializers.AUTH_DATA);


    public CarriageContraptionEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
            method = "startControlling",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            remap = false
    )
    private void mixinStartControlling(BlockPos controlsLocalPos, Player player, CallbackInfoReturnable<Boolean> ci) {
        if (player == null || player.isSpectator()) {
            return;
        }
        String playerName = player.getName().getString();

        if (!this.hasUsePermission$who_touched_my_train(playerName)) {
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.train_no_permission"), true);
            ci.setReturnValue(true);
        }
    }

    @Inject(
            method = "control",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            remap = false
    )
    private void controlMixin(BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player, CallbackInfoReturnable<Boolean> ci) {
        // noinspection resource
        if (level().isClientSide()) {
            return;
        }
        if (player == null || player.isSpectator()) {
            return;
        }
        String playerName = player.getName().getString();
        if (!this.hasUsePermission$who_touched_my_train(playerName)) {
            ci.setReturnValue(false);
        }
    }

    @Inject(
            method = "defineSynchedData",
            at = @At("RETURN")
    )
    private void defineSynchedDataMixin(CallbackInfo ci) {
        entityData.define(AUTH_DATA$who_touched_my_train, new CarriageAuthData());
    }

    @Inject(
            method = "onSyncedDataUpdated",
            at = @At("TAIL")
    )
    private void onSyncedDataUpdatedMixin(EntityDataAccessor<?> key, CallbackInfo ci) {
        if (AUTH_DATA$who_touched_my_train.equals(key)) {
            // do nothing for now
        }
    }

    @Inject(method = "writeAdditional", at = @At("TAIL"), remap = false)
    private void writeAdditionalMixin(CompoundTag compound, boolean spawnPacket, CallbackInfo ci) {
        CarriageAuthData authData = this.getAuthData$who_touched_my_train();
        authData.write(compound);
    }

    @Inject(method = "readAdditional", at = @At("TAIL"), remap = false)
    private void readAdditionalMixin(CompoundTag compound, boolean spawnPacket, CallbackInfo ci) {
        CarriageAuthData authData = this.getAuthData$who_touched_my_train();
        authData.read(compound);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickMixin(CallbackInfo ci) {
        CarriageAuthData authData = this.getAuthData$who_touched_my_train();
        //noinspection resource
        if (authData.isDirty() && !level().isClientSide()) {
            authData.setDirty(false);
            entityData.set(AUTH_DATA$who_touched_my_train, authData, true);
        }
    }

    @Override
    public CarriageAuthData getAuthData$who_touched_my_train() {
        return entityData.get(AUTH_DATA$who_touched_my_train);
    }

    @Override
    public boolean hasUsePermission$who_touched_my_train(String playerName) {
        if (!this.hasOwner$who_touched_my_train()) {
            return true;
        }
        CarriageAuthData authData = this.getAuthData$who_touched_my_train();
        if (authData.getPermissionLevel() == TrainPermissionLevel.PROTECTED) {
            return true;
        }

        return hasAuthorizedPlayer$who_touched_my_train(playerName);
    }

    @Override
    public boolean hasOwner$who_touched_my_train() {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        return data.getOwnerPlayerName() != null && !data.getOwnerPlayerName().isEmpty();
    }

    @Override
    public String getOwnerPlayerName$who_touched_my_train() {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        return data.getOwnerPlayerName();
    }

    @Override
    public void setOwnerPlayerName$who_touched_my_train(String playerName) {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        data.setOwnerPlayerName(playerName);
        this.authorizePlayer$who_touched_my_train(playerName);
        entityData.set(AUTH_DATA$who_touched_my_train, data, true);
    }

    @Override
    public boolean hasAuthorizedPlayer$who_touched_my_train(String playerName) {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        return data.hasAuthorizedPlayer(playerName);
    }

    @Override
    public void authorizePlayer$who_touched_my_train(String playerName) {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        data.authorizePlayer(playerName);
        entityData.set(AUTH_DATA$who_touched_my_train, data, true);
    }

    @Override
    public void deauthorizePlayer$who_touched_my_train(String playerName) {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        if (playerName.equals(data.getOwnerPlayerName())) {
            return;
        }
        data.deauthorizePlayer(playerName);
        entityData.set(AUTH_DATA$who_touched_my_train, data, true);
    }

    @Override
    public void setPublic$who_touched_my_train() {
        CarriageAuthData data = getAuthData$who_touched_my_train();
        data.setPublic();
        entityData.set(AUTH_DATA$who_touched_my_train, data, true);
    }

    @Override
    public UUID getEntityUUID$who_touched_my_train() {
        return this.uuid;
    }
}
