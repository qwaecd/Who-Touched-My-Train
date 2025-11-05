package com.qwaecd.wtmt.api;

public enum TrainPermissionLevel {
    /**
     * 默认权限等级, 任何人都可上锁
     */
    PUBLIC(0),
    /**
     * 仅使用权限
     */
    PROTECTED(1),
    /**
     * 仅私人权限
     */
    PRIVATE(2);
    private final int level;
    private TrainPermissionLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }

    public static TrainPermissionLevel fromLevel(int level) {
        for (TrainPermissionLevel permissionLevel : TrainPermissionLevel.values()) {
            if (permissionLevel.getLevel() == level) {
                return permissionLevel;
            }
        }
        return PUBLIC;
    }
}
