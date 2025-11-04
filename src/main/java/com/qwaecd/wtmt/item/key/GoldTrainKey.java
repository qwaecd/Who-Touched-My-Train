package com.qwaecd.wtmt.item.key;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.qwaecd.wtmt.data.AuthComponentData;
import com.qwaecd.wtmt.network.Channel;
import com.qwaecd.wtmt.network.packet.C2SRequestAuthorizePacket;
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

public class GoldTrainKey extends TrainKey {
    public GoldTrainKey(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
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
        if (!infoProvider.hasOwner$who_touched_my_train()) {
            return;
        }
        String playerName = player.getName().getString();

        //noinspection resource
        if (!player.level().isClientSide()) {
            String ownerName = infoProvider.getOwnerPlayerName$who_touched_my_train();
            CompoundTag tag = itemInHand.getTag();
            AuthComponentData trainAuthData = new AuthComponentData(ownerName, infoProvider);
            if (tag == null) {
                if (playerName.equals(ownerName)) {
                    CompoundTag authTag = itemInHand.getOrCreateTagElement(AuthComponentData.COMPONENT_NAME);
                    processKey(trainAuthData, authTag, player);
                }
                return;
            }
            CompoundTag authTag = itemInHand.getOrCreateTagElement(AuthComponentData.COMPONENT_NAME);

            if (infoProvider.hasPermission$who_touched_my_train(playerName))
                return;

            AuthComponentData keyAuthData = AuthComponentData.read(authTag);
            if (trainAuthData.isOverdue(keyAuthData.getGeneration())) {
                return;
            }

            if (verify(trainAuthData, keyAuthData)) {
//                Channel.sendToServer(C2SRequestAuthorizePacket.create(playerName, trainAuthData.getCarriageUUID()));
                infoProvider.authorizePlayer$who_touched_my_train(playerName);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag) {
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
        player.displayClientMessage(Component.translatable("message.who_touched_my_train.successfully_copied_key"), false);
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
