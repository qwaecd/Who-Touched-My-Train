package com.qwaecd.wtmt.data;

import com.qwaecd.wtmt.api.TrainPermissionLevel;

public interface IAuthDataAccessor {
    String getOwnerPlayerName();

    void setOwnerPlayerName(String ownerPlayerName);

    TrainPermissionLevel getPermissionLevel();

    void setPermissionLevel(TrainPermissionLevel level);

    boolean hasAuthorizedPlayer(String playerName);

    long getGeneration();
}
