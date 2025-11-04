package com.qwaecd.wtmt.server;

import java.util.UUID;

public class AuthReq {
    public final UUID trainUUID;
    public final String playerName;

    public AuthReq(UUID trainUUID, String playerName) {
        this.trainUUID = trainUUID;
        this.playerName = playerName;
    }
}
