package com.qwaecd.wtmt.network;

import com.qwaecd.wtmt.network.packet.C2SRequestAuthorizePacket;
import com.qwaecd.wtmt.tools.ModRL;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Channel {
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ModRL.InModSpace("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.messageBuilder(C2SRequestAuthorizePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SRequestAuthorizePacket::new)
                .encoder(C2SRequestAuthorizePacket::write)
                .consumerMainThread(C2SRequestAuthorizePacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }
}
