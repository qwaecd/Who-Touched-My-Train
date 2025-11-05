package com.qwaecd.wtmt.init;

import com.qwaecd.wtmt.item.TrainLock;
import com.qwaecd.wtmt.item.key.GoldTrainKey;
import com.qwaecd.wtmt.item.key.IronTrainKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class AllModItems {
    private static final Map<String, RegistryObject<Item>> itemMap = new HashMap<>();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> TRAIN_LOCK = register("train_lock", () -> new TrainLock(new Item.Properties()));
    public static final RegistryObject<Item> IRON_KEY = register("iron_key", () -> new IronTrainKey(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_KEY = register("gold_key", () -> new GoldTrainKey(new Item.Properties()));


    public static final RegistryObject<CreativeModeTab> ITEM_TAB = CREATIVE_MODE_TABS.register("wtmt_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(MOD_ID + ".wtmt_tab"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> GOLD_KEY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                itemMap.forEach(
                        (name, item) -> output.accept(item.get())
                );
            }).build());
    public static void registerAllItems(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    private static <T extends Item> RegistryObject<Item> register(String itemName, Supplier<T> itemSupplier) {
        itemMap.put(
                itemName,
                ITEMS.register(itemName, itemSupplier)
        );
        return itemMap.get(itemName);
    }
}
