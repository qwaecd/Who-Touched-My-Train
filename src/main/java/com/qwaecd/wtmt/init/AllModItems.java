package com.qwaecd.wtmt.init;

import com.qwaecd.wtmt.WhoTouchedMyTrain;
import com.qwaecd.wtmt.item.TrainLock;
import com.qwaecd.wtmt.item.key.AdminTrainKey;
import com.qwaecd.wtmt.item.key.GoldTrainKey;
import com.qwaecd.wtmt.item.key.IronTrainKey;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class AllModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MOD_ID);

    public static final ItemEntry<TrainLock> TRAIN_LOCK = register("train_lock", TrainLock::new);
    public static final ItemEntry<IronTrainKey> IRON_KEY = register("iron_key", IronTrainKey::new);
    public static final ItemEntry<GoldTrainKey> GOLD_KEY = register("gold_key", GoldTrainKey::new);
    public static final ItemEntry<AdminTrainKey> ADMIN_KEY = register("admin_key", AdminTrainKey::new);

    public static void registerAllItems() {
    }

    private static <T extends Item> ItemEntry<T> register(String itemName,  NonNullFunction<Item.Properties, T> factory) {
        return WhoTouchedMyTrain.REGISTRATE.item(itemName, factory).register();
    }

    private static <T extends Item> ItemEntry<T> register(String itemName, NonNullFunction<Item.Properties, T> factory, Consumer<ItemBuilder<T, CreateRegistrate>> modifier) {
        ItemBuilder<T, CreateRegistrate> itemBuilder = WhoTouchedMyTrain.REGISTRATE.item(itemName, factory);
        modifier.accept(itemBuilder);
        return itemBuilder.register();
    }
}
