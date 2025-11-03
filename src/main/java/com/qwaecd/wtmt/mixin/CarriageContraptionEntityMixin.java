package com.qwaecd.wtmt.mixin;


import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = CarriageContraptionEntity.class, remap = false)
public abstract class CarriageContraptionEntityMixin {

    @Unique
    private Set<String> who_touched_my_train$authorizedPlayers = new HashSet<>();

    @Inject(
            method = "startControlling",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            remap = false
    )
    private void mixinStartControlling(BlockPos controlsLocalPos, Player player, CallbackInfoReturnable<Boolean> ci) {
        if (player == null || player.isSpectator()) {
            return;
        }
        String playerName = player.getName().getString();
        if (who_touched_my_train$isAuthorizedPlayer(playerName)) {
            return;
        } else {
            player.displayClientMessage(Component.translatable("message.who_touched_my_train.train_no_permission"), true);
            ci.setReturnValue(false);
        }
    }

    @Unique
    private boolean who_touched_my_train$isAuthorizedPlayer(String playerName) {
        return who_touched_my_train$authorizedPlayers.contains(playerName);
    }
}
