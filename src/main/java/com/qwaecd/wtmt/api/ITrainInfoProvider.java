package com.qwaecd.wtmt.api;

import javax.annotation.Nullable;

public interface ITrainInfoProvider {
    default boolean hasPermission$who_touched_my_train(String playerName) {
        return true;
    }
    default boolean hasOwner$who_touched_my_train() {
        return false;
    }
    @Nullable
    default String getOwnerPlayerName$who_touched_my_train() {
        return null;
    }
    default void setOwnerPlayerName$who_touched_my_train(String playerName) {
        throw new UnsupportedOperationException();
    }
    default boolean hasAuthorizedPlayer$who_touched_my_train(String playerName) {
        return false;
    }
    default void authorizePlayer$who_touched_my_train(String playerName) {
        throw new UnsupportedOperationException();
    }
    default void deauthorizePlayer$who_touched_my_train(String playerName) {
        throw new UnsupportedOperationException();
    }
    default void setPublic$who_touched_my_train() {
        throw new UnsupportedOperationException();
    }
}
