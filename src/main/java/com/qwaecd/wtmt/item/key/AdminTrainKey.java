package com.qwaecd.wtmt.item.key;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class AdminTrainKey extends TrainKey implements IKeyAction {
    public AdminTrainKey(Properties properties) {
        super(properties);
    }

    @Override
    public void onControls(@Nonnull ItemStack itemInHand, @Nonnull Player player, @Nonnull ITrainInfoProvider infoProvider) {
        if (!infoProvider.hasOwner())
            return;
        //noinspection resource
        if (player.level().isClientSide()) {
            return;
        }
        if (player.isShiftKeyDown()) {
            // 直接解锁

            infoProvider.setPublic();
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.set_public"), true);
        } else {
            // 直接授权
            String playerName = player.getName().getString();

            if (infoProvider.hasAuthorizedPlayer(playerName))
                return;

            infoProvider.authorizePlayer(playerName);
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.successfully_authorized"), true);
        }
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack itemStack) {
        return true;
    }
}
