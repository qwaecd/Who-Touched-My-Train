package com.qwaecd.wtmt.item.key;

import net.minecraft.world.item.Item;

public abstract class TrainKey extends Item {
    public TrainKey(Properties properties) {
        super(properties.stacksTo(64));
    }
}
