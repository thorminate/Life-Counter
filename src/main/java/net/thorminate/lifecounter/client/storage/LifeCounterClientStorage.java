package net.thorminate.lifecounter.client.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LifeCounterClientStorage {
    private static final Map<UUID, Integer> playerLives = new HashMap<>();

    public static Map<UUID, Integer> getPlayerLives() {
        return playerLives;
    }

    public static void setPlayerLives(Map<UUID, Integer> playerLivesMap) {
        playerLives.clear();
        if (playerLivesMap != null) playerLives.putAll(playerLivesMap);
    }
}
