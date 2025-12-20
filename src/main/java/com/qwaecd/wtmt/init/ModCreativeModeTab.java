package com.qwaecd.wtmt.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.qwaecd.wtmt.WhoTouchedMyTrain.MOD_ID;

public final class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEM_TAB = CREATIVE_MODE_TABS.register("wtmt_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(MOD_ID + ".wtmt_tab"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> AllModItems.GOLD_KEY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                AllModItems.ITEMS.getEntries().forEach(
                        obj -> output.accept(obj.get())
                );
            }).build());

    public static void registerAll(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
