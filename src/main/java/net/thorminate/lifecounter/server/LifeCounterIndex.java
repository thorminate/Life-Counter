package net.thorminate.lifecounter.server;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.thorminate.lifecounter.server.command.LifeCounterCommand;
import net.thorminate.lifecounter.server.storage.WorldDataManager;

import java.util.UUID;

public class LifeCounterIndex {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> LifeCounterCommand.register(dispatcher));
    }

    public static void setPlayerLives(MinecraftServer server, UUID playerUuid, int amount) {
        server.getOverworld().getPersistentStateManager().getOrCreate(WorldDataManager.TYPE, WorldDataManager.LIVES_ID).setPlayerLives(playerUuid, amount);
    }

    public static int getPlayerLives(MinecraftServer server, UUID playerUuid) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(WorldDataManager.TYPE, WorldDataManager.LIVES_ID).getPlayerLives(playerUuid);
    }
}
