package com.qwaecd.wtmt;

import com.qwaecd.wtmt.init.AllModItems;
import com.qwaecd.wtmt.network.AllSerializers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WhoTouchedMyTrain.MOD_ID)
public class WhoTouchedMyTrain
{
    public static final String MOD_ID = "who_touched_my_train";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab("wtmt_tab",
                    t -> {
                        t.icon(() -> AllModItems.GOLD_KEY.get().getDefaultInstance());
                        t.title(Component.translatable("who_touched_my_train.wtmt_tab"));
                    }).build()
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );
    public WhoTouchedMyTrain(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        AllModItems.registerAllItems();
        AllSerializers.register(modEventBus);
        REGISTRATE.registerEventListeners(modEventBus);
//        Channel.register();
    }
}
