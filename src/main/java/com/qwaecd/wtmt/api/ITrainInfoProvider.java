package com.qwaecd.wtmt.api;

import com.qwaecd.wtmt.data.IAuthDataAccessor;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface ITrainInfoProvider {
    default IAuthDataAccessor getAuthData() {
        return getAuthData$who_touched_my_train();
    }

    IAuthDataAccessor getAuthData$who_touched_my_train();

    default UUID getEntityUUID() {
        return getEntityUUID$who_touched_my_train();
    }
    UUID getEntityUUID$who_touched_my_train();
    /**
     * 是否有使用权限, 不包含修改权限的权限
     */
    default boolean hasUsePermission(String playerName) {
        return hasUsePermission$who_touched_my_train(playerName);
    }
    boolean hasUsePermission$who_touched_my_train(String playerName);

    default boolean hasOwner() {
        return hasOwner$who_touched_my_train();
    }
    boolean hasOwner$who_touched_my_train();

    @Nullable
    default String getOwnerPlayerName() {
        return getOwnerPlayerName$who_touched_my_train();
    }
    @Nullable
    String getOwnerPlayerName$who_touched_my_train();

    default void setOwnerPlayerName(String playerName) {
        setOwnerPlayerName$who_touched_my_train(playerName);
    }
    void setOwnerPlayerName$who_touched_my_train(String playerName);

    default boolean hasAuthorizedPlayer(String playerName) {
        return hasAuthorizedPlayer$who_touched_my_train(playerName);
    }
    boolean hasAuthorizedPlayer$who_touched_my_train(String playerName);

    default void authorizePlayer(String playerName) {
        authorizePlayer$who_touched_my_train(playerName);
    }
    void authorizePlayer$who_touched_my_train(String playerName);

    default void deauthorizePlayer(String playerName) {
        deauthorizePlayer$who_touched_my_train(playerName);
    }
    void deauthorizePlayer$who_touched_my_train(String playerName);

    default void setPublic() {
        setPublic$who_touched_my_train();
    }
    void setPublic$who_touched_my_train();
}
