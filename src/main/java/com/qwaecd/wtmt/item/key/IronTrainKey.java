package com.qwaecd.wtmt.item.key;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.qwaecd.wtmt.data.AuthComponentData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class IronTrainKey extends TrainKey {
    public IronTrainKey(Properties properties) {
        super(properties);
    }

    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(Level level, @Nonnull Player player, @Nonnull InteractionHand usedHand) {
        if (level.isClientSide()) {
            return super.use(level, player, usedHand);
        }
        if (player.isShiftKeyDown()) {
            ItemStack itemStack = player.getItemInHand(usedHand);
            clearKey(itemStack);
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void onControls(@Nonnull ItemStack itemInHand, @Nonnull Player player, @Nonnull ITrainInfoProvider infoProvider) {
        if (!infoProvider.hasOwner$who_touched_my_train())
            return;

        String playerName = player.getName().getString();
        CompoundTag tag = itemInHand.getTag();
        String ownerName = infoProvider.getOwnerPlayerName$who_touched_my_train();
        AuthComponentData trainAuthData = new AuthComponentData(ownerName, infoProvider);
        if (playerName.equals(ownerName)) {
            // 刻钥匙
            //noinspection resource
            if (!player.level().isClientSide()) {
                processKey(trainAuthData, itemInHand.getOrCreateTagElement(AuthComponentData.COMPONENT_NAME), player);
            }
            return;
        }

        if (infoProvider.hasAuthorizedPlayer$who_touched_my_train(playerName) || tag == null)
            return;

        CompoundTag keyAuthTag = tag.getCompound(AuthComponentData.COMPONENT_NAME);
        if (keyAuthTag.isEmpty()) {
            return;
        }
        AuthComponentData keyAuthData = AuthComponentData.read(keyAuthTag);
        if (trainAuthData.isOverdue(keyAuthData.getGeneration())) {
            return;
        }

        if (verify(trainAuthData, keyAuthData)) {
            //noinspection resource
            if (!player.level().isClientSide()) {
                itemInHand.shrink(1);
                infoProvider.authorizePlayer$who_touched_my_train(playerName);
                player.displayClientMessage(Component.translatable("message.who_touched_my_train.successfully_authorized"), true);
            }
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltipComponents, flag);

        CompoundTag authTag = itemStack.getTagElement(AuthComponentData.COMPONENT_NAME);
        if (authTag == null) {
            return;
        }
        String ownerName = authTag.getString(AuthComponentData.OWNER_NAME);
        tooltipComponents.add(
                Component.translatable("who_touched_my_train.item.gold_train_key.tooltip", ownerName)
                        .withStyle(ChatFormatting.AQUA)
        );
        if(flag.isAdvanced()) {
            // F3 + H
            tooltipComponents.add(
                    Component.literal("Generation: " + authTag.getLong(AuthComponentData.GENERATION))
                            .withStyle(ChatFormatting.DARK_GRAY)
            );
        }
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        CompoundTag authTag = itemStack.getTagElement(AuthComponentData.COMPONENT_NAME);
        return authTag != null;
    }

    private void processKey(AuthComponentData authData, CompoundTag authTag, Player player) {
        // 刻钥匙
        authData.write(authTag);
        player.displayClientMessage(Component.translatable("message.who_touched_my_train.successfully_copied_key"), true);
    }

    private void clearKey(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            tag.remove(AuthComponentData.COMPONENT_NAME);
        }
    }

    private boolean verify(AuthComponentData trainAuthData, AuthComponentData keyAuthData) {
        return trainAuthData.getOwnerName().equals(keyAuthData.getOwnerName())
                && trainAuthData.getCarriageUUID().equals(keyAuthData.getCarriageUUID());
    }
}
