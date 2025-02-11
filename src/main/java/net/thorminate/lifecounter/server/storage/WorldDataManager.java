package net.thorminate.lifecounter.server.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;

import static net.minecraft.datafixer.DataFixTypes.LEVEL;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public class WorldDataManager extends PersistentState {
    private static final Map<UUID, Integer> playerLives = new HashMap<>();
    public static final String LIVES_ID = "lives_data";

    public WorldDataManager(NbtCompound nbt) {
        if (nbt.contains(LIVES_ID, NbtElement.COMPOUND_TYPE)) {
            NbtCompound playerDataFromNbt = nbt.getCompound(LIVES_ID);
            for (String uuidString : playerDataFromNbt.getKeys()) {
                UUID uuid = UUID.fromString(uuidString);
                int value = playerDataFromNbt.getInt(uuidString);
                playerLives.put(uuid, value);
            }
        }
    }

    public WorldDataManager() {}

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound playerDataFromNbt = new NbtCompound();
        for (Map.Entry<UUID, Integer> entry : playerLives.entrySet()) {
            playerDataFromNbt.putInt(entry.getKey().toString(), entry.getValue());
        }
        nbt.put(LIVES_ID, playerDataFromNbt);
        return nbt;
    }

    public void setPlayerLives(UUID playerUUID, int value) {
        playerLives.put(playerUUID, value);
        this.markDirty();
    }

    public int getPlayerLives(UUID playerUuid) {
        return playerLives.getOrDefault(playerUuid, 10);
    }

    public static final Type<WorldDataManager> TYPE = new Type<>(
            WorldDataManager::new,
            (nbt, registryLookup) -> new WorldDataManager(nbt),
            LEVEL
    );
}
