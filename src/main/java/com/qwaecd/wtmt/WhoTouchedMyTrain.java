package com.qwaecd.wtmt;

import com.mojang.logging.LogUtils;
import com.qwaecd.wtmt.init.AllModItems;
import com.qwaecd.wtmt.network.AllSerializers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WhoTouchedMyTrain.MOD_ID)
public class WhoTouchedMyTrain
{
    public static final String MOD_ID = "who_touched_my_train";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WhoTouchedMyTrain(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        AllModItems.registerAllItems(modEventBus);
        AllSerializers.register(modEventBus);
//        Channel.register();
    }
}
