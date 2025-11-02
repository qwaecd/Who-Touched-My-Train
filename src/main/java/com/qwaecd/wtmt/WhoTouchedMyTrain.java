package com.qwaecd.wtmt;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WhoTouchedMyTrain.MODID)
public class WhoTouchedMyTrain
{
    public static final String MODID = "who_touched_my_train";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WhoTouchedMyTrain(FMLJavaModLoadingContext context)
    {
    }
}
