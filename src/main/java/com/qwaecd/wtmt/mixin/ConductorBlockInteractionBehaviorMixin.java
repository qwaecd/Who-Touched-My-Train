package com.qwaecd.wtmt.mixin;


import com.qwaecd.wtmt.api.ITrainInfoProvider;
import com.simibubi.create.api.behaviour.interaction.ConductorBlockInteractionBehavior;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConductorBlockInteractionBehavior.class, remap = false)
public abstract class ConductorBlockInteractionBehaviorMixin {
    @Inject(
            method = "handlePlayerInteraction",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void onRightClick(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity, CallbackInfoReturnable<Boolean> ci) {
        if (!(contraptionEntity instanceof ITrainInfoProvider infoProvider)) {
            return;
        }
        String playerName = player.getName().getString();
        if (!infoProvider.hasUsePermission(playerName)) {
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.train_no_permission"), true);
            ci.setReturnValue(true);
        }
    }
}
