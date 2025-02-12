package net.thorminate.lifecounter.server.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.thorminate.lifecounter.LifeCounter.MOD_ID;

public record LivesDataPayload(Map<UUID, Integer> playerLives) implements CustomPayload {
    public static final Id<LivesDataPayload> ID = new Id<>(Identifier.of(MOD_ID, "player_lives_data_sync"));

    public static final PacketCodec<PacketByteBuf, LivesDataPayload> CODEC = PacketCodec.of(
            LivesDataPayload::write, LivesDataPayload::read
    );

    private void write(PacketByteBuf buf) {
        buf.writeInt(playerLives.size());
        for (Map.Entry<UUID, Integer> entry : playerLives.entrySet()) {
            buf.writeUuid(entry.getKey());
            buf.writeInt(entry.getValue());
        }
    }

    private static LivesDataPayload read(PacketByteBuf buf) {
        int size = buf.readInt();
        Map<UUID, Integer> playerLives = new HashMap<>();
        for (int i = 0; i < size; i++) {
            playerLives.put(buf.readUuid(), buf.readInt());
        }
        return new LivesDataPayload(playerLives);
    }

    @Override
    public Id<LivesDataPayload> getId() {
        return ID;
    }
}
