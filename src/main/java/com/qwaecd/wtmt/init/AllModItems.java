package com.qwaecd.wtmt.init;

import com.qwaecd.wtmt.item.key.DiamondTrainKey;
import com.qwaecd.wtmt.item.key.GoldTrainKey;
import com.qwaecd.wtmt.item.key.IronTrainKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class AllModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> GOLD_KEY = ITEMS.register("gold_key", () -> new GoldTrainKey(new Item.Properties()));
    public static final RegistryObject<Item> IRON_KEY = ITEMS.register("iron_key", () -> new IronTrainKey(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_KEY = ITEMS.register("diamond_key", () -> new DiamondTrainKey(new Item.Properties()));

    public static void registerAllItems(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
