package com.qwaecd.wtmt.server;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TrainEntityAuthRequest {
    private static final Map<UUID, ConcurrentLinkedQueue<AuthReq>> requestMap = new ConcurrentHashMap<>();

    public static boolean containsRequest(UUID trainUUID) {
        return requestMap.containsKey(trainUUID);
    }

    public static void offerRequest(UUID trainUUID, AuthReq authReq) {
        requestMap.computeIfAbsent(trainUUID, k -> new ConcurrentLinkedQueue<>()).offer(authReq);
    }

    public static void offerRequest(UUID trainUUID, String playerName) {
        requestMap.computeIfAbsent(trainUUID, k -> new ConcurrentLinkedQueue<>()).offer(new AuthReq(trainUUID, playerName));
    }

    @Nullable
    public static AuthReq pollRequest(UUID trainUUID) {
        ConcurrentLinkedQueue<AuthReq> queue = requestMap.get(trainUUID);
        if (queue != null) {
            AuthReq req = queue.poll();
            if (queue.isEmpty()) {
                requestMap.remove(trainUUID);
            }
            return req;
        }
        return null;
    }
}
