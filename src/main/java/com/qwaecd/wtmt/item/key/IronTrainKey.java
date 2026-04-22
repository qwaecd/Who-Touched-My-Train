package com.qwaecd.wtmt.item.key;

import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.qwaecd.wtmt.data.AuthComponentData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
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
        if (!infoProvider.hasOwner())
            return;

        String playerName = player.getName().getString();

        String ownerName = infoProvider.getOwnerPlayerName();
        AuthComponentData trainAuthData = new AuthComponentData(ownerName, infoProvider);
        if (playerName.equals(ownerName)) {
            // 刻钥匙
            //noinspection resource
            if (!player.level().isClientSide()) {
                processKey(trainAuthData, itemInHand, player);
            }
            return;
        }

        if (infoProvider.hasAuthorizedPlayer(playerName)) {
            return;
        }
        AuthComponentData keyAuthData = readAuthComponent(itemInHand);
        if (keyAuthData == null) {
            return;
        }

        if (trainAuthData.isOverdue(keyAuthData.getGeneration())) {
            return;
        }

        if (verify(trainAuthData, keyAuthData)) {
            //noinspection resource
            if (!player.level().isClientSide()) {
                itemInHand.shrink(1);
                infoProvider.authorizePlayer(playerName);
                player.displayClientMessage(Component.translatable("message.who_touched_my_train.successfully_authorized"), true);
            }
        }
    }

    public void appendHoverText(
            ItemStack itemStack,
            Item.TooltipContext context,
            List<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        AuthComponentData authData = readAuthComponent(itemStack);
        if (authData == null) {
            return;
        }
        String ownerName = authData.getOwnerName();
        tooltipComponents.add(
                Component.translatable("who_touched_my_train.item.gold_train_key.tooltip", ownerName)
                        .withStyle(ChatFormatting.AQUA)
        );
        if (tooltipFlag.isAdvanced()) {
            // F3 + H
            tooltipComponents.add(
                    Component.literal("Generation: " + authData.getGeneration())
                            .withStyle(ChatFormatting.DARK_GRAY)
            );
        }
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return readAuthComponent(itemStack) != null;
    }

    private void processKey(AuthComponentData authData, ItemStack itemStack, Player player) {
        // 刻钥匙
        writeAuthComponent(itemStack, authData);
        player.displayClientMessage(Component.translatable("message.who_touched_my_train.successfully_copied_key"), true);
    }

    private void clearKey(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }
        CompoundTag tag = customData.getUnsafe().copy();
        tag.remove(AuthComponentData.COMPONENT_NAME);
        if (tag.isEmpty()) {
            itemStack.remove(DataComponents.CUSTOM_DATA);
            return;
        }
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private boolean verify(AuthComponentData trainAuthData, AuthComponentData keyAuthData) {
        return trainAuthData.getOwnerName().equals(keyAuthData.getOwnerName())
                && trainAuthData.getCarriageUUID().equals(keyAuthData.getCarriageUUID());
    }

    @Nullable
    private AuthComponentData readAuthComponent(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }
        CompoundTag authTag = customData.getUnsafe().getCompound(AuthComponentData.COMPONENT_NAME);
        if (authTag.isEmpty()) {
            return null;
        }
        return AuthComponentData.read(authTag);
    }

    private void writeAuthComponent(ItemStack itemStack, AuthComponentData authData) {
        CompoundTag customDataTag = getCustomDataTag(itemStack);
        customDataTag.put(AuthComponentData.COMPONENT_NAME, authData.toComponentTag());
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(customDataTag));
    }

    private CompoundTag getCustomDataTag(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return new CompoundTag();
        }
        return customData.getUnsafe().copy();
    }
}
