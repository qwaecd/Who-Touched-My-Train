package com.qwaecd.wtmt.mixin;


import com.qwaecd.wtmt.init.AllModItems;
import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.qwaecd.wtmt.item.key.TrainKey;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsInteractionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ControlsInteractionBehaviour.class, remap = false)
public abstract class ControlsInteractionBehaviourMixin {

    @Inject(
            method = "handlePlayerInteraction",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void onRightClick(
            Player player,
            InteractionHand activeHand,
            BlockPos localPos,
            AbstractContraptionEntity contraptionEntity,
            CallbackInfoReturnable<Boolean> ci
    ) {
//        System.out.println("this is " + (player.level().isClientSide() ? "client" : "server"));
        if (!(contraptionEntity instanceof ITrainInfoProvider infoProvider)) {
            return;
        }

        ItemStack itemInHand = player.getItemInHand(activeHand);

        if (itemInHand.getItem() instanceof TrainKey trainKey) {
            trainKey.onControls(itemInHand, player, infoProvider);
            ci.setReturnValue(true);
        }

        boolean hasOwner = infoProvider.hasOwner$who_touched_my_train();
        String playerName = player.getName().getString();
        String ownerName = infoProvider.getOwnerPlayerName$who_touched_my_train();
//        System.out.println("owner: " + ownerName + ", hasOwner: " + hasOwner);
        if (itemInHand.is(AllModItems.TRAIN_LOCK.get())) {
            // 向列车加锁
            if (!hasOwner) {
                //noinspection resource
                if (!player.level().isClientSide()) {
                    infoProvider.setOwnerPlayerName$who_touched_my_train(playerName);
                    player.displayClientMessage(Component.translatable("message.who_touched_my_train.set_owner"), true);
                }
                ci.setReturnValue(true);
            }
        }


        if (!infoProvider.hasPermission$who_touched_my_train(playerName)) {
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.train_no_permission"), true);
            ci.setReturnValue(true);
        }
    }
}
