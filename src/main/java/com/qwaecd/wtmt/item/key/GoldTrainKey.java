package com.qwaecd.wtmt.item.key;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class GoldTrainKey extends TrainKey implements IKeyAction {
    public GoldTrainKey(Properties properties) {
        super(properties);
    }

    @Override
    public void onControls(@Nonnull ItemStack itemInHand, @Nonnull Player player, @Nonnull ITrainInfoProvider infoProvider) {
        String playerName = player.getName().getString();
        String ownerName = infoProvider.getOwnerPlayerName$who_touched_my_train();
        if (ownerName == null || !ownerName.equals(playerName)) {
            return;
        }
        // unlock
        infoProvider.setPublic$who_touched_my_train();
        //noinspection resource
        if (!player.level().isClientSide()) {
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.set_public"), true);
        }
    }
}
