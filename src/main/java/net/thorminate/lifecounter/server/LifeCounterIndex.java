package net.thorminate.lifecounter.server;

import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.thorminate.lifecounter.server.network.LivesDataPayload;
import net.thorminate.lifecounter.server.storage.WorldDataManager;

import java.util.Map;
import java.util.UUID;

public class LifeCounterIndex {
    public static void setPlayerLives(MinecraftServer server, UUID playerUuid, int amount) {
        server.getOverworld().getPersistentStateManager().getOrCreate(WorldDataManager.TYPE, WorldDataManager.LIVES_ID).setPlayerLives(playerUuid, amount);
    }

    public static int getPlayerLives(MinecraftServer server, UUID playerUuid) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(WorldDataManager.TYPE, WorldDataManager.LIVES_ID).getPlayerLives(playerUuid);
    }

    public static Map<UUID, Integer> getPlayerLives(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(WorldDataManager.TYPE, WorldDataManager.LIVES_ID).getAllPlayerLives();
    }

    public static void syncDataWithPlayers(MinecraftServer server) {
        Map<UUID, Integer> playerLives = getPlayerLives(server);

        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(new LivesDataPayload(playerLives)));
    }
}
