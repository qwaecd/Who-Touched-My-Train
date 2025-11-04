package com.qwaecd.wtmt.mixin;


import com.qwaecd.wtmt.init.AllModItems;
import com.qwaecd.wtmt.api.ITrainInfoProvider;
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
            remap = false
    )
    private void onRightClick(
            Player player,
            InteractionHand activeHand,
            BlockPos localPos,
            AbstractContraptionEntity contraptionEntity,
            CallbackInfoReturnable<Boolean> ci
    ) {
//        System.out.println("this is " + (player.level().isClientSide() ? "client" : "server"));
        ItemStack itemInHand = player.getItemInHand(activeHand);
        if (!itemInHand.is(AllModItems.TRAIN_LOCK.get())) {
            return;
        }
        if (!(contraptionEntity instanceof ITrainInfoProvider infoProvider)) {
            return;
        }
        boolean hasOwner = infoProvider.hasOwner$who_touched_my_train();
        String playerName = player.getName().getString();
        String ownerName = infoProvider.getOwnerPlayerName$who_touched_my_train();
        System.out.println("owner: " + ownerName + ", hasOwner: " + hasOwner);
        if (!hasOwner) {
            infoProvider.setOwnerPlayerName$who_touched_my_train(playerName);
            if (!player.level().isClientSide) {
                player.displayClientMessage(Component.translatable("message.who_touched_my_train.set_owner"), true);
            }
            return;
        }

        if (infoProvider.hasPermission$who_touched_my_train(playerName)) {
            return;
        }
    }
}
