package com.qwaecd.wtmt.item;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.qwaecd.wtmt.api.TrainPermissionLevel;
import com.qwaecd.wtmt.data.IAuthDataAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class TrainLock extends Item {
    public TrainLock(Properties properties) {
        super(properties.stacksTo(1));
    }

    public void onRightClickControls(@Nonnull ItemStack itemInHand, @Nonnull Player player, @Nonnull ITrainInfoProvider infoProvider) {
        String playerName = player.getName().getString();
        String ownerName = infoProvider.getOwnerPlayerName$who_touched_my_train();
        if (!playerName.equals(ownerName)) {
            return;
        }
        //noinspection resource
        if (!player.level().isClientSide()) {
            switchLockState(player, infoProvider);
        }
    }

    private void switchLockState(Player player,ITrainInfoProvider infoProvider) {
        IAuthDataAccessor authData = infoProvider.getAuthData$who_touched_my_train();
        TrainPermissionLevel permissionLevel = authData.getPermissionLevel();
        if (permissionLevel == TrainPermissionLevel.PRIVATE) {
            authData.setPermissionLevel(TrainPermissionLevel.PROTECTED);
        } else {
            authData.setPermissionLevel(TrainPermissionLevel.PRIVATE);
        }
        player.displayClientMessage(
                Component.translatable(
                        "message.who_touched_my_train.lock_state_changed",
                        Component.translatable("message.who_touched_my_train.lock_state_level." + authData.getPermissionLevel().name().toLowerCase())
                        )
        , true);
    }
}
