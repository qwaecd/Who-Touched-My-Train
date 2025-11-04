package com.qwaecd.wtmt.item.key;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class GoldTrainKey extends TrainKey {
    public GoldTrainKey(Properties properties) {
        super(properties);
    }

    @Override
    public void onControls(ItemStack itemInHand, @Nonnull Player player, @Nonnull ITrainInfoProvider infoProvider) {

    }
}
