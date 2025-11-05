package com.qwaecd.wtmt.api;

import com.qwaecd.wtmt.data.IAuthDataAccessor;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ITrainInfoProvider {
    default IAuthDataAccessor getAuthData$who_touched_my_train() {
        throw new UnsupportedOperationException();
    }
    default UUID getEntityUUID$who_touched_my_train() {
        throw new UnsupportedOperationException();
    }

    /**
     * 是否有使用权限, 不包含修改权限的权限
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    default boolean hasUsePermission$who_touched_my_train(String playerName) {
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
